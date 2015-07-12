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

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.factory.ItemFactory;
import com.uwsoft.editor.renderer.components.NodeComponent;
import com.uwsoft.editor.renderer.components.ParentNodeComponent;
import com.uwsoft.editor.utils.runtime.EntityUtils;

/**
 * Created by azakhary on 4/28/2015.
 */
public class DeleteItemsCommand extends EntityModifyRevertableCommand {

    private static final String CLASS_NAME = "com.uwsoft.editor.controller.commands.DeleteItemsCommand";
    public static final String DONE = CLASS_NAME + "DONE";

   private HashMap<Integer, Collection<Component>> backup;

    private void backup() {
        backup = EntityUtils.cloneEntities(sandbox.getSelector().getSelectedItems());
    }

    @Override
    public void doAction() {
        backup();
        sandbox.getSelector().removeCurrentSelectedItems();

        facade.sendNotification(DONE);
    }

    @Override
    public void undoAction() {
        Set<Entity> newEntitiesList = new HashSet<>();
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

            newEntitiesList.add(entity);
        }

        sandbox.getSelector().setSelections(newEntitiesList, true);
    }
}
