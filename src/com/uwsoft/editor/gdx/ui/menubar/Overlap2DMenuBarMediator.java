package com.uwsoft.editor.gdx.ui.menubar;

import com.badlogic.gdx.Gdx;
import com.kotcrab.vis.ui.widget.PopupMenu;
import com.uwsoft.editor.data.manager.DataManager;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.gdx.stage.UIStage;
import com.uwsoft.editor.gdx.ui.menubar.commands.EditMenuCommand;
import com.uwsoft.editor.gdx.ui.menubar.commands.FileMenuCommand;
import com.uwsoft.editor.renderer.data.SceneVO;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by sargis on 3/25/15.
 */
public class Overlap2DMenuBarMediator {
    private static final String TAG = Overlap2DMenuBarMediator.class.getCanonicalName();
    private final DataManager dataManager;
    private final Sandbox sandbox;
    private Overlap2DMenuBar overlap2DMenuBar;

    public Overlap2DMenuBarMediator() {
        dataManager = DataManager.getInstance();
        sandbox = Sandbox.getInstance();
    }

    public void editMenuItemClicked(EditMenuCommand command) {
        switch (command) {
            case CUT:
                break;
            case COPY:
                break;
            case PAST:
                break;
            case UNDO:
                break;
            case REDO:
                break;
        }
    }

    public void fileMenuItemClicked(FileMenuCommand command) {
        switch (command) {
            case NEW_PROJECT:
                showDialog("createNewProjectDialog");
                break;
            case OPEN_PROJECT:
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        final JFileChooser fileChooser = new JFileChooser(DataManager.getInstance().getWorkspacePath());
                        fileChooser.showOpenDialog(null);
                        if (fileChooser.getSelectedFile() == null) {
                            return;
                        }
                        final String path = fileChooser.getSelectedFile().getAbsolutePath();
                        if (path.length() > 0) {
                            Gdx.app.postRunnable(new Runnable() {
                                @Override
                                public void run() {
                                    dataManager.openProjectFromPath(path);
                                    sandbox.loadCurrentProject();
                                    onProjectOpened();
                                }
                            });
                        }
                    }
                });
                break;
            case SAVE_PROJECT:
                SceneVO vo = sandbox.sceneVoFromItems();
                dataManager.saveCurrentProject(vo);
                break;
            case IMPORT_TO_LIBRARY:
                showDialog("showImportDialog");
                break;
            case EXPORT:
                dataManager.exportProject();
                break;
            case EXPORT_SETTINGS:
                showDialog("showExportDialog");
                break;
            case EXIT:
                Gdx.app.exit();
                break;
        }
    }

    private void onProjectOpened() {
//        overlap2DMenuBar.add
    }


    private void showDialog(String dialog) {
        try {
            UIStage uiStage = sandbox.getUIStage();
            Method method = uiStage.dialogs().getClass().getMethod(dialog);
            method.invoke(uiStage.dialogs());
        } catch (SecurityException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void setTarget(Overlap2DMenuBar overlap2DMenuBar) {
        this.overlap2DMenuBar = overlap2DMenuBar;
    }
}
