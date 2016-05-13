package com.overlap2d.plugins.tiled.offset;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.commons.UIDraggablePanel;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.overlap2d.plugins.tiled.TiledPlugin;
import com.overlap2d.plugins.tiled.data.AttributeVO;
import com.overlap2d.plugins.tiled.view.Attribute;

/**
 * Created by mariam on 5/12/16.
 */
public class OffsetPanel extends UIDraggablePanel {

    private TiledPlugin tiledPlugin;
    private AttributeVO offsetAttributeX;
    private AttributeVO offsetAttributeY;


    public OffsetPanel(TiledPlugin tiledPlugin) {
        super("Offset");

        this.tiledPlugin = tiledPlugin;
        addCloseButton();

        initView();
    }

    private void initView() {
        offsetAttributeX = new AttributeVO();
        offsetAttributeX.title = "Tile offset x";
        Attribute offsetX = new Attribute(offsetAttributeX);
        add(offsetX)
            .pad(7)
            .row();

        offsetAttributeY = new AttributeVO();
        offsetAttributeY.title = "Tile offset y";
        Attribute offsetY = new Attribute(offsetAttributeY);
        add(offsetY)
            .pad(7)
            .row();

        VisTextButton addButton = new VisTextButton("Add");
        addButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Vector2 offset = new Vector2(offsetAttributeX.value, offsetAttributeY.value);
                tiledPlugin.facade.sendNotification(TiledPlugin.TILE_GRID_OFFSET_ADDED, offset);
                super.clicked(event, x, y);
            }
        });
        add(addButton)
            .pad(7)
            .center();
    }
}
