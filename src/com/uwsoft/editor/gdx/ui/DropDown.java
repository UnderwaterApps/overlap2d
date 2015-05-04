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

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.uwsoft.editor.gdx.actors.basic.PixelRect;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.EditorTextureManager;
import com.uwsoft.editor.mvc.proxy.ProjectManager;

/**
 * DropDown element with list of clickable panels
 * It requires listener being given from outside
 */
public class DropDown extends Group {

    private final Overlap2DFacade facade;
    private final ProjectManager projectManager;
    private Group container;
    private HashMap<Integer, String> listEntries = new HashMap<Integer, String>();
    private SelectionEvent eventListener;

    public DropDown(Group container) {
        this.container = container;
        container.addActor(this);
        facade = Overlap2DFacade.getInstance();
        projectManager = facade.retrieveProxy(ProjectManager.NAME);
    }

    public SelectionEvent getEventListener() {
        return eventListener;
    }

    public void setEventListener(SelectionEvent eventListener) {
        this.eventListener = eventListener;
    }

    public void clearItems() {
        listEntries.clear();
    }

    public void initView(float x, float y) {
        super.clear();
        setVisible(true);

        int iterator = 0;
        for (Map.Entry<Integer, String> entry : listEntries.entrySet()) {
            final Integer action = entry.getKey();
            String name = entry.getValue();

            final PixelRect rct = new PixelRect(130, 20);
            rct.setFillColor(new Color(0.32f, 0.32f, 0.32f, 1));
            rct.setBorderColor(new Color(0.22f, 0.22f, 0.22f, 1));

            rct.setY(-(iterator + 1) * rct.getHeight());

            addActor(rct);
            EditorTextureManager textureManager = facade.retrieveProxy(EditorTextureManager.NAME);
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
                    hide();
                }
            });

            iterator++;
        }

        Vector2 vector2 = container.stageToLocalCoordinates(new Vector2(x, y));
        setX(vector2.x);
        setY(container.getStage().getHeight() - vector2.y);
    }

    public void addItem(int action, String name) {
        listEntries.put(action, name);
    }

    public void hide() {
        setVisible(false);
    }

    public interface SelectionEvent {
        public void doAction(int action);

    }
}
