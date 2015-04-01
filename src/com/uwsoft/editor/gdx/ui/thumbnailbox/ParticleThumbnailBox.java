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

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.uwsoft.editor.mvc.proxy.TextureManager;
import com.uwsoft.editor.gdx.stage.UIStage;
import com.uwsoft.editor.gdx.ui.payloads.AssetPayloadObject;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.ProjectManager;

/**
 * Created by azakhary on 7/3/2014.
 */
public class ParticleThumbnailBox extends DraggableThumbnailBox {
    private final Overlap2DFacade facade;
    private final ProjectManager projectManager;

    public ParticleThumbnailBox(UIStage s, String particleName) {
        super(s);
        facade = Overlap2DFacade.getInstance();
        projectManager = facade.retrieveProxy(ProjectManager.NAME);
        TextureManager textureManager = facade.retrieveProxy(TextureManager.NAME);
        Image img = new Image(textureManager.getEditorAsset("resizeIconChecked"));

        this.setWidth(thumbnailSize);
        this.setHeight(thumbnailSize);

        img.setX(getWidth() / 2 - img.getWidth() / 2);
        img.setY(getHeight() / 2 - img.getHeight() / 2);

        addActor(img);


        Image payloadImg = new Image(textureManager.getEditorAsset("resizeIconChecked"));
        AssetPayloadObject payload = new AssetPayloadObject();
        payload.assetName = particleName;
        payload.type = AssetPayloadObject.AssetType.Particle;

        DraggableThumbnailEvent event = new DraggableThumbnailEvent() {
            @Override
            public void drop(AssetPayloadObject pld, float x, float y) {
                stage.getSandbox().getUac().createParticleItem(pld.assetName, x, y);
            }
        };

        initDragDrop(stage, payloadImg, payload, event);
    }
}
