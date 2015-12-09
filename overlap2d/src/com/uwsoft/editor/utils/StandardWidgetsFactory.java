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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.InputValidator;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.NumberSelector;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;
import com.uwsoft.editor.proxy.CursorManager;
import com.uwsoft.editor.view.ui.widget.CursorListener;

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
        return createTextField("default");
    }
    
    public static  VisTextField createTextField(String style) {
    	VisTextField visTextField = new VisTextField("", style);
    	visTextField.addListener(new CursorListener(CursorManager.TEXT));
    	return visTextField;
    }

    public static  VisTextField createTextField(String style, boolean textCursor)
    {
    	VisTextField visTextField = new VisTextField();
        Skin skin = VisUI.getSkin();
        visTextField.setStyle(skin.get(style, VisTextField.VisTextFieldStyle.class));
        if(textCursor)
        	visTextField.addListener(new CursorListener(CursorManager.TEXT));
        return visTextField;
    }
    
    public static  VisTextField createTextField(String style, VisTextField.TextFieldFilter textFieldFilter) {
        VisTextField visTextField = createTextField(style);
        visTextField.setTextFieldFilter(textFieldFilter);
        return visTextField;
    }
    
    public static  VisValidatableTextField createValidableTextField(InputValidator inputValidator) {
        VisValidatableTextField visTextField = createValidableTextField("default", inputValidator);
        return visTextField;
    }

    public static  VisValidatableTextField createValidableTextField(String style, InputValidator inputValidator) {
        VisValidatableTextField visTextField = new VisValidatableTextField(inputValidator);
        Skin skin = VisUI.getSkin();
        visTextField.setStyle(skin.get(style, VisTextField.VisTextFieldStyle.class));
        visTextField.addListener(new CursorListener(CursorManager.TEXT));
        return visTextField;
    }

    public static  VisValidatableTextField createValidableTextField(String style, InputValidator inputValidator, VisTextField.TextFieldFilter textFieldFilter) {
        VisValidatableTextField visTextField = createValidableTextField(style, inputValidator);
        visTextField.setTextFieldFilter(textFieldFilter);
        visTextField.addListener(new CursorListener(CursorManager.TEXT));
        return visTextField;
    }

    public static NumberSelector createNumberSelector(int min, int max) {
        return createNumberSelector("default", min, max);
    }

    public static NumberSelector createNumberSelector(String style, int min, int max) {
        return createNumberSelector(style, 0, min, max, 1);
    }

    public static NumberSelector createNumberSelector(String style, int initialValue, int min, int max, int step) {
        NumberSelector selector = new NumberSelector(style, "", initialValue, min, max, step);
        return selector;
    }
}
