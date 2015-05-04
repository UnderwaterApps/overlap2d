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
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.renderer.legacy.data.ResolutionEntryVO;

public class CreateNewResolutionDialog extends VisDialog {
    public static final String CREATE_BTN_CLICKED = "com.uwsoft.editor.mvc.view.ui.dialog.CreateNewResolutionDialog" + ".CREATE_BTN_CLICKED";
    private final VisTextField nameVisTextField;
    private final VisTextField widthVisTextField;
    private final VisTextField heightVisTextField;
    private final VisSelectBox<String> basedOnVisSelectBox;


    public CreateNewResolutionDialog() {
        super("Create New Resolution");

        addCloseButton();
        VisTable mainTable = new VisTable();
        mainTable.pad(15);
        mainTable.add("Name :").padRight(5).right();
        nameVisTextField = new VisTextField();
        mainTable.add(nameVisTextField).colspan(3).expandX().fillX();
        mainTable.row().padTop(20);
        mainTable.add("Size :").padRight(5).right();
        widthVisTextField = new VisTextField();
        mainTable.add(widthVisTextField).padRight(5).width(60);
        mainTable.add("X").padRight(5);
        heightVisTextField = new VisTextField();
        mainTable.add(heightVisTextField).width(60);
        mainTable.row().padTop(20);
        mainTable.add("Based on :").padRight(5).right();
        basedOnVisSelectBox = new VisSelectBox<>();
        basedOnVisSelectBox.setItems("width", "height");
        mainTable.add(basedOnVisSelectBox).colspan(3).expandX().fillX();
        mainTable.row().padTop(20);
        VisTextButton createBtn = new VisTextButton("Create");
        createBtn.addListener(new CrateButtonCliclListener());
        mainTable.add(createBtn).right().colspan(4);
        add(mainTable);
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
            resolutionEntryVO.base = basedOnVisSelectBox.getSelectedIndex();
            facade.sendNotification(CREATE_BTN_CLICKED, resolutionEntryVO);
        }
    }

}
