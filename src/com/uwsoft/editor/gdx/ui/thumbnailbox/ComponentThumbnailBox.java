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

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.uwsoft.editor.mvc.view.stage.UIStage;
import com.uwsoft.editor.gdx.ui.payloads.AssetPayloadObject;

/**
 * Created by azakhary on 7/3/2014.
 */
public class ComponentThumbnailBox extends DraggableThumbnailBox {

    public ComponentThumbnailBox(UIStage s, float width, String text) {
        super(s);
        setWidth(width);

        Label lbl = new Label(text, stage.textureManager.editorSkin);
        lbl.setWidth(getWidth());
        addActor(lbl);

        setHeight(lbl.getHeight());
        rc.setVisible(false);

        Label payloadLbl = new Label(text, stage.textureManager.editorSkin);
        AssetPayloadObject payload = new AssetPayloadObject();
        payload.assetName = text;
        payload.type = AssetPayloadObject.AssetType.Component;

        DraggableThumbnailEvent event = new DraggableThumbnailEvent() {
            @Override
            public void drop(AssetPayloadObject pld, float x, float y) {
                stage.getSandbox().getUac().createComponent(pld.assetName, x, y);
            }
        };

        initDragDrop(stage, payloadLbl, payload, event);
    }
}
