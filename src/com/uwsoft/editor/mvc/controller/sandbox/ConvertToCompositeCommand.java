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

import java.util.HashSet;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.factory.ItemFactory;
import com.uwsoft.editor.mvc.view.MidUIMediator;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.ParentNodeComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.utils.runtime.ComponentRetriever;
import com.uwsoft.editor.utils.runtime.EntityUtils;

/**
 * Created by azakhary on 4/28/2015.
 */
public class ConvertToCompositeCommand extends EntityModifyRevertableCommand {

    protected Integer entityId;
    protected Integer parentEntityId;

    @Override
    public void doAction() {
        // get entity list
        HashSet<Entity> entities = (HashSet<Entity>) sandbox.getSelector().getSelectedItems();

        // what will be the position of new composite?
        Vector2 position = EntityUtils.getLeftBottomPoint(entities);

        //create new entity
        Entity entity = ItemFactory.get().createCompositeItem(position);
        entityId = EntityUtils.getEntityId(entity);
        sandbox.getEngine().addEntity(entity);

        // what was the parent component of entities
        parentEntityId = EntityUtils.getEntityId(sandbox.getCurrentViewingEntity());

        // rebase children
        EntityUtils.changeParent(entities, entity);

        //reposition children
        for(Entity tmpEntity: entities) {
            TransformComponent transformComponent = ComponentRetriever.get(tmpEntity, TransformComponent.class);
            transformComponent.x-=position.x;
            transformComponent.y-=position.y;

            // also remove followers (they will be created again once user enters the composite
            // TODO:
        }
        // recalculate composite size
        DimensionsComponent dimensionsComponent = ComponentRetriever.get(entity, DimensionsComponent.class);
        Vector2 newSize = EntityUtils.getRightTopPoint(entities);
        dimensionsComponent.width = newSize.x;
        dimensionsComponent.height = newSize.y;

        //let everyone know
        Overlap2DFacade.getInstance().sendNotification(ItemFactory.NEW_ITEM_ADDED, entity);
        sandbox.getSelector().setSelection(entity, true);
    }

    @Override
    public void undoAction() {
        MidUIMediator midUIMediator = Overlap2DFacade.getInstance().retrieveMediator(MidUIMediator.NAME);

        //get the entity
        Entity entity = EntityUtils.getByUniqueId(entityId);
        Entity oldParentEntity = EntityUtils.getByUniqueId(parentEntityId);
        HashSet<Entity> children = EntityUtils.getChildren(entity);

        // what will be the position diff of children?
        Vector2 positionDiff = EntityUtils.getPosition(entity);

        //rebase children back to root
        EntityUtils.changeParent(children, oldParentEntity);



        //reposition children
        for(Entity tmpEntity: children) {
            TransformComponent transformComponent = ComponentRetriever.get(tmpEntity, TransformComponent.class);
            transformComponent.x+=positionDiff.x;
            transformComponent.y+=positionDiff.y;

            //bring back the followers
            // TODO:
        }

        // remove composite
        midUIMediator.removeFollower(entity);
        sandbox.getEngine().removeEntity(entity);
    }
}
