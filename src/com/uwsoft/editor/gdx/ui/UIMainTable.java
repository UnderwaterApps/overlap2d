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

package com.uwsoft.editor.gdx.ui;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.uwsoft.editor.data.manager.DataManager;
import com.uwsoft.editor.gdx.stage.UIStage;
import com.uwsoft.editor.gdx.ui.layer.UILayerBox;
import com.uwsoft.editor.gdx.ui.menubar.Overlap2DMenuBar;
import com.uwsoft.editor.gdx.ui.menubar.Overlap2DMenuBarMediator;

/**
 * Created by sargis on 9/10/14.
 */
public class UIMainTable extends Table {
	private final UIStage uiStage;
	public UICompositePanel compositePanel;
	public UILayerBox layerPanel;
	public UILightBox lightBox;
	public UIItemsBox itemsBox;
	public UIPropertiesBox propertiesPanel;
	public UILibraryBox libraryPanel;
	public Table rightTable;
	public Table centerTable;
	public Table leftTable;
	private UIToolBox toolPanel;
	public Label lblZoom;
	public Label lblX;
	public Label lblY;
	
	private StringBuilder strBuilder;

	public Overlap2DMenuBarMediator menuMediator;

	public UIMainTable(UIStage uiStage) {
		this.uiStage = uiStage;
		
		strBuilder = new StringBuilder();
		
		//debug(); // turn on all debug lines (uiMainTable, cell, and widget)
		//debugTable(); // turn on only uiMainTable lines
		//debugCell();

		top();
		setFillParent(true);
		row().colspan(3);
		//
		initTop();
		row();
		//
		initLeft(); // column 1
		initCenter(); // column 2
		initRight(); // column 3

		//menu.setZIndex(9999);
	}
	
	private void initTop() {
		// init menu bar
		menuMediator = new Overlap2DMenuBarMediator();
		Overlap2DMenuBar menuBar = new Overlap2DMenuBar(menuMediator);
		add(menuBar.getTable()).fillX().expandX();
		
		row().colspan(3);
		
		compositePanel = new UICompositePanel(uiStage);
		compositePanel.initPanel();
		add(compositePanel).left().expandX();
	}

	private void initLeft() {
		//
		leftTable = new Table();
		//leftTable.debug();
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
		add(leftTable).top().left().padLeft(5);
	}

	private void initCenter() {
		centerTable = new Table();
		centerTable.padTop(10);

		//centerTable.debug();

		// UI labels over sandbox
		lblX = new Label("", DataManager.getInstance().textureManager.editorSkin);
		lblX.setTouchable(Touchable.disabled);
		centerTable.add(lblX).top().padRight(10);
		
		lblY = new Label("", DataManager.getInstance().textureManager.editorSkin);
		lblY.setTouchable(Touchable.disabled);
		centerTable.add(lblY).top().padRight(10);
		
		lblZoom = new Label("", DataManager.getInstance().textureManager.editorSkin);
		lblZoom.setTouchable(Touchable.disabled);
		centerTable.add(lblZoom).top().padRight(10);
		
		add(centerTable).top().expand().right();
	}
	
	private void initRight() {
		rightTable = new Table();
//		rightTable.debug();
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
		add(rightTable).top().right().padRight(5);
	}
	
	public void updateZoom(int zoom)
	{
		strBuilder.setLength(0);
		strBuilder.append("Zoom: ");
		strBuilder.append(String.valueOf(zoom));
		strBuilder.append("%");
		lblZoom.setText(strBuilder.toString());
	}
	
	public void updateXandY(Vector2 coordinates)
	{
		strBuilder.setLength(0);
		strBuilder.append("x: ");
		strBuilder.append(String.valueOf((int) coordinates.x));
		lblX.setText(strBuilder.toString());
		
		strBuilder.setLength(0);
		strBuilder.append("y: ");
		strBuilder.append(String.valueOf((int) coordinates.y));
		lblY.setText(strBuilder.toString());
	}

}
