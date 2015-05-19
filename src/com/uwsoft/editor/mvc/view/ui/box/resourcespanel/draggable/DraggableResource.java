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

package com.uwsoft.editor.mvc.view.ui.box.resourcespanel.draggable;

import java.util.function.BiFunction;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Pools;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.gdx.ui.payloads.ResourcePayloadObject;

/**
 * Created by azakhary on 7/3/2014.
 */
public class DraggableResource extends DragAndDrop {

    protected final Sandbox sandbox;
    private final DraggableResourceView viewComponent;
    private BiFunction<String, Vector2, Boolean> factoryFunction;

    public DraggableResource(DraggableResourceView viewComponent) {
        this.viewComponent = viewComponent;
        sandbox = Sandbox.getInstance();
    }

    public void initDragDrop() {
        addSource(new DragAndDrop.Source((Actor) viewComponent) {
            public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                DragAndDrop.Payload payload = new DragAndDrop.Payload();
                Actor dragActor = viewComponent.getDragActor();
                ResourcePayloadObject payloadData = viewComponent.getPayloadData();
                payloadData.xOffset = dragActor.getWidth() / 2;
                payloadData.yOffset = dragActor.getHeight() / 2;
                payload.setDragActor(dragActor);
                payload.setObject(payloadData);
                payload.setInvalidDragActor(null);
                setDragActorPosition(-dragActor.getWidth() / 2, dragActor.getHeight() / 2);
                return payload;
            }
        });

        addTarget(new DragAndDrop.Target(sandbox.getUIStage().dummyTarget) {
            @Override
            public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                return true;
            }

            @Override
            public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                Vector2 vector2 = Pools.obtain(Vector2.class);
                vector2.x = x;
                vector2.y = y;
                DraggableResource.this.drop(payload, vector2);
                Pools.free(vector2);
            }
        });
    }

    private void drop(DragAndDrop.Payload payload, Vector2 vector2) {
        ResourcePayloadObject resourcePayloadObject = (ResourcePayloadObject) payload.getObject();
        vector2.sub(resourcePayloadObject.xOffset, resourcePayloadObject.yOffset);
        factoryFunction.apply(resourcePayloadObject.name, vector2);
    }

    public DraggableResourceView getViewComponent() {
        return viewComponent;
    }

    public void setFactoryFunction(BiFunction<String, Vector2, Boolean> factoryFunction) {
        this.factoryFunction = factoryFunction;
    }
}
