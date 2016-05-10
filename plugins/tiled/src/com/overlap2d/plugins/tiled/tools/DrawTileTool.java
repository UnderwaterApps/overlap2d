package com.overlap2d.plugins.tiled.tools;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.commons.view.tools.Tool;
import com.overlap2d.plugins.tiled.TiledPlugin;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.MainItemComponent;
import com.uwsoft.editor.renderer.components.TextureRegionComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.data.ProjectInfoVO;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.renderer.utils.CustomVariables;

/**
 * Created by mariam on 3/29/16.
 */
public class DrawTileTool implements Tool {

    public static final String NAME = "ADD_TILE_TOOL";

    private TiledPlugin tiledPlugin;
    private float gridWidth;
    private float gridHeight;

    public DrawTileTool(TiledPlugin tiledPlugin) {
        this.tiledPlugin = tiledPlugin;
    }

    @Override
    public void initTool() {

    }

    @Override
    public boolean stageMouseDown(float x, float y) {
        initGridThings();
        drawImage(x, y);
        return true;
    }

    @Override
    public void stageMouseUp(float x, float y) {
    }

    @Override
    public void stageMouseDragged(float x, float y) {
        drawImage(x, y);
    }

    @Override
    public void stageMouseDoubleClick(float x, float y) {

    }

    @Override
    public boolean itemMouseDown(Entity entity, float x, float y) {
        initGridThings();
        drawOnEntity(entity, x, y);
        return true;
    }

    @Override
    public void itemMouseUp(Entity entity, float x, float y) {
    }

    @Override
    public void itemMouseDragged(Entity entity, float x, float y) {
        drawImage(x, y);
    }

    @Override
    public void itemMouseDoubleClick(Entity entity, float x, float y) {
        if (!tiledPlugin.isOnCurrentSelectedLayer(entity)) return;
        if (entity != null) {
            TextureRegionComponent textureRegionComponent = ComponentRetriever.get(entity, TextureRegionComponent.class);
            // there is already other tile under this one
            if (textureRegionComponent.regionName.equals(tiledPlugin.selectedTileName)) {
                //rotate
                TransformComponent transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
                if (transformComponent.scaleX > 0 && transformComponent.scaleY > 0) {
                    transformComponent.scaleX *= -1f;
                    return;
                }
                if (transformComponent.scaleX < 0 && transformComponent.scaleY > 0) {
                    transformComponent.scaleY *= -1f;
                    return;
                }
                if (transformComponent.scaleX < 0 && transformComponent.scaleY < 0) {
                    transformComponent.scaleX *= -1f;
                    return;
                }
                if (transformComponent.scaleX > 0 && transformComponent.scaleY < 0) {
                    transformComponent.scaleY *= -1f;
                    return;
                }
            }
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void handleNotification(Notification notification) {

    }

    @Override
    public void keyDown(Entity entity, int keycode) {
        if(keycode == Input.Keys.SHIFT_LEFT) {
            tiledPlugin.getPluginAPI().toolHotSwap(tiledPlugin.deleteTileTool);
            tiledPlugin.deleteTileTool.setHotSwapped();
        }
    }

    @Override
    public void keyUp(Entity entity, int keycode) {

    }

    private void initGridThings() {
        gridWidth = tiledPlugin.dataToSave.getParameterVO().gridWidth;
        gridHeight = tiledPlugin.dataToSave.getParameterVO().gridHeight;
    }

    private void drawImage(float x, float y) {
        if (tiledPlugin.selectedTileName.equals("")) return;
        Entity underneathTile = tiledPlugin.getPluginEntityWithCoordinate(x, y);
        if (underneathTile != null) {
            updateRegion(underneathTile, tiledPlugin.selectedTileName);
            return;
        }
        float newX = MathUtils.floor(x / gridWidth) * gridWidth;
        float newY = MathUtils.floor(y / gridHeight) * gridHeight;

        Entity imageEntity = tiledPlugin.getPluginAPI().drawImage(tiledPlugin.selectedTileName, new Vector2(newX, newY));
        MainItemComponent mainItemComponent = ComponentRetriever.get(imageEntity, MainItemComponent.class);
        mainItemComponent.tags.add(TiledPlugin.TILE_TAG);

        int row = MathUtils.floor(newY / gridHeight);
        int column = MathUtils.floor(newX / gridWidth);
        CustomVariables customVariables = new CustomVariables();
        customVariables.setVariable("row", Float.toString(row));
        customVariables.setVariable("column", Float.toString(column));
        mainItemComponent.customVars = customVariables.saveAsString();
    }

    private void drawOnEntity(Entity entity, float x, float y) {
        if (!tiledPlugin.isOnCurrentSelectedLayer(entity)) return;
        if (entity != null) {
            TextureRegionComponent textureRegionComponent = ComponentRetriever.get(entity, TextureRegionComponent.class);
            // there is already other tile under this one
            if(textureRegionComponent.regionName.equals(tiledPlugin.selectedTileName)) {
                return;
            } else {
                //replace
                updateRegion(entity, tiledPlugin.selectedTileName);
            }
            return;
        }
        drawImage(x, y);
    }

    private void updateRegion(Entity entity, String region) {
        TextureRegionComponent textureRegionComponent = ComponentRetriever.get(entity, TextureRegionComponent.class);
        DimensionsComponent size = ComponentRetriever.get(entity, DimensionsComponent.class);
        textureRegionComponent.regionName = tiledPlugin.selectedTileName;
        textureRegionComponent.region = tiledPlugin.getPluginAPI().getSceneLoader().getRm().getTextureRegion(region);
        ProjectInfoVO projectInfoVO = tiledPlugin.getPluginAPI().getSceneLoader().getRm().getProjectVO();
        float ppwu = projectInfoVO.pixelToWorld;
        size.width = textureRegionComponent.region.getRegionWidth() / ppwu;
        size.height = textureRegionComponent.region.getRegionHeight() / ppwu;
    }

}
