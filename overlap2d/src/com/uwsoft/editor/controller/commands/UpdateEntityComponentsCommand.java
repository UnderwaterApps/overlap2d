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

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.commons.MsgAPI;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.utils.runtime.ComponentCloner;
import com.uwsoft.editor.utils.runtime.EntityUtils;

/**
 * Created by azakhary on 6/3/2015.
 */
public class UpdateEntityComponentsCommand extends EntityModifyRevertableCommand {

    private Array<Component> backupComponents = new Array<>();
    private Integer entityId;

    @Override
    public void doAction() {
        Object[] payload = getNotification().getBody();

        Entity entity = (Entity) payload[0];
        entityId = EntityUtils.getEntityId(entity);
        Array<Component> components = (Array<Component>) payload[1];

        for(int i = 0; i < components.size; i++) {
            //backup the original component
            Component originalComponent = ComponentRetriever.get(entity, components.get(i).getClass());
            backupComponents.add(ComponentCloner.get(originalComponent));

            //now modify the entity component from provided data
            ComponentCloner.set(originalComponent, components.get(i));
        }

        Overlap2DFacade.getInstance().sendNotification(MsgAPI.ITEM_DATA_UPDATED, entity);
    }

    @Override
    public void undoAction() {
        Entity entity = EntityUtils.getByUniqueId(entityId);
        for(int i = 0; i < backupComponents.size; i++) {
            Component entityComponent = ComponentRetriever.get(entity, backupComponents.get(i).getClass());
            ComponentCloner.set(entityComponent, backupComponents.get(i));
        }

        facade.sendNotification(MsgAPI.ITEM_DATA_UPDATED, entity);
    }
}
