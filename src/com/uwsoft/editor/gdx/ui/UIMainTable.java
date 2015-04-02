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

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.uwsoft.editor.gdx.stage.UIStage;
import com.uwsoft.editor.gdx.ui.layer.UILayerBox;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.view.Overlap2DMenuBar;
import com.uwsoft.editor.mvc.view.Overlap2DMenuBarMediator;

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
    public Table leftTable;
    public Overlap2DMenuBarMediator menuMediator;
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

        //menu.setZIndex(9999);
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
        //TODO: need to be changed!
        Overlap2DMenuBar menuBar = (Overlap2DMenuBar) Overlap2DFacade.getInstance().retrieveMediator(Overlap2DMenuBarMediator.NAME).getViewComponent();
        add(menuBar.getTable()).fillX().expandX().row();
        //
        compositePanel = new UICompositePanel(uiStage);
        compositePanel.initPanel();
        add(compositePanel).left().expandX();
        add();
    }
}
