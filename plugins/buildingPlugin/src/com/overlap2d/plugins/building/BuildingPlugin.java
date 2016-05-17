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
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import net.mountainblade.modular.annotations.Implementation;

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
    public static final String ROW = "ROW";
    public static final String COLUMN = "COLUMN";

    private BuildingPanelMediator buildingPanelMediator;

    private Json buildingJson;
    private FileHandle fileHandle;


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
        buildingJson.setUsePrototypes(false);
        fileHandle = Gdx.files.absolute(pluginAPI.getProjectPath() + "/building.json");
    }

    public void savePluginData(BuildingVO buildingVO) {
        String dataString = buildingJson.prettyPrint(buildingVO);
        fileHandle.writeString(dataString, false);
    }

    public boolean isTile(Entity entity) {
        return ComponentRetriever.get(entity, MainItemComponent.class).tags.contains(TILE_TAG);
    }


}
