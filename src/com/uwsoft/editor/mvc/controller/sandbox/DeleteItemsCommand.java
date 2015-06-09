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

package com.uwsoft.editor.mvc.controller.sandbox;

import java.util.*;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.factory.ItemFactory;
import com.uwsoft.editor.renderer.components.NodeComponent;
import com.uwsoft.editor.renderer.components.ParentNodeComponent;
import com.uwsoft.editor.renderer.legacy.data.CompositeVO;
import com.uwsoft.editor.utils.runtime.ComponentCloner;
import com.uwsoft.editor.utils.runtime.ComponentRetriever;
import com.uwsoft.editor.utils.runtime.EntityUtils;

/**
 * Created by azakhary on 4/28/2015.
 */
public class DeleteItemsCommand extends RevertableCommand {

   private HashMap<Integer, Collection<Component>> backup;

    private void backup() {
        backup = new HashMap<>();

        Set<Entity> items = sandbox.getSelector().getSelectedItems();

        for(Entity entity: items) {
            Collection<Component> components = ComponentCloner.cloneAll(ComponentRetriever.getComponents(entity));
            backup.put(EntityUtils.getEntityId(entity), components);
        }
    }

    @Override
    public void doAction() {
        backup();
        sandbox.getSelector().removeCurrentSelectedItems();
    }

    @Override
    public void undoAction() {
        for (Collection<Component> components : backup.values()) {
            Entity entity = new Entity();
            for(Component component: components) {
                entity.add(component);
            }
            sandbox.getEngine().addEntity(entity);
            sandbox.getSceneControl().sceneLoader.entityFactory.updateMap(entity);
            Entity parentEntity = entity.getComponent(ParentNodeComponent.class).parentEntity;
            NodeComponent nodeComponent = parentEntity.getComponent(NodeComponent.class);
            nodeComponent.addChild(entity);
            Overlap2DFacade.getInstance().sendNotification(ItemFactory.NEW_ITEM_ADDED, entity);
        }
    }
}
