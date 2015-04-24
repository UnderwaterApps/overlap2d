package com.uwsoft.editor.mvc.view.ui.properties.panels;

import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.uwsoft.editor.mvc.view.ui.properties.UIItemProperties;

/**
 * Created by azakhary on 4/24/15.
 */
public class UILabelItemProperties extends UIItemProperties {

    private VisSelectBox<String> fontFamilySelectBox;

    public UILabelItemProperties() {
        super();

        fontFamilySelectBox = new VisSelectBox<>();

        add(new VisLabel("Font Family", Align.right)).padRight(5).width(50).left();
        add(fontFamilySelectBox).width(55).padRight(5);
        row().padTop(5);

        setListeners();
    }

    private void setListeners() {

    }
}
