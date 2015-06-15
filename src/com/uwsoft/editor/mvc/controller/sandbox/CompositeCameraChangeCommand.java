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
import com.uwsoft.editor.renderer.components.CompositeTransformComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.ViewPortComponent;
import com.uwsoft.editor.utils.runtime.ComponentRetriever;
import com.uwsoft.editor.utils.runtime.EntityUtils;

/**
 * Created by azakhary on 4/28/2015.
 */
public class CompositeCameraChangeCommand extends RevertableCommand {

    private static final String CLASS_NAME = "com.uwsoft.editor.mvc.controller.sandbox.CompositeCameraChangeCommand";
    public static final String DONE = CLASS_NAME + "DONE";

    private Integer previousViewEntityId;
    private Integer enteringInto;

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
        facade.sendNotification(DONE, enteringInto);
        
        CompositeTransformComponent compositeTransformComponent = ComponentRetriever.get(entity, CompositeTransformComponent.class);
        CompositeTransformComponent previousCompositeTransformComponent = ComponentRetriever.get(oldEntity, CompositeTransformComponent.class);
        previousCompositeTransformComponent.transform = true;
        compositeTransformComponent.transform = false;
        
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
        facade.sendNotification(DONE, previousViewEntityId);

        CompositeTransformComponent compositeTransformComponent = ComponentRetriever.get(currEntity, CompositeTransformComponent.class);
        CompositeTransformComponent previousCompositeTransformComponent = ComponentRetriever.get(oldEntity, CompositeTransformComponent.class);
        previousCompositeTransformComponent.transform = false;
        compositeTransformComponent.transform = true;
    }
}
