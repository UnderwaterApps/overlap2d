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

import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;
import com.uwsoft.editor.gdx.ui.layer.LayerItem;
import com.uwsoft.editor.gdx.ui.layer.UILayerBox;

/**
 * Created by sargis on 7/2/14.
 */
public class LayerItemTarget extends Target {
    private final LayerItem layerItem;
    private final UILayerBox uiLayerBox;

    public LayerItemTarget(LayerItem layerItem, UILayerBox uiLayerBox) {
        super(layerItem);
        this.layerItem = layerItem;
        this.uiLayerBox = uiLayerBox;
    }

    @Override
    public boolean drag(Source source, Payload payload, float x, float y, int pointer) {
        if (source.getActor() != layerItem) {
            layerItem.showLayerRowSeparator();
        }
        return true;
    }

    @Override
    public void drop(Source source, Payload payload, float x, float y, int pointer) {
        LayerItem sourceLayerItem = (LayerItem) source.getActor();
        if (layerItem == sourceLayerItem) {
            return;
        }
        uiLayerBox.arrangeLayers(sourceLayerItem, layerItem);
//        ArrayList<LayerItemVO> layers = sandboxStage.getCurrentScene().dataVO.composite.layers;
//        int index = -1;
//        for (LayerItemVO layer : layers) {
//            if (layer.layerName.equals(sourceLayerItem.getLayerName())) {
//                index = layers.indexOf(layer);
//                break;
//            }
//        }
//        if (index != -1) {
//
//        }
//        layers.addAll(0, layers.subList(0, 20));
    }

    public void reset(Source source, Payload payload) {
        if (source.getActor() == layerItem) {
            return;
        }
        layerItem.hideLayerRowSeparator();
    }
}
