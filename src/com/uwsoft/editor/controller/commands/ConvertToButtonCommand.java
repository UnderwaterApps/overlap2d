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

package com.uwsoft.editor.controller.commands;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.renderer.components.LayerMapComponent;
import com.uwsoft.editor.renderer.components.MainItemComponent;
import com.uwsoft.editor.renderer.components.additional.ButtonComponent;
import com.uwsoft.editor.renderer.data.LayerItemVO;
import com.uwsoft.editor.renderer.factory.EntityFactory;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.utils.runtime.EntityUtils;

import java.util.HashSet;

/**
 * Created by azakhary on 4/28/2015.
 */
public class ConvertToButtonCommand extends ConvertToCompositeCommand {


    @Override
    public void doAction() {
        Entity entity;

        HashSet<Entity> entities = (HashSet<Entity>) sandbox.getSelector().getSelectedItems();
        Entity item = entities.iterator().next();

        if(entities.size() == 1 && EntityUtils.getType(item) == EntityFactory.COMPOSITE_TYPE) {
            entity = item;
        } else {
            super.doAction();
            entity = EntityUtils.getByUniqueId(entityId);
        }

        //create layers
        LayerMapComponent layerMapComponent = ComponentRetriever.get(entity, LayerMapComponent.class);
        layerMapComponent.addLayer(new LayerItemVO("normal"));
        layerMapComponent.addLayer(new LayerItemVO("pressed"));

        // adding button logic
        MainItemComponent mainItemComponent = ComponentRetriever.get(entity, MainItemComponent.class);
        mainItemComponent.tags.add("button");
        entity.add(new ButtonComponent());
    }

    @Override
    public void undoAction() {
        super.undoAction();
    }
}
