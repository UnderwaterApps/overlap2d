/*
 * ******************************************************************************
 *  * Copyright 2015 See AUTHORS file.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

package com.uwsoft.editor.mvc.view.ui.properties.boxes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.util.Validators;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.VisValidableTextField;
import com.kotcrab.vis.ui.widget.color.ColorPicker;
import com.kotcrab.vis.ui.widget.color.ColorPickerAdapter;
import com.uwsoft.editor.gdx.ui.components.ColorPickerButton;
import com.uwsoft.editor.mvc.event.CheckBoxChangeListener;
import com.uwsoft.editor.mvc.event.KeyboardListener;
import com.uwsoft.editor.mvc.view.ui.properties.UIAbstractProperties;
import com.uwsoft.editor.mvc.view.ui.properties.UIItemProperties;

/**
 * Created by azakhary on 4/15/2015.
 */
public class UIBasicItemProperties extends UIItemProperties {

    public static final String TINT_COLOR_BUTTON_CLICKED = "com.uwsoft.editor.mvc.view.ui.properties.boxes.UIBasicItemProperties" + ".TINT_COLOR_BUTTON_CLICKED";

    private VisTextField idBox;

    private VisTextField xValue;
    private VisTextField yValue;
    private VisTextField widthValue;
    private VisTextField heightValue;
    private VisTextField scaleXValue;
    private VisTextField scaleYValue;
    private VisCheckBox flipVertical;
    private VisCheckBox flipHorizontal;
    private ColorPickerButton tintColorComponent;
    private VisTextField rotationValue;

    public UIBasicItemProperties() {
        super();

        tintColorComponent = new ColorPickerButton();

        Validators.FloatValidator floatValidator = new Validators.FloatValidator();

        idBox = new VisTextField();
        xValue = new VisValidableTextField(floatValidator);
        yValue = new VisValidableTextField(floatValidator);
        widthValue = new VisValidableTextField(floatValidator);
        heightValue = new VisValidableTextField(floatValidator);
        scaleXValue = new VisValidableTextField(floatValidator);
        scaleYValue = new VisValidableTextField(floatValidator);
        flipVertical = new VisCheckBox(null);
        flipHorizontal = new VisCheckBox(null);
        tintColorComponent = new ColorPickerButton();
        rotationValue = new VisValidableTextField(floatValidator);

        add(new VisLabel("Identifier:", Align.right)).padRight(5).colspan(2).fillX();
        add(idBox).width(120).colspan(2);
        row().padTop(5);
        add(new VisLabel("X:", Align.right)).padRight(5).width(50).right();
        add(xValue).width(55).padRight(5);
        add(new VisLabel("Y:", Align.right)).padRight(5).width(50).right();
        add(yValue).width(55);
        row().padTop(5);
        add(new VisLabel("Width:", Align.right)).padRight(5).width(55).right();
        add(widthValue).width(55).padRight(5);
        add(new VisLabel("Height:", Align.right)).padRight(5).width(55).right();
        add(heightValue).width(55);
        row().padTop(5);
        add(new VisLabel("Scale X:", Align.right)).padRight(5).width(55).right();
        add(scaleXValue).width(55).padRight(5);
        add(new VisLabel("Scale Y:", Align.right)).padRight(5).width(55).right();
        add(scaleYValue).width(55);
        row().padTop(5);
        add(new VisLabel("Flip X:", Align.right)).padRight(5).width(55).right();
        add(flipVertical).padRight(5).left();
        add(new VisLabel("Flip Y:", Align.right)).padRight(5).width(55).right();
        add(flipHorizontal).left();
        row().padTop(5);
        add(new VisLabel("Rotation:", Align.right)).padRight(5).colspan(2).fillX();
        add(rotationValue).width(120).colspan(2);
        row().padTop(5);
        add(new VisLabel("Tint:", Align.right)).padRight(5).colspan(2).fillX();
        add(tintColorComponent).width(120).colspan(2);

        setListeners();
    }

    public String getIdBoxValue() {
        return idBox.getText();
    }

    public void setIdBoxValue(String idBox) {
        this.idBox.setText(idBox);
    }

    public String getRotationValue() {
        return rotationValue.getText();
    }

    public void setRotationValue(String rotationValue) {
        this.rotationValue.setText(rotationValue);
    }

    public String getXValue() {
        return xValue.getText();
    }

    public void setXValue(String xValue) {
        this.xValue.setText(xValue);
    }

    public String getYValue() {
        return yValue.getText();
    }

    public void setYValue(String yValue) {
        this.yValue.setText(yValue);
    }

    //private TextButton customVarsBtn;

    public String getWidthValue() {
        return widthValue.getText();
    }

    public void setWidthValue(String widthValue) {
        this.widthValue.setText(widthValue);
    }

    public String getHeightValue() {
        return heightValue.getText();
    }

    public void setHeightValue(String heightValue) {
        this.heightValue.setText(heightValue);
    }

    public String getScaleXValue() {
        return scaleXValue.getText();
    }

    public void setScaleXValue(String scaleXValue) {
        this.scaleXValue.setText(scaleXValue);
    }

    public String getScaleYValue() {
        return scaleYValue.getText();
    }

    public void setScaleYValue(String scaleYValue) {
        this.scaleYValue.setText(scaleYValue);
    }

    public boolean getFlipV() {
        return flipVertical.isChecked();
    }

    public void setFlipV(boolean flipVertical) {
        this.flipVertical.setChecked(flipVertical);
    }

    public boolean getFlipH() {
        return flipHorizontal.isChecked();
    }

    public void setFlipH(boolean flipHorizontal) {
        this.flipHorizontal.setChecked(flipHorizontal);
    }

    public Color getTintColor() {
        return tintColorComponent.getColorValue();
    }

    public void setTintColor(Color tintColor) {
        tintColorComponent.setColorValue(tintColor);
        facade.sendNotification(UIAbstractProperties.PROPERTIES_UPDATED);
    }

    private void setListeners() {
        idBox.addListener(new KeyboardListener(UIAbstractProperties.PROPERTIES_UPDATED));
        xValue.addListener(new KeyboardListener(UIAbstractProperties.PROPERTIES_UPDATED));
        yValue.addListener(new KeyboardListener(UIAbstractProperties.PROPERTIES_UPDATED));
        widthValue.addListener(new KeyboardListener(UIAbstractProperties.PROPERTIES_UPDATED));
        heightValue.addListener(new KeyboardListener(UIAbstractProperties.PROPERTIES_UPDATED));
        scaleXValue.addListener(new KeyboardListener(UIAbstractProperties.PROPERTIES_UPDATED));
        scaleYValue.addListener(new KeyboardListener(UIAbstractProperties.PROPERTIES_UPDATED));
        flipVertical.addListener(new CheckBoxChangeListener(UIAbstractProperties.PROPERTIES_UPDATED));
        flipHorizontal.addListener(new CheckBoxChangeListener(UIAbstractProperties.PROPERTIES_UPDATED));
        rotationValue.addListener(new KeyboardListener(UIAbstractProperties.PROPERTIES_UPDATED));

        tintColorComponent.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                facade.sendNotification(TINT_COLOR_BUTTON_CLICKED);
            }
        });
    }
}
