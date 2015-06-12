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

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.ViewPortComponent;
import com.uwsoft.editor.utils.runtime.ComponentRetriever;
import com.uwsoft.editor.utils.runtime.EntityUtils;

/**
 * Created by azakhary on 4/28/2015.
 */
public class EditCompositeCommand extends RevertableCommand {

    private static final String CLASS_NAME = "com.uwsoft.editor.mvc.controller.sandbox.EditCompositeCommand";
    public static final String VIEW_COMPOSITE_CHANGED = CLASS_NAME + "VIEW_COMPOSITE_CHANGED";

    private Integer previousViewEntityId;
    private Integer enteringInto;
    private TransformComponent curTransformBackup = new TransformComponent();

    @Override
    public void doAction() {
        Entity entity = getNotification().getBody();
        Entity oldEntity = sandbox.getCurrentViewingEntity();
        previousViewEntityId = EntityUtils.getEntityId(oldEntity);
        enteringInto = EntityUtils.getEntityId(entity);

        ViewPortComponent viewPortComponent = ComponentRetriever.get(oldEntity, ViewPortComponent.class);
        oldEntity.remove(ViewPortComponent.class);
        entity.add(viewPortComponent);
        sandbox.setCurrentViewingEntity(entity);

        sandbox.getSelector().clearSelections();
        facade.sendNotification(VIEW_COMPOSITE_CHANGED);
        
        TransformComponent transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
        
        // Back up current Trnasform Component
        curTransformBackup.x = transformComponent.x;
        curTransformBackup.y = transformComponent.y;
        curTransformBackup.scaleX = transformComponent.scaleX;
        curTransformBackup.scaleY = transformComponent.scaleY;
        curTransformBackup.rotation = transformComponent.rotation;
        
        // set all to 0 for current Trnasform Component
        transformComponent.x = 0;
        transformComponent.y = 0;
        transformComponent.scaleX = 1f;
        transformComponent.scaleY = 1f;
        transformComponent.rotation = 0;
        
    }

    @Override
    public void undoAction() {
        Entity oldEntity = EntityUtils.getByUniqueId(previousViewEntityId);
        Entity currEntity = sandbox.getCurrentViewingEntity();

        ViewPortComponent viewPortComponent = ComponentRetriever.get(currEntity, ViewPortComponent.class);
        currEntity.remove(ViewPortComponent.class);
        oldEntity.add(viewPortComponent);
        sandbox.setCurrentViewingEntity(oldEntity);

        sandbox.getSelector().setSelection(EntityUtils.getByUniqueId(enteringInto), true);
        facade.sendNotification(VIEW_COMPOSITE_CHANGED);
        
        TransformComponent transformComponent = ComponentRetriever.get(currEntity, TransformComponent.class);
        
        //revert from back up
        transformComponent.x = curTransformBackup.x;
        transformComponent.y = curTransformBackup.y;
        transformComponent.scaleX = curTransformBackup.scaleX;
        transformComponent.scaleY = curTransformBackup.scaleY;
        transformComponent.rotation = curTransformBackup.rotation;
    }
}
