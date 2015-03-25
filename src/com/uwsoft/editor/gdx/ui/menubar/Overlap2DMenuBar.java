package com.uwsoft.editor.gdx.ui.menubar;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.Menu;
import com.kotcrab.vis.ui.widget.MenuBar;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.PopupMenu;
import com.uwsoft.editor.gdx.ui.menubar.commands.EditMenuCommand;
import com.uwsoft.editor.gdx.ui.menubar.commands.FileMenuCommand;


public class Overlap2DMenuBar extends MenuBar {
    private static final String TAG = Overlap2DMenuBar.class.getCanonicalName();
    private final Overlap2DMenuBarMediator mediator;
    private final FileMenu fileMenu;


    public Overlap2DMenuBar(Overlap2DMenuBarMediator mediator) {
        this.mediator = mediator;
        fileMenu = new FileMenu();
        addMenu(fileMenu);
        addMenu(new EditMenu());
        mediator.setTarget(this);
    }

    public PopupMenu getScenesPopupMenu() {
        return fileMenu.scenesPopupMenu;
    }

    class EditMenu extends Menu {


        public EditMenu() {
            super("Edit");
            pad(5);
            addItem(new MenuItem("Cut", new EditMenuListener(EditMenuCommand.CUT)));
            addItem(new MenuItem("Copy", new EditMenuListener(EditMenuCommand.COPY)));
            addItem(new MenuItem("Paste", new EditMenuListener(EditMenuCommand.PAST)));
            addItem(new MenuItem("Undo", new EditMenuListener(EditMenuCommand.UNDO)));
            addItem(new MenuItem("Redo", new EditMenuListener(EditMenuCommand.REDO)));
        }

        private class EditMenuListener extends ChangeListener {

            private final EditMenuCommand menuCommand;

            public EditMenuListener(EditMenuCommand menuCommand) {
                this.menuCommand = menuCommand;
            }

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                mediator.editMenuItemClicked(menuCommand);
            }
        }
    }

    class FileMenu extends Menu {

        public final PopupMenu scenesPopupMenu;

        public FileMenu() {
            super("File");
            pad(5);
            addItem(new MenuItem("New Project", new FileMenuListener(FileMenuCommand.NEW_PROJECT)));
            addItem(new MenuItem("Open Project", new FileMenuListener(FileMenuCommand.OPEN_PROJECT)));
            addItem(new MenuItem("Save Project", new FileMenuListener(FileMenuCommand.SAVE_PROJECT)));
            //
            MenuItem scenesMenuItem = new MenuItem("Scenes");
            scenesPopupMenu = new PopupMenu();
            scenesPopupMenu.addItem(new MenuItem("Create New Scene"));
            scenesPopupMenu.addItem(new MenuItem("Delete Current Scene"));
            scenesPopupMenu.addSeparator();
            scenesMenuItem.setSubMenu(scenesPopupMenu);
            addItem(scenesMenuItem);
            //
            addSeparator();
            addItem(new MenuItem("Import to Library", new FileMenuListener(FileMenuCommand.IMPORT_TO_LIBRARY)));
            addItem(new MenuItem("Export", new FileMenuListener(FileMenuCommand.EXPORT)));
            addItem(new MenuItem("Export Settings", new FileMenuListener(FileMenuCommand.EXPORT_SETTINGS)));
            addItem(new MenuItem("Exit", new FileMenuListener(FileMenuCommand.EXIT)));
        }

        private class FileMenuListener extends ChangeListener {
            private final FileMenuCommand menuCommand;

            public FileMenuListener(FileMenuCommand menuCommand) {
                this.menuCommand = menuCommand;
            }

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log(TAG, "menuCommand : " + menuCommand);
                mediator.fileMenuItemClicked(menuCommand);
            }
        }
    }


}
