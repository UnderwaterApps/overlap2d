package com.uwsoft.editor.mvc.view.ui.properties.panels;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by azakhary on 4/24/15.
 */
public class UILabelItemProperties extends UIItemCollapsibleProperties {

    public static final String prefix = "com.uwsoft.editor.mvc.view.ui.properties.panels.UILabelItemProperties";

    public static final String FONT_FAMILY_SELECTED = prefix + ".FONT_FAMILY_SELECTED";

    private HashMap<Integer, String> alignNames = new HashMap<>();

    private VisSelectBox<String> fontFamilySelectBox;
    private VisSelectBox<String> alignSelectBox;
    private VisCheckBox boldCheckBox;
    private VisCheckBox italicCheckBox;
    private VisValidableTextField fontSizeField;

    public UILabelItemProperties() {
        super("Label");

        Validators.IntegerValidator intValidator = new Validators.IntegerValidator();

        fontFamilySelectBox = new VisSelectBox<>();
        alignSelectBox = new VisSelectBox<>();
        boldCheckBox = new VisCheckBox(null);
        italicCheckBox = new VisCheckBox(null);
        fontSizeField = new VisValidableTextField(intValidator);

        fontFamilySelectBox.setMaxListCount(10);
        alignSelectBox.setMaxListCount(10);

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
        mainTable.row().padTop(5);
        mainTable.add(createLabel("Align", Align.right)).padRight(5).width(50).left();
        mainTable.add(alignSelectBox).width(90).padRight(5);
        mainTable.row().padTop(5);
        setListeners();

        setAlignList();
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

    public void setAlignList() {
        alignNames.clear();
        alignNames.put(Align.left, "Left");
        alignNames.put(Align.right, "Right");
        alignNames.put(Align.center, "Center");
        alignNames.put(Align.topLeft, "Top Left");
        alignNames.put(Align.topRight, "Top Right");
        alignNames.put(Align.bottomLeft, "Bottom Left");
        alignNames.put(Align.bottomRight, "Bottom Right");
        alignNames.put(Align.top, "Top");
        alignNames.put(Align.bottom, "Bottom");

        String[] arr = new String[alignNames.size()];
        int i = 0;
        for (Map.Entry<Integer, String> entry : alignNames.entrySet()) {
            arr[i++] = entry.getValue();
        }
        alignSelectBox.setItems(arr);
    }

    public void setAlignValue(int align) {
        alignSelectBox.setSelected(alignNames.get(align));
    }

    public int getAlignValue() {
        for (Map.Entry<Integer, String> entry : alignNames.entrySet()) {
            Integer key = entry.getKey();
            String value = entry.getValue();
            if(alignSelectBox.getSelected().equals(value)) {
                return key;
            }
        }
        return 0;
    }


    public void setFontFamilyList(Array<String> fontFamilies) {
        fontFamilySelectBox.setItems(fontFamilies);
    }

    public void setFontFamily(String name) {
        fontFamilySelectBox.setSelected(name);
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
        alignSelectBox.addListener(new SelectBoxChangeListener(PROPERTIES_UPDATED));
        boldCheckBox.addListener(new CheckBoxChangeListener(PROPERTIES_UPDATED));
        italicCheckBox.addListener(new CheckBoxChangeListener(PROPERTIES_UPDATED));
        fontSizeField.addListener(new KeyboardListener(PROPERTIES_UPDATED));
    }
}
