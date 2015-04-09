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

package com.uwsoft.editor.mvc.view.ui;

import com.kotcrab.vis.ui.util.dialog.DialogUtils;
import com.puremvc.patterns.mediator.SimpleMediator;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.Overlap2D;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.ProjectManager;
import com.uwsoft.editor.mvc.proxy.ResolutionManager;
import com.uwsoft.editor.renderer.data.ResolutionEntryVO;

/**
 * Created by sargis on 4/8/15.
 */
public class UIResolutionBoxMediator extends SimpleMediator<UIResolutionBox> {
    private static final String TAG = UIResolutionBoxMediator.class.getCanonicalName();
    public static final String NAME = TAG;
    private ProjectManager projectManager;

    public UIResolutionBoxMediator() {
        super(NAME, new UIResolutionBox());
    }

    @Override
    public void onRegister() {
        super.onRegister();
        facade = Overlap2DFacade.getInstance();
        projectManager = facade.retrieveProxy(ProjectManager.NAME);
    }

    @Override
    public String[] listNotificationInterests() {
        return new String[]{
                Overlap2D.PROJECT_OPENED,
                UIResolutionBox.CHANGE_RESOLUTION_BTN_CLICKED,
                UIResolutionBox.DELETE_RESOLUTION_BTN_CLICKED,
                UIResolutionBox.REPACK_BTN_CLICKED
        };
    }

    @Override
    public void handleNotification(Notification notification) {
        super.handleNotification(notification);
        Sandbox sandbox = Sandbox.getInstance();
        ResolutionEntryVO resolutionEntryVO;
        switch (notification.getName()) {
            case Overlap2D.PROJECT_OPENED:
                viewComponent.init();
                break;
            case UIResolutionBox.CHANGE_RESOLUTION_BTN_CLICKED:
                resolutionEntryVO = notification.getBody();
                String name = sandbox.sceneControl.getCurrentSceneVO().sceneName;
                projectManager.openProjectAndLoadAllData(projectManager.getCurrentProjectVO().projectName, resolutionEntryVO.name);
                sandbox.loadCurrentProject(name);
                break;
            case UIResolutionBox.DELETE_RESOLUTION_BTN_CLICKED:
                resolutionEntryVO = notification.getBody();
                DialogUtils.showConfirmDialog(sandbox.getUIStage(),
                        "Delete Resolution",
                        "Are you sure you want to delete resolution: " + resolutionEntryVO.name + "?",
                        new String[]{"Delete", "Cancel"}, new Integer[]{0, 1},
                        result -> {
                            if (result == 0) {
                                ResolutionManager resolutionManager = facade.retrieveProxy(ResolutionManager.NAME);
                                resolutionManager.deleteResolution(resolutionEntryVO);
                                String sceneName = sandbox.sceneControl.getCurrentSceneVO().sceneName;
                                sandbox.loadCurrentProject(sceneName);
                            }
                        });
                break;

            case UIResolutionBox.REPACK_BTN_CLICKED:
                ResolutionManager resolutionManager = facade.retrieveProxy(ResolutionManager.NAME);
                resolutionManager.rePackProjectImagesForAllResolutions();
                String sceneName = sandbox.sceneControl.getCurrentSceneVO().sceneName;
                sandbox.loadCurrentProject(sceneName);
                break;
        }
    }
}
