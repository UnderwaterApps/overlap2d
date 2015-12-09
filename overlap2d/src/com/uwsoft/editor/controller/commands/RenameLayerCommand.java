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
import com.uwsoft.editor.renderer.components.NodeComponent;
import com.uwsoft.editor.renderer.components.ZIndexComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.view.stage.Sandbox;

/**
 * Created by azakhary on 9/7/2015.
 */
public class RenameLayerCommand extends EntityModifyRevertableCommand {
    private static final String CLASS_NAME = "com.uwsoft.editor.controller.commands.RenameLayerCommand";
    public static final String DONE = CLASS_NAME + "DONE";

    @Override
    public void doAction() {
        String[] payload = getNotification().getBody();
        String oldName = payload[0];
        String newName = payload[1];

        if(oldName.equals(newName)) cancel();

        renameLayer(oldName, newName);

        facade.sendNotification(DONE);
    }

    @Override
    public void undoAction() {
        String[] payload = getNotification().getBody();
        String oldName = payload[0];
        String newName = payload[1];

        renameLayer(newName, oldName);

        facade.sendNotification(DONE);
    }

    private void renameLayer(String fromName, String toName) {
        Entity viewEntity = Sandbox.getInstance().getCurrentViewingEntity();
        NodeComponent nodeComponent = ComponentRetriever.get(viewEntity, NodeComponent.class);
        LayerMapComponent layerMapComponent = ComponentRetriever.get(viewEntity, LayerMapComponent.class);

        layerMapComponent.rename(fromName, toName);

        for(Entity childEntity: nodeComponent.children) {
            ZIndexComponent zIndexComponent = ComponentRetriever.get(childEntity, ZIndexComponent.class);
            if(zIndexComponent.layerName.equals(fromName)) {
                zIndexComponent.layerName = toName;
            }
        }
    }

    public static String[] payload(String oldName, String newName) {
        String[] payload = new String[2];
        payload[0] = oldName;
        payload[1] = newName;

        return payload;
    }
}
