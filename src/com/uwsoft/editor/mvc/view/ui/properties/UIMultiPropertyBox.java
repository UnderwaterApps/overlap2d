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

package com.uwsoft.editor.mvc.view.ui.properties;

import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.uwsoft.editor.gdx.ui.properties.IPropertyBox;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.renderer.actor.IBaseItem;

import java.util.ArrayList;

/**
 * Created by azakhary on 4/15/2015.
 */
public class UIMultiPropertyBox extends VisWindow {

    private final VisTable mainTable;
    private final VisTable propertiesTable;

    public UIMultiPropertyBox() {
        super("Properties");

        setMovable(false);
        mainTable = new VisTable();
        mainTable.top();
        mainTable.addSeparator().padBottom(5);
        propertiesTable = new VisTable();
        mainTable.add(propertiesTable);

        add(mainTable).width(250);
    }

    public void initView() {

    }

    public void clearAll() {
        propertiesTable.clear();
        propertiesTable.reset();
    }

    public void addPropertyBox(UIAbstractProperties viewComponent) {
        propertiesTable.add(viewComponent);
    }
}
