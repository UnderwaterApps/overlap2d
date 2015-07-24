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

package com.uwsoft.editor.view.ui.dialog;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisRadioButton;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.commons.O2DDialog;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.renderer.data.ResolutionEntryVO;

public class CreateNewResolutionDialog extends O2DDialog {
    public static final String CREATE_BTN_CLICKED = "com.uwsoft.editor.view.ui.dialog.CreateNewResolutionDialog" + ".CREATE_BTN_CLICKED";
    private final VisTextField nameVisTextField;
    private VisTextField widthVisTextField;
    private VisTextField heightVisTextField;
    private ButtonGroup<VisRadioButton> buttonGroup;
    private VisRadioButton basedOnWidthRadioButton;
    private VisRadioButton basedOnHeightRadioButton;


    public CreateNewResolutionDialog() {
        super("Create New Resolution");
        addCloseButton();
        VisTable mainTable = new VisTable();

        mainTable.padTop(6).padRight(6).padBottom(22);
        mainTable.add("Name:").padRight(5).right();
        nameVisTextField = createTextField("");
        mainTable.add(nameVisTextField).colspan(3).width(177).height(21);
        mainTable.row().padTop(10);
        mainTable.add("Resolution:").padRight(5).right().top();
        mainTable.add(getDimensionsTable()).left();
        mainTable.row().padTop(20);
        VisTextButton createBtn = new VisTextButton("Create", "orange");
        createBtn.addListener(new CrateButtonCliclListener());
        mainTable.add(createBtn).width(93).height(24).colspan(2);
        add(mainTable);
    }

    private Table getDimensionsTable() {
        buttonGroup = new ButtonGroup<>();
        VisTextField.TextFieldFilter.DigitsOnlyFilter digitsOnlyFilter = new VisTextField.TextFieldFilter.DigitsOnlyFilter();
        VisTable dimensionsTable = new VisTable();
        widthVisTextField = createTextField("", digitsOnlyFilter);
        dimensionsTable.add(new VisLabel("Width:")).left().padRight(3);
        dimensionsTable.add(widthVisTextField).width(45).height(21).padRight(7);
        basedOnWidthRadioButton = new VisRadioButton(null);
        dimensionsTable.add(basedOnWidthRadioButton);
        dimensionsTable.add("Based on");
        dimensionsTable.row().padTop(10);
        heightVisTextField = createTextField("", digitsOnlyFilter);
        dimensionsTable.add(new VisLabel("Height:")).left().padRight(7);
        dimensionsTable.add(heightVisTextField).width(45).height(21).left();
        basedOnHeightRadioButton = new VisRadioButton(null);
        dimensionsTable.add(basedOnHeightRadioButton);
        dimensionsTable.add("Based on");
        buttonGroup.add(basedOnWidthRadioButton);
        buttonGroup.add(basedOnHeightRadioButton);
        return dimensionsTable;
    }

    private class CrateButtonCliclListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            super.clicked(event, x, y);
            Overlap2DFacade facade = Overlap2DFacade.getInstance();
            ResolutionEntryVO resolutionEntryVO = new ResolutionEntryVO();
            resolutionEntryVO.name = nameVisTextField.getText();
            resolutionEntryVO.width = Integer.parseInt(widthVisTextField.getText());
            resolutionEntryVO.height = Integer.parseInt(heightVisTextField.getText());
            resolutionEntryVO.base = buttonGroup.getCheckedIndex();
            facade.sendNotification(CREATE_BTN_CLICKED, resolutionEntryVO);
        }
    }

}
