package com.uwsoft.editor.gdx.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.uwsoft.editor.controlles.NameConstants;
import com.uwsoft.editor.controlles.UIController;
import com.uwsoft.editor.data.manager.DataManager;
import com.uwsoft.editor.gdx.Overlap2DListener;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.gdx.stage.SandboxStage;
import com.uwsoft.editor.gdx.stage.UIStage;
import com.uwsoft.editor.gdx.ui.dialogs.ConfirmDialog;
import com.uwsoft.editor.gdx.ui.dialogs.InputDialog;
import com.uwsoft.editor.interfaces.IObserver;
import com.uwsoft.editor.renderer.data.SceneVO;

import java.io.File;

public class LevelEditorScreen extends Screen implements IObserver {

    public SandboxStage sandboxStage;
    public UIStage uiStage;
    private boolean paused = false;

    private Sandbox sandbox;

    public LevelEditorScreen(Overlap2DListener game) {
        super(game);

        sandbox = new Sandbox();
        sandboxStage = sandbox.getSandboxStage();
        uiStage = sandbox.getUIStage();
        sandboxStage.sandbox = sandbox;

        // check for demo project
        File demoDir = new File(DataManager.getInstance().getRootPath() + File.separator + "examples" + File.separator + "OverlapDemo");
        if (demoDir.isDirectory() && demoDir.exists()) {
            DataManager.getInstance().openProjectFromPath(demoDir.getAbsolutePath() + File.separator + "project.pit");
            sandbox.loadCurrentProject();
            uiStage.loadCurrentProject();
            sandboxStage.getCamera().position.set(400, 200, 0);
        }
    }

    @Override
    public void render(float deltaTime) {
        if (paused) {
            return;
        }
        GL20 gl = Gdx.gl;
        gl.glClearColor(0.129f, 0.129f, 0.129f, 1.0f);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        sandboxStage.act(deltaTime);
        sandboxStage.draw();

        uiStage.act(deltaTime);
        uiStage.draw();
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume(boolean b) {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }

    @Override
    public void show() {
        // TODO Auto-generated method stub

    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public String getObserverName() {
        return NameConstants.GDX_API;
    }

    @Override
    public String[] listNotificationInterests() {
        return new String[]{
                NameConstants.SAVE_PROJECT,
                NameConstants.OPEN_PROJECT,
                NameConstants.CREATE_PROJECT,
                NameConstants.IMPORT_DIALOG_SHOW,
                NameConstants.UNDO,
                NameConstants.REDO,
                NameConstants.CUT_ACTION,
                NameConstants.COPY_ACTION,
                NameConstants.PASTE_ACTION,
                NameConstants.EXIT_GDX,
                NameConstants.CRATE_NEW_SCENE,
                NameConstants.DELETE_CURRENT_SCENE,
                NameConstants.LOAD_SCENE,
                NameConstants.SHOW_BUILD_SETTING,
                NameConstants.BUILD_PROJECT
        };
    }

    @Override
    public void handleNotification(final String notificationName, final Object body) {
        Gdx.app.postRunnable(new Runnable() {

            @Override
            public void run() {
                switch (notificationName) {
                    case NameConstants.EXIT_GDX:
                        Gdx.app.exit();
                        break;
                    case NameConstants.CREATE_PROJECT:
                        uiStage.dialogs().createNewProjectDialg();
                        break;
                    case NameConstants.OPEN_PROJECT:
                        DataManager.getInstance().openProjectFromPath((String) body);
                        sandbox.loadCurrentProject();
                        uiStage.loadCurrentProject();
                        break;
                    case NameConstants.SAVE_PROJECT:
                        SceneVO vo = sandbox.sceneVoFromItems();
                        //uiStage.compositePanel.updateOriginalItem();
                        DataManager.getInstance().saveScene(vo);
                        DataManager.getInstance().saveCurrentProject();
                        break;
                    case NameConstants.UNDO:
                        sandbox.undo();
                        break;
                    case NameConstants.REDO:
                        sandbox.redo();
                        break;
                    case NameConstants.CUT_ACTION:
                        sandbox.cutAction();
                        break;
                    case NameConstants.COPY_ACTION:
                        sandbox.copyAction();
                        break;
                    case NameConstants.PASTE_ACTION:
                        sandbox.pasteAction(0, 0, false);
                        break;
                    case NameConstants.IMPORT_DIALOG_SHOW:
                        uiStage.dialogs().showImportDialog();
                        break;
                    case NameConstants.CRATE_NEW_SCENE:
                        InputDialog inputDialog = uiStage.dialogs().showInputDialog();
                        inputDialog.setListener(new InputDialog.InputDialogListener() {
                            @Override
                            public void onConfirm(String input) {
                                if (input == null || input.equals("")) {
                                    return;
                                }
                                SceneVO sceneVO = DataManager.getInstance().createNewScene(input);
                                sandboxStage.loadScene(input);
                                uiStage.reInitLibrary();
                                UIController.instance.sendNotification(NameConstants.NEW_SCENE_CRATED, sceneVO);
                            }
                        });
                        break;
                    case NameConstants.DELETE_CURRENT_SCENE:
                        ConfirmDialog confirmDialog = uiStage.dialogs().showConfirmDialog();
                        confirmDialog.setDescription("Are you sure you want to delete current scene?");
                        confirmDialog.setListener(new ConfirmDialog.ConfirmDialogListener() {

                            @Override
                            public void onConfirm() {
                                DataManager.getInstance().deleteCurrentScene();
                                sandboxStage.loadScene("MainScene");
                                uiStage.reInitLibrary();
                                UIController.instance.sendNotification(NameConstants.SCENE_DELETED, DataManager.getInstance().getCurrentProjectInfoVO());
                            }

                            @Override
                            public void onCancel() {

                            }
                        });
                        break;
                    case NameConstants.LOAD_SCENE:
                        sandboxStage.loadScene((String) body);
                        uiStage.reInitLibrary();
                        break;
                    case NameConstants.SHOW_BUILD_SETTING:
                        uiStage.dialogs().showExportDialog();
                        break;
                    case NameConstants.BUILD_PROJECT:
                        DataManager.getInstance().buildProject();
                        break;
                }

            }
        });

    }

    @Override
    public void resize(int width, int height) {
        uiStage.resize(width, height);//getViewport().update(width, height, true);
        sandboxStage.resize(width, height);//getViewport().update(width, height, true);
    }

	
	
	/*
    public void updateSelectedObject(Actor body) {
		gameStage.updateSelectedObject(body);
	}
	
	public void updateCustomVars(String str) {
		gameStage.updateCustomVars(str);
	}
	

	@Override
	public void handleNotification(String notificationName, Object body) {
		// TODO Auto-generated method stub
		
	}*/

}
