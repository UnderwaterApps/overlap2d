/*
 * ******************************************************************************
 *  * Copyright 2015 See AUTHORS file.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

package com.overlap2d.plugins.building;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.commons.plugins.O2DPluginAdapter;
import com.commons.plugins.PluginAPI;
import com.overlap2d.plugins.building.data.BuildingVO;
import com.uwsoft.editor.renderer.components.MainItemComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.renderer.utils.CustomVariables;
import net.mountainblade.modular.annotations.Implementation;

import java.io.IOException;
import java.io.Writer;

/**
 * Created by azakhary on 7/24/2015.
 */
@Implementation(authors = "azakhary", version = "0.0.1")
public class BuildingPlugin extends O2DPluginAdapter {
    public static final String CLASS_NAME = "com.uwsoft.editor.plugins.building";

    public static final String PANEL_OPEN = CLASS_NAME + ".PANEL_OPEN";
    public static final String SAVE_DATA = CLASS_NAME + ".SAVE_DATA";
    public static final String FILE_MENU = "com.uwsoft.editor.view.Overlap2DMenuBar.FILE_MENU";
    public static final String TILE_TAG         = "TILE";
    private final String row = "row";
    private final String column = "column";

    private BuildingPanelMediator buildingPanelMediator;

    private Json buildingJson;
    private FileHandle fileHandle;

    private CustomVariables currentCustomVars;
    private CustomVariables leftCustomVars;
    private CustomVariables bottomCustomVars;
    private Entity leftEntity, bottomEntity;
    public int leftColumn;
    public int bottomRow;

    public BuildingPlugin() {
        buildingPanelMediator = new BuildingPanelMediator(this);
    }

    @Override
    public void initPlugin() {
        facade.registerMediator(buildingPanelMediator);
        pluginAPI.addMenuItem(FILE_MENU, "Save the building", SAVE_DATA);
    }

    public PluginAPI getPluginAPI() {
        return pluginAPI;
    }

    public void initPluginData() {
        buildingJson = new Json();
        buildingJson.setOutputType(JsonWriter.OutputType.json);
        fileHandle = Gdx.files.absolute(pluginAPI.getProjectPath() + "/building.json");

        currentCustomVars = new CustomVariables();
        leftCustomVars = new CustomVariables();
        bottomCustomVars = new CustomVariables();
        initPluginMinEntities();
        System.out.println("min-max: leftColumn = "+leftColumn+", bottomRow = "+bottomRow);
    }

    public void savePluginData(BuildingVO buildingVO) {
        String dataString = buildingJson.toJson(buildingVO);
        fileHandle.writeString(dataString, false);
    }

    public boolean isTile(Entity entity) {
        return ComponentRetriever.get(entity, MainItemComponent.class).tags.contains(TILE_TAG);
    }

    public void initPluginMinEntities() {
        if (pluginAPI.getProjectEntities().size() == 0) return;
        bottomEntity = leftEntity = pluginAPI.getProjectEntities().iterator().next();

        pluginAPI.getProjectEntities().forEach(entity -> {
            if (!isTile(entity)) return;

            currentCustomVars.loadFromString(ComponentRetriever.get(entity, MainItemComponent.class).customVars);
            int currentRow = Integer.parseInt(currentCustomVars.getStringVariable(row));
            int currentColumn = Integer.parseInt(currentCustomVars.getStringVariable(column));

            leftCustomVars.loadFromString(ComponentRetriever.get(leftEntity, MainItemComponent.class).customVars);
            leftColumn = Integer.parseInt(leftCustomVars.getStringVariable(column));
            if (currentColumn < leftColumn) {
                leftColumn = currentColumn;
                leftEntity = entity;
            }

            bottomCustomVars.loadFromString(ComponentRetriever.get(bottomEntity, MainItemComponent.class).customVars);
            bottomRow = Integer.parseInt(bottomCustomVars.getStringVariable(row));
            if (currentRow < bottomRow) {
                bottomRow = currentRow;
                bottomEntity = entity;
            }

        });
    }
}
