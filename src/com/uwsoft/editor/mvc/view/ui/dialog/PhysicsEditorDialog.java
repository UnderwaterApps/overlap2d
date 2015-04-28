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

package com.uwsoft.editor.mvc.view.ui.dialog;

import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisTable;
import com.uwsoft.editor.gdx.ui.components.ItemPhysicsEditor;


/**
 * Created by azakhary on 4/28/2015.
 */
public class PhysicsEditorDialog extends O2DDialog {

    private VisTable mainTable;
    private VisTable controlsTable;
    private ItemPhysicsEditor itemPhysicsEditor;


    private VisSelectBox<String> bodyTypeBox;

    public PhysicsEditorDialog() {
        super("Physics Editor");
        setModal(true);
        addCloseButton();

        mainTable = new VisTable();
        controlsTable = new VisTable();
        itemPhysicsEditor = new ItemPhysicsEditor(400, 400);

        initSidePanel();

        mainTable.add(controlsTable).top();
        mainTable.add(itemPhysicsEditor);

        mainTable.row();

        add(mainTable);
        row();
    }

    private void initSidePanel() {

        bodyTypeBox = new VisSelectBox<>();
        Array<String> types = new Array<>();
        types.add("STATIC");
        types.add("KINEMATIC");
        types.add("DYNAMIC");
        bodyTypeBox.setItems(types);

        controlsTable.add(new VisLabel("Body type:", Align.right)).padRight(5).colspan(2).fillX();
        controlsTable.add(bodyTypeBox).width(120).colspan(2);
        controlsTable.row().padTop(5);

        controlsTable.add(new VisLabel("Mass:", Align.right)).padRight(5).colspan(2).fillX();
        controlsTable.add(bodyTypeBox).width(120).colspan(2);
        controlsTable.row().padTop(5);

        controlsTable.add(new VisLabel("Center of Mass:", Align.right)).padRight(5).colspan(2).fillX();
        controlsTable.add(bodyTypeBox).width(120).colspan(2);
        controlsTable.row().padTop(5);

        controlsTable.add(new VisLabel("Rotational Intertia:", Align.right)).padRight(5).colspan(2).fillX();
        controlsTable.add(bodyTypeBox).width(120).colspan(2);
        controlsTable.row().padTop(5);

        controlsTable.add(new VisLabel("Dumping:", Align.right)).padRight(5).colspan(2).fillX();
        controlsTable.add(bodyTypeBox).width(120).colspan(2);
        controlsTable.row().padTop(5);

        controlsTable.add(new VisLabel("Gravity Scale:", Align.right)).padRight(5).colspan(2).fillX();
        controlsTable.add(bodyTypeBox).width(120).colspan(2);
        controlsTable.row().padTop(5);

        controlsTable.add(new VisLabel("Density:", Align.right)).padRight(5).colspan(2).fillX();
        controlsTable.add(bodyTypeBox).width(120).colspan(2);
        controlsTable.row().padTop(5);

        controlsTable.add(new VisLabel("Friction:", Align.right)).padRight(5).colspan(2).fillX();
        controlsTable.add(bodyTypeBox).width(120).colspan(2);
        controlsTable.row().padTop(5);

        controlsTable.add(new VisLabel("Restitution:", Align.right)).padRight(5).colspan(2).fillX();
        controlsTable.add(bodyTypeBox).width(120).colspan(2);
        controlsTable.row().padTop(5);
    }
}
