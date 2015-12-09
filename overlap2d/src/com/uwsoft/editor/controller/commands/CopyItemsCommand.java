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

import java.util.Set;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.uwsoft.editor.renderer.components.MainItemComponent;
import com.uwsoft.editor.renderer.data.*;
import com.uwsoft.editor.renderer.factory.EntityFactory;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.view.stage.Sandbox;

/**
 * Created by azakhary on 4/28/2015.
 */
public class CopyItemsCommand extends RevertableCommand {

    @Override
    public void doAction() {
        String data = getJsonStringFromEntities(sandbox.getSelector().getSelectedItems());

        Object[] payload = new Object[2];
        payload[0] = new Vector2(Sandbox.getInstance().getCamera().position.x,Sandbox.getInstance().getCamera().position.y);
        payload[1] = data;
        Sandbox.getInstance().copyToClipboard(payload);
    }

    @Override
    public void undoAction() {
        // we do not restore clipboard at this time, as it is not a string but a reference, which honestly sucks ass
    }

    public static String getJsonStringFromEntities(Set<Entity> entities) {
        CompositeVO holderComposite = new CompositeVO();
        for(Entity entity : entities) {
            int entityType = ComponentRetriever.get(entity, MainItemComponent.class).entityType;
            if(entityType == EntityFactory.COMPOSITE_TYPE) {
                CompositeItemVO vo = new CompositeItemVO();
                vo.loadFromEntity(entity);
                holderComposite.sComposites.add(vo);
            }
            if(entityType == EntityFactory.IMAGE_TYPE) {
                SimpleImageVO vo = new SimpleImageVO();
                vo.loadFromEntity(entity);
                holderComposite.sImages.add(vo);
            }
            if(entityType == EntityFactory.NINE_PATCH) {
                Image9patchVO vo = new Image9patchVO();
                vo.loadFromEntity(entity);
                holderComposite.sImage9patchs.add(vo);
            }
            if(entityType == EntityFactory.LABEL_TYPE) {
                LabelVO vo = new LabelVO();
                vo.loadFromEntity(entity);
                holderComposite.sLabels.add(vo);
            }
            if(entityType == EntityFactory.PARTICLE_TYPE) {
                ParticleEffectVO vo = new ParticleEffectVO();
                vo.loadFromEntity(entity);
                holderComposite.sParticleEffects.add(vo);
            }
            if(entityType == EntityFactory.SPRITE_TYPE) {
                SpriteAnimationVO vo = new SpriteAnimationVO();
                vo.loadFromEntity(entity);
                holderComposite.sSpriteAnimations.add(vo);
            }
            if(entityType == EntityFactory.SPRITER_TYPE) {
                SpriterVO vo = new SpriterVO();
                vo.loadFromEntity(entity);
                holderComposite.sSpriterAnimations.add(vo);
            }
            if(entityType == EntityFactory.SPINE_TYPE) {
                SpineVO vo = new SpineVO();
                vo.loadFromEntity(entity);
                holderComposite.sSpineAnimations.add(vo);
            }
            if(entityType == EntityFactory.COLOR_PRIMITIVE) {
                ColorPrimitiveVO vo = new ColorPrimitiveVO();
                vo.loadFromEntity(entity);
                holderComposite.sColorPrimitives.add(vo);
            }
            if(entityType == EntityFactory.LIGHT_TYPE) {
                LightVO vo = new LightVO();
                vo.loadFromEntity(entity);
                holderComposite.sLights.add(vo);
            }
        }

        Json json = new Json();
        String result = json.toJson(holderComposite);

        return result;
    }
}
