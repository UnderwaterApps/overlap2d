package com.uwsoft.editor.gdx.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.gdx.extension.ui.menu.*;
import com.gdx.extension.ui.tab.TabContainer;

/**
 * Created by azakhary on 3/24/2015.
 */
public class UIMenuContainer extends TabContainer {

	 public UIMenuContainer(Stage stage, Skin skin) {
		  super(skin);

		  MenuBar _menuBar = new MenuBar(skin);

		  ContextMenu fileMenu = new ContextMenu();
		  ContextMenu editMenu = new ContextMenu();
		  ContextMenu helpMenu = new ContextMenu();

		  TextButton fileButton = new TextButton("File", skin);
		  TextButton editButton = new TextButton("Edit", skin);
		  TextButton helpButton = new TextButton("Help", skin);


		  fileMenu.addMenu(new BasicMenuItem("New Project", skin));
		  fileMenu.addMenu(new BasicMenuItem("Open Project", skin));
		  fileMenu.addMenu(new BasicMenuItem("Save Project", skin));
		  SubMenuItem sceneMenuItem = new SubMenuItem("Scene", skin);
		  ContextMenu sceneMenu = new ContextMenu();
		  sceneMenu.addMenu(new BasicMenuItem("Create New Scene", skin));
		  sceneMenu.addMenu(new BasicMenuItem("Delete Current Scene", skin));
		  sceneMenuItem.setContextMenu(sceneMenu, stage);
		  fileMenu.addMenu(sceneMenuItem);
		  fileMenu.addMenu(new BasicMenuItem("Import to Library", skin));
		  fileMenu.addMenu(new BasicMenuItem("Export", skin));
		  fileMenu.addMenu(new BasicMenuItem("Export Settings", skin));
		  fileMenu.addMenu(new BasicMenuItem("Exit", skin));

		  editMenu.addMenu(new BasicMenuItem("Cut", skin));
		  editMenu.addMenu(new BasicMenuItem("Copy", skin));
		  editMenu.addMenu(new BasicMenuItem("Paste", skin));
		  editMenu.addMenu(new BasicMenuItem("Undo", skin));
		  editMenu.addMenu(new BasicMenuItem("Redo", skin));

		  helpMenu.addMenu(new BasicMenuItem("Online Documentation", skin));


		  fileButton.setWidth(75f); editButton.setWidth(75f); helpButton.setWidth(75f);
		  _menuBar.addContextMenu(fileButton, fileMenu, stage);
		  _menuBar.addContextMenu(editButton, editMenu, stage);
		  _menuBar.addContextMenu(helpButton, helpMenu, stage);


		  /*
		  fileMenu.addMenu(new BasicMenuItem("Open ...", skin));

		  SubMenuItem _subMenu = new SubMenuItem("Recent ...", skin);
		  ContextMenu _recentMenu = new ContextMenu();
		  _recentMenu.addMenu(new BasicMenuItem("something.txt", skin));
		  _recentMenu.addMenu(new BasicMenuItem("somethingelse.txt", skin));
		  _recentMenu.addMenu(new BasicMenuItem("again.txt", skin));
		  _subMenu.setContextMenu(_recentMenu, stage);
		  fileMenu.addMenu(_subMenu);

		  SubMenuItem _testSub = new SubMenuItem("Test", skin);
		  ContextMenu _textContext = new ContextMenu();
		  _testSub.setContextMenu(_textContext, stage);
		  _textContext.addMenu(new BasicMenuItem("Test", skin));
		  fileMenu.addMenu(_testSub);


		  fileButton.setWidth(75f);
		  _menuBar.addContextMenu(fileButton, fileMenu, stage);


		  _editMenu.addMenu(new BasicMenuItem("Copy", skin));
		  _editMenu.addMenu(new BasicMenuItem("Cut", skin));
		  _editMenu.addMenu(new BasicMenuItem("Paste", skin));


		  editButton.setWidth(75f);
		  _menuBar.addContextMenu(editButton, _editMenu, stage);


		  _helpMenu.addMenu(new BasicMenuItem("About", skin));


		  helpButton.setWidth(75f);
		  _menuBar.addContextMenu(helpButton, _helpMenu, stage);
			*/

		  add(_menuBar).expandX().fillX().minHeight(25f).row();

		  top();
	 }
}
