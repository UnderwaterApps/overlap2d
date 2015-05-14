package com.uwsoft.editor.mvc.view.ui.properties.panels;

import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.util.Validators;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisValidableTextField;
import com.uwsoft.editor.mvc.event.CheckBoxChangeListener;
import com.uwsoft.editor.mvc.event.KeyboardListener;
import com.uwsoft.editor.mvc.event.SelectBoxChangeListener;
import com.uwsoft.editor.mvc.view.ui.properties.UIItemCollapsibleProperties;

/**
 * Created by azakhary on 4/24/15.
 */
public class UILabelItemProperties extends UIItemCollapsibleProperties {

    public static final String prefix = "com.uwsoft.editor.mvc.view.ui.properties.panels.UILabelItemProperties";

    public static final String FONT_FAMILY_SELECTED = prefix + ".FONT_FAMILY_SELECTED";

    private VisSelectBox<String> fontFamilySelectBox;
    private VisCheckBox boldCheckBox;
    private VisCheckBox italicCheckBox;
    private VisValidableTextField fontSizeField;

    public UILabelItemProperties() {
        super("Label");

        Validators.IntegerValidator intValidator = new Validators.IntegerValidator();

        fontFamilySelectBox = new VisSelectBox<>();
        boldCheckBox = new VisCheckBox(null);
        italicCheckBox = new VisCheckBox(null);
        fontSizeField = new VisValidableTextField(intValidator);

        fontFamilySelectBox.setMaxListCount(10);

        mainTable.add(createLabel("Font Family", Align.right)).padRight(5).width(50).left();
        mainTable.add(fontFamilySelectBox).width(90).padRight(5);
        mainTable.row().padTop(5);
        mainTable.add(createLabel("Bold", Align.right)).padRight(5).width(50).left();
        mainTable.add(boldCheckBox).width(55).padRight(5);
        mainTable.row().padTop(5);
        mainTable.add(createLabel("Italic", Align.right)).padRight(5).width(50).left();
        mainTable.add(italicCheckBox).width(55).padRight(5);
        mainTable.row().padTop(5);
        mainTable.add(createLabel("Font Size", Align.right)).padRight(5).width(50).left();
        mainTable.add(fontSizeField).width(55).padRight(5);
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
