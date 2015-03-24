package com.uwsoft.editor.gdx.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.gdx.extension.ui.menu.ContextMenu;
import com.gdx.extension.ui.menu.MenuBar;
import com.gdx.extension.ui.menu.MenuItem;
import com.uwsoft.editor.data.manager.EditorResourceManager;
import com.uwsoft.editor.data.manager.TextureManager;
import com.uwsoft.editor.gdx.stage.UIStage;
import com.uwsoft.editor.gdx.ui.layer.UILayerBox;
import com.uwsoft.editor.view.MenuToolbar;

/**
 * Created by sargis on 9/10/14.
 */
public class UIMainTable extends Table {
    private final UIStage uiStage;
    public UICompositePanel compositePanel;
    public UILayerBox layerPanel;
    public UILightBox lightBox;
    public UIItemsBox itemsBox;
	 private UIMenuContainer menu;
	 public UIPropertiesBox propertiesPanel;
    public UILibraryBox libraryPanel;
    public Table rightTable;
    public Table leftTable;
    private UIToolBox toolPanel;

    public UIMainTable(UIStage uiStage) {
        this.uiStage = uiStage;
        //debug(); // turn on all debug lines (uiMainTable, cell, and widget)
        //debugTable(); // turn on only uiMainTable lines
        //debugCell();

        top();
        setFillParent(true);
        //
        initTop();
        row();
        initLeft();
        initRight();

		  menu.setZIndex(9999);
    }

    private void initRight() {
        rightTable = new Table();
//        rightTable.debug();
//        rightTable.debugTable();
//        rightTable.debugCell();
        rightTable.padTop(10);
        //
        propertiesPanel = new UIPropertiesBox(uiStage);
        propertiesPanel.initPanel();
        rightTable.add(propertiesPanel).top().fillY();
        rightTable.row();
        //
        libraryPanel = new UILibraryBox(uiStage);
        libraryPanel.initPanel();
        rightTable.add(libraryPanel).top().fillY();
        rightTable.row();
        //
        layerPanel = new UILayerBox(uiStage);
        layerPanel.initPanel();
        rightTable.add(layerPanel).top().fillY();
        //
        add(rightTable).top().right().padRight(5).expand();
    }

    private void initLeft() {
        //
        leftTable = new Table();
//        leftTable.debug();
//        leftTable.debugTable();
//        leftTable.debugCell();
        leftTable.padTop(10);
        //
        toolPanel = new UIToolBox(uiStage);
        toolPanel.initPanel();
        leftTable.add(toolPanel).top().fillY();
        leftTable.row();
        //
        lightBox = new UILightBox(uiStage);
        lightBox.initPanel();
        leftTable.add(lightBox).top().fillY();
        leftTable.row();
        //
        itemsBox = new UIItemsBox(uiStage);
        itemsBox.initPanel();
        leftTable.add(itemsBox).top().fillY();
        //
        add(leftTable).top().left().padLeft(5).expand();
    }

    private void initTop() {
		  // init menu bar
		  Skin skin = ((EditorResourceManager)uiStage.essentials.rm).getNewEditorSkin();

		  menu = new UIMenuContainer(uiStage, skin);

		  add(menu).left().expandX();

		  row();

        compositePanel = new UICompositePanel(uiStage);
        compositePanel.initPanel();
        add(compositePanel).left().expandX();

        add();
    }
}
