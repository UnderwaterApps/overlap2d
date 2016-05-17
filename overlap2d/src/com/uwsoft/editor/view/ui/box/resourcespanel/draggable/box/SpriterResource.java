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

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.commons.ResourcePayloadObject;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.renderer.data.SpriterVO;
import com.uwsoft.editor.view.ui.box.UIResourcesBoxMediator;
import com.uwsoft.editor.view.ui.widget.actors.SpriterActor;

/**
 * Created by hayk on 19/12/2014.
 */
public class SpriterResource extends BoxItemResource {
    private final Overlap2DFacade facade;
    private Actor payloadActor;
    private ResourcePayloadObject payload;
    private boolean isMouseInside = true;

    public SpriterResource(String animationName) {
        facade = Overlap2DFacade.getInstance();

        SpriterVO vo = new SpriterVO();
        vo.animationName = animationName;
        SpriterActor animThumb = new SpriterActor(animationName, sandbox.getSceneControl().sceneLoader.getRm());

        if (animThumb.getWidth() > thumbnailSize || animThumb.getHeight() > thumbnailSize) {
            // resizing is needed
            float scaleFactor = 1.0f;
            if (animThumb.getWidth() > animThumb.getHeight()) {
                //scale by width
                scaleFactor = 1.0f / (animThumb.getWidth() / thumbnailSize);
            } else {
                scaleFactor = 1.0f / (animThumb.getHeight() / thumbnailSize);
            }
            animThumb.setScale(scaleFactor);

            animThumb.setX((getWidth()-animThumb.getWidth())/2);
            animThumb.setY((getHeight()-animThumb.getHeight())/2);

        } else {
            // put it in middle
            animThumb.setX((getWidth() - animThumb.getWidth()) / 2);
            animThumb.setY((getHeight() - animThumb.getHeight()) / 2);
        }

        addActor(animThumb);

        payloadActor = new SpriterActor(animationName, sandbox.getSceneControl().sceneLoader.getRm());
        payload = new ResourcePayloadObject();
        payload.name = animationName;
        payload.className = getClass().getName();

        setWidth(thumbnailSize);
        setHeight(thumbnailSize);

        super.act(1f);
        setRightClickEvent(UIResourcesBoxMediator.SPRITER_ANIMATION_RIGHT_CLICK, payload.name);
    }


    @Override
    public void act(float delta) {
        //if (isMouseInside) {
            super.act(delta);
        //}
    }

    @Override
    public Actor getDragActor() {
        return payloadActor;
    }

    @Override
    public ResourcePayloadObject getPayloadData() {
        return payload;
    }
}
