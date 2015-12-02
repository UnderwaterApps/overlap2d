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

package com.uwsoft.editor.controller.commands.component;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.controller.commands.EntityModifyRevertableCommand;
import com.uwsoft.editor.renderer.components.light.LightObjectComponent;
import com.uwsoft.editor.renderer.data.LightVO;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.utils.runtime.EntityUtils;

/**
 * Created by azakhary on 6/16/2015.
 */
public class UpdateLightDataCommand extends EntityModifyRevertableCommand {

    private Integer enitityId;
    private LightVO backup;

    @Override
    public void doAction() {
        Object[] payload = getNotification().getBody();
        Entity entity = (Entity) payload[0];
        LightVO vo = (LightVO) payload[1];

        backup = new LightVO();
        backup.loadFromEntity(entity);

        LightObjectComponent lightObjectComponent = ComponentRetriever.get(entity, LightObjectComponent.class);

        lightObjectComponent.rays = vo.rays;
        lightObjectComponent.isStatic = vo.isStatic;
        lightObjectComponent.isXRay = vo.isXRay;
        lightObjectComponent.coneDegree = vo.coneDegree;
        lightObjectComponent.distance = vo.distance;
        lightObjectComponent.softnessLength = vo.softnessLength;
        lightObjectComponent.directionDegree = vo.directionDegree;
        
    }

    @Override
    public void undoAction() {
        Entity entity = EntityUtils.getByUniqueId(enitityId);
        LightObjectComponent lightObjectComponent = ComponentRetriever.get(entity, LightObjectComponent.class);

        lightObjectComponent.rays = backup.rays;
        lightObjectComponent.isStatic = backup.isStatic;
        lightObjectComponent.isXRay = backup.isXRay;
        lightObjectComponent.coneDegree = backup.coneDegree;
        lightObjectComponent.distance = backup.distance;
        lightObjectComponent.softnessLength  = backup.softnessLength;
        lightObjectComponent.directionDegree = backup.directionDegree;
    }

    public static Object payload(Entity entity, LightVO vo) {
        Object[] payload = new Object[2];
        payload[0] = entity;
        payload[1] = vo;

        return payload;
    }
}
