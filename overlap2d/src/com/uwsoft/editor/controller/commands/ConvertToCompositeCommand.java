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
import com.badlogic.gdx.math.Vector2;
import com.commons.MsgAPI;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.factory.ItemFactory;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.ZIndexComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.utils.runtime.EntityUtils;
import com.uwsoft.editor.view.ui.FollowersUIMediator;
import com.uwsoft.editor.view.ui.box.UILayerBoxMediator;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by azakhary on 4/28/2015.
 */
public class ConvertToCompositeCommand extends EntityModifyRevertableCommand {

    private static final String CLASS_NAME = "com.uwsoft.editor.controller.commands.ConvertToCompositeCommand";
    public static final String DONE = CLASS_NAME + "DONE";

    protected Integer entityId;
    protected Integer parentEntityId;

    protected HashMap<Integer, String> layersBackup;

    @Override
    public void doAction() {
        // get entity list
        HashSet<Entity> entities = (HashSet<Entity>) sandbox.getSelector().getSelectedItems();
        UILayerBoxMediator layerBoxMediator = facade.retrieveMediator(UILayerBoxMediator.NAME);

        if(layersBackup == null) {
            // backup layer data
            layersBackup = new HashMap<>();
            for(Entity entity: entities) {
                ZIndexComponent zIndexComponent = ComponentRetriever.get(entity, ZIndexComponent.class);
                int tmpId = EntityUtils.getEntityId(entity);
                layersBackup.put(tmpId, zIndexComponent.layerName);
            }
        }

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
        for(Entity childEntity: entities) {
            TransformComponent transformComponent = ComponentRetriever.get(childEntity, TransformComponent.class);
            transformComponent.x -= position.x;
            transformComponent.y -=position.y;

            // put it on default layer
            ZIndexComponent zIndexComponent = ComponentRetriever.get(childEntity, ZIndexComponent.class);
            zIndexComponent.layerName = "Default";

        }
        // recalculate composite size
        DimensionsComponent dimensionsComponent = ComponentRetriever.get(entity, DimensionsComponent.class);
        Vector2 newSize = EntityUtils.getRightTopPoint(entities);
        dimensionsComponent.width = newSize.x;
        dimensionsComponent.height = newSize.y;
        dimensionsComponent.boundBox.set(0, 0, newSize.x, newSize.y);

        ZIndexComponent zIndexComponent = ComponentRetriever.get(entity, ZIndexComponent.class);
        zIndexComponent.layerName = layerBoxMediator.getCurrentSelectedLayerName();

        //let everyone know
        Overlap2DFacade.getInstance().sendNotification(DONE);
        Overlap2DFacade.getInstance().sendNotification(MsgAPI.NEW_ITEM_ADDED, entity);
        sandbox.getSelector().setSelection(entity, true);

    }

    @Override
    public void undoAction() {
        FollowersUIMediator followersUIMediator = Overlap2DFacade.getInstance().retrieveMediator(FollowersUIMediator.NAME);

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

            // put layer data back
            ZIndexComponent zIndexComponent = ComponentRetriever.get(entity, ZIndexComponent.class);
            zIndexComponent.layerName = layersBackup.get(EntityUtils.getEntityId(tmpEntity));
        }

        // remove composite
        followersUIMediator.removeFollower(entity);
        sandbox.getEngine().removeEntity(entity);

        Overlap2DFacade.getInstance().sendNotification(DONE);

        sandbox.getSelector().setSelections(children, true);
    }
}
