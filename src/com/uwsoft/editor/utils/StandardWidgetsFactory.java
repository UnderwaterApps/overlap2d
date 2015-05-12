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

package com.uwsoft.editor.utils;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.InputValidator;
import com.kotcrab.vis.ui.VisUI;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.VisValidableTextField;

/**
 * Creates standard widgets like labels or text fields with provided standard Overlap2D specific visual style.
 * Singleton helper class
 *
 * Created by azakhary on 5/12/2015.
 */
public class StandardWidgetsFactory {

    private static StandardWidgetsFactory instance;

    private StandardWidgetsFactory() {

    }

    public static StandardWidgetsFactory getInstance() {
        if(instance == null) {
            instance = new StandardWidgetsFactory();
        }

        return instance;
    }

    public static VisLabel createLabel(String text, int alignment) {
        Skin skin = VisUI.getSkin();
        VisLabel visLabel = new VisLabel(text, alignment);
        visLabel.setStyle(skin.get("small", Label.LabelStyle.class));
        return visLabel;
    }

    public static VisLabel createLabel(String text) {
        return createLabel(text, Align.right);
    }


    public static  VisTextField createTextField() {
        return new VisTextField("", "light");
    }

    public static  VisTextField createTextField(String text) {
        return new VisTextField(text, "light");
    }

    public static  VisTextField createTextField(String text, VisTextField.TextFieldFilter textFieldFilter) {
        VisTextField visTextField = createTextField(text);
        visTextField.setTextFieldFilter(textFieldFilter);
        return visTextField;
    }

    public static  VisValidableTextField createValidableTextField(String text, InputValidator inputValidator) {
        VisValidableTextField visTextField = new VisValidableTextField(inputValidator);
        visTextField.setText(text);
        Skin skin = VisUI.getSkin();
        visTextField.setStyle(skin.get("light", VisTextField.VisTextFieldStyle.class));
        return visTextField;
    }

    public static  VisValidableTextField createValidableTextField(InputValidator inputValidator) {
        VisValidableTextField visTextField = createValidableTextField("", inputValidator);
        return visTextField;
    }

    public static  VisValidableTextField createValidableTextField(String text, InputValidator inputValidator, VisTextField.TextFieldFilter textFieldFilter) {
        VisValidableTextField visTextField = createValidableTextField(text, inputValidator);
        visTextField.setTextFieldFilter(textFieldFilter);
        return visTextField;
    }
}
