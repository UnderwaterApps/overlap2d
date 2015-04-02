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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.Menu;
import com.kotcrab.vis.ui.widget.MenuBar;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.PopupMenu;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.renderer.data.SceneVO;
import org.apache.commons.lang3.SystemUtils;

import java.util.ArrayList;


public class Overlap2DMenuBar extends MenuBar {
    public static final String FILE_MENU = "com.uwsoft.editor.mvc.view.Overlap2DMenuBar" + ".FILE_MENU";
    public static final String NEW_PROJECT = "com.uwsoft.editor.mvc.view.Overlap2DMenuBar" + ".NEW_PROJECT";
    public static final String OPEN_PROJECT = "com.uwsoft.editor.mvc.view.Overlap2DMenuBar" + ".OPEN_PROJECT";
    public static final String SAVE_PROJECT = "com.uwsoft.editor.mvc.view.Overlap2DMenuBar" + ".SAVE_PROJECT";
    public static final String IMPORT_TO_LIBRARY = "com.uwsoft.editor.mvc.view.Overlap2DMenuBar" + ".IMPORT_TO_LIBRARY";
    public static final String EXPORT = "com.uwsoft.editor.mvc.view.Overlap2DMenuBar" + ".EXPORT";
    public static final String EXPORT_SETTINGS = "com.uwsoft.editor.mvc.view.Overlap2DMenuBar" + ".EXPORT_SETTINGS";
    public static final String EXIT = "com.uwsoft.editor.mvc.view.Overlap2DMenuBar" + ".EXIT";
    public static final String CRATE_NEW_SCENE = "com.uwsoft.editor.mvc.view.Overlap2DMenuBar" + ".CRATE_NEW_SCENE";
    public static final String DELETE_CURRENT_SCENE = "com.uwsoft.editor.mvc.view.Overlap2DMenuBar" + ".DELETE_CURRENT_SCENE";
    //
    public static final String EDIT_MENU = "com.uwsoft.editor.mvc.view.Overlap2DMenuBar" + ".EDIT_MENU";
    public static final String CUT = "com.uwsoft.editor.mvc.view.Overlap2DMenuBar" + ".CUT";
    public static final String COPY = "com.uwsoft.editor.mvc.view.Overlap2DMenuBar" + ".COPY";
    public static final String PAST = "com.uwsoft.editor.mvc.view.Overlap2DMenuBar" + ".PAST";
    public static final String UNDO = "com.uwsoft.editor.mvc.view.Overlap2DMenuBar" + ".UNDO";
    public static final String REDO = "com.uwsoft.editor.mvc.view.Overlap2DMenuBar" + ".REDO";
    //
    private static final String TAG = Overlap2DMenuBar.class.getCanonicalName();
    private final FileMenu fileMenu;
    private final String maskKey;
    private final EditMenu editMenu;
    private final Overlap2DFacade facade;

    public Overlap2DMenuBar() {
        facade = Overlap2DFacade.getInstance();
        maskKey = SystemUtils.IS_OS_MAC_OSX || SystemUtils.IS_OS_MAC ? "Cmd" : "Ctrl";
        fileMenu = new FileMenu();
        editMenu = new EditMenu();
        addMenu(fileMenu);
        addMenu(editMenu);
    }


    public void addScenes(ArrayList<SceneVO> scenes) {
        fileMenu.addScenes(scenes);
    }

    public void reInitScenes(ArrayList<SceneVO> scenes) {
        fileMenu.reInitScenes(scenes);
    }

    public void setProjectOpen(boolean open) {
        fileMenu.setProjectOpen(open);
        editMenu.setProjectOpen(open);
    }

    class EditMenu extends Menu {


        private final MenuItem cut;
        private final MenuItem copy;
        private final MenuItem paste;
        private final MenuItem undo;
        private final MenuItem redo;

