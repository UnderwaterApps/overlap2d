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

package com.uwsoft.editor.mvc.view.ui.box.resourcespanel.draggable.thumbnail;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.uwsoft.editor.gdx.actors.basic.PixelRect;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.gdx.ui.payloads.ResourcePayloadObject;
import com.uwsoft.editor.mvc.view.ui.box.resourcespanel.draggable.DraggableResourceView;

import java.util.function.BiFunction;

/**
 * Created by sargis on 5/6/15.
 */
public abstract class ThumbnailBoxResource extends Group implements DraggableResourceView {
    protected final Sandbox sandbox;
    protected float thumbnailSize = 50;
    protected PixelRect rc;
    private BiFunction<String, Vector2, Boolean> factoryFunction;

    public ThumbnailBoxResource() {
        sandbox = Sandbox.getInstance();
        rc = new PixelRect(thumbnailSize, thumbnailSize);
        rc.setFillColor(new Color(1, 1, 1, 0.2f));
        rc.setBorderColor(new Color(1, 1, 1, 0.4f));
        addActor(rc);
        setWidth(thumbnailSize);
        setHeight(thumbnailSize);
    }

    protected void setFactoryMethod(BiFunction<String, Vector2, Boolean> factoryFunction) {
        this.factoryFunction = factoryFunction;
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
}
