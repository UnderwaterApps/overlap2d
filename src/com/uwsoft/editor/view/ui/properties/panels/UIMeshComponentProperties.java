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
import com.uwsoft.editor.Overlap2D;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.event.ButtonToNotificationListener;
import com.uwsoft.editor.view.ui.properties.UIItemCollapsibleProperties;
import com.uwsoft.editor.view.ui.properties.UIRemovableProperties;

/**
 * Created by azakhary on 7/2/2015.
 */
public class UIMeshComponentProperties extends UIRemovableProperties {

    public static final String prefix = "com.uwsoft.editor.view.ui.properties.panels.UIMeshComponentProperties";

    public static final String ADD_DEFAULT_MESH_BUTTON_CLICKED = prefix + ".ADD_DEFAULT_MESH_BUTTON_CLICKED";
    public static final String CLOSE_CLICKED = prefix + ".CLOSE_CLICKED";

    private VisTextButton addDefaultMeshButton;

    private VisLabel verticesCountLbl;

    public UIMeshComponentProperties() {
        super("Mesh Component");
    }

    public void initView() {
        mainTable.clear();

        verticesCountLbl = new VisLabel("", Align.left);

        mainTable.add(new VisLabel("Vertices: ", Align.left)).left().padRight(10);
        mainTable.add(verticesCountLbl).right().fillX();
        mainTable.row();

        initListeners();
    }

    public void setVerticesCount(int count) {
        verticesCountLbl.setText(count+"");
    }

    public void initEmptyView() {
        mainTable.clear();

        addDefaultMeshButton = new VisTextButton("Create Default Mesh");

        mainTable.add(new VisLabel("There is no vertices in this mesh", Align.center));
        mainTable.row();
        mainTable.add(addDefaultMeshButton).center();

        initEmptyviewListeners();
    }

    private void initListeners() {

    }

    private void initEmptyviewListeners() {
        addDefaultMeshButton.addListener(new ButtonToNotificationListener(ADD_DEFAULT_MESH_BUTTON_CLICKED));
    }

    @Override
    public void onClose() {
        Overlap2DFacade.getInstance().sendNotification(CLOSE_CLICKED);
    }
}
