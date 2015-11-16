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

package com.uwsoft.editor.view.ui.box;


import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.Validators;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;
import com.uwsoft.editor.event.KeyboardListener;

/**
 * Created by azakhary on 4/15/2015.
 */
public class UIGridBox extends UIBaseBox {

    public static final String GRID_SIZE_TEXT_FIELD_UPDATED = "com.uwsoft.editor.view.ui.box.UIGridBox" + ".GRID_SIZE_TEXT_FIELD_UPDATED";
    private VisValidatableTextField gridSizeTextField;

    public UIGridBox() {
        super();
        init();
        setVisible(false);
    }

    @Override
    public void update() {
        setVisible(true);
    }

    private void init() {
        VisLabel lbl = new VisLabel("Grid Size:");
        add(lbl).padRight(4);
        gridSizeTextField = new VisValidatableTextField(new Validators.IntegerValidator());
        gridSizeTextField.setStyle(VisUI.getSkin().get("light", VisTextField.VisTextFieldStyle.class));
        //gridSizeTextField.setRightAligned(true);
        gridSizeTextField.addListener(new KeyboardListener(GRID_SIZE_TEXT_FIELD_UPDATED));
        add(gridSizeTextField).width(60);
    }

    public void setGridSize(int gridSize) {
        gridSizeTextField.setText(gridSize + "");
    }
}
