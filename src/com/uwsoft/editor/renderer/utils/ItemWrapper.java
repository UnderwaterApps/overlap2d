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

package com.uwsoft.editor.renderer.utils;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.renderer.components.MainItemComponent;
import com.uwsoft.editor.renderer.components.NodeComponent;
import com.uwsoft.editor.renderer.components.ParentNodeComponent;

import java.util.HashMap;

/**
 * Created by azakhary on 7/8/2015.
 */
public class ItemWrapper {

    private Entity entity;

    private NodeComponent nodeComponent;
    private HashMap<String, Entity> childMap = new HashMap<String, Entity>();

    public ItemWrapper() {
        // empty wrapper is better then null pointer
    }

    public ItemWrapper(Entity entity) {
        this.entity = entity;
        nodeComponent = ComponentRetriever.get(entity, NodeComponent.class);
        if(nodeComponent != null) {
            for (Entity child : nodeComponent.children) {
                MainItemComponent mainItemComponent = ComponentRetriever.get(child, MainItemComponent.class);
                childMap.put(mainItemComponent.itemIdentifier, child);
            }
        }
    }

    public ItemWrapper getChild(String id) {
        Entity entity = childMap.get(id);
        if(entity == null) return new ItemWrapper();

        return new ItemWrapper(entity);
    }

    public Component getComponent(Class clazz) {
        return ComponentRetriever.get(entity, clazz);
    }

    public ItemWrapper addChild(Entity child) {
        if(nodeComponent != null) {
            ParentNodeComponent parentNodeComponent = child.getComponent(ParentNodeComponent.class);
            parentNodeComponent.parentEntity = entity;
            nodeComponent.children.add(child);

            return  new ItemWrapper(child);
        }

        return new ItemWrapper();
    }

    public int getType() {
        MainItemComponent mainItemComponent = ComponentRetriever.get(entity, MainItemComponent.class);
        return mainItemComponent.entityType;
    }
}
