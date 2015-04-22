package com.uwsoft.editor.mvc.view.ui.box;

import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.uwsoft.editor.mvc.event.SelectBoxChangeListener;

/**
 * Created by CyberJoe on 4/22/2015.
 */
public class UIFontChooser extends UIBaseBox {

    public static final String FONT_SELECTED = "com.uwsoft.editor.mvc.view.ui.box.UIFontChooser" + ".FONT_SELECTED";

    private VisSelectBox<String> selectBox;

    public UIFontChooser() {
        selectBox = new VisSelectBox<>();
        add(selectBox);

        selectBox.addListener(new SelectBoxChangeListener(FONT_SELECTED));
    }

    public void setSelectBoxItems(Array<String> newItems) {
        selectBox.setItems(newItems);
    }

    public String getSelectedFont() {
        return selectBox.getSelected();
    }

    @Override
    public void update() {

    }
}
