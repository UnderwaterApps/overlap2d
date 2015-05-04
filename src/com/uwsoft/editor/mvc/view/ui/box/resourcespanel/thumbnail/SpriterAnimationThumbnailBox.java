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

package com.uwsoft.editor.mvc.view.ui.box.resourcespanel.thumbnail;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.uwsoft.editor.gdx.ui.payloads.AssetPayloadObject;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.ProjectManager;
import com.uwsoft.editor.mvc.proxy.EditorTextureManager;
import com.uwsoft.editor.mvc.view.ui.box.UIResourcesBoxMediator;
import com.uwsoft.editor.mvc.view.ui.box.resourcespanel.thumbnail.AnimationThumbnailBox;
import com.uwsoft.editor.renderer.actor.SpriterActor;
import com.uwsoft.editor.renderer.data.SpriterVO;

/**
 * Created by hayk on 19/12/2014.
 */
public class SpriterAnimationThumbnailBox extends AnimationThumbnailBox {


    private final Overlap2DFacade facade;
    private final ProjectManager projectManager;
    private AssetPayloadObject payload;

    private float scaleSize = 1;

    private boolean isMouseInside = true;

    private String animationName;

    public SpriterAnimationThumbnailBox(String animationName) {
        super();
        this.animationName = animationName;
        facade = Overlap2DFacade.getInstance();
        projectManager = facade.retrieveProxy(ProjectManager.NAME);
        SpriterVO vo = new SpriterVO();
        vo.animationName = animationName;
        SpriterActor animThumb = new SpriterActor(vo, sandbox.getSceneControl().getEssentials());

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
            animThumb.setSpriterScale(scaleFactor);

//            animThumb.setX((getWidth()-animThumb.getWidth())/2);
//            animThumb.setY((getHeight()-animThumb.getHeight())/2);
            animThumb.setX(0);
            animThumb.setY(0);
        } else {
            // put it in middle
            animThumb.setX(0);
            animThumb.setY(0);
        }

        addActor(animThumb);
        EditorTextureManager textureManager = facade.retrieveProxy(EditorTextureManager.NAME);
        Image payloadImg = new Image(textureManager.getEditorAsset("resizeIconChecked"));
        payload = new AssetPayloadObject();
        payload.assetName = animationName;
        payload.type = AssetPayloadObject.AssetType.Spriter;

        DraggableThumbnailEvent event = (pld, x, y) -> sandbox.getUac().createSpriterAnimation(payload.assetName, x, y);

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

    @Override
    protected void showRightClickDropDown() {
        Overlap2DFacade.getInstance().sendNotification(UIResourcesBoxMediator.ANIMATION_RIGHT_CLICK, animationName);
    }
}
