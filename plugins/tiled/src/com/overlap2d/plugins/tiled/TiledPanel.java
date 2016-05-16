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

package com.overlap2d.plugins.tiled;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.commons.UIDraggablePanel;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPane;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPaneListener;
import com.overlap2d.plugins.tiled.data.TileVO;
import com.overlap2d.plugins.tiled.manager.ResourcesManager;
import com.overlap2d.plugins.tiled.view.tabs.GridTilesTab;
import com.overlap2d.plugins.tiled.view.tabs.SettingsTab;
import com.puremvc.patterns.facade.Facade;
import com.puremvc.patterns.facade.SimpleFacade;

/**
 * Created by mariam on 2/2/2016.
 */
public class TiledPanel extends UIDraggablePanel {

    public static final float GRID_WIDTH = 200f;
    public static final float GRID_HEIGHT = 250f;
    public static final float DROP_WIDTH = 210f;
    public static final float DROP_HEIGHT = 140f;
    public static final float SETTINGS_WIDTH = 248f;
    public static final float SETTINGS_HEIGHT = 150f;
    public static final float BOTTOM_BAR_X = 177f;
    public static final float BOTTOM_BAR_DELTA_Y = 6f;

    public TiledPlugin tiledPlugin;
    public Facade facade;

    protected TabbedPane tabbedPane;
    protected VisTable tabTable; //table inside of each tab
    protected Table paneTable; //table for 'tabs' row

    private GridTilesTab tilesTab;
    private SettingsTab settingsTab;
    private Image bottomBar;
    private VisTable mainTable;
    private Engine engine;
    private ResourcesManager resourcesManager;

    public TiledPanel(TiledPlugin tiledPlugin) {
        super("Tiles");
        this.tiledPlugin = tiledPlugin;

        facade = SimpleFacade.getInstance();

        mainTable = new VisTable();
        add(mainTable)
                .padLeft(-2)
                .padRight(2);

        tabTable = new VisTable();
    }

    public void initView() {
        if (bottomBar == null) {
            this.resourcesManager = tiledPlugin.pluginRM;
            bottomBar = new Image(resourcesManager.getTextureRegion("tab-back-line"));
            bottomBar.setWidth(80);
            addActorBefore(mainTable, bottomBar);
        }

        mainTable.clear();

        TabbedPane.TabbedPaneStyle style = new TabbedPane.TabbedPaneStyle();
        VisTextButton.VisTextButtonStyle btnStyle = new VisTextButton.VisTextButtonStyle();
        btnStyle.up = new TextureRegionDrawable(resourcesManager.getTextureRegion("plugin-tab-inactive"));
        btnStyle.checked = new TextureRegionDrawable(resourcesManager.getTextureRegion("plugin-tab-active"));
        btnStyle.font = VisUI.getSkin().getFont("default-font");
        btnStyle.fontColor = VisUI.getSkin().getColor("white");
        style.buttonStyle = btnStyle;
        tabbedPane = new TabbedPane(style);
        paneTable = tabbedPane.getTable();

        mainTable.add(paneTable)
                .left()
                .top()
                .expandX()
                .row();

        tabTable.clear();
        paneTable.row();
        paneTable.add(tabTable)
                .left()
                .top()
                .row();

        tabbedPane.addListener(new TabbedPaneListener() {

            @Override
            public void switchedTab (Tab tab) {
                if (tab == null) {
                    return;
                }

                float WIDTH = 0;
                float HEIGHT = 0;
                if (tab instanceof SettingsTab) {
                    WIDTH = SETTINGS_WIDTH;
                    HEIGHT = SETTINGS_HEIGHT;
                } else if (tab instanceof GridTilesTab) {
                    if (GridTilesTab.isDrop) {
                        WIDTH = DROP_WIDTH;
                        HEIGHT = DROP_HEIGHT;
                    } else {
                        WIDTH = GRID_WIDTH;
                        HEIGHT = GRID_HEIGHT;
                    }
                }

                Table content = tab.getContentTable();

                tabTable.clearChildren();
                tabTable.add(content)
                        .width(WIDTH)
                        .height(HEIGHT)
                        .row();
                pack();

                setBottomBarPosition(HEIGHT);
                setFixedPosition();
            }

            @Override
            public void removedTab(Tab tab) {

            }

            @Override
            public void removedAllTabs() {

            }

        });

        initTabs();

        pack();
    }

    public void setBottomBarPosition(float panelHeight) {
        bottomBar.setX(BOTTOM_BAR_X);
        bottomBar.setY(panelHeight + BOTTOM_BAR_DELTA_Y);
    }

    public void setFixedPosition() {
        setPosition(56f, 765f - getPrefHeight());
    }

    public Table getDropTable() {
        return tilesTab.getContentTable();
    }

    public void reInitGridSettings() {
        settingsTab.resetGridCategory();
    }

    public void addTile(String tileName) {
        tilesTab.addTile(tileName);
    }

    public void selectTile(TileVO tileVO) {
        tilesTab.selectTile(tileVO);
    }

    public void removeTile() {
        tilesTab.removeTile();
        reInitTabTable();
        setFixedPosition();
        tilesTab.scrollTiles();
    }

    private void initTabs() {
        tilesTab = new GridTilesTab(this, 0);
        tilesTab.initView();
        tabbedPane.insert(tilesTab.getTabIndex(), tilesTab);

        settingsTab = new SettingsTab(this, "Settings", 1);
        settingsTab.initView();
        tabbedPane.insert(settingsTab.getTabIndex(), settingsTab);

        reInitTabTable();
    }

    public void reInitTabTable() {
        float width = GridTilesTab.isDrop ? DROP_WIDTH : GRID_WIDTH;
        float height = GridTilesTab.isDrop ? DROP_HEIGHT : GRID_HEIGHT;
        tabTable.clear();
        tabTable.add(tilesTab.getContentTable())
                .width(width)
                .height(height);
        tabTable.pack();
        setBottomBarPosition(height);
        pack();
    }

    public void initLockView() {
        mainTable.clear();

        mainTable.add(new VisLabel("no scenes open")).right();
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }
}
