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

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.uwsoft.editor.mvc.Overlap2DFacade;

import java.util.ArrayList;


/**
 * Created by azakhary on 4/17/2015.
 */
public class UILayerBox extends VisWindow {

    public static final String LAYER_ROW_CLICKED = "com.uwsoft.editor.mvc.view.ui.box.UILayerBox" + ".LAYER_ROW_CLICKED";
    public static final String CREATE_NEW_LAYER = "com.uwsoft.editor.mvc.view.ui.box.UILayerBox" + ".CREATE_NEW_LAYER";
    public static final String DELETE_NEW_LAYER = "com.uwsoft.editor.mvc.view.ui.box.UILayerBox" + ".DELETE_NEW_LAYER";

    private Overlap2DFacade facade;

    private VisTable mainTable;
    private VisTable contentTable;
    private VisTable bottomPane;

    private VisScrollPane scrollPane;
    private VisTable layersTable;

    private Array<UILayerItem> rows = new Array<>();

    public int getCurrentSelectedLayerIndex() {
        return currentSelectedLayerIndex;
    }

    public int currentSelectedLayerIndex = 0;

    public UILayerBox() {
        super("Layers");

        facade = Overlap2DFacade.getInstance();

        setMovable(false);
        mainTable = new VisTable();
        mainTable.top();
        mainTable.addSeparator().padBottom(5);
        contentTable = new VisTable();
        mainTable.add(contentTable);

        layersTable = new VisTable();
        scrollPane = new VisScrollPane(layersTable);
        scrollPane.setFadeScrollBars(false);
        contentTable.add(scrollPane).width(230).height(150);
        contentTable.row();

        add(contentTable).width(250);
        row();

        scrollPane.layout();

        bottomPane = new VisTable();

        VisTextButton newBtn = new VisTextButton("new");
        VisTextButton deleteBtn = new VisTextButton("delete");
        bottomPane.add(newBtn);
        bottomPane.add(deleteBtn);

        newBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                facade.sendNotification(CREATE_NEW_LAYER);
            }
        });

        deleteBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                facade.sendNotification(DELETE_NEW_LAYER);
            }
        });

        add(bottomPane);
    }

    public void clearItems() {
        layersTable.clear();
        rows.clear();
    }

    public void addItem(UILayerItem item) {
        layersTable.add(item).left().expandX();
        layersTable.row();

        rows.add(item);

        item.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                for(int i = 0; i < rows.size; i++) {
                    if(i != rows.indexOf(item, true)) {
                        rows.get(i).setSelected(false);
                    }
                }
                item.setSelected(true);
                currentSelectedLayerIndex = rows.indexOf(item, true);

                facade.sendNotification(LAYER_ROW_CLICKED, item);
            }
        });
    }
}
