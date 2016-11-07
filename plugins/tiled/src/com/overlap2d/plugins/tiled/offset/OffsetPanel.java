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

    private final String TILE_OFFSET_X = "Tile offset x";
    private final String TILE_OFFSET_Y = "Tile offset y";

    private TiledPlugin tiledPlugin;
    private Table mainTable;
    private Category offsetCategory;
    private AttributeVO offsetAttributeX;
    private AttributeVO offsetAttributeY;


    public OffsetPanel(TiledPlugin tiledPlugin) {
        super("Offset");

        this.tiledPlugin = tiledPlugin;
        addCloseButton();

        mainTable = new Table();
        add(mainTable).pad(3);

        initView();
    }

    private void initView() {

        offsetAttributeX = new AttributeVO(TILE_OFFSET_X, true);
        offsetAttributeY = new AttributeVO(TILE_OFFSET_Y, true);

        Array<AttributeVO> attributeVOs = new Array<>();
        attributeVOs.add(offsetAttributeX);
        attributeVOs.add(offsetAttributeY);
        offsetCategory = new Category(new CategoryVO("", attributeVOs));
        mainTable.add(offsetCategory)
                .pad(7)
                .row();

        VisTextButton addButton = new VisTextButton("Set");
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
        offsetAttributeX = new AttributeVO(TILE_OFFSET_X, tiledPlugin.getSelectedTileGridOffset().x, true);
        offsetAttributeY = new AttributeVO(TILE_OFFSET_Y, tiledPlugin.getSelectedTileGridOffset().y, true);

        Array<AttributeVO> attributeVOs = new Array<>();
        attributeVOs.add(offsetAttributeX);
        attributeVOs.add(offsetAttributeY);
        offsetCategory.reInitView(attributeVOs);
    }
}
