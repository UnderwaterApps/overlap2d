package com.overlap2d.plugins.tiled;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.commons.UIDraggablePanel;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.overlap2d.plugins.tiled.data.AttributeVO;
import com.overlap2d.plugins.tiled.view.Attribute;

/**
 * Created by mariam on 5/12/16.
 */
public class OffsetPanel extends UIDraggablePanel {

    private TiledPlugin tiledPlugin;
    private AttributeVO offsetAttribute;


    public OffsetPanel(TiledPlugin tiledPlugin) {
        super("Offset");

        this.tiledPlugin = tiledPlugin;
        addCloseButton();

        initView();
    }

    private void initView() {
        offsetAttribute = new AttributeVO();
        offsetAttribute.title = "Tile offset";
        Attribute offset = new Attribute(offsetAttribute);
        add(offset)
            .pad(7);

        VisTextButton addButton = new VisTextButton("Add");
        addButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                tiledPlugin.facade.sendNotification(TiledPlugin.TILE_GRID_OFFSET_ADDED, offsetAttribute.value);
                super.clicked(event, x, y);
            }
        });
        add(addButton)
            .pad(7);
    }
}
