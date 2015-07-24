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

package com.overlap2d.plugins.performance;

import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.puremvc.patterns.facade.SimpleFacade;
import com.commons.UIDraggablePanel;

/**
 * Created by azakhary on 7/24/2015.
 */
public class PerformancePanel extends UIDraggablePanel {

    private SimpleFacade facade;

    private VisTable mainTable;

    public PerformancePanel() {
        super("Performance");
        addCloseButton();

        facade = SimpleFacade.getInstance();

        mainTable = new VisTable();

        VisLabel label = new VisLabel("Hello World");
        mainTable.add(label).center();

        add(mainTable).width(222).height(200);
    }
}
