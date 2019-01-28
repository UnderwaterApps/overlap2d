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

package com.uwsoft.editor.view.ui.properties;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.InputValidator;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;

/**
 * Created by azakhary on 4/15/2015.
 */
public abstract class UIItemProperties extends UIAbstractProperties {

    final Skin skin;

    public UIItemProperties() {
        skin = VisUI.getSkin();
    }

    protected VisLabel createLabel(String text, int alignment) {
        VisLabel visLabel = new VisLabel(text, alignment);
        visLabel.setStyle(skin.get("small", Label.LabelStyle.class));
        return visLabel;
    }

    protected VisLabel createLabel(String text) {
        return createLabel(text, Align.right);
    }

    protected VisTextField createTextField() {
        return new VisTextField("", "default");
    }

    protected VisTextField createTextField(String text) {
        return new VisTextField(text, "default");
    }

    protected VisTextField createTextField(String text, VisTextField.TextFieldFilter textFieldFilter) {
        VisTextField visTextField = createTextField(text);
        visTextField.setTextFieldFilter(textFieldFilter);
        return visTextField;
    }

    protected VisValidatableTextField createValidableTextField(String text, InputValidator inputValidator) {
        VisValidatableTextField visTextField = new VisValidatableTextField(inputValidator);
        visTextField.setText(text);
        visTextField.setStyle(skin.get("default", VisTextField.VisTextFieldStyle.class));
        return visTextField;
    }

    protected VisValidatableTextField createValidableTextField(InputValidator inputValidator) {
        VisValidatableTextField visTextField = createValidableTextField("", inputValidator);
        return visTextField;
    }

    protected VisValidatableTextField createValidableTextField(String text, InputValidator inputValidator, VisTextField.TextFieldFilter textFieldFilter) {
        VisValidatableTextField visTextField = createValidableTextField(text, inputValidator);
        visTextField.setTextFieldFilter(textFieldFilter);
        return visTextField;
    }
}
