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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.uwsoft.editor.mvc.proxy.TextureManager;
import com.uwsoft.editor.gdx.actors.basic.PixelRect;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.DataManager;

import java.util.HashMap;
import java.util.Map;

public class SelectionActions extends Group {

    public static final int GROUP_ITEMS = 1;
    public static final int EDIT_COMPOSITE = 2;
    public static final int CONVERT_TO_BUTTON = 3;
    public static final int CUT = 4;
    public static final int COPY = 5;
    public static final int PASTE = 6;
    public static final int DELETE = 7;
    public static final int ADD_TO_LIBRARY = 8;
    public static final int EDIT_PHYSICS = 9;
    public static final int EDIT_ASSET_PHYSICS = 10;
    public static final int DO_NOTHING = 99;
    private final Overlap2DFacade facade;
    private final DataManager dataManager;

    private Group instance;
    private HashMap<Integer, String> listEntries = new HashMap<Integer, String>();
    private SelectionEvent eventListener;

    public SelectionActions() {
        instance = this;
        facade = Overlap2DFacade.getInstance();
        dataManager = facade.retrieveProxy(DataManager.NAME);
    }

    public SelectionEvent getEventListener() {
        return eventListener;
    }

    public void setEventListener(SelectionEvent eventListener) {
        this.eventListener = eventListener;
    }

    public void initView() {
        int iterator = 0;
        for (Map.Entry<Integer, String> entry : listEntries.entrySet()) {
            final Integer action = entry.getKey();
            String name = entry.getValue();

            final PixelRect rct = new PixelRect(130, 20);
            rct.setFillColor(new Color(0.32f, 0.32f, 0.32f, 1));
            rct.setBorderColor(new Color(0.22f, 0.22f, 0.22f, 1));

            rct.setY(-(iterator + 1) * rct.getHeight());

            addActor(rct);
            TextureManager textureManager = facade.retrieveProxy(TextureManager.NAME);
            Label lbl = new Label(name, textureManager.editorSkin);
            lbl.setX(3);
            lbl.setY(rct.getY() + 3);
            lbl.setColor(new Color(1, 1, 1, 0.65f));
            lbl.setTouchable(Touchable.disabled);
            addActor(lbl);

            rct.addListener(new ClickListener() {
                @Override
                public boolean mouseMoved(InputEvent event, float x, float y) {
                    rct.setFillColor(new Color(0.26f, 0.26f, 0.26f, 1));
                    return true;
                }

                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    rct.setFillColor(new Color(0.32f, 0.32f, 0.32f, 1));
                }

                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    if (eventListener != null) {
                        eventListener.doAction(action);
                    }
                    instance.remove();
                }
            });

            iterator++;
        }
    }

    public void addItem(int action, String name) {
        listEntries.put(action, name);
    }

    public interface SelectionEvent {
        public void doAction(int action);
    }
}
