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

package com.uwsoft.editor.view.ui;

import com.badlogic.gdx.Gdx;
import com.kotcrab.vis.ui.widget.VisTable;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.view.menu.Overlap2DMenuBar;
import com.uwsoft.editor.view.menu.Overlap2DMenuBarMediator;
import com.uwsoft.editor.view.ui.box.UIAlignBox;
import com.uwsoft.editor.view.ui.box.UIAlignBoxMediator;
import com.uwsoft.editor.view.ui.box.UIItemsTreeBox;
import com.uwsoft.editor.view.ui.box.UIItemsTreeBoxMediator;
import com.uwsoft.editor.view.ui.box.UILayerBox;
import com.uwsoft.editor.view.ui.box.UILayerBoxMediator;
import com.uwsoft.editor.view.ui.box.UIMultiPropertyBox;
import com.uwsoft.editor.view.ui.box.UIMultiPropertyBoxMediator;
import com.uwsoft.editor.view.ui.box.UIResourcesBox;
import com.uwsoft.editor.view.ui.box.UIResourcesBoxMediator;
import com.uwsoft.editor.view.ui.box.UIToolBox;
import com.uwsoft.editor.view.ui.box.UIToolBoxMediator;
import com.uwsoft.editor.view.ui.widget.O2DLogo;

/**
 * Created by sargis on 9/10/14.
 */
public class UIMainTable extends VisTable {
    private final VisTable topTable;
    private final VisTable middleTable;
    private final Overlap2DFacade facade;

    //TODO: fuck! make this private!!!!
    public UISubmenuBar compositePanel;
    public UIItemsTreeBox itemsBox;

    public UIMainTable() {
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
        initToolsPanel();
        initLeftBoxesPanel();
        initRightBoxesPanel();
    }

    private void initLeftBoxesPanel() {
        VisTable leftBoxesPanel = new VisTable();
        UIAlignBoxMediator uiAlignBoxMediator = facade.retrieveMediator(UIAlignBoxMediator.NAME);
        UIAlignBox uiAlignBox = uiAlignBoxMediator.getViewComponent();
        leftBoxesPanel.add(uiAlignBox).expandX().fillX();
        leftBoxesPanel.row();
        UIItemsTreeBoxMediator uiItemsTreeBoxMediator = facade.retrieveMediator(UIItemsTreeBoxMediator.NAME);
        itemsBox = uiItemsTreeBoxMediator.getViewComponent();
        leftBoxesPanel.add(itemsBox).expandX().fillX().maxHeight(600).top();
        middleTable.add(leftBoxesPanel).top().left().expand().padTop(15).padLeft(16);
    }

    private void initCompisitePanel() {
        compositePanel = new UISubmenuBar();
        topTable.add(compositePanel).fillX().expandX().colspan(2);
    }

    private void initRightBoxesPanel() {
        VisTable rightPanel = new VisTable();

        //PropertyBox
        UIMultiPropertyBoxMediator multiPropertyBoxMediator = facade.retrieveMediator(UIMultiPropertyBoxMediator.NAME);
        UIMultiPropertyBox multiPropertyBox = multiPropertyBoxMediator.getViewComponent();
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

    private void initToolsPanel() {
        //
        VisTable toolsPanel = new VisTable();
        toolsPanel.background("toolbar-bg");
        //
        UIToolBoxMediator uiToolBoxMediator = facade.retrieveMediator(UIToolBoxMediator.NAME);
        UIToolBox uiToolBox = uiToolBoxMediator.getViewComponent();
        toolsPanel.add(uiToolBox).top().expandY().padTop(4);
        //
        middleTable.add(toolsPanel).top().left().width(40).height(Gdx.graphics.getHeight()).expandY();
    }


    private void initMenuBar() {
        Overlap2DMenuBarMediator overlap2DMenuBarMediator = facade.retrieveMediator(Overlap2DMenuBarMediator.NAME);
        Overlap2DMenuBar menuBar = overlap2DMenuBarMediator.getViewComponent();
        topTable.add(new O2DLogo()).left();
        topTable.add(menuBar.getTable().padLeft(24)).fillX().expandX();
    }
}
