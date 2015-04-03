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

package com.uwsoft.editor.ui.widget;

import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;

/**
 * Created by sargis on 4/3/15.
 */
public class InputFileWidget extends VisTable {
    private final VisTextField textField;
    private final VisTextButton browsBtn;

    public InputFileWidget(boolean setVisDefaults) {
        super(setVisDefaults);
        textField = new VisTextField("");
        add(textField).padRight(3);
        browsBtn = new VisTextButton("...");
        add(browsBtn);
    }

    public InputFileWidget() {
        this(true);
    }

    public String getValue() {
        return textField.getText();
    }

    public void setValue(String value) {
        textField.setText(value);
    }
}
