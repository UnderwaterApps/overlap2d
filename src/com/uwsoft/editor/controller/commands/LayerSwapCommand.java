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
import com.uwsoft.editor.renderer.components.LayerMapComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.utils.runtime.EntityUtils;
import com.uwsoft.editor.view.stage.Sandbox;

/**
 * Created by azakhary on 8/18/2015.
 */
public class LayerSwapCommand extends EntityModifyRevertableCommand {
    private static final String CLASS_NAME = "com.uwsoft.editor.controller.commands.LayerSwapCommand";
    public static final String DONE = CLASS_NAME + "DONE";

    private Integer entityId;

    private String sourceName;
    private String targetName;

    private void backup() {
        if(entityId == null) {
            Object[] payload = getNotification().getBody();
            sourceName = (String) payload[0];
            targetName = (String) payload[1];
            entityId = EntityUtils.getEntityId(Sandbox.getInstance().getCurrentViewingEntity());
        }
    }

    @Override
    public void doAction() {
        backup();

        Entity viewingEntity = EntityUtils.getByUniqueId(entityId);
        LayerMapComponent layerMapComponent = ComponentRetriever.get(viewingEntity, LayerMapComponent.class);
        layerMapComponent.swap(sourceName, targetName);

        facade.sendNotification(DONE);

    }

    @Override
    public void undoAction() {
        Entity viewingEntity = EntityUtils.getByUniqueId(entityId);
        LayerMapComponent layerMapComponent = ComponentRetriever.get(viewingEntity, LayerMapComponent.class);
        layerMapComponent.swap(targetName, sourceName);

        facade.sendNotification(DONE);
    }
}
