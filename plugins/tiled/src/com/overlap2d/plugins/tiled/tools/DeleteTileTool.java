package com.overlap2d.plugins.tiled.tools;

import com.badlogic.ashley.core.Entity;
import com.commons.MsgAPI;
import com.commons.view.tools.Tool;
import com.overlap2d.plugins.tiled.TiledPlugin;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.renderer.components.MainItemComponent;
import com.uwsoft.editor.renderer.components.TextureRegionComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

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
        return true;
    }

    @Override
    public void itemMouseUp(Entity entity, float x, float y) {
        deleteEntity(entity);
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
        tiledPlugin.getPluginAPI().selectEntity(entity);

        if (tiledPlugin.isTile(entity)) { // check is special for itemMouseUp(). Item may not be tile
            String regionName = ComponentRetriever.get(entity, TextureRegionComponent.class).regionName;
            tiledPlugin.facade.sendNotification(MsgAPI.ACTION_DELETE, regionName);
            tiledPlugin.facade.sendNotification(MsgAPI.DELETE_ITEMS_COMMAND_DONE, regionName);
        }
    }

    private void deleteEntityWithCoordinate (float x, float y) {
        Entity entity = tiledPlugin.getPluginEntityWithCoordinate(x, y);
        if (entity != null) {
            deleteEntity(entity);
        }
    }
}
