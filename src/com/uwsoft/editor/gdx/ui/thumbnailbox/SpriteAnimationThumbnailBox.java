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

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.uwsoft.editor.gdx.ui.payloads.AssetPayloadObject;
import com.uwsoft.editor.renderer.actor.SpriteAnimation;
import com.uwsoft.editor.renderer.legacy.data.SpriteAnimationVO;

/**
 * Created by azakhary on 7/3/2014.
 */
public class SpriteAnimationThumbnailBox extends AnimationThumbnailBox {


    private final String animationName;
    private AssetPayloadObject payload;

    private float scaleSize = 1;

    private boolean isMouseInside = false;

    public SpriteAnimationThumbnailBox(String animationName) {
        super();
        this.animationName = animationName;
        SpriteAnimationVO vo = new SpriteAnimationVO();
        vo.animationName = animationName;
        final SpriteAnimation animThumb = new SpriteAnimation(vo, null);

        if (animThumb.getWidth() > thumbnailSize || animThumb.getHeight() > thumbnailSize) {
            // resizing is needed
            float scaleFactor = 1.0f;
            if (animThumb.getWidth() > animThumb.getHeight()) {
                //scale by width
                scaleFactor = 1.0f / (animThumb.getWidth() / thumbnailSize);
            } else {
                scaleFactor = 1.0f / (animThumb.getHeight() / thumbnailSize);
            }
            scaleSize = scaleFactor;
            animThumb.setScale(scaleFactor);
        }
        animThumb.start();
        // put it in middle
        animThumb.setX(/*(getWidth() - animThumb.getWidth()) / 2*/0);
        animThumb.setY(/*(getHeight() - animThumb.getHeight()) / 2*/0);


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

        animThumb.start();
        addActor(animThumb);

        Image payloadImg = new Image(animThumb.getAtlasRegionAt(0));
        payload = new AssetPayloadObject();
        payload.assetName = animationName;
        payload.type = AssetPayloadObject.AssetType.Sprite;

        DraggableThumbnailEvent event = (pld, x, y) -> sandbox.getUac().createSpriteAnimation(payload.assetName, x, y);

        initDragDrop(payloadImg, payload, event);

        setWidth(thumbnailSize);
        setHeight(thumbnailSize);

        super.act(1f);
    }

    @Override
    public void act(float delta) {
        if (isMouseInside) {
            super.act(delta);
        }
    }
}
