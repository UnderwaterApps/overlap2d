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

package com.uwsoft.editor.mvc.view.ui.box;

import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPane;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPaneListener;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.view.ui.box.resourcespanel.UIImagesTab;

/**
 * Created by azakhary on 4/17/2015.
 */
public class UIResourcesBox extends VisWindow {


    private Overlap2DFacade facade;

    private VisTable mainTable;
    private VisTable contentTable;
    private VisTable tabContent;

    private TabbedPane tabbedPane;

    public UIResourcesBox() {
        super("Resources");

        facade = Overlap2DFacade.getInstance();

        setMovable(false);
        mainTable = new VisTable();
        mainTable.top();
        mainTable.addSeparator().padBottom(5);
        contentTable = new VisTable();
        mainTable.add(contentTable);

        add(contentTable).width(250);
        row();

        tabContent = new VisTable();

        tabbedPane = new TabbedPane();
        tabbedPane.addListener(new TabbedPaneListener() {
            @Override
            public void switchedTab(Tab tab) {
                if(tab == null) return;
                setActiveTabContent(tab);
            }

            @Override
            public void removedTab(Tab tab) {

            }

            @Override
            public void removedAllTabs() {

            }
        });

        contentTable.add(tabbedPane.getTable());
        contentTable.row();
        contentTable.add(tabContent);
        contentTable.row();
    }

    public void setActiveTabContent(Tab tab) {
        tabContent.clear();
        tabContent.add(tab.getContentTable());
    }

    public void addTab(int index, Tab tab) {
        tabbedPane.insert(index, tab);
    }
}
