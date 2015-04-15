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

package com.uwsoft.editor.mvc.view.ui.box;


import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.kotcrab.vis.ui.util.Validators;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisValidableTextField;
import com.uwsoft.editor.mvc.event.KeyboardListener;

/**
 * Created by azakhary on 4/15/2015.
 */
public class UIGridBox extends UIBaseBox {

    private VisValidableTextField gridSizeTextField;

    public static final String GRID_SIZE_TEXT_FIELD_UPDATED = "com.uwsoft.editor.mvc.view.ui.box.UIGridBox" + ".GRID_SIZE_TEXT_FIELD_UPDATED";

    public UIGridBox() {
       super();

    }

    @Override
    public void init() {
        VisLabel lbl = new VisLabel("Grid Snapping:");
        add(lbl);

        gridSizeTextField = new VisValidableTextField(new Validators.IntegerValidator());
        gridSizeTextField.setRightAligned(true);
        gridSizeTextField.setWidth(40);

        gridSizeTextField.addListener(new KeyboardListener(GRID_SIZE_TEXT_FIELD_UPDATED));

        add(gridSizeTextField);
    }

    public void setGridSize(int gridSize) {
        gridSizeTextField.setText(gridSize+"");
    }
}