        public EditMenu() {
            super("Edit");
            pad(5);
            cut = new MenuItem("Cut", new EditMenuListener(CUT)).setShortcut(maskKey + " + X");
            copy = new MenuItem("Copy", new EditMenuListener(COPY)).setShortcut(maskKey + " + C");
            paste = new MenuItem("Paste", new EditMenuListener(PAST)).setShortcut(maskKey + " + P");
            undo = new MenuItem("Undo", new EditMenuListener(UNDO)).setShortcut(maskKey + " + Z");
            redo = new MenuItem("Redo", new EditMenuListener(REDO)).setShortcut(maskKey + " + Y");
            addItem(cut);
            addItem(copy);
            addItem(paste);
            addItem(undo);
            addItem(redo);
        }

        public void setProjectOpen(boolean open) {
            cut.setDisabled(!open);
            copy.setDisabled(!open);
            paste.setDisabled(!open);
            undo.setDisabled(!open);
            redo.setDisabled(!open);
        }

        private class EditMenuListener extends ChangeListener {

            private final String menuCommand;

            public EditMenuListener(String menuCommand) {
                this.menuCommand = menuCommand;
            }

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                facade.sendNotification(menuCommand, null, EDIT_MENU);
            }
        }
    }

    class FileMenu extends Menu {

        private final PopupMenu scenesPopupMenu;
        private final Array<MenuItem> sceneMenuItems;
        private final MenuItem saveProject;
        private final MenuItem scenesMenuItem;
        private final MenuItem importToLibrary;
        private final MenuItem export;
        private final MenuItem exportSettings;

        public FileMenu() {
            super("File");
            pad(5);
            saveProject = new MenuItem("Save Project", new FileMenuListener(SAVE_PROJECT));
            addItem(new MenuItem("New Project", new FileMenuListener(NEW_PROJECT)));
            addItem(new MenuItem("Open Project", new FileMenuListener(OPEN_PROJECT)));
            addItem(saveProject);
            //
            scenesMenuItem = new MenuItem("Scenes");
            scenesPopupMenu = new PopupMenu();
            scenesPopupMenu.addItem(new MenuItem("Create New Scene", new FileMenuListener(CRATE_NEW_SCENE)));
            scenesPopupMenu.addItem(new MenuItem("Delete Current Scene", new FileMenuListener(DELETE_CURRENT_SCENE)));
            scenesPopupMenu.addSeparator();
            scenesMenuItem.setSubMenu(scenesPopupMenu);
            addItem(scenesMenuItem);
            //
            addSeparator();
            importToLibrary = new MenuItem("Import to Library", new FileMenuListener(IMPORT_TO_LIBRARY));
            export = new MenuItem("Export", new FileMenuListener(EXPORT));
            exportSettings = new MenuItem("Export Settings", new FileMenuListener(EXPORT_SETTINGS));
            addItem(importToLibrary);
            addItem(export);
            addItem(exportSettings);
            addItem(new MenuItem("Exit", new FileMenuListener(EXIT)));
            sceneMenuItems = new Array<>();
        }

        public void addScenes(ArrayList<SceneVO> scenes) {
            for (SceneVO sceneVO : scenes) {
                MenuItem menuItem = new MenuItem(sceneVO.sceneName, new SceneMenuItemListener(sceneVO.sceneName));
                sceneMenuItems.add(menuItem);
                scenesPopupMenu.addItem(menuItem);
            }
        }

        public void reInitScenes(ArrayList<SceneVO> scenes) {
            for (MenuItem menuItem : sceneMenuItems) {
                menuItem.remove();
            }
            sceneMenuItems.clear();
            addScenes(scenes);
        }

        public void setProjectOpen(boolean open) {
            saveProject.setDisabled(!open);
            scenesMenuItem.setDisabled(!open);
            importToLibrary.setDisabled(!open);
            export.setDisabled(!open);
            exportSettings.setDisabled(!open);
        }

        private class SceneMenuItemListener extends ChangeListener {
            private final String sceneName;

            public SceneMenuItemListener(String sceneName) {
                this.sceneName = sceneName;
            }

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log(TAG, "sceneName : " + sceneName);
                mediator.sceneMenuItemClicked(sceneName);
            }
        }

        private class FileMenuListener extends ChangeListener {
            private final String menuCommand;

            public FileMenuListener(String menuCommand) {
                this.menuCommand = menuCommand;
            }

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                facade.sendNotification(menuCommand, null, FILE_MENU);
            }
        }
    }


}
