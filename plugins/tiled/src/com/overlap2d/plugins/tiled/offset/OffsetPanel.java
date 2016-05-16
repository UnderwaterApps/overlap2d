package com.overlap2d.plugins.tiled.offset;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.commons.UIDraggablePanel;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.overlap2d.plugins.tiled.TiledPlugin;
import com.overlap2d.plugins.tiled.data.AttributeVO;
import com.overlap2d.plugins.tiled.data.CategoryVO;
import com.overlap2d.plugins.tiled.view.Category;

/**
 * Created by mariam on 5/12/16.
 */
public class OffsetPanel extends UIDraggablePanel {

    private final String TILE_OFFSET_WIDTH = "Tile offset width";
    private final String TILE_OFFSET_HEIGHT = "Tile offset height";

    private TiledPlugin tiledPlugin;
    private Table mainTable;
    private Category offsetCategory;


    public OffsetPanel(TiledPlugin tiledPlugin) {
        super("Offset");

        this.tiledPlugin = tiledPlugin;
        addCloseButton();

        mainTable = new Table();
        add(mainTable).pad(3);

        initView();
    }

    private void initView() {

        AttributeVO offsetAttributeX = new AttributeVO(TILE_OFFSET_WIDTH);
        AttributeVO offsetAttributeY = new AttributeVO(TILE_OFFSET_HEIGHT);

        Array<AttributeVO> attributeVOs = new Array<>();
        attributeVOs.add(offsetAttributeX);
        attributeVOs.add(offsetAttributeY);
        offsetCategory = new Category(new CategoryVO("", attributeVOs));
        mainTable.add(offsetCategory)
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
        mainTable.add(addButton);
    }

    public void refreshOffsetValues() {
        AttributeVO offsetAttributeX = new AttributeVO(TILE_OFFSET_WIDTH, tiledPlugin.getSelectedTileGridOffset().x);
        AttributeVO offsetAttributeY = new AttributeVO(TILE_OFFSET_HEIGHT, tiledPlugin.getSelectedTileGridOffset().y);

        Array<AttributeVO> attributeVOs = new Array<>();
        attributeVOs.add(offsetAttributeX);
        attributeVOs.add(offsetAttributeY);
        offsetCategory.reInitView(attributeVOs);
    }
}
