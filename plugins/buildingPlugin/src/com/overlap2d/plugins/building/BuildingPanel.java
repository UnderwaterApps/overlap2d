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

package com.overlap2d.plugins.building;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.puremvc.patterns.facade.SimpleFacade;
import com.commons.UIDraggablePanel;

/**
 * Created by azakhary on 7/24/2015.
 */
public class BuildingPanel extends UIDraggablePanel {

    private SimpleFacade facade;

    private VisTable mainTable;

    private Engine engine;

    public BuildingPanel() {
        super("Building");
        addCloseButton();

        facade = SimpleFacade.getInstance();

        mainTable = new VisTable();
        add(mainTable).width(100);
    }

    public void initView() {
        mainTable.clear();

        mainTable.add(new VisLabel("Entity count: ")).right();
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
