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

package com.overlap2d.extensions.spine;

import box2dLight.RayHandler;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.World;
import com.esotericsoftware.spine.*;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.SpineDataComponent;
import com.uwsoft.editor.renderer.data.MainItemVO;
import com.uwsoft.editor.renderer.data.ProjectInfoVO;
import com.uwsoft.editor.renderer.data.SpineVO;
import com.uwsoft.editor.renderer.factory.EntityFactory;
import com.uwsoft.editor.renderer.factory.component.ComponentFactory;
import com.uwsoft.editor.renderer.resources.IResourceRetriever;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

/**
 * Created by azakhary on 5/22/2015.
 */
public class SpineComponentFactory extends ComponentFactory {

    private SpineObjectComponent spineObjectComponent;

    public SpineComponentFactory() {
        super();
    }

    public SpineComponentFactory(RayHandler rayHandler, World world, IResourceRetriever rm) {
        super(rayHandler, world, rm);
    }

    @Override
    public void createComponents(Entity root, Entity entity, MainItemVO vo) {
        createCommonComponents(entity, vo, EntityFactory.SPINE_TYPE);
        createParentNodeComponent(root, entity);
        createNodeComponent(root, entity);
        createPhysicsComponents(entity, vo);
        spineObjectComponent = createSpineObjectComponent(entity, (SpineVO) vo);
        createSpineDataComponent(entity, (SpineVO) vo);
    }

    @Override
    protected DimensionsComponent createDimensionsComponent(Entity entity, MainItemVO vo) {
        DimensionsComponent component = new DimensionsComponent();

        entity.add(component);
        return component;
    }

    protected SpineObjectComponent createSpineObjectComponent(Entity entity, SpineVO vo) {
        ProjectInfoVO projectInfoVO = rm.getProjectVO();

        SpineObjectComponent component = new SpineObjectComponent();
        component.skeletonJson = new SkeletonJson(rm.getSkeletonAtlas(vo.animationName));
        component.skeletonData = component.skeletonJson.readSkeletonData((rm.getSkeletonJSON(vo.animationName)));

        BoneData rootBone = component.skeletonData.getBones().get(0); // this has to be the root bone.
        rootBone.setScale(vo.scaleX / projectInfoVO.pixelToWorld, vo.scaleY / projectInfoVO.pixelToWorld); // TODO: resolution part and multipliers
        component.skeleton = new Skeleton(component.skeletonData); // Skeleton holds skeleton state (bone positions, slot attachments, etc).
        AnimationStateData stateData = new AnimationStateData(component.skeletonData); // Defines mixing (crossfading) between animations.
        component.state = new AnimationState(stateData); // Holds the animation state for a skeleton (current animation, time, etc).

        DimensionsComponent dimensionsComponent = ComponentRetriever.get(entity, DimensionsComponent.class);
        component.computeBoundBox(dimensionsComponent);

        component.setAnimation(vo.currentAnimationName.isEmpty() ? component.skeletonData.getAnimations().get(0).getName() : vo.currentAnimationName);

        entity.add(component);

        return component;
    }

    protected SpineDataComponent createSpineDataComponent(Entity entity, SpineVO vo) {
        SpineDataComponent component = new SpineDataComponent();
        component.animationName = vo.animationName;

        component.currentAnimationName = vo.currentAnimationName.isEmpty() ? spineObjectComponent.skeletonData.getAnimations().get(0).getName() : vo.currentAnimationName;

        entity.add(component);

        return component;
    }
}
