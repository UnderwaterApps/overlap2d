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

package com.uwsoft.editor.gdx.ui.thumbnailbox;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.uwsoft.editor.gdx.actors.basic.PixelRect;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.gdx.ui.payloads.AssetPayloadObject;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.view.ui.box.UIResourcesBoxMediator;

/**
 * Created by azakhary on 7/3/2014.
 */
public class DraggableThumbnailBox extends Group {


    protected final Sandbox sandbox;
    protected float thumbnailSize = 50;
    protected PixelRect rc;
    private DraggableThumbnailEvent draggingEvent;

    public DraggableThumbnailBox() {
        sandbox = Sandbox.getInstance();
        rc = new PixelRect(thumbnailSize, thumbnailSize);
        rc.setFillColor(new Color(1, 1, 1, 0.2f));
        rc.setBorderColor(new Color(1, 1, 1, 0.4f));
        addActor(rc);
        setWidth(thumbnailSize);
        setHeight(thumbnailSize);

        initAdditionalListeners();
    }

    public void initDragDrop(final Actor thumbnail, final AssetPayloadObject payloadData, DraggableThumbnailEvent event) {
        initDragDrop(thumbnail, 1, 1, payloadData, event);
    }

    public void initDragDrop(final Actor thumbnail, final float itemScaleX, final float itemScaleY, final AssetPayloadObject payloadData, DraggableThumbnailEvent event) {
        this.draggingEvent = event;
        final DragAndDrop dragAndDrop = new DragAndDrop();

        dragAndDrop.addSource(new DragAndDrop.Source(this) {
            public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                DragAndDrop.Payload payload = new DragAndDrop.Payload();

                payloadData.xOffset = thumbnail.getWidth() / 2;
                payloadData.yOffset = thumbnail.getHeight() / 2;
                payload.setDragActor(thumbnail);
                payload.setObject(payloadData);
                payload.setInvalidDragActor(null);
//                payload.setValidDragActor();
                dragAndDrop.setDragActorPosition(-thumbnail.getWidth() / 2, thumbnail.getHeight() / 2);

                return payload;
            }
        });

        dragAndDrop.addTarget(new DragAndDrop.Target(sandbox.getUIStage().dummyTarget) {
            @Override
            public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                return true;
            }

            @Override
            public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                AssetPayloadObject pld = ((AssetPayloadObject) (payload.getObject()));
                draggingEvent.drop(pld, x - pld.xOffset, y - pld.yOffset);
            }
        });
    }

    public interface DraggableThumbnailEvent {
        public void drop(AssetPayloadObject pld, float x, float y);
    }

    public void initAdditionalListeners() {
        addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (button == Input.Buttons.RIGHT) {
                    showRightClickDropDown();
                }
                super.touchUp(event, x, y, pointer, button);
            }

        });
    }

    protected void showRightClickDropDown() { }

}
