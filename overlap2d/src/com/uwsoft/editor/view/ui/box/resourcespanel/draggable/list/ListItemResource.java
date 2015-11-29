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

package com.uwsoft.editor.view.ui.box.resourcespanel.draggable.list;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.puremvc.patterns.facade.SimpleFacade;
import com.uwsoft.editor.view.stage.Sandbox;
import com.uwsoft.editor.view.ui.box.resourcespanel.draggable.DraggableResourceView;

/**
 * Created by sargis on 5/6/15.
 */
public abstract class ListItemResource extends Button implements DraggableResourceView {

    protected final Sandbox sandbox;
    private final Image icon;

    public ListItemResource(String name, String styleName) {
        super(VisUI.getSkin().get(styleName, ListItemResourceStyle.class));
        sandbox = Sandbox.getInstance();
        icon = new Image(getStyle().resourceUp);
        add(icon);
        add(new VisLabel(name, getStyle().labelStyle)).expandX().fillX();
    }

    public void setRightClickEvent(String eventName, String payload) {
        addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                return true;
            }
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                if(button == Input.Buttons.RIGHT) {
                    SimpleFacade.getInstance().sendNotification(eventName, payload);
                }
            }
        });
    }


    @Override
    public ListItemResourceStyle getStyle() {
        return (ListItemResourceStyle) super.getStyle();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (isOver()) {
            icon.setDrawable(getStyle().resourceOver);
        } else if (isPressed()) {
            icon.setDrawable(getStyle().resourceDown);
        } else {
            icon.setDrawable(getStyle().resourceUp);
        }
    }

    public static class ListItemResourceStyle extends Button.ButtonStyle {
        public Drawable resourceUp, resourceDown, resourceOver;
        public Label.LabelStyle labelStyle;
    }
}
