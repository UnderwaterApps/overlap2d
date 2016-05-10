package com.overlap2d.plugins.tiled.tools;

import com.badlogic.ashley.core.Entity;
import com.commons.view.tools.Tool;
import com.overlap2d.plugins.tiled.TiledPlugin;
import com.puremvc.patterns.observer.Notification;

/**
 * Created by mariam on 4/5/16.
 */
public class DeleteTileTool implements Tool {

    public static final String NAME = "DELETE_TILE_TOOL";

    private TiledPlugin tiledPlugin;

    public DeleteTileTool(TiledPlugin tiledPlugin) {
        this.tiledPlugin = tiledPlugin;
    }

    @Override
    public void initTool() {
    }

    @Override
    public boolean stageMouseDown(float x, float y) {
        return true;
    }

    @Override
    public void stageMouseUp(float x, float y) {
    }

    @Override
    public void stageMouseDragged(float x, float y) {
        deleteEntityWithCoordinate(x, y);
    }

    @Override
    public void stageMouseDoubleClick(float x, float y) {

    }

    @Override
    public boolean itemMouseDown(Entity entity, float x, float y) {
        deleteEntityWithCoordinate(x, y);
        return true;
    }

    @Override
    public void itemMouseUp(Entity entity, float x, float y) {

    }

    @Override
    public void itemMouseDragged(Entity entity, float x, float y) {
        deleteEntityWithCoordinate(x, y);
    }

    @Override
    public void itemMouseDoubleClick(Entity entity, float x, float y) {

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

    }

    private void deleteEntity(Entity entity) {
        if (tiledPlugin.isTile(entity) && tiledPlugin.isOnCurrentSelectedLayer(entity)) {
            tiledPlugin.getPluginAPI().removeFollower(entity);
            tiledPlugin.getPluginAPI().getEngine().removeEntity(entity);
        }
    }

    private void deleteEntityWithCoordinate (float x, float y) {
        Entity entity = tiledPlugin.getPluginEntityWithCoordinate(x, y);
        if (entity != null) {
            deleteEntity(entity);
        }
    }
}
