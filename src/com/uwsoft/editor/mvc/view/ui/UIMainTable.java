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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.widget.VisTable;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.view.Overlap2DMenuBar;
import com.uwsoft.editor.mvc.view.Overlap2DMenuBarMediator;
import com.uwsoft.editor.mvc.view.stage.UIStage;
import com.uwsoft.editor.mvc.view.ui.box.*;
import com.uwsoft.editor.ui.widget.O2DLogo;

/**
 * Created by sargis on 9/10/14.
 */
public class UIMainTable extends VisTable {
    private final UIStage uiStage;
    private final VisTable topTable;
    private final VisTable middleTable;
    private final Overlap2DFacade facade;
    public UISubmenuBar compositePanel;
    public UIItemsTreeBox itemsBox;
    public UIMultiPropertyBox multiPropertyBox;
    public Table rightPanel;
    public Table leftPanel;
    public Overlap2DMenuBarMediator menuMediator;
    private UIToolBox toolPanel;

    public UIMainTable(UIStage uiStage) {
        this.uiStage = uiStage;
        facade = Overlap2DFacade.getInstance();
//        debug();
        setFillParent(true);
        top();
        topTable = new VisTable();
//        topTable.debug();
        middleTable = new VisTable();
//        middleTable.debug();
        add(topTable).fillX().expandX();
        row();
        add(middleTable).fillX().padTop(1);
        //
        initMenuBar();
        topTable.row();
        initCompisitePanel();
        initLeftToolsPanel();
        initRightToolsPanel();
    }

    private void initCompisitePanel() {
        compositePanel = new UISubmenuBar();
        topTable.add(compositePanel).fillX().expandX().colspan(2);
//        compositePanel.initPanel();
    }

    private void initRightToolsPanel() {
        rightPanel = new Table();
//        rightPanel.debug();
        //PropertyBox
        UIMultiPropertyBoxMediator multiPropertyBoxMediator = facade.retrieveMediator(UIMultiPropertyBoxMediator.NAME);
        multiPropertyBox = multiPropertyBoxMediator.getViewComponent();
        rightPanel.add(multiPropertyBox).top();
        rightPanel.row();

        //ResourcesBox
        UIResourcesBoxMediator resourceBoxMediator = facade.retrieveMediator(UIResourcesBoxMediator.NAME);
        UIResourcesBox resourceBox = resourceBoxMediator.getViewComponent();
        rightPanel.add(resourceBox).top();
        rightPanel.row();

        //LayerBox
        UILayerBoxMediator layerBoxMediator = facade.retrieveMediator(UILayerBoxMediator.NAME);
        UILayerBox layerBox = layerBoxMediator.getViewComponent();
        rightPanel.add(layerBox).top();

        //
        middleTable.add(rightPanel).top().right().expand().padTop(2);
    }

    private void initLeftToolsPanel() {
        //
        leftPanel = new VisTable();
        leftPanel.background("toolbar-bg");
        //
        UIToolBoxMediator uiToolBoxMediator = facade.retrieveMediator(UIToolBoxMediator.NAME);
        toolPanel = uiToolBoxMediator.getViewComponent();
        leftPanel.add(toolPanel).top().expandY().padTop(4);
        //
        UIAlignBoxMediator uiAlignBoxMediator = facade.retrieveMediator(UIAlignBoxMediator.NAME);
        UIAlignBox uiAlignBox = uiAlignBoxMediator.getViewComponent();
        // leftPanel.add(uiAlignBox).expandX().fillX();
        //
//        leftPanel.add(lightBox);
//        leftPanel.row();
        //
        UIItemsTreeBoxMediator uiItemsTreeBoxMediator = facade.retrieveMediator(UIItemsTreeBoxMediator.NAME);
        itemsBox = uiItemsTreeBoxMediator.getViewComponent();
//        itemsBox.initPanel();
        //leftPanel.add(itemsBox).expandX().fillX();
        //
        middleTable.add(leftPanel).top().left().width(40).height(Gdx.graphics.getHeight()).expandY();
    }

    private void initMenuBar() {
        //TODO: need to be changed!
        Overlap2DMenuBarMediator overlap2DMenuBarMediator = facade.retrieveMediator(Overlap2DMenuBarMediator.NAME);
        Overlap2DMenuBar menuBar = overlap2DMenuBarMediator.getViewComponent();
        topTable.add(new O2DLogo()).left();
        topTable.add(menuBar.getTable().padLeft(24)).fillX().expandX();
    }
}
