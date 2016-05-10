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

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.commons.plugins.O2DPluginAdapter;
import com.commons.plugins.PluginAPI;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.overlap2d.plugins.tiled.manager.ResourcesManager;
import com.overlap2d.plugins.tiled.save.DataToSave;
import com.overlap2d.plugins.tiled.save.SaveDataManager;
import com.overlap2d.plugins.tiled.tools.DeleteTileTool;
import com.overlap2d.plugins.tiled.tools.DrawTileTool;
import com.uwsoft.editor.renderer.components.MainItemComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.ZIndexComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import net.mountainblade.modular.annotations.Implementation;


/**
 * Created by mariam on 2/2/2016.
 */
@Implementation(authors = "azakhary", version = "0.0.1")
public class TiledPlugin extends O2DPluginAdapter {

    //-------notifications---------//
    public static final String CLASS_NAME = "com.overlap2d.plugins.tiled";
    public static final String TILE_ADDED       = CLASS_NAME + ".TILE_ADDED";
    public static final String TILE_SELECTED    = CLASS_NAME + ".TILE_SELECTED";
    public static final String PANEL_OPEN       = CLASS_NAME + ".PANEL_OPEN";
    public static final String OPEN_DROP_DOWN   = CLASS_NAME + ".OPEN_DROP_DOWN";
    public static final String DELETE_TILE      = CLASS_NAME + ".DELETE_TILE";
    public static final String GRID_CHANGED     = CLASS_NAME + ".GRID_CHANGED";
    //-------end--------//

    public static final String TILE_ADD_TOOL    = "TILE_ADD_TOOL";
    public static final String TILE_DELETE_TOOL = "TILE_DELETE_TOOL";
    public static final String TILE_TAG         = "TILE";

    public DataToSave dataToSave;
    public SaveDataManager saveDataManager;
    public String selectedTileName = "";
    public boolean isSceneLoaded = false;
    public DrawTileTool drawTileTool;
    public DeleteTileTool deleteTileTool;
    public TiledPanelMediator tiledPanelMediator;
    public ResourcesManager pluginRM;

    public TransformComponent leftComponent;
    public TransformComponent bottomComponent;
    private Entity leftEntity, bottomEntity;

    public TiledPlugin() {
        tiledPanelMediator = new TiledPanelMediator(this);
    }

    @Override
    public void initPlugin() {
        facade.registerMediator(tiledPanelMediator);

        pluginRM = new ResourcesManager(this);

        initTools();

        Skin skin = VisUI.getSkin();
        VisImageButton.VisImageButtonStyle tileAddButtonStyle = new VisImageButton.VisImageButtonStyle();
        tileAddButtonStyle.up = skin.getDrawable("toolbar-normal");
        tileAddButtonStyle.down = skin.getDrawable("toolbar-down");
        tileAddButtonStyle.checked = skin.getDrawable("toolbar-down");
        tileAddButtonStyle.over = skin.getDrawable("toolbar-over");
        tileAddButtonStyle.imageUp = new TextureRegionDrawable(pluginRM.getTextureRegion("tool-tilebrush"));
        pluginAPI.addTool(TILE_ADD_TOOL, tileAddButtonStyle, true, drawTileTool);

        VisImageButton.VisImageButtonStyle tileDeleteButtonStyle = new VisImageButton.VisImageButtonStyle();
        tileDeleteButtonStyle.up = skin.getDrawable("toolbar-normal");
        tileDeleteButtonStyle.down = skin.getDrawable("toolbar-down");
        tileDeleteButtonStyle.checked = skin.getDrawable("toolbar-down");
        tileDeleteButtonStyle.over = skin.getDrawable("toolbar-over");
        tileDeleteButtonStyle.imageUp = new TextureRegionDrawable(pluginRM.getTextureRegion("tool-tileeraser"));
        pluginAPI.addTool(TILE_DELETE_TOOL, tileDeleteButtonStyle, false, deleteTileTool);


    }

    public PluginAPI getPluginAPI() {
        return pluginAPI;
    }

    public void initSaveData() {
        saveDataManager = new SaveDataManager(pluginAPI.getProjectPath());
        dataToSave = saveDataManager.dataToSave;
    }

    private void initTools() {
        drawTileTool = new DrawTileTool(this);
        deleteTileTool = new DeleteTileTool(this);
    }


    public Entity getPluginEntityWithCoordinate(float x, float y) {
        for (Entity entity : pluginAPI.getProjectEntities()) {
            if(!isTile(entity)) continue;
            TransformComponent transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
            Rectangle tmp = new Rectangle(transformComponent.x, transformComponent.y,
                    dataToSave.getParameterVO().gridWidth, dataToSave.getParameterVO().gridHeight);

            boolean isEntityVisible = pluginAPI.isEntityVisible(entity);
            if (isEntityVisible && tmp.contains(x, y) && isOnCurrentSelectedLayer(entity)) {
                return entity;
            }
        }
        return null;
    }

    public float getPixelToWorld() {
        return pluginAPI.getSceneLoader().getRm().getProjectVO().pixelToWorld;
    }

    public boolean isTile(Entity entity) {
        return ComponentRetriever.get(entity, MainItemComponent.class).tags.contains(TILE_TAG);
    }

    public boolean isOnCurrentSelectedLayer(Entity entity) {
        ZIndexComponent entityZComponent = ComponentRetriever.get(entity, ZIndexComponent.class);
        return entityZComponent.layerName.equals(pluginAPI.getCurrentSelectedLayerName());
    }

    public void initPluginMinEntities() {
        bottomEntity = leftEntity = pluginAPI.getProjectEntities().iterator().next();
        pluginAPI.getProjectEntities().forEach(entity -> {
            TransformComponent transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
            leftComponent = ComponentRetriever.get(leftEntity, TransformComponent.class);
            bottomComponent = ComponentRetriever.get(bottomEntity, TransformComponent.class);
            if (transformComponent.x < leftComponent.x) {
                leftComponent = transformComponent;
                leftEntity = entity;
            }
            if (transformComponent.y < bottomComponent.y) {
                bottomComponent = transformComponent;
                bottomEntity = entity;
            }
        });
    }
}
