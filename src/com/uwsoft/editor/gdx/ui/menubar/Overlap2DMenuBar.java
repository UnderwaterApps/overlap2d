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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.util.dialog.DialogUtils;
import com.kotcrab.vis.ui.widget.Menu;
import com.kotcrab.vis.ui.widget.MenuBar;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.PopupMenu;
import com.uwsoft.editor.data.manager.PreferencesManager;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.gdx.ui.menubar.commands.EditMenuCommand;
import com.uwsoft.editor.gdx.ui.menubar.commands.FileMenuCommand;
import com.uwsoft.editor.renderer.data.SceneVO;

import org.apache.commons.lang3.SystemUtils;

import java.io.File;
import java.util.ArrayList;


public class Overlap2DMenuBar extends MenuBar {
    private static final String TAG = Overlap2DMenuBar.class.getCanonicalName();
    private final Overlap2DMenuBarMediator mediator;
    private final FileMenu fileMenu;
    private final String maskKey;
    private final EditMenu editMenu;


    public Overlap2DMenuBar(Overlap2DMenuBarMediator mediator) {
        this.mediator = mediator;
        maskKey = SystemUtils.IS_OS_MAC_OSX || SystemUtils.IS_OS_MAC ? "Cmd" : "Ctrl";
        fileMenu = new FileMenu();
        editMenu = new EditMenu();
        addMenu(fileMenu);
        addMenu(editMenu);
        mediator.setTarget(this);
    }


    public void addScenes(ArrayList<SceneVO> scenes) {
        fileMenu.addScenes(scenes);
    }

    public void reInitScenes(ArrayList<SceneVO> scenes) {
        fileMenu.reInitScenes(scenes);
    }
    
    public void reInitRecent(ArrayList<String> paths) { 
    	fileMenu.reInitRecent(paths);
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
            cut = new MenuItem("Cut", new EditMenuListener(EditMenuCommand.CUT)).setShortcut(maskKey + " + X");
            copy = new MenuItem("Copy", new EditMenuListener(EditMenuCommand.COPY)).setShortcut(maskKey + " + C");
            paste = new MenuItem("Paste", new EditMenuListener(EditMenuCommand.PAST)).setShortcut(maskKey + " + P");
            undo = new MenuItem("Undo", new EditMenuListener(EditMenuCommand.UNDO)).setShortcut(maskKey + " + Z");
            redo = new MenuItem("Redo", new EditMenuListener(EditMenuCommand.REDO)).setShortcut(maskKey + " + Y");
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

        private final PopupMenu scenesPopupMenu;
        private final Array<MenuItem> sceneMenuItems;
        private final MenuItem saveProject;
        private final MenuItem scenesMenuItem;
        private final MenuItem importToLibrary;
        private final MenuItem export;
        private final MenuItem exportSettings;
        
        private final PopupMenu recentProjectsPopupMenu;
        private final Array<MenuItem> recentProjectsMenuItems;
        private final MenuItem recentProjectsMenuItem;

        public FileMenu() {
            super("File");
            pad(5);
            saveProject = new MenuItem("Save Project", new FileMenuListener(FileMenuCommand.SAVE_PROJECT));
            addItem(new MenuItem("New Project", new FileMenuListener(FileMenuCommand.NEW_PROJECT)));
            addItem(new MenuItem("Open Project", new FileMenuListener(FileMenuCommand.OPEN_PROJECT)));
            addItem(saveProject);
            //
            scenesMenuItem = new MenuItem("Scenes");
            scenesPopupMenu = new PopupMenu();

            scenesMenuItem.setSubMenu(scenesPopupMenu);
            addItem(scenesMenuItem);
            //
            addSeparator();
            importToLibrary = new MenuItem("Import to Library", new FileMenuListener(FileMenuCommand.IMPORT_TO_LIBRARY));
            export = new MenuItem("Export", new FileMenuListener(FileMenuCommand.EXPORT));
            exportSettings = new MenuItem("Export Settings", new FileMenuListener(FileMenuCommand.EXPORT_SETTINGS));
            addItem(importToLibrary);
            addItem(export);
            addItem(exportSettings);
            //
            addSeparator();
            recentProjectsMenuItem = new MenuItem("Recent Projects...");
            recentProjectsPopupMenu = new PopupMenu();
            recentProjectsMenuItem.setSubMenu(recentProjectsPopupMenu);
            addItem(recentProjectsMenuItem);
            recentProjectsMenuItems = new Array<>();
            PreferencesManager prefs = PreferencesManager.getInstance();
            prefs.buildRecentHistory();
            addRecent(prefs.getRecentHistory());
            //
            addSeparator();
            addItem(new MenuItem("Exit", new FileMenuListener(FileMenuCommand.EXIT)));
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
            sceneMenuItems.clear();
				scenesPopupMenu.clear();

				scenesPopupMenu.addItem(new MenuItem("Create New Scene", new FileMenuListener(FileMenuCommand.CRATE_NEW_SCENE)));
				scenesPopupMenu.addItem(new MenuItem("Delete Current Scene", new FileMenuListener(FileMenuCommand.DELETE_CURRENT_SCENE)));

				scenesPopupMenu.addSeparator();

            addScenes(scenes);
        }
        
        public String getFolderName(String path) {
        	File path1 = new File(path);
        	File path2 = new File(path1.getParent());
        	return path2.getName();
        }
        
        public void addRecent(ArrayList<String> paths) { 
        	for (String path : paths) {
        		MenuItem menuItem = new MenuItem(getFolderName(path),new RecentProjectListener(path));
        		recentProjectsMenuItems.add(menuItem);
        		recentProjectsPopupMenu.addItem(menuItem);
        	}
        }
        
        public void reInitRecent(ArrayList<String> paths) {
        	recentProjectsMenuItems.clear();
        	recentProjectsPopupMenu.clear();
        	
        	addRecent(paths);
        }

        public void setProjectOpen(boolean open) {
            saveProject.setDisabled(!open);
            scenesMenuItem.setDisabled(!open);
            importToLibrary.setDisabled(!open);
            export.setDisabled(!open);
            exportSettings.setDisabled(!open);
        }
        
        private class RecentProjectListener extends ChangeListener {
        	private final String path;
        	public RecentProjectListener(String path) {
        		this.path = path;
        	}
        	
        	@Override
        	public void changed(ChangeEvent event, Actor actor) {
        		Gdx.app.log(TAG,"recentProject : " + path);
        		if (!new File(path).exists()) {
        			DialogUtils.showErrorDialog(Sandbox.getInstance().getUIStage(), "Current project does not exist!");
        			return;
        		}
        		mediator.recentProjectItemClicked(path);
        	}
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
