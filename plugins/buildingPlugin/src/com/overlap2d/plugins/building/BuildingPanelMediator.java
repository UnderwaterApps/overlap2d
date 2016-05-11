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

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.commons.plugins.PluginAPI;
import com.overlap2d.plugins.building.data.BlockVO;
import com.overlap2d.plugins.building.data.BuildingVO;
import com.overlap2d.plugins.building.data.DecorVO;
import com.overlap2d.plugins.building.data.ItemVO;
import com.puremvc.patterns.mediator.SimpleMediator;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.renderer.components.MainItemComponent;
import com.uwsoft.editor.renderer.components.TextureRegionComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.renderer.utils.CustomVariables;

import java.util.HashSet;

/**
 * Created by azakhary on 7/24/2015.
 */
public class BuildingPanelMediator extends SimpleMediator<BuildingPanel> {
    private static final String TAG = BuildingPanelMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    public static final String SCENE_LOADED = "com.uwsoft.editor.proxy.SceneDataManager.SCENE_LOADED";

    private BuildingPlugin buildingPlugin;
    private Entity leftEntity, bottomEntity;
    public int minCol;
    public int minRow;
    private CustomVariables currentCustomVars;
    private CustomVariables leftCustomVars;
    private CustomVariables bottomCustomVars;

    public BuildingPanelMediator(BuildingPlugin buildingPlugin) {
        super(NAME, new BuildingPanel());
        this.buildingPlugin = buildingPlugin;

        viewComponent.initLockView();

        currentCustomVars = new CustomVariables();
        leftCustomVars = new CustomVariables();
        bottomCustomVars = new CustomVariables();
    }

    @Override
    public String[] listNotificationInterests() {
        return new String[]{
                SCENE_LOADED,
                BuildingPlugin.PANEL_OPEN,
                BuildingPlugin.SAVE_DATA
        };
    }


    @Override
    public void handleNotification(Notification notification) {
        super.handleNotification(notification);
        switch (notification.getName()) {
            case SCENE_LOADED:
                viewComponent.initView();
                Engine engine = buildingPlugin.getAPI().getEngine();
                viewComponent.setEngine(engine);

                buildingPlugin.initPluginData();
                break;
            case BuildingPlugin.SAVE_DATA:
                saveGameData();
                break;
            case BuildingPlugin.PANEL_OPEN:
                viewComponent.show(buildingPlugin.getAPI().getUIStage());
                break;
        }
    }

    private void saveGameData() {
        initPluginMinEntities();
        BuildingVO buildingVO = new BuildingVO();

        HashSet<Entity> entities = buildingPlugin.getPluginAPI().getProjectEntities();
        for (Entity entity : entities) {
            MainItemComponent mainItemComponent = ComponentRetriever.get(entity, MainItemComponent.class);
            currentCustomVars.loadFromString(mainItemComponent.customVars);
            ItemVO itemVO = new ItemVO();
            if (buildingPlugin.isTile(entity)) {
                BlockVO blockVO = new BlockVO();
                TextureRegionComponent textureRegionComponent = ComponentRetriever.get(entity, TextureRegionComponent.class);
                blockVO.regionName = textureRegionComponent.regionName;
                blockVO.col = currentCustomVars.getIntegerVariable(BuildingPlugin.COLUMN) - minCol;
                blockVO.row = currentCustomVars.getIntegerVariable(BuildingPlugin.ROW) - minRow;
                itemVO.blocks.add(blockVO);
            } else {
                DecorVO decorVO = new DecorVO();
                itemVO.decors.add(decorVO);
            }
            buildingVO.items.add(itemVO);
        }

        buildingPlugin.savePluginData(buildingVO);
    }

    private void initPluginMinEntities() {
        PluginAPI pluginAPI = buildingPlugin.getPluginAPI();
        if (pluginAPI.getProjectEntities().size() == 0) return;
        bottomEntity = leftEntity = pluginAPI.getProjectEntities().iterator().next();

        pluginAPI.getProjectEntities().forEach(entity -> {
            if (!buildingPlugin.isTile(entity)) return;

            currentCustomVars.loadFromString(ComponentRetriever.get(entity, MainItemComponent.class).customVars);
            int currentRow = currentCustomVars.getIntegerVariable(BuildingPlugin.ROW);
            int currentColumn = currentCustomVars.getIntegerVariable(BuildingPlugin.COLUMN);

            leftCustomVars.loadFromString(ComponentRetriever.get(leftEntity, MainItemComponent.class).customVars);
            minCol = leftCustomVars.getIntegerVariable(BuildingPlugin.COLUMN);
            if (currentColumn < minCol) {
                minCol = currentColumn;
                leftEntity = entity;
            }

            bottomCustomVars.loadFromString(ComponentRetriever.get(bottomEntity, MainItemComponent.class).customVars);
            minRow = bottomCustomVars.getIntegerVariable(BuildingPlugin.ROW);
            if (currentRow < minRow) {
                minRow = currentRow;
                bottomEntity = entity;
            }

        });
    }
}
