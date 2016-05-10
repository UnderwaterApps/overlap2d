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

package com.uwsoft.editor.view.ui.box.resourcespanel.draggable.list;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.commons.ResourcePayloadObject;
import com.uwsoft.editor.view.ui.box.UIResourcesBoxMediator;

/**
 * Created by azakhary on 7/3/2014.
 */
public class LibraryItemResource extends ListItemResource {

    private final Image payloadImg;
    private final ResourcePayloadObject payload;
    private String key;

    public LibraryItemResource(String key) {
        super(key, "library");
        this.key = key;
        payloadImg = new Image(getStyle().resourceOver);
        payloadImg.setScale(2);
        payloadImg.getColor().a = .85f;
        payload = new ResourcePayloadObject();
        payload.name = key;
        payload.className = getClass().getName();

        setRightClickEvent(UIResourcesBoxMediator.LIBRARY_ITEM_RIGHT_CLICK, payload.name);
    }

    public String getKey() {
        return key;
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
