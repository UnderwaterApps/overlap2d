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
import com.badlogic.gdx.utils.Array;
import com.overlap2d.plugins.building.data.BlockVO;
import com.overlap2d.plugins.building.data.BuildingVO;
import com.overlap2d.plugins.building.data.DecorVO;
import com.overlap2d.plugins.building.data.ItemVO;
import com.puremvc.patterns.mediator.SimpleMediator;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.renderer.components.MainItemComponent;
import com.uwsoft.editor.renderer.components.TextureRegionComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

import java.util.Comparator;
import java.util.HashSet;

/**
 * Created by azakhary on 7/24/2015.
 */
public class BuildingPanelMediator extends SimpleMediator<BuildingPanel> {
    private static final String TAG = BuildingPanelMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    public static final String SCENE_LOADED = "com.uwsoft.editor.proxy.SceneDataManager.SCENE_LOADED";

    private BuildingPlugin buildingPlugin;

    public BuildingPanelMediator(BuildingPlugin buildingPlugin) {
        super(NAME, new BuildingPanel());
        this.buildingPlugin = buildingPlugin;

        viewComponent.initLockView();


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
        BuildingVO buildingVO = new BuildingVO();

        HashSet<Entity> entities = buildingPlugin.getPluginAPI().getProjectEntities();
        for (Entity entity : entities) {
            MainItemComponent mainItemComponent = ComponentRetriever.get(entity, MainItemComponent.class);
            ItemVO itemVO = new ItemVO();
            if (mainItemComponent.tags.contains(buildingPlugin.TILE_TAG)) {
                BlockVO blockVO = new BlockVO();
                TextureRegionComponent textureRegionComponent = ComponentRetriever.get(entity, TextureRegionComponent.class);
                blockVO.regionName = textureRegionComponent.regionName;
                TransformComponent transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
                blockVO.x = transformComponent.x;
                blockVO.y = transformComponent.y;
                itemVO.blocks.add(blockVO);
            } else {
                DecorVO decorVO = new DecorVO();
                itemVO.decors.add(decorVO);
            }
            buildingVO.items.add(itemVO);
        }

        buildingPlugin.savePluginData(buildingVO);
    }
}
