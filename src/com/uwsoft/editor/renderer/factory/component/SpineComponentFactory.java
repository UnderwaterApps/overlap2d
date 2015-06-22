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
import com.badlogic.gdx.physics.box2d.World;
import com.esotericsoftware.spine.*;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.spine.SpineDataComponent;
import com.uwsoft.editor.renderer.data.MainItemVO;
import com.uwsoft.editor.renderer.data.SpineVO;
import com.uwsoft.editor.renderer.factory.EntityFactory;
import com.uwsoft.editor.renderer.resources.IResourceRetriever;

/**
 * Created by azakhary on 5/22/2015.
 */
public class SpineComponentFactory extends ComponentFactory {

    public SpineComponentFactory(RayHandler rayHandler, World world, IResourceRetriever rm) {
        super(rayHandler, world, rm);
    }

    @Override
    public void createComponents(Entity root, Entity entity, MainItemVO vo) {
        createCommonComponents(entity, vo, EntityFactory.SPINE_TYPE);
        createParentNodeComponent(root, entity);
        createNodeComponent(root, entity);
        createPhysicsComponents(entity, vo);
        createSpineDataComponent(entity, (SpineVO) vo);
    }

    @Override
    protected DimensionsComponent createDimensionsComponent(Entity entity, MainItemVO vo) {
        DimensionsComponent component = new DimensionsComponent();
        component.height = 100;
        component.width = 100;

        entity.add(component);
        return component;
    }

    protected SpineDataComponent createSpineDataComponent(Entity entity, SpineVO vo) {
        SpineDataComponent component = new SpineDataComponent();
        component.animationName = vo.animationName;
        component.skeletonJson = new SkeletonJson(rm.getSkeletonAtlas(component.animationName));
        component.skeletonData = component.skeletonJson.readSkeletonData((rm.getSkeletonJSON(component.animationName)));

        BoneData rootBone = component.skeletonData.getBones().get(0); // this has to be the root bone.
        rootBone.setScale(vo.scaleX, vo.scaleY); //TODO resolution part
        component.skeleton = new Skeleton(component.skeletonData); // Skeleton holds skeleton state (bone positions, slot attachments, etc).
        AnimationStateData stateData = new AnimationStateData(component.skeletonData); // Defines mixing (crossfading) between animations.
        component.state = new AnimationState(stateData); // Holds the animation state for a skeleton (current animation, time, etc).

        //TODO: this can be done better, just don't have time right now
        DimensionsComponent dimensionsComponent = entity.getComponent(DimensionsComponent.class);

        component.computeBoundBox(dimensionsComponent);
        // todo: fix this, it's a temporary solution
        component.setAnimation(component.currentAnimationName.isEmpty() ? component.skeletonData.getAnimations().get(0).getName() : component.currentAnimationName);

        entity.add(component);

        return component;
    }
}
