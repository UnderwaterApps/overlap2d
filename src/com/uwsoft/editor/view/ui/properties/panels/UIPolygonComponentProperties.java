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

package com.uwsoft.editor.view.ui.properties.panels;

import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.event.ButtonToNotificationListener;
import com.uwsoft.editor.view.ui.properties.UIRemovableProperties;

/**
 * Created by azakhary on 7/2/2015.
 */
public class UIPolygonComponentProperties extends UIRemovableProperties {

    public static final String prefix = "com.uwsoft.editor.view.ui.properties.panels.UIPolygonComponentProperties";

    public static final String COPY_BUTTON_CLICKED = prefix + ".COPY_BUTTON_CLICKED";
    public static final String PASTE_BUTTON_CLICKED = prefix + ".PASTE_BUTTON_CLICKED";
    public static final String ADD_DEFAULT_MESH_BUTTON_CLICKED = prefix + ".ADD_DEFAULT_MESH_BUTTON_CLICKED";
    public static final String CLOSE_CLICKED = prefix + ".CLOSE_CLICKED";

    private VisTextButton addDefaultMeshButton;

    private VisLabel verticesCountLbl;
    private VisTextButton copyBtn;
    private VisTextButton pasteBtn;

    public UIPolygonComponentProperties() {
        super("Polygon Component");
    }

    public void initView() {
        mainTable.clear();

        verticesCountLbl = new VisLabel("", Align.left);

        copyBtn = new VisTextButton("copy");
        pasteBtn = new VisTextButton("paste");

        mainTable.add(new VisLabel("Vertices: ", Align.left)).left().padRight(3);
        mainTable.add(verticesCountLbl).left().width(67);
        mainTable.add(copyBtn).right().padRight(4);
        mainTable.add(pasteBtn).right().padRight(4);
        mainTable.row();

        initListeners();
    }

    public void setVerticesCount(int count) {
        verticesCountLbl.setText(count+"");
    }

    public void initEmptyView() {
        mainTable.clear();

        addDefaultMeshButton = new VisTextButton("Make Default");

        mainTable.add(new VisLabel("There is no vertices in this shape", Align.center));
        mainTable.row();
        mainTable.add(addDefaultMeshButton).center();

        initEmptyViewListeners();
    }

    private void initListeners() {
        copyBtn.addListener(new ButtonToNotificationListener(COPY_BUTTON_CLICKED));
        pasteBtn.addListener(new ButtonToNotificationListener(PASTE_BUTTON_CLICKED));
    }

    private void initEmptyViewListeners() {
        addDefaultMeshButton.addListener(new ButtonToNotificationListener(ADD_DEFAULT_MESH_BUTTON_CLICKED));
    }

    @Override
    public void onClose() {
        Overlap2DFacade.getInstance().sendNotification(CLOSE_CLICKED);
    }
}
