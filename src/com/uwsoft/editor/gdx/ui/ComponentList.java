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

package com.uwsoft.editor.gdx.ui;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.uwsoft.editor.gdx.ui.thumbnailbox.ComponentThumbnailBox;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.EditorTextureManager;
import com.uwsoft.editor.mvc.view.stage.UIStage;

import java.util.HashMap;

public class ComponentList extends Group {

    private final Overlap2DFacade facade;
    private final EditorTextureManager textureManager;

    public ComponentList(final UIStage s, float width, float height) {
        facade = Overlap2DFacade.getInstance();
        textureManager = facade.retrieveProxy(EditorTextureManager.NAME);
        this.setWidth(width);
        this.setHeight(height);
        final Table container = new Table();
        Table table = new Table();
        Group listContainer = new Group();
        container.setX(0);
        container.setY(0);
        container.setWidth(getWidth() - 1);
        container.setHeight(getHeight() - 20);
        listContainer.setWidth(getWidth() - 20);
        listContainer.setHeight(getHeight() - 25);
        final ScrollPane scroll = new ScrollPane(table, textureManager.editorSkin);
        container.add(scroll).colspan(4).width(getWidth());
        container.row();
        scroll.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                event.stop();
                return true;
            }
        });
        scroll.setHeight(getHeight() - 20);
        scroll.setY(0);
        scroll.setFlickScroll(false);

        HashMap<String, String> components = new HashMap<String, String>();

        components.put("Label", "Label");
        components.put("TextBoxItem", "Text Field");
        components.put("ButtonItem", "Text Button");
        components.put("CheckBox", "CheckBox");
        components.put("SelectBox", "SelectBox");

        Label dummyTst = new Label("dummy", textureManager.editorSkin);
        if (components.size() * dummyTst.getHeight() > listContainer.getHeight())
            listContainer.setHeight(components.size() * dummyTst.getHeight());
        dummyTst = null;

        int iter = 1;
        for (final Object value : components.values()) {

            ComponentThumbnailBox thumb = new ComponentThumbnailBox(getWidth(), (String) value);
            thumb.setX(0);
            thumb.setY(listContainer.getHeight() - thumb.getHeight() * iter);
            listContainer.addActor(thumb);
            iter++;
        }

        table.add(listContainer);
        table.row();

        addActor(container);
    }
}
