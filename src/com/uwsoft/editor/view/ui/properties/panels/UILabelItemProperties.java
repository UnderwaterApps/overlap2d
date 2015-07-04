package com.uwsoft.editor.view.ui.properties.panels;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.util.Validators;
import com.kotcrab.vis.ui.widget.*;
import com.uwsoft.editor.event.CheckBoxChangeListener;
import com.uwsoft.editor.event.KeyboardListener;
import com.uwsoft.editor.event.NumberSelectorOverlapListener;
import com.uwsoft.editor.event.SelectBoxChangeListener;
import com.uwsoft.editor.view.ui.properties.UIItemCollapsibleProperties;

/**
 * Created by azakhary on 4/24/15.
 */
public class UILabelItemProperties extends UIItemCollapsibleProperties {

    public static final String prefix = "com.uwsoft.editor.view.ui.properties.panels.UILabelItemProperties";

    public static final String FONT_FAMILY_SELECTED = prefix + ".FONT_FAMILY_SELECTED";

    private HashMap<Integer, String> alignNames = new HashMap<>();

    private VisSelectBox<String> fontFamilySelectBox;
    private VisSelectBox<String> alignSelectBox;
    private VisCheckBox boldCheckBox;
    private VisCheckBox italicCheckBox;
    private NumberSelector fontSizeField;
    private VisTextArea textArea;

    public UILabelItemProperties() {
        super("Label");

        Validators.IntegerValidator intValidator = new Validators.IntegerValidator();

        fontFamilySelectBox = new VisSelectBox<>();
        alignSelectBox = new VisSelectBox<>();
        boldCheckBox = new VisCheckBox(null);
        italicCheckBox = new VisCheckBox(null);
        fontSizeField = new NumberSelector("", 12, 0, 100);

        fontFamilySelectBox.setMaxListCount(10);
        alignSelectBox.setMaxListCount(10);

        VisTable textEditTable = new VisTable();
        textArea = new VisTextArea();

        mainTable.add(createLabel("Font Family", Align.right)).padRight(5).width(90).left();
        mainTable.add(fontFamilySelectBox).width(90).padRight(5);
        mainTable.row().padTop(5);
        /*
        mainTable.add(createLabel("Bold", Align.right)).padRight(5).width(90).left();
        mainTable.add(boldCheckBox).width(55).padRight(5);
        mainTable.row().padTop(5);
        mainTable.add(createLabel("Italic", Align.right)).padRight(5).width(90).left();
        mainTable.add(italicCheckBox).width(55).padRight(5);
        mainTable.row().padTop(5);
        */
        mainTable.add(createLabel("Font Size", Align.right)).padRight(5).width(90).left();
        mainTable.add(fontSizeField).width(55).padRight(5);
        mainTable.row().padTop(5);
        mainTable.add(createLabel("Align", Align.right)).padRight(5).width(90).left();
        mainTable.add(alignSelectBox).width(90).padRight(5);
        mainTable.row().padTop(5);
        mainTable.add(textEditTable).colspan(2).width(200);
        mainTable.row().padTop(5);

        textEditTable.add(textArea).width(200).height(65);

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

    public String getText() {
        return textArea.getText();
    }

    public void setText(String text) {
        textArea.setText(text);
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

    public int getFontSize() {
        return fontSizeField.getValue();
    }

    public void setFontSize(int fontSize) {
        fontSizeField.setValue(fontSize);
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    private void setListeners() {
        fontFamilySelectBox.addListener(new SelectBoxChangeListener(getUpdateEventName()));
        alignSelectBox.addListener(new SelectBoxChangeListener(getUpdateEventName()));
        boldCheckBox.addListener(new CheckBoxChangeListener(getUpdateEventName()));
        italicCheckBox.addListener(new CheckBoxChangeListener(getUpdateEventName()));
        fontSizeField.addChangeListener(new NumberSelectorOverlapListener(getUpdateEventName()));
        textArea.addListener(new KeyboardListener(getUpdateEventName()));
    }
}
