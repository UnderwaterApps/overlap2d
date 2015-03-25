package com.uwsoft.editor.gdx.ui.menubar;

import com.badlogic.gdx.Gdx;
import com.uwsoft.editor.data.manager.DataManager;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.gdx.stage.UIStage;
import com.uwsoft.editor.gdx.ui.menubar.commands.EditMenuCommand;
import com.uwsoft.editor.gdx.ui.menubar.commands.FileMenuCommand;

import javax.swing.*;

/**
 * Created by sargis on 3/25/15.
 */
public class Overlap2DMenuBarMediator {
    private final DataManager dataManager;
    private final Sandbox sandbox;

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
                                }
                            });
                        }
                    }
                });
                break;
            case SAVE_PROJECT:
                break;
            case IMPORT_TO_LIBRARY:
                break;
            case EXPORT:
                break;
            case EXPORT_SETTINGS:
                break;
            case EXIT:
                System.exit(0);
                break;
        }
    }
}
