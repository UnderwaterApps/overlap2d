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

package com.uwsoft.editor.mvc.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.kotcrab.vis.ui.util.dialog.DialogUtils;
import com.kotcrab.vis.ui.util.dialog.InputDialogListener;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;
import com.puremvc.patterns.mediator.SimpleMediator;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.data.manager.PreferencesManager;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.ProjectManager;
import com.uwsoft.editor.mvc.proxy.SceneDataManager;
import com.uwsoft.editor.renderer.data.SceneVO;

/**
 * Created by sargis on 3/25/15.
 */
public class Overlap2DMenuBarMediator extends SimpleMediator<Overlap2DMenuBar> {
    private static final String TAG = Overlap2DMenuBarMediator.class.getCanonicalName();
    public static final String NAME = TAG;
    private ProjectManager projectManager;

    public Overlap2DMenuBarMediator() {
        super(NAME, new Overlap2DMenuBar());
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
                //FILE
                Overlap2DMenuBar.NEW_PROJECT,
                Overlap2DMenuBar.OPEN_PROJECT,
                Overlap2DMenuBar.SAVE_PROJECT,
                Overlap2DMenuBar.IMPORT_TO_LIBRARY,
                Overlap2DMenuBar.EXPORT,
                Overlap2DMenuBar.EXPORT_SETTINGS,
                Overlap2DMenuBar.EXIT,
                Overlap2DMenuBar.NEW_SCENE,
                Overlap2DMenuBar.SELECT_SCENE,
                Overlap2DMenuBar.DELETE_CURRENT_SCENE,
                //EDIT
                Overlap2DMenuBar.CUT,
                Overlap2DMenuBar.COPY,
                Overlap2DMenuBar.PAST,
                Overlap2DMenuBar.UNDO,
                Overlap2DMenuBar.REDO,
                //General
                ProjectManager.PROJECT_OPENED,
        };
    }

    @Override
    public void handleNotification(Notification notification) {
        super.handleNotification(notification);
        String type = notification.getType();
        if (type == null) {
            handleGeneralNotification(notification);
            return;
        }
        switch (type) {
            case Overlap2DMenuBar.FILE_MENU:
                handleFileMenuNotification(notification);
                break;
            case Overlap2DMenuBar.EDIT_MENU:
                handleEditMenuNotification(notification);
                break;
            default:
                break;
        }
    }

    private void handleGeneralNotification(Notification notification) {
        switch (notification.getName()) {
            case ProjectManager.PROJECT_OPENED:
                onProjectOpened();
                break;
        }
    }

    private void handleEditMenuNotification(Notification notification) {
        Sandbox sandbox = Sandbox.getInstance();
        switch (notification.getName()) {
            case Overlap2DMenuBar.CUT:
                sandbox.getUac().cutAction();
                break;
            case Overlap2DMenuBar.COPY:
                sandbox.getUac().copyAction();
                break;
            case Overlap2DMenuBar.PAST:
                sandbox.getUac().pasteAction(0, 0, false);
                break;
            case Overlap2DMenuBar.UNDO:
                sandbox.getUac().undo();
                break;
            case Overlap2DMenuBar.REDO:
                sandbox.getUac().redo();
                break;
        }
    }

    private void handleFileMenuNotification(Notification notification) {
        Sandbox sandbox = Sandbox.getInstance();
        switch (notification.getName()) {
            case Overlap2DMenuBar.NEW_PROJECT:
                break;
            case Overlap2DMenuBar.OPEN_PROJECT:
                showOpenProject();
                break;
            case Overlap2DMenuBar.SAVE_PROJECT:
                SceneVO vo = sandbox.sceneVoFromItems();
                projectManager.saveCurrentProject(vo);
                break;
            case Overlap2DMenuBar.IMPORT_TO_LIBRARY:
                //showDialog("showImportDialog");
                break;
            case Overlap2DMenuBar.RECENT_PROJECTS:
                recentProjectItemClicked(notification.getBody());
                //showDialog("showImportDialog");
                break;
            case Overlap2DMenuBar.EXPORT:
                projectManager.exportProject();
                break;
            case Overlap2DMenuBar.EXPORT_SETTINGS:
                //showDialog("showExportDialog");
                break;
            case Overlap2DMenuBar.EXIT:
                Gdx.app.exit();
                break;
            case Overlap2DMenuBar.NEW_SCENE:
                DialogUtils.showInputDialog(sandbox.getUIStage(), "Create New Scene", "Scene Name : ", new InputDialogListener() {
                    @Override
                    public void finished(String input) {
                        if (input == null || input.equals("")) {
                            return;
                        }
                        SceneDataManager sceneDataManager = facade.retrieveProxy(SceneDataManager.NAME);
                        sceneDataManager.createNewScene(input);
                        sandbox.loadScene(input);
                        onScenesChanged();
                    }

                    @Override
                    public void canceled() {

                    }
                });
                break;
            case Overlap2DMenuBar.SELECT_SCENE:
                sceneMenuItemClicked(notification.getBody());
                break;
            case Overlap2DMenuBar.DELETE_CURRENT_SCENE:
                DialogUtils.showConfirmDialog(sandbox.getUIStage(),
                        "Delete Scene", "Do you realy want to delete '" + projectManager.currentProjectVO.lastOpenScene + "' scene?",
                        new String[]{"Delete", "Cancel"}, new Integer[]{0, 1}, result -> {
                            if (result == 0) {
                                SceneDataManager sceneDataManager = facade.retrieveProxy(SceneDataManager.NAME);
                                sceneDataManager.deleteCurrentScene();
                                sandbox.loadScene("MainScene");
                                onScenesChanged();
                            }
                        });
                break;
        }
    }

    private void onScenesChanged() {
        viewComponent.reInitScenes(projectManager.currentProjectInfoVO.scenes);
    }

    private void onProjectOpened() {
        viewComponent.reInitScenes(projectManager.currentProjectInfoVO.scenes);
        viewComponent.setProjectOpen(true);
    }

    public void showOpenProject() {
        Sandbox sandbox = Sandbox.getInstance();
        //chooser creation
        FileChooser fileChooser = new FileChooser(FileChooser.Mode.OPEN);

        // TODO: does not show folders on Windows
        //fileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);
        //fileChooser.setFileFilter(new SuffixFileFilter(".pit"));

        fileChooser.setMultiselectionEnabled(false);
        fileChooser.setListener(new FileChooserAdapter() {
            @Override
            public void selected(FileHandle file) {
                String path = file.file().getAbsolutePath();
                if (path.length() > 0) {
                    projectManager.openProjectFromPath(path);
                    sandbox.loadCurrentProject();
                }
            }
        });
        sandbox.getUIStage().addActor(fileChooser.fadeIn());
    }

    public void recentProjectItemClicked(String path) {
        PreferencesManager prefs = PreferencesManager.getInstance();
        prefs.buildRecentHistory();
        prefs.pushHistory(path);
        Sandbox sandbox = Sandbox.getInstance();
        projectManager.openProjectFromPath(path);
        sandbox.loadCurrentProject();
    }

    public void sceneMenuItemClicked(String sceneName) {
        Sandbox sandbox = Sandbox.getInstance();
        sandbox.loadScene(sceneName);
    }
}
