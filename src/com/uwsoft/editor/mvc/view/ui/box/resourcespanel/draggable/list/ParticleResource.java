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

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.uwsoft.editor.gdx.ui.payloads.ResourcePayloadObject;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.EditorTextureManager;

/**
 * Created by azakhary on 7/3/2014.
 */
public class ParticleResource extends ListItemResource {
    private final Overlap2DFacade facade;
    private final Image payloadImg;
    private final ResourcePayloadObject payload;


    public ParticleResource(String particleName) {
        super(particleName, "particle");
        facade = Overlap2DFacade.getInstance();
        EditorTextureManager textureManager = facade.retrieveProxy(EditorTextureManager.NAME);
        payloadImg = new Image(textureManager.getEditorAsset("resizeIconChecked"));
        payload = new ResourcePayloadObject();
        payload.name = particleName;
    }

    @Override
    public Actor getDragActor() {
        return payloadImg;
    }

    @Override
    public ResourcePayloadObject getPayloadData() {
        return payload;
    }

}
