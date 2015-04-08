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
import com.uwsoft.editor.gdx.ui.*;
import com.uwsoft.editor.gdx.ui.layer.UILayerBox;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.view.Overlap2DMenuBar;
import com.uwsoft.editor.mvc.view.Overlap2DMenuBarMediator;
import com.uwsoft.editor.mvc.view.stage.UIStage;

/**
 * Created by sargis on 9/10/14.
 */
public class UIMainTable extends VisTable {
    private final UIStage uiStage;
    private final VisTable topTable;
    private final VisTable middleTable;
    public UICompositePanel compositePanel;
    public UILayerBox layerPanel;
    public UILightBox lightBox;
    public UIItemsBox itemsBox;
    public UIPropertiesBox propertiesPanel;
    public UILibraryBox libraryPanel;
    public Table rightToolsPanel;
    public Table leftToolsPanel;
    public Overlap2DMenuBarMediator menuMediator;
    private UIToolBox toolPanel;

    public UIMainTable(UIStage uiStage) {
        this.uiStage = uiStage;
        debug();
        setFillParent(true);
        top();
        topTable = new VisTable();
        middleTable = new VisTable();
        add(topTable).fillX();
        row();
        add(middleTable).fillX().padTop(10);
        //
        initMenuBar();
        topTable.row();
        initCompisitePanel();
        initLeftToolsPalel();
        initRightToolsPanel();
    }

    private void initCompisitePanel() {
        compositePanel = new UICompositePanel(uiStage);
        compositePanel.initPanel();
        topTable.add(compositePanel).left().fillX();
    }

    private void initRightToolsPanel() {
        rightToolsPanel = new Table();
        //
        propertiesPanel = new UIPropertiesBox(uiStage);
        propertiesPanel.initPanel();
        rightToolsPanel.add(propertiesPanel).top().fillY();
        rightToolsPanel.row();
        //
        libraryPanel = new UILibraryBox(uiStage);
        libraryPanel.initPanel();
        rightToolsPanel.add(libraryPanel).top().fillY();
        rightToolsPanel.row();
        //
        layerPanel = new UILayerBox(uiStage);
        layerPanel.initPanel();
        rightToolsPanel.add(layerPanel).top().fillY();
        //
        middleTable.add(rightToolsPanel).top().right().expand();
    }

    private void initLeftToolsPalel() {
        //
        leftToolsPanel = new VisTable();
        //
        toolPanel = new UIToolBox(uiStage);
        toolPanel.initPanel();
        leftToolsPanel.add(toolPanel).top().fillY();
        leftToolsPanel.row();
        //
        lightBox = new UILightBox(uiStage);
        lightBox.initPanel();
        leftToolsPanel.add(lightBox).top().fillY();
        leftToolsPanel.row();
        //
        itemsBox = new UIItemsBox(uiStage);
        itemsBox.initPanel();
        leftToolsPanel.add(itemsBox).top().fillY();
        //
        middleTable.add(leftToolsPanel).top().left().expand();
    }

    private void initMenuBar() {
        //TODO: need to be changed!
        Overlap2DMenuBar menuBar = (Overlap2DMenuBar) Overlap2DFacade.getInstance().retrieveMediator(Overlap2DMenuBarMediator.NAME).getViewComponent();
        topTable.add(menuBar.getTable()).left().fillX();
    }
}
