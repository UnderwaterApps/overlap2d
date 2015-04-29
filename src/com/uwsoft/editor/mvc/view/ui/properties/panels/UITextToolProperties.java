package com.uwsoft.editor.mvc.view.ui.properties.panels;

import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisValidableTextField;
import com.uwsoft.editor.mvc.event.CheckBoxChangeListener;
import com.uwsoft.editor.mvc.event.KeyboardListener;
import com.uwsoft.editor.mvc.event.SelectBoxChangeListener;
import com.uwsoft.editor.mvc.view.ui.properties.UIItemProperties;

/**
 * Created by azakhary on 4/24/15.
 */
public class UITextToolProperties extends UILabelItemProperties {

    public static final String prefix = "com.uwsoft.editor.mvc.view.ui.properties.panels.UITextToolProperties";

    public static final String FONT_FAMILY_SELECTED = prefix + ".FONT_FAMILY_SELECTED";

    public UITextToolProperties() {
        super();

    }
}
