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

package com.uwsoft.editor.view.ui.properties.panels;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.util.Validators;
import com.kotcrab.vis.ui.widget.*;
import com.uwsoft.editor.view.ui.widget.components.TintButton;
import com.uwsoft.editor.event.CheckBoxChangeListener;
import com.uwsoft.editor.event.KeyboardListener;
import com.uwsoft.editor.view.ui.properties.UIAbstractProperties;

/**
 * Created by azakhary on 4/16/2015.
 */
public class UISceneProperties extends UIAbstractProperties {


    public static final String AMBIENT_COLOR_BUTTON_CLICKED = "com.uwsoft.editor.view.ui.properties.panels.UISceneProperties" + ".AMBIENT_COLOR_BUTTON_CLICKED";

    private VisLabel pixelsPerWorldUnitField;
    private VisCheckBox physicsEnabledCheckBox;
    private VisTextField gravityXTextField;
    private VisTextField gravityYTextField;
    private VisTextField sleepVelocityTextField;
    private VisCheckBox enableLightsCheckBox;
    private VisCheckBox diffuseCheckBox;
    private TintButton ambientColorComponent;

    public UISceneProperties() {
        super();

        Validators.FloatValidator floatValidator = new Validators.FloatValidator();

        pixelsPerWorldUnitField = new VisLabel("1");
        physicsEnabledCheckBox = new VisCheckBox(null);
        gravityXTextField = new VisValidatableTextField(floatValidator);
        gravityYTextField = new VisValidatableTextField(floatValidator);
        sleepVelocityTextField = new VisValidatableTextField(floatValidator);
        enableLightsCheckBox = new VisCheckBox(null);
        diffuseCheckBox = new VisCheckBox(null);
        ambientColorComponent = new TintButton(29, 21);

        pad(5);
        add(new VisLabel("Pixels per WU:", Align.right)).padRight(5).width(115);
        add(pixelsPerWorldUnitField).width(30).left().padLeft(7);
        row().padTop(5);
        add(new VisLabel("Physics enabled:", Align.right)).padRight(5).width(115);
        add(physicsEnabledCheckBox).left();
        row().padTop(5);
        add(new VisLabel("Gravity X:", Align.right)).padRight(5).width(115);
        add(gravityXTextField).width(115);
        row().padTop(5);
        add(new VisLabel("Gravity Y:", Align.right)).padRight(5).width(115);
        add(gravityYTextField).width(115);
        row().padTop(5);
        add(new VisLabel("Sleep velocity:", Align.right)).padRight(5).width(115);
        add(sleepVelocityTextField).width(115);
        row().padTop(5);
        addSeparator().colspan(2).padTop(5).padBottom(5);
        add(new VisLabel("Enable lights:", Align.right)).padRight(5).width(115);
        add(enableLightsCheckBox).left();
        row().padTop(5);
        add(new VisLabel("Diffuse:", Align.right)).padRight(5).width(115);
        add(diffuseCheckBox).left();
        row().padTop(5);
        add(new VisLabel("AL Color:", Align.right)).padRight(5).width(115);
        add(ambientColorComponent).left();
        row().padTop(5);

        setListeners();
    }

    public boolean isDiffuse() {
        return diffuseCheckBox.isChecked();
    }

    public void setDiffuse(boolean isDiffuse) {
        this.diffuseCheckBox.setChecked(isDiffuse);
    }

    public boolean isPhysicsEnabled() {
        return physicsEnabledCheckBox.isChecked();
    }

    public void setPhysicsEnable(boolean isPhysicsEnabled) {
        this.physicsEnabledCheckBox.setChecked(isPhysicsEnabled);
    }

    public String getGravityXValue() {
        return gravityXTextField.getText();
    }

    public void setGravityXValue(String gravityXValue) {
        this.gravityXTextField.setText(gravityXValue);
    }

    public String getGravityYValue() {
        return gravityYTextField.getText();
    }

    public void setGravityYValue(String gravityYValue) {
        this.gravityYTextField.setText(gravityYValue);
    }

    public String getSleepVelocityValue() {
        return sleepVelocityTextField.getText();
    }

    public void setSleepVelocityValue(String sleepVelocityValue) {
        this.sleepVelocityTextField.setText(sleepVelocityValue);
    }

    public boolean isLightsEnabled() {
        return enableLightsCheckBox.isChecked();
    }

    public void setLightsEnabled(boolean isLightsEnabled) {
        this.enableLightsCheckBox.setChecked(isLightsEnabled);
    }

    public Color getAmbientColor() {
        return ambientColorComponent.getColorValue();
    }

    public void setAmbientColor(Color tintColor) {
        ambientColorComponent.setColorValue(tintColor);
    }

    public int getPixelsPerWorldUnit() {
        return Integer.parseInt(pixelsPerWorldUnitField.getText().toString());
    }

    public void setPixelsPerWorldUnit(int value) {
        pixelsPerWorldUnitField.setText(value+"");
    }

    @Override
    public String getPrefix() {
        return this.getClass().getCanonicalName();
    }

    private void setListeners() {
        physicsEnabledCheckBox.addListener(new CheckBoxChangeListener(getUpdateEventName()));
        gravityXTextField.addListener(new KeyboardListener(getUpdateEventName()));
        gravityYTextField.addListener(new KeyboardListener(getUpdateEventName()));
        sleepVelocityTextField.addListener(new KeyboardListener(getUpdateEventName()));
        enableLightsCheckBox.addListener(new CheckBoxChangeListener(getUpdateEventName()));
        diffuseCheckBox.addListener(new CheckBoxChangeListener(getUpdateEventName()));

        ambientColorComponent.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                facade.sendNotification(AMBIENT_COLOR_BUTTON_CLICKED);
            }
        });
    }
}
