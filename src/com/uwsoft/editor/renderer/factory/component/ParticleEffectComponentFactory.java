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

package com.uwsoft.editor.renderer.factory.component;

import box2dLight.RayHandler;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.particle.ParticleComponent;
import com.uwsoft.editor.renderer.data.MainItemVO;
import com.uwsoft.editor.renderer.data.ParticleEffectVO;
import com.uwsoft.editor.renderer.data.ProjectInfoVO;
import com.uwsoft.editor.renderer.data.ResolutionEntryVO;
import com.uwsoft.editor.renderer.factory.EntityFactory;
import com.uwsoft.editor.renderer.resources.IResourceRetriever;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

/**
 * Created by azakhary on 5/22/2015.
 */
public class ParticleEffectComponentFactory extends ComponentFactory {


    public ParticleEffectComponentFactory(RayHandler rayHandler, World world, IResourceRetriever rm) {
        super(rayHandler, world, rm);
    }

    @Override
    public void createComponents(Entity root, Entity entity, MainItemVO vo) {
        createCommonComponents(entity, vo, EntityFactory.PARTICLE_TYPE);
        createParentNodeComponent(root, entity);
        createNodeComponent(root, entity);
        createParticleComponent(entity, (ParticleEffectVO) vo);
    }

    @Override
    protected DimensionsComponent createDimensionsComponent(Entity entity, MainItemVO vo) {
        DimensionsComponent component = new DimensionsComponent();

        ProjectInfoVO projectInfoVO = rm.getProjectVO();
        float boundBoxSize = 70f;
        component.boundBox = new Rectangle((-boundBoxSize/2f)/projectInfoVO.pixelToWorld, (-boundBoxSize/2f)/projectInfoVO.pixelToWorld, boundBoxSize/projectInfoVO.pixelToWorld, boundBoxSize/projectInfoVO.pixelToWorld);

        entity.add(component);
        return component;
    }

    protected ParticleComponent createParticleComponent(Entity entity, ParticleEffectVO vo) {
        ParticleComponent component = new ParticleComponent();
        component.particleName = vo.particleName;
		ParticleEffect particleEffect = new ParticleEffect(rm.getParticleEffect(vo.particleName));
        component.particleEffect = particleEffect;

        ProjectInfoVO projectInfoVO = rm.getProjectVO();

        component.worldMultiplyer = 1f/projectInfoVO.pixelToWorld;

        entity.add(component);
        return component;
    }
}
