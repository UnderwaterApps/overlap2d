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

import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.uwsoft.editor.utils.StandardWidgetsFactory;

/**
 * Created by azakhary on 5/12/2015.
 */
public class CustomVariablesDialog extends O2DDialog {

    private VisTextField keyField;
    private VisTextField valueField;
    private VisTextButton addButton;

    public CustomVariablesDialog() {
        super("Custom Variables");
        addCloseButton();

        VisTable mainTable = new VisTable();

        VisTable variablesList = new VisTable();

        mainTable.add(variablesList);
        mainTable.row();
        mainTable.add(createAddVariableTable());
        mainTable.row();

        add(mainTable);
    }

    private VisTable createAddVariableTable() {
        VisTable addVariableTable = new VisTable();

        keyField = createTextField();
        valueField = createTextField();
        addButton = new VisTextButton("Add");

        addVariableTable.add(StandardWidgetsFactory.createLabel("New"));
        addVariableTable.add(keyField);
        addVariableTable.add(valueField);
        addVariableTable.add(addButton);

        addVariableTable.row();

        return addVariableTable;
    }
}
