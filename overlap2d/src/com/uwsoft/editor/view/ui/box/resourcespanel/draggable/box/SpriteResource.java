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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.commons.ResourcePayloadObject;
import com.uwsoft.editor.renderer.data.SpriteAnimationVO;
import com.uwsoft.editor.renderer.resources.IResourceRetriever;
import com.uwsoft.editor.view.ui.box.UIResourcesBoxMediator;
import com.uwsoft.editor.view.ui.widget.actors.SpriteAnimationActor;

/**
 * Created by azakhary on 7/3/2014.
 */
public class SpriteResource extends BoxItemResource {


    private final SpriteAnimationActor payloadActor;
    private ResourcePayloadObject payload;


    private boolean isMouseInside = false;

    public SpriteResource(String animationName) {
        super();
        SpriteAnimationVO vo = new SpriteAnimationVO();
        vo.animationName = animationName;

        IResourceRetriever rm = sandbox.getSceneControl().sceneLoader.getRm();

        SpriteAnimationActor animThumb = new SpriteAnimationActor(animationName, rm);
        animThumb.setFPS(vo.fps);

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
        }

        // put it in middle
        animThumb.setX((getWidth() - animThumb.getWidth()*animThumb.getScaleX()) / 2f);
        animThumb.setY((getHeight() - animThumb.getHeight()*animThumb.getScaleY()) / 2f);
        animThumb.setPaused(false);


        addListener(new ClickListener() {
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                isMouseInside = true;
                super.enter(event, x, y, pointer, fromActor);
            }

            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                isMouseInside = false;
                super.enter(event, x, y, pointer, toActor);
            }
        });

        addActor(animThumb);

        payloadActor = new SpriteAnimationActor(animationName, rm);
        payload = new ResourcePayloadObject();
        payload.name = animationName;
        payload.className = getClass().getName();

        setHeight(thumbnailSize);
        setRightClickEvent(UIResourcesBoxMediator.SPRITE_ANIMATION_RIGHT_CLICK, payload.name);
    }


    @Override
    public void act(float delta) {
        if (isMouseInside) {
            super.act(delta);
        }
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
