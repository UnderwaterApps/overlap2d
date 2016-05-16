package com.overlap2d.plugins.tiled.offset;

import com.badlogic.gdx.math.Vector2;
import com.overlap2d.plugins.tiled.TiledPlugin;
import com.puremvc.patterns.mediator.SimpleMediator;
import com.puremvc.patterns.observer.Notification;

/**
 * Created by mariam on 5/12/16.
 */
public class OffsetPanelMediator extends SimpleMediator<OffsetPanel> {

    private static final String TAG = OffsetPanelMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    private TiledPlugin tiledPlugin;


    public OffsetPanelMediator(TiledPlugin tiledPlugin) {
        super(NAME, tiledPlugin.offsetPanel);

        this.tiledPlugin = tiledPlugin;
    }

    @Override
    public String[] listNotificationInterests() {
        return new String[] {
                TiledPlugin.ACTION_OPEN_OFFSET_PANEL,
                TiledPlugin.TILE_GRID_OFFSET_ADDED
        };
    }

    @Override
    public void handleNotification(Notification notification) {
        super.handleNotification(notification);

        switch (notification.getName()) {
            case TiledPlugin.ACTION_OPEN_OFFSET_PANEL:
                viewComponent.refreshOffsetValues();
                viewComponent.show(tiledPlugin.getAPI().getUIStage());
                break;
            case TiledPlugin.TILE_GRID_OFFSET_ADDED:
                Vector2 offsetValue = notification.getBody();
                tiledPlugin.setSelectedTileGridOffset(offsetValue);
                tiledPlugin.applySelectedTileGridOffset();
                break;
        }

    }
}
