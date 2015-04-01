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

package com.uwsoft.editor.gdx.ui.menubar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;
import com.uwsoft.editor.mvc.proxy.SceneDataManager;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.gdx.stage.UIStage;
import com.uwsoft.editor.gdx.ui.dialogs.ConfirmDialog;
import com.uwsoft.editor.gdx.ui.dialogs.InputDialog;
import com.uwsoft.editor.gdx.ui.menubar.commands.EditMenuCommand;
import com.uwsoft.editor.gdx.ui.menubar.commands.FileMenuCommand;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.ProjectManager;
import com.uwsoft.editor.renderer.data.SceneVO;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by sargis on 3/25/15.
 */
public class Overlap2DMenuBarMediator {
    private static final String TAG = Overlap2DMenuBarMediator.class.getCanonicalName();
    private final ProjectManager projectManager;
    private final Sandbox sandbox;
    private final Overlap2DFacade facade;
    private Overlap2DMenuBar overlap2DMenuBar;

    public Overlap2DMenuBarMediator() {
        facade = Overlap2DFacade.getInstance();
        projectManager = facade.retrieveProxy(ProjectManager.NAME);
        sandbox = Sandbox.getInstance();
    }

    public void editMenuItemClicked(EditMenuCommand command) {
        switch (command) {
            case CUT:
                sandbox.getUac().cutAction();
                break;
            case COPY:
                sandbox.getUac().copyAction();
                break;
            case PAST:
                sandbox.getUac().pasteAction(0, 0, false);
                break;
            case UNDO:
                sandbox.getUac().undo();
                break;
            case REDO:
                sandbox.getUac().redo();
                break;
        }
    }

    public void fileMenuItemClicked(FileMenuCommand command) {
        switch (command) {
            case NEW_PROJECT:
                showDialog("createNewProjectDialog");
                break;
            case OPEN_PROJECT:
                showOpenProject();
                break;
            case SAVE_PROJECT:
                SceneVO vo = sandbox.sceneVoFromItems();
                projectManager.saveCurrentProject(vo);
                break;
            case IMPORT_TO_LIBRARY:
                showDialog("showImportDialog");
                break;
            case EXPORT:
                projectManager.exportProject();
                break;
            case EXPORT_SETTINGS:
                showDialog("showExportDialog");
                break;
            case EXIT:
                Gdx.app.exit();
                break;
            case CRATE_NEW_SCENE:

                showInputDialog(new InputDialog.InputDialogListener() {
                    @Override
                    public void onConfirm(String input) {
                        if (input == null || input.equals("")) {
                            return;
                        }
                        SceneDataManager sceneDataManager = facade.retrieveProxy(SceneDataManager.NAME);
                        sceneDataManager.createNewScene(input);
                        sandbox.loadScene(input);
                        onScenesChanged();
                    }
                });
                break;
            case DELETE_CURRENT_SCENE:
                showConfirmDialog("Are you sure you want to delete current scene?",
                        new ConfirmDialog.ConfirmDialogListener() {
                            @Override
                            public void onConfirm() {
                                SceneDataManager sceneDataManager = facade.retrieveProxy(SceneDataManager.NAME);
                                sceneDataManager.deleteCurrentScene();
                                sandbox.loadScene("MainScene");
                                onScenesChanged();
                            }

                            @Override
                            public void onCancel() {

                            }
                        });
                break;
        }
    }

    private void onScenesChanged() {
        overlap2DMenuBar.reInitScenes(projectManager.currentProjectInfoVO.scenes);
    }

    private void onProjectOpened() {
        overlap2DMenuBar.addScenes(projectManager.currentProjectInfoVO.scenes);
        overlap2DMenuBar.setProjectOpen(true);
    }

	 public void showOpenProject() {
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
						  onProjectOpened();
					 }
				}
		  });
		  sandbox.getUIStage().addActor(fileChooser.fadeIn());
	 }

	 public void showInputDialog(InputDialog.InputDialogListener inputDialogListener) {
        UIStage uiStage = sandbox.getUIStage();
        InputDialog inputDialog = uiStage.dialogs().showInputDialog();
        inputDialog.setListener(inputDialogListener);
    }

    public void showDialog(String dialog) {
        try {
            UIStage uiStage = sandbox.getUIStage();
            Method method = uiStage.dialogs().getClass().getMethod(dialog);
            method.invoke(uiStage.dialogs());
        } catch (SecurityException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

	 public void showConfirmDialog(String description, ConfirmDialog.ConfirmDialogListener confirmDialogListener) {
        final UIStage uiStage = sandbox.getUIStage();
        ConfirmDialog confirmDialog = uiStage.dialogs().showConfirmDialog();
        confirmDialog.setDescription(description);
        confirmDialog.setListener(confirmDialogListener);
    }

    public void setTarget(Overlap2DMenuBar overlap2DMenuBar) {
        this.overlap2DMenuBar = overlap2DMenuBar;
        overlap2DMenuBar.setProjectOpen(false);
    }

    public void sceneMenuItemClicked(String sceneName) {
        sandbox.loadScene(sceneName);
    }
}
