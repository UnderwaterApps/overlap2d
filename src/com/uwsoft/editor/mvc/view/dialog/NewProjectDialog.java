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

package com.uwsoft.editor.mvc.view.dialog;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.*;
import com.puremvc.patterns.observer.Notifier;
import com.uwsoft.editor.gdx.stage.UIStage;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.ProjectManager;

public class NewProjectDialog extends VisDialog implements Notifier {

    private final UIStage uiStage;
    private Overlap2DFacade facade;
    private ProjectManager projectManager;
    private VisTextButton createBtn;
    private TextButton fileChooserBtn;
    private TextField projectPathTextBox;
    private TextField origWidth;
    private TextField origHeight;

    public NewProjectDialog(UIStage uiStage) {
        super("Create New Project");
        facade = Overlap2DFacade.getInstance();
        this.uiStage = uiStage;

        setModal(true);
        addCloseButton();
        VisTable mainTable = new VisTable();
//        mainTable.debug();
        VisTable projectPathTable = new VisTable();
        projectPathTable.add(new VisLabel("Project Folder:")).right().padRight(5);
        projectPathTable.add(new VisTextField("")).padRight(3);
        VisTextButton browsBtn = new VisTextButton("...");
        browsBtn.addListener(new BrowsBtnClickListener());
        projectPathTable.add(browsBtn).left();
        mainTable.add(projectPathTable).padBottom(15);
        //
        mainTable.row();
        //
        VisTable projectResolutionTable = new VisTable();
        projectResolutionTable.add(new VisLabel("Original Resolution:")).right().padRight(5);
        projectResolutionTable.add(new VisTextField("2400")).width(50).padRight(3);
        projectResolutionTable.add(new VisLabel("X")).left().padRight(3);
        projectResolutionTable.add(new VisTextField("1140")).width(50).left();
        mainTable.add(projectResolutionTable).padBottom(15);
        //
        mainTable.row();
        //
        VisTextButton createBtn = new VisTextButton("Create New Project");
        createBtn.addListener(new CreateBtnClickListener());
        mainTable.add(createBtn).right();
        //
        add(mainTable).pad(15);
//        uiStage.setKeyboardFocus(ui.getTextBoxById("projectPath"));
//
//        projectPathTextBox = ui.getTextBoxById("projectPath");
//        projectPathTextBox.getOnscreenKeyboard().show(true); //why not working this is :( benbenutta!
//
//        fileChooserBtn = ui.getTextButtonById("directoryChooseBtn");
//
//        origWidth = ui.getTextBoxById("width");
//        origHeight = ui.getTextBoxById("height");
//
//        origWidth.setText("2400");
//        origHeight.setText("1440");
//
//        crateBtnCrateProject();
//        facade = Overlap2DFacade.getInstance();
//        projectManager = facade.retrieveProxy(ProjectManager.NAME);
//        fileChooserBtn.addListener(new ClickListener() {
//            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
//                super.touchUp(event, x, y, pointer, button);
//                FileChooser fileChooser = new FileChooser(FileChooser.Mode.OPEN);
//                fileChooser.setSelectionMode(FileChooser.SelectionMode.DIRECTORIES);
//                fileChooser.setMultiselectionEnabled(false);
//                fileChooser.setListener(new FileChooserAdapter() {
//                    @Override
//                    public void selected(FileHandle file) {
//                        super.selected(file);
//                        projectPathTextBox.setText(file.path());
//                    }
//
//                });
//                stage.addActor(fileChooser.fadeIn());
//            }
//        });
    }

    private void crateBtnCrateProject() {
//        createBtn = ui.getTextButtonById("createBtn");
//        createBtn.addListener(new ClickListener() {
//            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
//                super.touchUp(event, x, y, pointer, button);
//                createProject();
//            }
//        });
    }

    private void createProject() {
//        String projectPath = projectPathTextBox.getText();
//        if (projectPath == null || projectPath.equals("")) {
//            return;
//        }
//
//        String projectName = new File(projectPath).getName();
//
//        if (projectName == null || projectName.equals("")) {
//            return;
//        }
//
//        String workSpacePath = projectPath.substring(0, projectPath.lastIndexOf(projectName));
//        if (workSpacePath.length() > 0) {
//            projectManager.setLastOpenedPath(workSpacePath);
//            projectManager.setWorkspacePath(workSpacePath);
//        }
//
//        int origWidthValue = 0;
//        int origHeightValue = 0;
//
//        try {
//            origWidthValue = Integer.parseInt(origWidth.getText());
//            origHeightValue = Integer.parseInt(origHeight.getText());
//        } catch (Exception ignored) {
//        }
//
//        try {
//            projectManager.createEmptyProject(projectName, origWidthValue, origHeightValue);
//            projectManager.openProjectAndLoadAllData(projectName);
//            stage.getSandbox().loadCurrentProject();
//            stage.loadCurrentProject();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        remove();
    }

    @Override
    public void sendNotification(String notificationName, Object body, String type) {

    }

    @Override
    public void sendNotification(String notificationName, Object body) {

    }

    @Override
    public void sendNotification(String notificationName) {

    }

    private class CreateBtnClickListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            super.clicked(event, x, y);
            createProject();
        }
    }

    private class BrowsBtnClickListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            super.clicked(event, x, y);
            createProject();
        }
    }
}
