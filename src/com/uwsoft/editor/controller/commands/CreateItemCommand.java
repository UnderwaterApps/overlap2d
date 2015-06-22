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
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.factory.ItemFactory;
import com.uwsoft.editor.view.MidUIMediator;
import com.uwsoft.editor.utils.runtime.EntityUtils;

/**
 * Created by azakhary on 6/9/2015.
 */
public class CreateItemCommand extends EntityModifyRevertableCommand {

    private Integer entityId;

    @Override
    public void doAction() {
        Entity entity = getNotification().getBody();

        entityId = EntityUtils.getEntityId(entity);

        sandbox.getEngine().addEntity(entity);
        Overlap2DFacade.getInstance().sendNotification(ItemFactory.NEW_ITEM_ADDED, entity);

        // select newly created item
        sandbox.getSelector().setSelection(entity, true);
    }

    @Override
    public void undoAction() {
        Entity entity = EntityUtils.getByUniqueId(entityId);

        MidUIMediator midUIMediator = Overlap2DFacade.getInstance().retrieveMediator(MidUIMediator.NAME);
        midUIMediator.removeFollower(entity);
        sandbox.getEngine().removeEntity(entity);
    }
}
