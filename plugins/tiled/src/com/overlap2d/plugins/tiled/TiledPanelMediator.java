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

package com.overlap2d.plugins.tiled;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.commons.MsgAPI;
import com.commons.ResourcePayloadObject;
import com.overlap2d.plugins.tiled.data.TileVO;
import com.overlap2d.plugins.tiled.view.tabs.SettingsTab;
import com.puremvc.patterns.mediator.SimpleMediator;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.MainItemComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.vo.CursorData;

import java.util.HashMap;

/**
 * Created by mariam on 2/2/2016.
 */
public class TiledPanelMediator extends SimpleMediator<TiledPanel> {
    private static final String TAG = TiledPanelMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    public static final String SCENE_LOADED = "com.uwsoft.editor.proxy.SceneDataManager.SCENE_LOADED";

    private TiledPlugin tiledPlugin;

    public TiledPanelMediator(TiledPlugin tiledPlugin) {
        super(NAME, new TiledPanel(tiledPlugin));
        this.tiledPlugin = tiledPlugin;

        viewComponent.initLockView();
    }

    @Override
    public String[] listNotificationInterests() {
        return new String[]{
                SCENE_LOADED,
                TiledPlugin.TILE_ADDED,
                TiledPlugin.TILE_SELECTED,
                TiledPlugin.ACTION_DELETE_TILE,
                TiledPlugin.ACTION_SET_OFFSET,
                TiledPlugin.PANEL_OPEN,
                TiledPlugin.OPEN_DROP_DOWN,
                TiledPlugin.GRID_CHANGED,
                SettingsTab.OK_BTN_CLICKED,
                MsgAPI.ACTION_SET_GRID_SIZE_FROM_ITEM,
                MsgAPI.ACTION_DELETE_IMAGE_RESOURCE,
                MsgAPI.ITEM_DATA_UPDATED,
                MsgAPI.TOOL_SELECTED
        };
    }

    @Override
    public void handleNotification(Notification notification) {
        super.handleNotification(notification);

        String tileName;

        switch (notification.getName()) {
            case SCENE_LOADED:
                tiledPlugin.isSceneLoaded = true;

                tiledPlugin.initSaveData();
                viewComponent.initView();

                DragAndDrop.Target target = new DragAndDrop.Target(viewComponent.getDropTable()) {
                    @Override
                    public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                        return true;
                    }

                    @Override
                    public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {

                        ResourcePayloadObject resourcePayloadObject = (ResourcePayloadObject) payload.getObject();
                        if (!resourcePayloadObject.className.endsWith(".ImageResource")) return; //only image resource can become a tile!

                        String tileName = resourcePayloadObject.name;
                        if (tiledPlugin.dataToSave.containsTile(tileName)) return;

                        tiledPlugin.facade.sendNotification(TiledPlugin.TILE_ADDED, tileName);

                    }
                };
                tiledPlugin.facade.sendNotification(MsgAPI.ADD_TARGET, target);
                Engine engine = tiledPlugin.getAPI().getEngine();
                viewComponent.setEngine(engine);
                viewComponent.setFixedPosition();
                break;
            case TiledPlugin.TILE_ADDED:
                tileName = notification.getBody();
                viewComponent.addTile(tileName);
                viewComponent.setFixedPosition();

                tiledPlugin.dataToSave.addTile(tileName);
                tiledPlugin.saveDataManager.save();
                break;
            case TiledPlugin.TILE_SELECTED:
                viewComponent.selectTile(notification.getBody());
                break;
            case TiledPlugin.OPEN_DROP_DOWN:
                tileName = notification.getBody();
                HashMap<String, String> actionsSet = new HashMap<>();
                actionsSet.put(TiledPlugin.ACTION_DELETE_TILE, "Delete");
                actionsSet.put(TiledPlugin.ACTION_OPEN_OFFSET_PANEL, "Set offset");
                tiledPlugin.facade.sendNotification(TiledPlugin.TILE_SELECTED, tiledPlugin.dataToSave.getTile(tileName));
                tiledPlugin.getAPI().showPopup(actionsSet, tileName);
                break;
            case MsgAPI.ACTION_DELETE_IMAGE_RESOURCE:
                tileName = notification.getBody();
                tiledPlugin.facade.sendNotification(TiledPlugin.ACTION_DELETE_TILE, tileName);
                break;
            case TiledPlugin.ACTION_DELETE_TILE:
                String tn = notification.getBody();
                if (!tiledPlugin.dataToSave.containsTile(tn)) return;
                tiledPlugin.dataToSave.removeTile(tn);
                tiledPlugin.saveDataManager.save();
                tiledPlugin.setSelectedTileVO(new TileVO());

                viewComponent.removeTile();
                break;
            case TiledPlugin.PANEL_OPEN:
                if(viewComponent.isOpen) {
                    break;
                }

                viewComponent.show(tiledPlugin.getAPI().getUIStage());

                if(tiledPlugin.isSceneLoaded) {
                    viewComponent.setFixedPosition();
                }
                break;
            case MsgAPI.TOOL_SELECTED:
                String body = notification.getBody();
                String cursorName = null;
                switch (body.toString()) {
                    case TiledPlugin.TILE_ADD_TOOL:
                        cursorName = "tile";
                        tiledPlugin.facade.sendNotification(TiledPlugin.PANEL_OPEN);
                        tiledPlugin.facade.sendNotification(MsgAPI.HIDE_GRID);
                        break;
                    case TiledPlugin.TILE_DELETE_TOOL:
                        cursorName = "tile-eraser";
                        tiledPlugin.facade.sendNotification(MsgAPI.HIDE_GRID);
                        break;
                    default:
                        tiledPlugin.facade.sendNotification(MsgAPI.SHOW_GRID);
                        viewComponent.hide();
                        break;
                }
                if (cursorName != null) {
                    CursorData cursorData = new CursorData(cursorName, 14, 14);
                    TextureRegion region = tiledPlugin.pluginRM.getTextureRegion(cursorName);
                    System.out.println("cursor: "+cursorName);
//                    tiledPlugin.getPluginAPI().setCursor(cursorData, region);
                }
                break;
            case SettingsTab.OK_BTN_CLICKED:
                tiledPlugin.dataToSave.setParameterVO(notification.getBody());
                tiledPlugin.saveDataManager.save();
                break;
            case TiledPlugin.GRID_CHANGED:
                viewComponent.reInitGridSettings();
                tiledPlugin.saveDataManager.save();
                break;
            case MsgAPI.ACTION_SET_GRID_SIZE_FROM_ITEM:
                Entity observable = notification.getBody();
                DimensionsComponent dimensionsComponent = ComponentRetriever.get(observable, DimensionsComponent.class);
                tiledPlugin.dataToSave.setGrid(dimensionsComponent.width, dimensionsComponent.height);
                tiledPlugin.facade.sendNotification(TiledPlugin.GRID_CHANGED);
                break;
            case MsgAPI.ITEM_DATA_UPDATED:
                Entity item = notification.getBody();
                if (tiledPlugin.isTile(item)) {
                    ComponentRetriever.get(item, MainItemComponent.class).tags.remove(TiledPlugin.TILE_TAG);
                }
                break;
        }
    }


}
