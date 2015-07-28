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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.factory.ItemFactory;
import com.uwsoft.editor.renderer.components.NodeComponent;
import com.uwsoft.editor.renderer.components.ParentNodeComponent;
import com.uwsoft.editor.renderer.data.CompositeVO;
import com.uwsoft.editor.utils.runtime.EntityUtils;
import com.uwsoft.editor.view.ui.FollowersUIMediator;

/**
 * Created by azakhary on 4/28/2015.
 */
public class DeleteItemsCommand extends EntityModifyRevertableCommand {

    private static final String CLASS_NAME = "com.uwsoft.editor.controller.commands.DeleteItemsCommand";
    public static final String DONE = CLASS_NAME + "DONE";

    private String backup;
    private Array<Integer> entityIdsToDelete;

    private void backup() {
        Set<Entity> entitySet = new HashSet<>();
        if(entityIdsToDelete == null) {
            entityIdsToDelete = new Array<>();
            entitySet = sandbox.getSelector().getSelectedItems();
            for(Entity entity: entitySet) {
                entityIdsToDelete.add(EntityUtils.getEntityId(entity));
            }
        } else {
            for(Integer entityId: entityIdsToDelete) {
                entitySet.add(EntityUtils.getByUniqueId(entityId));
            }
        }

        backup = CopyItemsCommand.getJsonStringFromEntities(entitySet);
    }

    @Override
    public void doAction() {
        backup();

        FollowersUIMediator followersUIMediator = Overlap2DFacade.getInstance().retrieveMediator(FollowersUIMediator.NAME);
        for (Integer entityId : entityIdsToDelete) {
            Entity item = EntityUtils.getByUniqueId(entityId);
            followersUIMediator.removeFollower(item);
            sandbox.getEngine().removeEntity(item);
        }

        sandbox.getSelector().getCurrentSelection().clear();

        facade.sendNotification(DONE);
    }

    @Override
    public void undoAction() {
        Json json =  new Json();
        CompositeVO compositeVO = json.fromJson(CompositeVO.class, backup);
        Set<Entity> newEntitiesList = PasteItemsCommand.createEntitiesFromVO(compositeVO);

        for (Entity entity : newEntitiesList) {
            Overlap2DFacade.getInstance().sendNotification(ItemFactory.NEW_ITEM_ADDED, entity);
        }

        sandbox.getSelector().setSelections(newEntitiesList, true);
    }

    public void setItemsToDelete(Set<Entity> entities) {
        entityIdsToDelete = new Array<>();
        for(Entity entity: entities) {
            entityIdsToDelete.add(EntityUtils.getEntityId(entity));
        }
    }
}
