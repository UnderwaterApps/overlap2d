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

import com.puremvc.patterns.mediator.SimpleMediator;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.view.stage.Sandbox;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.proxy.ProjectManager;
import com.uwsoft.editor.view.menu.Overlap2DMenuBar;
import com.uwsoft.editor.view.stage.UIStage;

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
                NewProjectDialog.CREATE_BTN_CLICKED
        };
    }

    @Override
    public void onRegister() {
        super.onRegister();
        facade = Overlap2DFacade.getInstance();

        ProjectManager projectManager = facade.retrieveProxy(ProjectManager.NAME);
        viewComponent.setDefaultWorkspacePath(projectManager.getWorkspacePath().path());
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
            case NewProjectDialog.CREATE_BTN_CLICKED:
                ProjectManager projectManager = facade.retrieveProxy(ProjectManager.NAME);
                int originWidth = Integer.parseInt(viewComponent.getOriginWidth());
                int originHeight = Integer.parseInt(viewComponent.getOriginHeight());
                int pixelPerWorldUnit = Integer.parseInt(viewComponent.getPixelPerWorldUnit());
                projectManager.createNewProject(notification.getBody(), originWidth, originHeight, pixelPerWorldUnit);
                //TODO: this should be not here
                sandbox.loadCurrentProject();
                viewComponent.hide();
                break;
        }

    }
}
