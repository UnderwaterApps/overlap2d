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

package com.uwsoft.editor.mvc.view.ui.dialog;

import com.kotcrab.vis.ui.util.Validators;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.VisValidableTextField;
import com.uwsoft.editor.utils.StandardWidgetsFactory;

/**
 * Created by azakhary on 5/12/2015.
 */
public class EditSpriteAnimationDialog extends O2DDialog {

    private VisTextField nameField;
    private VisValidableTextField fromFrameField;
    private VisValidableTextField toFrameField;
    private VisTextButton addButton;

    public EditSpriteAnimationDialog() {
        super("Edit Sprite Animation Ranges");
        addCloseButton();

        VisTable mainTable = new VisTable();

        VisTable animationsList = new VisTable();

        mainTable.add(animationsList);
        mainTable.row();
        mainTable.add(createNewAnimationTable());
        mainTable.row();

        add(mainTable);
    }

    private VisTable createNewAnimationTable() {
        VisTable newAnimationTable = new VisTable();

        Validators.IntegerValidator integerValidator = new Validators.IntegerValidator();

        nameField = createTextField();
        fromFrameField = createValidableTextField(integerValidator);
        toFrameField = createValidableTextField(integerValidator);
        addButton = new VisTextButton("Add");

        newAnimationTable.add(StandardWidgetsFactory.createLabel("Name"));
        newAnimationTable.add(nameField);
        newAnimationTable.add(StandardWidgetsFactory.createLabel("Frames"));
        newAnimationTable.add(fromFrameField);
        newAnimationTable.add(toFrameField);
        newAnimationTable.add(addButton);
        newAnimationTable.row();

        return newAnimationTable;
    }
}
