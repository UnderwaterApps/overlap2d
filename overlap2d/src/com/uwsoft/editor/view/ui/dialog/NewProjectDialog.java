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

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.commons.O2DDialog;
import com.kotcrab.vis.ui.util.form.SimpleFormValidator;
import com.kotcrab.vis.ui.widget.*;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.view.ui.validator.NewProjectDialogValidator;
import com.uwsoft.editor.view.ui.widget.InputFileWidget;

import java.io.File;

public class NewProjectDialog extends O2DDialog {
    public static final String CREATE_BTN_CLICKED = "com.uwsoft.editor.view.ui.dialog.NewProjectDialog" + ".CREATE_BTN_CLICKED";
    private static final String DEFAULT_ORIGIN_WIDTH = "1920";
    private static final String DEFAULT_ORIGIN_HEIGHT = "1200";
    private static final String DEFAULT_PPWU = "80";

    private final InputFileWidget workspacePathField;
    private final VisValidatableTextField projectName;
    private VisTextField originWidthTextField;
    private VisTextField originHeightTextField;
    private String defaultWorkspacePath;
    private VisTextField pixelsPerWorldUnitField;
    private NewProjectDialogValidator newProjectDialogValidator;

    NewProjectDialog() {
        super("Create New Project");
        setModal(true);
        addCloseButton();
        VisTable mainTable = new VisTable();
//        mainTable.debug();
        mainTable.padTop(6).padRight(6).padBottom(22);
        //
        VisLabel projectNameLavel = new VisLabel("Project Name:");
        mainTable.add(projectNameLavel).right().padRight(5);
        projectName = createValidableTextField("", new SimpleFormValidator.EmptyInputValidator("Cannot be empty"));
        mainTable.add(projectName).height(21).expandX().fillX();
        //
        mainTable.row().padTop(10);
        //
        mainTable.add(new VisLabel("Project Folder:")).right().padRight(5);
        workspacePathField = new InputFileWidget(FileChooser.Mode.OPEN, FileChooser.SelectionMode.DIRECTORIES, false);
        workspacePathField.setTextFieldWidth(156);
        mainTable.add(workspacePathField);
        //
        mainTable.row().padTop(10);
        mainTable.add(new Separator()).padTop(2).padBottom(2).fill().expand();
        mainTable.row().padTop(10);
        //
        mainTable.add(new VisLabel("Original Size")).top().left().padRight(5);
        mainTable.add(getDimensionsTable()).left();
        //

        //
        mainTable.row().padTop(23);
        //
        VisTextButton createBtn = new VisTextButton("Create", "orange");

        mainTable.add(createBtn).width(93).height(25).colspan(2);
        //
        add(mainTable);
        //
        createBtn.addListener(new BtnClickListener(CREATE_BTN_CLICKED));

        newProjectDialogValidator = new NewProjectDialogValidator();
    }

    private Table getDimensionsTable() {
        VisTextField.TextFieldFilter.DigitsOnlyFilter digitsOnlyFilter = new VisTextField.TextFieldFilter.DigitsOnlyFilter();
        VisTable dimensionsTable = new VisTable();
        originWidthTextField = createTextField(DEFAULT_ORIGIN_WIDTH, digitsOnlyFilter);
        dimensionsTable.add(new VisLabel("Width:")).left().padRight(3);
        dimensionsTable.add(originWidthTextField).width(45).height(21).padRight(3);
        dimensionsTable.row().padTop(10);
        originHeightTextField = createTextField(DEFAULT_ORIGIN_HEIGHT, digitsOnlyFilter);
        dimensionsTable.add(new VisLabel("Height:")).left().padRight(3);
        dimensionsTable.add(originHeightTextField).width(45).height(21).left();
        dimensionsTable.row().padTop(10);
        pixelsPerWorldUnitField = createTextField(DEFAULT_PPWU, digitsOnlyFilter);
        dimensionsTable.add(new VisLabel("Pixels per WUnit:")).left().padRight(3);
        dimensionsTable.add(pixelsPerWorldUnitField).width(45).height(21).left();
        return dimensionsTable;
    }

    @Override
    public VisDialog show(Stage stage, Action action) {
        originWidthTextField.setText(DEFAULT_ORIGIN_WIDTH);
        originHeightTextField.setText(DEFAULT_ORIGIN_HEIGHT);
        workspacePathField.resetData();
        workspacePathField.setValue(new FileHandle(defaultWorkspacePath));
        return super.show(stage, action);
    }

    public String getOriginWidth() {
        return originWidthTextField.getText();
    }

    public String getPixelPerWorldUnit() {
        return pixelsPerWorldUnitField.getText();
    }

    public String getOriginHeight() {
        return originHeightTextField.getText();
    }

    public String getDefaultWorkspacePath() {
        return defaultWorkspacePath;
    }

    public void setDefaultWorkspacePath(String defaultWorkspacePath) {
        this.defaultWorkspacePath = defaultWorkspacePath;
    }

    private class BtnClickListener extends ClickListener {
        private final String command;

        public BtnClickListener(String command) {
            this.command = command;
        }

        @Override
        public void clicked(InputEvent event, float x, float y) {
            super.clicked(event, x, y);
            Overlap2DFacade facade = Overlap2DFacade.getInstance();
            if (newProjectDialogValidator.validate(getStage(), projectName)) {
                facade.sendNotification(command, workspacePathField.getValue().path() + File.separator + projectName.getText());
            }
        }
    }
}
