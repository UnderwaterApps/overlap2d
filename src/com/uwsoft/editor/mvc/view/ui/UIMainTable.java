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

package com.uwsoft.editor.mvc.view.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.widget.VisTable;
import com.uwsoft.editor.gdx.ui.UILibraryBox;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.view.Overlap2DMenuBar;
import com.uwsoft.editor.mvc.view.Overlap2DMenuBarMediator;
import com.uwsoft.editor.mvc.view.stage.UIStage;
import com.uwsoft.editor.mvc.view.ui.box.*;

/**
 * Created by sargis on 9/10/14.
 */
public class UIMainTable extends VisTable {
    private final UIStage uiStage;
    private final VisTable topTable;
    private final VisTable middleTable;
    private final Overlap2DFacade facade;
    public UISubmenuBar compositePanel;
    public UILayerBox layerPanel;
    public UIItemsTreeBox itemsBox;
    public UIMultiPropertyBox multiPropertyBox;
    public UILibraryBox libraryPanel;
    public Table rightToolsPanel;
    public Table leftToolsPanel;
    public Overlap2DMenuBarMediator menuMediator;
    private UIToolBox toolPanel;

    public UIMainTable(UIStage uiStage) {
        this.uiStage = uiStage;
        facade = Overlap2DFacade.getInstance();
//        debug();
        setFillParent(true);
        top();
        topTable = new VisTable();
        middleTable = new VisTable();
        add(topTable).fillX().expandX();
        row();
        add(middleTable).fillX().padTop(10);
        //
        initMenuBar();
        topTable.row();
        topTable.addSeparator();
        topTable.row();
        initCompisitePanel();
        initLeftToolsPanel();
        initRightToolsPanel();
    }

    private void initCompisitePanel() {
        compositePanel = new UISubmenuBar();
        topTable.add(compositePanel).fillX().expandX();
//        compositePanel.initPanel();
    }

    private void initRightToolsPanel() {
        rightToolsPanel = new Table();
        //
        UIMultiPropertyBoxMediator multiPropertyBoxMediator = facade.retrieveMediator(UIMultiPropertyBoxMediator.NAME);
        multiPropertyBox = multiPropertyBoxMediator.getViewComponent();

        //propertiesPanel = new UIPropertiesBox();
//        propertiesPanel.initPanel();
        rightToolsPanel.add(multiPropertyBox).top().fillY();
        rightToolsPanel.row().padTop(5);


        //
        //libraryPanel = new UILibraryBox(uiStage);
        // libraryPanel.initPanel();
        //rightToolsPanel.add(libraryPanel).top().fillY();
        //rightToolsPanel.row();
        //
        //layerPanel = new UILayerBoxOld(uiStage);
        //layerPanel.initPanel();
        //rightToolsPanel.add(layerPanel).top().fillY();
        //rightToolsPanel.row();

        UIResourcesBoxMediator resourceBoxMediator = facade.retrieveMediator(UIResourcesBoxMediator.NAME);
        UIResourcesBox resourceBox = resourceBoxMediator.getViewComponent();
        rightToolsPanel.add(resourceBox).top().fillY();
        rightToolsPanel.row().padTop(5);

        UILayerBoxMediator layerBoxMediator = facade.retrieveMediator(UILayerBoxMediator.NAME);
        layerPanel = layerBoxMediator.getViewComponent();
        rightToolsPanel.add(layerPanel).top().fillY();

        //
        middleTable.add(rightToolsPanel).top().right().expand();
    }

    private void initLeftToolsPanel() {
        //
        leftToolsPanel = new VisTable();
        //
        UIToolBoxMediator uiToolBoxMediator = facade.retrieveMediator(UIToolBoxMediator.NAME);
        toolPanel = uiToolBoxMediator.getViewComponent();
        leftToolsPanel.add(toolPanel).expandX().fillX();
        leftToolsPanel.row().padTop(5);
        //
        UIAlignBoxMediator uiAlignBoxMediator = facade.retrieveMediator(UIAlignBoxMediator.NAME);
        UIAlignBox uiAlignBox = uiAlignBoxMediator.getViewComponent();
        leftToolsPanel.add(uiAlignBox).expandX().fillX();
        leftToolsPanel.row().padTop(5);
        //
//        leftToolsPanel.add(lightBox);
//        leftToolsPanel.row();
        //
        UIItemsTreeBoxMediator uiItemsTreeBoxMediator = facade.retrieveMediator(UIItemsTreeBoxMediator.NAME);
        itemsBox = uiItemsTreeBoxMediator.getViewComponent();
//        itemsBox.initPanel();
        leftToolsPanel.add(itemsBox).expandX().fillX();
        //
        middleTable.add(leftToolsPanel).top().left().expand();
    }

    private void initMenuBar() {
        //TODO: need to be changed!
        Overlap2DMenuBarMediator overlap2DMenuBarMediator = facade.retrieveMediator(Overlap2DMenuBarMediator.NAME);
        Overlap2DMenuBar menuBar = overlap2DMenuBarMediator.getViewComponent();
        topTable.add(menuBar.getTable()).fillX().expandX();
    }
}
