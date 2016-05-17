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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.commons.plugins.O2DPluginAdapter;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.overlap2d.plugins.tiled.data.TileVO;
import com.overlap2d.plugins.tiled.manager.ResourcesManager;
import com.overlap2d.plugins.tiled.offset.OffsetPanel;
import com.overlap2d.plugins.tiled.offset.OffsetPanelMediator;
import com.overlap2d.plugins.tiled.save.DataToSave;
import com.overlap2d.plugins.tiled.save.SaveDataManager;
import com.overlap2d.plugins.tiled.tools.DeleteTileTool;
import com.overlap2d.plugins.tiled.tools.DrawTileTool;
import com.uwsoft.editor.renderer.components.MainItemComponent;
import com.uwsoft.editor.renderer.components.TextureRegionComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.ZIndexComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.renderer.utils.CustomVariables;
import net.mountainblade.modular.annotations.Implementation;


/**
 * Created by mariam on 2/2/2016.
 */
@Implementation(authors = "azakhary", version = "0.0.1")
public class TiledPlugin extends O2DPluginAdapter {

    //-------notifications---------//
    public static final String CLASS_NAME = "com.overlap2d.plugins.tiled";
    public static final String TILE_ADDED                   = CLASS_NAME + ".TILE_ADDED";
    public static final String TILE_SELECTED                = CLASS_NAME + ".TILE_SELECTED";
    public static final String PANEL_OPEN                   = CLASS_NAME + ".PANEL_OPEN";
    public static final String OPEN_DROP_DOWN               = CLASS_NAME + ".OPEN_DROP_DOWN";
    public static final String GRID_CHANGED                 = CLASS_NAME + ".GRID_CHANGED";
    public static final String ACTION_DELETE_TILE           = CLASS_NAME + ".ACTION_DELETE_TILE";
    public static final String ACTION_SET_OFFSET            = CLASS_NAME + ".ACTION_SET_OFFSET";
    public static final String ACTION_OPEN_OFFSET_PANEL     = CLASS_NAME + ".ACTION_OPEN_OFFSET_PANEL";
    public static final String TILE_GRID_OFFSET_ADDED       = CLASS_NAME + ".TILE_GRID_OFFSET_ADDED";
    //-------end--------//

    public static final String TILE_ADD_TOOL    = "TILE_ADD_TOOL";
    public static final String TILE_DELETE_TOOL = "TILE_DELETE_TOOL";
    public static final String TILE_TAG         = "TILE";
    public static final String ROW = "ROW";
    public static final String COLUMN = "COLUMN";

    public DataToSave dataToSave;
    public SaveDataManager saveDataManager;
    public boolean isSceneLoaded = false;
    public DrawTileTool drawTileTool;
    public DeleteTileTool deleteTileTool;
    public ResourcesManager pluginRM;
    public OffsetPanel offsetPanel;

    private TileVO selectedTileVO;
    private CustomVariables currentEntityCustomVariables;
    private MainItemComponent currentEntityMainItemComponent;
    private TransformComponent currentEntityTransformComponent;

    public TiledPlugin() {
        selectedTileVO = new TileVO();
        currentEntityCustomVariables = new CustomVariables();
    }

    @Override
    public void initPlugin() {
        facade.registerMediator(new TiledPanelMediator(this));

        pluginRM = new ResourcesManager(this);
        offsetPanel = new OffsetPanel(this);

        facade.registerMediator(new OffsetPanelMediator(this));

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

    public void initSaveData() {
        saveDataManager = new SaveDataManager(pluginAPI.getProjectPath());
        dataToSave = saveDataManager.dataToSave;
    }

    private void initTools() {
        drawTileTool = new DrawTileTool(this);
        deleteTileTool = new DeleteTileTool(this);
    }


    public Entity getPluginEntityWithParams(int row, int column) {
        for (Entity entity : pluginAPI.getProjectEntities()) {
            if(!isTile(entity)) continue;
            boolean isEntityVisible = pluginAPI.isEntityVisible(entity);
            if (!isEntityVisible || !isOnCurrentSelectedLayer(entity)) continue;

            currentEntityMainItemComponent = ComponentRetriever.get(entity, MainItemComponent.class);
            currentEntityCustomVariables.loadFromString(currentEntityMainItemComponent.customVars);
            if (currentEntityCustomVariables.getIntegerVariable(ROW) == row
                    && currentEntityCustomVariables.getIntegerVariable(COLUMN) == column) {
                return entity;
            }
        }
        return null;
    }

    public Entity getPluginEntityWithCoords(float x, float y) {
        for (Entity entity : pluginAPI.getProjectEntities()) {
            if (!isTile(entity)) continue;
            boolean isEntityVisible = pluginAPI.isEntityVisible(entity);
            if (!isEntityVisible || !isOnCurrentSelectedLayer(entity)) continue;

            currentEntityTransformComponent = ComponentRetriever.get(entity, TransformComponent.class);
            Rectangle tmp = new Rectangle(
                    currentEntityTransformComponent.x,
                    currentEntityTransformComponent.y,
                    dataToSave.getParameterVO().gridWidth,
                    dataToSave.getParameterVO().gridHeight);
            if (tmp.contains(x, y)) {
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

    public void setSelectedTileName (String regionName) {
        selectedTileVO.regionName = regionName;
    }

    public String getSelectedTileName() {
        return selectedTileVO.regionName;
    }

    public Vector2 getSelectedTileGridOffset() {
        return selectedTileVO.gridOffset;
    }

    public void setSelectedTileGridOffset (Vector2 gridOffset) {
        selectedTileVO.gridOffset = gridOffset;
    }

    public TileVO getSelectedTileVO() {
        return selectedTileVO;
    }

    public void setSelectedTileVO(TileVO selectedTileVO) {
        this.selectedTileVO = selectedTileVO;
    }

    public void applySelectedTileGridOffset() {
        pluginAPI.getProjectEntities().forEach(entity -> {
            if (!(isTile(entity))) return;
            TextureRegionComponent textureRegionComponent = ComponentRetriever.get(entity, TextureRegionComponent.class);
            TransformComponent transformComponent  =ComponentRetriever.get(entity, TransformComponent.class);
            if (selectedTileVO.regionName.equals(textureRegionComponent.regionName)) {
                transformComponent.x -= selectedTileVO.gridOffset.x;
                transformComponent.y -= selectedTileVO.gridOffset.y;
            }
        });
        saveOffsetChanges();
    }

    private void saveOffsetChanges() {
        dataToSave.setTileGridOffset(selectedTileVO);
        saveDataManager.save();
    }
}
