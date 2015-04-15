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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.uwsoft.editor.gdx.ui.payloads.AssetPayloadObject;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.ProjectManager;
import com.uwsoft.editor.mvc.proxy.TextureManager;
import com.uwsoft.editor.mvc.view.stage.UIStage;
import com.uwsoft.editor.renderer.data.CompositeItemVO;

/**
 * Created by azakhary on 7/3/2014.
 */
public class LibraryItemThumbnailBox extends DraggableThumbnailBox {

    private final Image bgImg;
    private final Label label;
    private final Label payloadLbl;
    private final AssetPayloadObject payload;
    private final CompositeItemVO compositeItemVO;
    private final Overlap2DFacade facade;
    private final ProjectManager projectManager;
    private final TextureManager textureManager;
    private String key;

    public LibraryItemThumbnailBox(UIStage s, float width, String key, CompositeItemVO compositeItemVO) {
        super(s);
        facade = Overlap2DFacade.getInstance();
        textureManager = facade.retrieveProxy(TextureManager.NAME);
        this.key = key;
        this.compositeItemVO = compositeItemVO;
        projectManager = facade.retrieveProxy(ProjectManager.NAME);
        TextureManager textureManager = facade.retrieveProxy(TextureManager.NAME);
        setWidth(width);
        bgImg = new Image(textureManager.getEditorAsset("pixel"));
        bgImg.setColor(0.425f, 0.425f, 0.425f, 1.0f);


        label = new Label(key, textureManager.editorSkin);
        label.setWidth(getWidth());
        setHeight(label.getHeight());

        bgImg.setScaleX(getWidth());
        bgImg.setScaleY(getHeight());

        addActor(bgImg);
        addActor(label);
        rc.setVisible(false);

        payloadLbl = new Label(key, textureManager.editorSkin);
        payload = new AssetPayloadObject();
        payload.assetName = key;
        payload.type = AssetPayloadObject.AssetType.Component;

        DraggableThumbnailEvent event = (pld, x, y) -> stage.getSandbox().getUac().createItemFromLibrary(pld.assetName, x, y);

        initDragDrop(stage, payloadLbl, payload, event);
    }

    public void select() {
        bgImg.setColor(90f / 255f, 102f / 255f, 121f / 255f, 1.0f);
    }

    public void unselect() {
        bgImg.setColor(0.425f, 0.425f, 0.425f, 1.0f);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
        label.setText(key);
        payloadLbl.setText(key);
        payload.assetName = key;
    }

    public CompositeItemVO getCompositeItemVO() {
        return compositeItemVO;
    }
}
