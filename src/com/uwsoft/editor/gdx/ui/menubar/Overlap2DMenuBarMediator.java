package com.uwsoft.editor.gdx.ui.menubar;

import com.uwsoft.editor.data.manager.DataManager;
import com.uwsoft.editor.gdx.ui.menubar.commands.EditMenuCommand;
import com.uwsoft.editor.gdx.ui.menubar.commands.FileMenuCommand;

import javax.swing.*;

/**
 * Created by sargis on 3/25/15.
 */
public class Overlap2DMenuBarMediator {
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
                final JFileChooser fc = new JFileChooser(DataManager.getInstance().getWorkspacePath());
                fc.showOpenDialog(null);
                if (fc.getSelectedFile() == null) {
                    return;
                }
                String dirpath = fc.getSelectedFile().getAbsolutePath().toString();
                if (dirpath != null && dirpath.length() > 0) {
                    //UIController.instance.sendNotification(NameConstants.OPEN_PROJECT, dirpath);
                }
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
