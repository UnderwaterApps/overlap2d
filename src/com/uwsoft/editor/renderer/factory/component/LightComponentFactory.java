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
import com.badlogic.gdx.physics.box2d.World;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.light.LightObjectComponent;
import com.uwsoft.editor.renderer.legacy.data.LightVO;
import com.uwsoft.editor.renderer.legacy.data.MainItemVO;
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
        createCommonComponents(entity, vo);
        createParentNodeComponent(root, entity);
        createNodeComponent(root, entity);
        createPhysicsComponents(entity, vo);
        createLightObjectComponent(entity, (LightVO) vo);
    }

    @Override
    protected DimensionsComponent createDimensionsComponent(Entity entity, MainItemVO vo) {
        DimensionsComponent component = new DimensionsComponent();
        component.height = 40;
        component.width = 40;

        entity.add(component);
        return component;
    }

    protected LightObjectComponent createLightObjectComponent(Entity entity, LightVO vo) {
        LightObjectComponent component = new LightObjectComponent();
        component.coneDegree = vo.coneDegree;
        component.directionDegree = vo.directionDegree;
        component.distance = vo.distance;
        component.isStatic = vo.isStatic;
        component.isXRay = vo.isXRay;
        component.rays = vo.rays;
        component.type = vo.type;

        if (component.type == LightVO.LightType.POINT) {
            component.lightObject = new PointLight(rayHandler, component.rays);
            component.lightObject.setColor(new Color(vo.tint[0], vo.tint[1], vo.tint[2], vo.tint[3]));
            // TODO Physics and resolution part
            component.lightObject.setDistance(component.distance * PhysicsBodyLoader.SCALE);
            component.lightObject.setPosition(vo.x * PhysicsBodyLoader.SCALE, vo.y * PhysicsBodyLoader.SCALE);
            component.lightObject.setStaticLight(component.isStatic);
            component.lightObject.setActive(true);
            component.lightObject.setXray(component.isXRay);

        } else {
            component.lightObject = new ConeLight(rayHandler, component.rays, Color.WHITE, 1, 0, 0, 0, 0);
            // Color asd = new Color(vo.tint);
            component.lightObject.setColor(new Color(vo.tint[0], vo.tint[1], vo.tint[2], vo.tint[3]));
            component.lightObject.setDistance(component.distance * PhysicsBodyLoader.SCALE);
            component.lightObject.setPosition(vo.x * PhysicsBodyLoader.SCALE, vo.y * PhysicsBodyLoader.SCALE);
            component.lightObject.setStaticLight(component.isStatic);
            component.lightObject.setDirection(component.directionDegree);
            ((ConeLight) component.lightObject).setConeDegree(component.coneDegree);
            component.lightObject.setActive(true);
            component.lightObject.setXray(component.isXRay);
        }

        entity.add(component);
        return component;
    }
}
