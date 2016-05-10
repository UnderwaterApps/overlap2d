package com.overlap2d.plugins.tiled.view;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.util.FloatDigitsOnlyFilter;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.overlap2d.plugins.tiled.data.AttributeVO;

/**
 * Created by mariam on 2/5/16.
 */
public class Attribute extends Table {

    public Attribute(AttributeVO attributeVO) {
        add(new VisLabel(attributeVO.title));
        VisTextField visTextField = new VisTextField();
        visTextField.setTextFieldFilter(new FloatDigitsOnlyFilter(false));
        visTextField.setMaxLength(5);
        visTextField.setText(attributeVO.value+"");
        visTextField.setTextFieldListener((VisTextField textField, char c) -> {
            if (!textField.getText().equals("")) {
                attributeVO.value = Float.parseFloat(textField.getText());
            }
        });
        add(visTextField)
                .width(50)
                .padLeft(5);
    }
}
