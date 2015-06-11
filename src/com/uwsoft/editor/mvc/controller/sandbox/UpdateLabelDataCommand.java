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
import com.uwsoft.editor.Overlap2D;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.renderer.components.label.LabelComponent;
import com.uwsoft.editor.renderer.factory.component.LabelComponentFactory;
import com.uwsoft.editor.utils.runtime.ComponentRetriever;
import com.uwsoft.editor.utils.runtime.EntityUtils;

/**
 * Created by azakhary on 6/11/2015.
 */
public class UpdateLabelDataCommand extends RevertableCommand {

    Integer entityId;

    @Override
    public void doAction() {
        Object[] payload = getNotification().getBody();
        Entity entity = (Entity) payload[0];
        entityId = EntityUtils.getEntityId(entity);

        LabelComponent labelComponent = ComponentRetriever.get(entity, LabelComponent.class);

        labelComponent.fontName = (String) payload[1];
        labelComponent.fontSize = (int) payload[2];
        labelComponent.setAlignment((Integer) payload[3]);

        labelComponent.setStyle(LabelComponentFactory.generateStyle(Sandbox.getInstance().getSceneControl().sceneLoader.getRm(), labelComponent.fontName, labelComponent.fontSize));

        facade.sendNotification(Overlap2D.ITEM_PROPERTY_DATA_FINISHED_MODIFYING, entity);
    }

    @Override
    public void undoAction() {

    }
}
