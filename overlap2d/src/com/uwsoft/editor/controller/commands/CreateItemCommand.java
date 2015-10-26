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
import com.badlogic.gdx.utils.Array;
import com.commons.MsgAPI;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.renderer.components.NodeComponent;
import com.uwsoft.editor.renderer.components.ZIndexComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.utils.runtime.EntityUtils;
import com.uwsoft.editor.view.stage.Sandbox;
import com.uwsoft.editor.view.ui.FollowersUIMediator;

import java.util.HashSet;

/**
 * Created by azakhary on 6/9/2015.
 */
public class CreateItemCommand extends EntityModifyRevertableCommand {

    private Integer entityId;
    private Array<Integer> previousSelectionIds;

    @Override
    public void doAction() {
        Entity entity = getNotification().getBody();

        entityId = EntityUtils.getEntityId(entity);

        sandbox.getEngine().addEntity(entity);

        // z-index
        NodeComponent nodeComponent = ComponentRetriever.get(Sandbox.getInstance().getCurrentViewingEntity(), NodeComponent.class);
        ZIndexComponent zindexComponent = ComponentRetriever.get(entity, ZIndexComponent.class);
        zindexComponent.setZIndex(nodeComponent.children.size);

        Overlap2DFacade.getInstance().sendNotification(MsgAPI.NEW_ITEM_ADDED, entity);

        // select newly created item
        // get current selection
        HashSet<Entity> previousSelection = new HashSet<>(Sandbox.getInstance().getSelector().getSelectedItems());
        previousSelectionIds = EntityUtils.getEntityId(previousSelection);
        sandbox.getSelector().setSelection(entity, true);
    }

    @Override
    public void undoAction() {
        Entity entity = EntityUtils.getByUniqueId(entityId);

        FollowersUIMediator followersUIMediator = Overlap2DFacade.getInstance().retrieveMediator(FollowersUIMediator.NAME);
        followersUIMediator.removeFollower(entity);
        sandbox.getEngine().removeEntity(entity);

        Sandbox.getInstance().getSelector().setSelections(EntityUtils.getByUniqueId(previousSelectionIds), true);
    }
}
