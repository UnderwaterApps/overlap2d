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

import com.badlogic.gdx.files.FileHandle;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;
import com.puremvc.patterns.mediator.SimpleMediator;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.gdx.stage.UIStage;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.ProjectManager;
import com.uwsoft.editor.mvc.view.Overlap2DMenuBar;

/**
 * Created by sargis on 4/1/15.
 */
public class NewProjectDialogMediator extends SimpleMediator<NewProjectDialog> {
    private static final String TAG = NewProjectDialogMediator.class.getCanonicalName();
    private static final String NAME = TAG;

    public NewProjectDialogMediator() {
        super(NAME, new NewProjectDialog());
    }

    @Override
    public String[] listNotificationInterests() {
        return new String[]{
                Overlap2DMenuBar.NEW_PROJECT,
                NewProjectDialog.BROWS,
                NewProjectDialog.CREATE
        };
    }

    @Override
    public void onRegister() {
        super.onRegister();
        facade = Overlap2DFacade.getInstance();
    }

    @Override
    public void handleNotification(Notification notification) {
        super.handleNotification(notification);
        Sandbox sandbox = Sandbox.getInstance();
        UIStage uiStage = sandbox.getUIStage();
        switch (notification.getName()) {
            case Overlap2DMenuBar.NEW_PROJECT:
                viewComponent.show(uiStage);
                break;
            case NewProjectDialog.CREATE:
                ProjectManager projectManager = facade.retrieveProxy(ProjectManager.NAME);
                String projectPath = viewComponent.getProjectPath();
                int originWidth = Integer.parseInt(viewComponent.getOriginWidth());
                int originHeight = Integer.parseInt(viewComponent.getOriginHeight());
                projectManager.createNewProject(projectPath, originWidth, originHeight);
                //TODO: this should be not hear
                sandbox.loadCurrentProject();
                viewComponent.hide();
                break;
            case NewProjectDialog.BROWS:
                FileChooser fileChooser = new FileChooser(FileChooser.Mode.OPEN);
                fileChooser.setSelectionMode(FileChooser.SelectionMode.DIRECTORIES);
                fileChooser.setMultiselectionEnabled(false);
                fileChooser.setListener(new FileChooserAdapter() {
                    @Override
                    public void selected(FileHandle file) {
                        super.selected(file);
                        viewComponent.setProjectPath(file.path());
                    }

                });
                uiStage.addActor(fileChooser.fadeIn());
                break;
        }

    }
}
