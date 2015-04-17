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

package com.uwsoft.editor.gdx.ui.layer.drag;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;
import com.uwsoft.editor.gdx.ui.layer.LayerItem;
import com.uwsoft.editor.gdx.ui.layer.UILayerBoxOld;

/**
 * Created by sargis on 7/2/14.
 */
public class LayerItemSource extends Source {

    private final LayerItem layerItem;
    private final UILayerBoxOld uiLayerBoxOld;

    public LayerItemSource(LayerItem layerItem, UILayerBoxOld uiLayerBoxOld) {
        super(layerItem);
        this.layerItem = layerItem;
        this.uiLayerBoxOld = uiLayerBoxOld;
    }

    public Payload dragStart(InputEvent event, float x, float y, int pointer) {
        Payload payload = new Payload();
        Actor dragActor = new LayerItem(layerItem);
        dragActor.getColor().a = 0.7f;
        payload.setDragActor(dragActor);

//
//        Actor validDragActor = new Image(icon);
        //payload.setValidDragActor(layerItem);
//
//        Actor invalidDragActor = new Image(icon);
        //payload.setInvalidDragActor(actor);
        uiLayerBoxOld.startLayerDragging(layerItem);
        return payload;
    }

    @Override
    public void dragStop(InputEvent event, float x, float y, int pointer, Payload payload, Target target) {
        uiLayerBoxOld.stopLayerDragging(layerItem);
    }
}
