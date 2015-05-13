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

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.*;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.renderer.utils.CustomVariables;
import com.uwsoft.editor.utils.StandardWidgetsFactory;

import java.util.Map;

/**
 * Created by azakhary on 5/12/2015.
 */
public class CustomVariablesDialog extends UIDraggablePanel {
    public static final String PREFIX = "com.uwsoft.editor.mvc.view.ui.dialog.CustomVariablesDialog";
    public static final String ADD_BUTTON_PRESSED = PREFIX + ".ADD_BUTTON_PRESSED";
    public static final String DELETE_BUTTON_PRESSED = PREFIX + ".DELETE_BUTTON_PRESSED";

    private Overlap2DFacade facade;

    private VisTextField keyField;
    private VisTextField valueField;
    private VisTextButton addButton;

    private VisTable mainTable;
    private VisTable variablesList;

    public CustomVariablesDialog() {
        super("Custom Variables");
        addCloseButton();

        facade = Overlap2DFacade.getInstance();

        mainTable = new VisTable();

        variablesList = new VisTable();

        mainTable.add(createAddVariableTable());
        mainTable.row();
        mainTable.add(variablesList);
        mainTable.row();

        add(mainTable).padBottom(4);

        initListeners();
    }

    private VisTable createAddVariableTable() {
        VisTable addVariableTable = new VisTable();

        keyField = StandardWidgetsFactory.createTextField();
        valueField = StandardWidgetsFactory.createTextField();
        addButton = new VisTextButton("Add");

        addVariableTable.add(keyField).width(120).padLeft(6);
        addVariableTable.add(valueField).width(120).padLeft(5);
        addVariableTable.add(addButton).width(38).padLeft(4).padRight(5);

        addVariableTable.row();

        return addVariableTable;
    }

    public void updateView(CustomVariables vars) {
        variablesList.clear();

        variablesList.add(StandardWidgetsFactory.createLabel("Key name")).width(124).height(20).align(Align.center).padLeft(6);
        variablesList.add(StandardWidgetsFactory.createLabel("Value")).width(124).height(20).align(Align.center).padLeft(1);
        variablesList.row();

        for (Map.Entry<String, String> entry : vars.getHashMap().entrySet()) {
            final String key = entry.getKey();
            String value = entry.getValue();

            VisTable keyTbl = new VisTable();
            keyTbl.setBackground(VisUI.getSkin().getDrawable("layer-bg"));
            VisTable valueTbl = new VisTable();
            valueTbl.setBackground(VisUI.getSkin().getDrawable("layer-bg"));
            keyTbl.add(new VisLabel(key)).align(Align.left).width(124).padLeft(2);
            valueTbl.add(new VisLabel(value)).align(Align.left).width(124).padLeft(2);

            VisImageButton trashBtn = new VisImageButton("trash-button");

            variablesList.add(keyTbl).height(20).padLeft(6);
            variablesList.add(valueTbl).height(20).padLeft(1);
            variablesList.add(trashBtn).padLeft(10);
            variablesList.row().padBottom(2);

            trashBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    facade.sendNotification(DELETE_BUTTON_PRESSED, key);
                }
            });
        }
        invalidateHeight();
    }

    private void initListeners() {
        addButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                facade.sendNotification(ADD_BUTTON_PRESSED);
            }
        });
    }

    public String getKey() {
        return keyField.getText();
    }

    public String getValue() {
        return valueField.getText();
    }

    public void setKeyFieldValue(String key) {
        keyField.setText(key);
    }

    public void setValueFieldValue(String value) {
        valueField.setText(value);
    }
}
