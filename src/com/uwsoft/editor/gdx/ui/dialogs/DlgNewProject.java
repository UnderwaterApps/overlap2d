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

package com.uwsoft.editor.gdx.ui.dialogs;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;
import com.uwsoft.editor.data.manager.DataManager;
import com.uwsoft.editor.gdx.stage.UIStage;

import java.io.File;
import java.io.IOException;

public class DlgNewProject extends CompositeDialog {

    private TextButton createBtn;
    private TextButton fileChooserBtn;
    private TextField projectPathTextBox;
    private TextField origWidth;
    private TextField origHeight;

    public DlgNewProject(UIStage s) {
        super(s, "NewProjectDlg", 260, 210);

        setTitle("Create New Project");

        s.setKeyboardFocus(ui.getTextBoxById("projectPath"));

        projectPathTextBox = ui.getTextBoxById("projectPath");
        projectPathTextBox.getOnscreenKeyboard().show(true); //why not working this is :( benbenutta!

        fileChooserBtn = ui.getTextButtonById("directoryChooseBtn");

        origWidth = ui.getTextBoxById("width");
        origHeight = ui.getTextBoxById("height");

        origWidth.setText("2400");
        origHeight.setText("1440");

        crateBtnCrateProject();

        fileChooserBtn.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                FileChooser fileChooser = new FileChooser(FileChooser.Mode.OPEN);
                fileChooser.setSelectionMode(FileChooser.SelectionMode.DIRECTORIES);
                fileChooser.setMultiselectionEnabled(false);
                fileChooser.setListener(new FileChooserAdapter() {
                    @Override
                    public void selected(FileHandle file) {
                        super.selected(file);
                        projectPathTextBox.setText(file.path());
                    }

                });
                stage.addActor(fileChooser.fadeIn());
            }
        });
    }

    private void crateBtnCrateProject() {
        createBtn = ui.getTextButtonById("createBtn");
        createBtn.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                createProject();
            }
        });
    }

    private void createProject() {
        String projectPath = projectPathTextBox.getText();
        if (projectPath == null || projectPath.equals("")) {
            return;
        }

        String projectName = new File(projectPath).getName();

        if (projectName == null || projectName.equals("")) {
            return;
        }

        String workSpacePath = projectPath.substring(0, projectPath.lastIndexOf(projectName));
        if (workSpacePath.length() > 0) {
            DataManager.getInstance().setLastOpenedPath(workSpacePath);
            DataManager.getInstance().setWorkspacePath(workSpacePath);
        }

        int origWidthValue = 0;
        int origHeightValue = 0;

        try {
            origWidthValue = Integer.parseInt(origWidth.getText());
            origHeightValue = Integer.parseInt(origHeight.getText());
        } catch (Exception ignored) {
        }

        try {
            DataManager.getInstance().createEmptyProject(projectName, origWidthValue, origHeightValue);
            DataManager.getInstance().openProjectAndLoadAllData(projectName);
            stage.getSandbox().loadCurrentProject();
            stage.loadCurrentProject();
        } catch (IOException e) {
            e.printStackTrace();
        }

        remove();
    }

}
