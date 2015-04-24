package com.uwsoft.editor.mvc.view.ui.properties.panels;

import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisValidableTextField;
import com.uwsoft.editor.mvc.event.CheckBoxChangeListener;
import com.uwsoft.editor.mvc.event.KeyboardListener;
import com.uwsoft.editor.mvc.event.SelectBoxChangeListener;
import com.uwsoft.editor.mvc.view.ui.properties.UIAbstractProperties;
import com.uwsoft.editor.mvc.view.ui.properties.UIItemProperties;

/**
 * Created by azakhary on 4/24/15.
 */
public class UILabelItemProperties extends UIItemProperties {

    public static final String prefix = "com.uwsoft.editor.mvc.view.ui.properties.panels.UILabelItemProperties";

    public static final String FONT_FAMILY_SELECTED = prefix + ".FONT_FAMILY_SELECTED";

    private VisSelectBox<String> fontFamilySelectBox;
    private VisCheckBox boldCheckBox;
    private VisCheckBox italicCheckBox;
    private VisValidableTextField fontSizeField;

    public UILabelItemProperties() {
        super();


        fontFamilySelectBox = new VisSelectBox<>();
        boldCheckBox = new VisCheckBox(null);
        italicCheckBox = new VisCheckBox(null);
        fontSizeField = new VisValidableTextField();

        add(new VisLabel("Font Family", Align.right)).padRight(5).width(50).left();
        add(fontFamilySelectBox).width(55).padRight(5);
        row().padTop(5);
        add(new VisLabel("Bold", Align.right)).padRight(5).width(50).left();
        add(boldCheckBox).width(55).padRight(5);
        row().padTop(5);
        add(new VisLabel("Italic", Align.right)).padRight(5).width(50).left();
        add(italicCheckBox).width(55).padRight(5);
        row().padTop(5);
        add(new VisLabel("Font Size", Align.right)).padRight(5).width(50).left();
        add(fontSizeField).width(55).padRight(5);
        row().padTop(5);

        setListeners();
    }

    public String getFontFamily() {
        return fontFamilySelectBox.getSelected();
    }

    public boolean isBold() {
        return boldCheckBox.isChecked();
    }

    public boolean isItalic() {
        return italicCheckBox.isChecked();
    }

    public void setFontFamilyList(Array<String> fontFamilies) {
        fontFamilySelectBox.setItems(fontFamilies);
    }

    public void setFontFamily(String name) {

    }

    public void setStyle(boolean bold, boolean italic) {
        boldCheckBox.setChecked(bold);
        italicCheckBox.setChecked(italic);
    }

    public String getFontSize() {
        return fontSizeField.getText();
    }

    public void setFontSize(String fontSize) {
        fontSizeField.setText(fontSize);
    }


    private void setListeners() {
        fontFamilySelectBox.addListener(new SelectBoxChangeListener(PROPERTIES_UPDATED));
        boldCheckBox.addListener(new CheckBoxChangeListener(PROPERTIES_UPDATED));
        italicCheckBox.addListener(new CheckBoxChangeListener(PROPERTIES_UPDATED));
        fontSizeField.addListener(new KeyboardListener(PROPERTIES_UPDATED));
    }
}
