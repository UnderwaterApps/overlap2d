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

package com.commons;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.util.InputValidator;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;

/**
 * Created by sargis on 4/27/15.
 */
public class O2DDialog extends VisDialog {
    protected final Skin skin;

    public O2DDialog(String title) {
        super(title);
        skin = VisUI.getSkin();
    }

    @Override
    public void addCloseButton() {
        VisImageButton closeButton = new VisImageButton("close-window");
        this.getTitleTable().add(closeButton).padBottom(2);
        closeButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                O2DDialog.this.close();
            }
        });

        if (this.getTitleTable().getChildren().size == 2) {
            this.getTitleTable().getCell(this.getTitleLabel()).padLeft(closeButton.getWidth() * 2.0F);
        }
    }

    protected VisTextField createTextField() {
        return new VisTextField("", "light");
    }

    protected VisTextField createTextField(String text) {
        return new VisTextField(text, "light");
    }

    protected VisTextField createTextField(String text, VisTextField.TextFieldFilter textFieldFilter) {
        VisTextField visTextField = createTextField(text);
        visTextField.setTextFieldFilter(textFieldFilter);
        return visTextField;
    }

    protected VisValidatableTextField createValidableTextField(String text, InputValidator inputValidator) {
        VisValidatableTextField visTextField = new VisValidatableTextField(inputValidator);
        visTextField.setText(text);
        visTextField.setStyle(skin.get("light", VisTextField.VisTextFieldStyle.class));
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
