package com.overlap2d.plugins.tiled.view.tabs;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.overlap2d.plugins.tiled.TiledPanel;
import com.overlap2d.plugins.tiled.data.AttributeVO;
import com.overlap2d.plugins.tiled.data.CategoryVO;
import com.overlap2d.plugins.tiled.data.ParameterVO;
import com.overlap2d.plugins.tiled.view.Category;

/**
 * Created by mariam on 2/4/16.
 */
public class SettingsTab extends DefaultTab {

    private static final String CLASS_NAME = "com.overlap2d.plugins.tiled.view.tabs.SettingsTab";
    public static final String OK_BTN_CLICKED = CLASS_NAME+".OK_BTN_CLICKED";

    private ParameterVO currentParameters;
    private Category grid;

    public SettingsTab(TiledPanel panel, String tabTitle, int tabIndex) {
        super(panel, tabTitle, tabIndex);

        currentParameters = panel.tiledPlugin.dataToSave.getParameterVO();
    }

    @Override
    public void initView() {
        Array<AttributeVO> gridAttributes = new Array<>();
        gridAttributes.add(new AttributeVO("Width", currentParameters.gridWidth));
        gridAttributes.add(new AttributeVO("Height", currentParameters.gridHeight));
        CategoryVO gridVO = new CategoryVO("Grid size: ", gridAttributes);
        grid = new Category(gridVO);
        content.add(grid)
                .expandX()
                .colspan(2)
                .padTop(10)
                .left()
                .top()
                .row();
        panel.tiledPlugin.dataToSave.setParameterVO(currentParameters);

        VisTextButton okBtn = new VisTextButton("Save");
        content.add(okBtn)
                .width(70)
                .pad(20)
                .colspan(2)
                .expandX()
                .center()
                .bottom()
                .row();

        okBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                currentParameters.gridWidth = grid.getAttributeVO("Width: ").value;
                currentParameters.gridHeight = grid.getAttributeVO("Height: ").value;
                panel.facade.sendNotification(OK_BTN_CLICKED, currentParameters);
            }
        });
    }

    public void resetGridCategory() {
        Array<AttributeVO> gridAttributes = new Array<>();
        gridAttributes.add(new AttributeVO("Width: ", currentParameters.gridWidth));
        gridAttributes.add(new AttributeVO("Height: ", currentParameters.gridHeight));
        grid.reInitView(gridAttributes);
    }
}
