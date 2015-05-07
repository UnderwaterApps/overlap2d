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

package com.uwsoft.editor.mvc.view.ui.box.resourcespanel.draggable.list;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.gdx.ui.payloads.ResourcePayloadObject;
import com.uwsoft.editor.mvc.view.ui.box.resourcespanel.draggable.DraggableResourceView;

import java.util.function.BiFunction;

/**
 * Created by sargis on 5/6/15.
 */
public abstract class ListItemResource extends Button implements DraggableResourceView {

    protected final Sandbox sandbox;
    private final Image icon;
    private BiFunction<String, Vector2, Boolean> factoryFunction;

    public ListItemResource(String name) {
        super(VisUI.getSkin().get("default", ListItemResourceStyle.class));
        sandbox = Sandbox.getInstance();
        icon = new Image(getStyle().resourceUp);
        add(icon);
        add(new VisLabel(name, getStyle().labelStyle)).expandX().fillX();
    }

    protected void setFactoryMethod(BiFunction<String, Vector2, Boolean> factoryFunction) {
        this.factoryFunction = factoryFunction;
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

    private Boolean itemDropped(DragAndDrop.Payload payload, Vector2 vector2) {
        ResourcePayloadObject resourcePayloadObject = (ResourcePayloadObject) payload.getObject();
        vector2.sub(resourcePayloadObject.xOffset, resourcePayloadObject.yOffset);
        factoryFunction.apply(resourcePayloadObject.name, vector2);
        return true;
    }

    @Override
    public void drop(DragAndDrop.Payload payload, Vector2 vector2) {
        itemDropped(payload, vector2);
    }

    public static class ListItemResourceStyle extends Button.ButtonStyle {
        public Drawable resourceUp, resourceDown, resourceOver;
        public Label.LabelStyle labelStyle;
    }
}
