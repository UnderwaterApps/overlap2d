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

package com.uwsoft.editor.view.ui.box.resourcespanel.draggable.box;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.puremvc.patterns.facade.SimpleFacade;
import com.uwsoft.editor.view.ui.box.UIResourcesBoxMediator;
import com.uwsoft.editor.view.ui.widget.actors.basic.PixelRect;
import com.uwsoft.editor.view.stage.Sandbox;
import com.uwsoft.editor.view.ui.box.resourcespanel.draggable.DraggableResourceView;

/**
 * Created by sargis on 5/6/15.
 */
public abstract class BoxItemResource extends Group implements DraggableResourceView {
    protected final Sandbox sandbox;
    protected float thumbnailSize = 50;
    protected PixelRect rc;

    public BoxItemResource() {
        sandbox = Sandbox.getInstance();
        rc = new PixelRect(thumbnailSize, thumbnailSize);
        rc.setFillColor(new Color(1, 1, 1, 0.2f));
        rc.setBorderColor(new Color(1, 1, 1, 0.4f));
        addActor(rc);
        setWidth(thumbnailSize);
        setHeight(thumbnailSize);
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
}
