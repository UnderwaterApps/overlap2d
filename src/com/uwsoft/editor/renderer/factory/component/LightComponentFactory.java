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

import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.light.LightObjectComponent;
import com.uwsoft.editor.renderer.data.LightVO;
import com.uwsoft.editor.renderer.data.MainItemVO;
import com.uwsoft.editor.renderer.data.ProjectInfoVO;
import com.uwsoft.editor.renderer.factory.EntityFactory;
import com.uwsoft.editor.renderer.physics.PhysicsBodyLoader;
import com.uwsoft.editor.renderer.resources.IResourceRetriever;

/**
 * Created by azakhary on 5/22/2015.
 */
public class LightComponentFactory extends ComponentFactory {

    public LightComponentFactory(RayHandler rayHandler, World world, IResourceRetriever rm) {
        super(rayHandler, world, rm);
    }

    @Override
    public void createComponents(Entity root, Entity entity, MainItemVO vo) {
        createCommonComponents(entity, vo, EntityFactory.LIGHT_TYPE);
        createParentNodeComponent(root, entity);
        createNodeComponent(root, entity);
        createLightObjectComponent(entity, (LightVO) vo);
    }

    @Override
    protected DimensionsComponent createDimensionsComponent(Entity entity, MainItemVO vo) {
        DimensionsComponent component = new DimensionsComponent();
        ProjectInfoVO projectInfoVO = rm.getProjectVO();
        component.boundBox = new Rectangle(-10f/projectInfoVO.pixelToWorld, -10f/projectInfoVO.pixelToWorld, 20f/projectInfoVO.pixelToWorld, 20f/projectInfoVO.pixelToWorld);

        entity.add(component);
        return component;
    }

    protected LightObjectComponent createLightObjectComponent(Entity entity, LightVO vo) {
        if(vo.softnessLength == -1f) {
            vo.softnessLength = vo.distance * 0.1f * PhysicsBodyLoader.getScale();
        }
        LightObjectComponent component = new LightObjectComponent(vo.type);
        component.coneDegree = vo.coneDegree;
        component.directionDegree = vo.directionDegree;
        component.distance = vo.distance;
        component.softnessLength = vo.softnessLength;
        component.isStatic = vo.isStatic;
        component.isXRay = vo.isXRay;
        component.rays = vo.rays;

        if (component.getType() == LightVO.LightType.POINT) {
            component.lightObject = new PointLight(rayHandler, component.rays);
        } else {
            component.lightObject = new ConeLight(rayHandler, component.rays, Color.WHITE, 1, 0, 0, 0, 0);
        }
        
        component.lightObject.setSoftnessLength(component.softnessLength);

        entity.add(component);
        return component;
    }
}
