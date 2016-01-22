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
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.TextureRegionComponent;
import com.uwsoft.editor.renderer.components.sprite.AnimationComponent;
import com.uwsoft.editor.renderer.components.sprite.SpriteAnimationComponent;
import com.uwsoft.editor.renderer.components.sprite.SpriteAnimationStateComponent;
import com.uwsoft.editor.renderer.data.*;
import com.uwsoft.editor.renderer.factory.EntityFactory;
import com.uwsoft.editor.renderer.resources.IResourceRetriever;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by azakhary on 5/22/2015.
 */
public class SpriteComponentFactory extends ComponentFactory {

    public SpriteComponentFactory(RayHandler rayHandler, World world, IResourceRetriever rm) {
        super(rayHandler, world, rm);
    }

    @Override
    public void createComponents(Entity root, Entity entity, MainItemVO vo) {
        createCommonComponents(entity, vo, EntityFactory.SPRITE_TYPE);
        createParentNodeComponent(root, entity);
        createNodeComponent(root, entity);
        createSpriteAnimationDataComponent(entity, (SpriteAnimationVO) vo);
    }

    @Override
    protected DimensionsComponent createDimensionsComponent(Entity entity, MainItemVO vo) {
        DimensionsComponent component = new DimensionsComponent();

        SpriteAnimationVO sVo = (SpriteAnimationVO) vo;
        Array<TextureAtlas.AtlasRegion> regions = rm.getSpriteAnimation(sVo.animationName).getRegions();

        ResolutionEntryVO resolutionEntryVO = rm.getLoadedResolution();
        ProjectInfoVO projectInfoVO = rm.getProjectVO();
        float multiplier = resolutionEntryVO.getMultiplier(rm.getProjectVO().originalResolution);
        component.width = (float) regions.get(0).getRegionWidth() * multiplier / projectInfoVO.pixelToWorld;
        component.height = (float) regions.get(0).getRegionHeight() * multiplier / projectInfoVO.pixelToWorld;

        entity.add(component);
        return component;
    }

    protected SpriteAnimationComponent createSpriteAnimationDataComponent(Entity entity, SpriteAnimationVO vo) {
        SpriteAnimationComponent spriteAnimationComponent = new SpriteAnimationComponent();
        spriteAnimationComponent.animationName = vo.animationName;

        spriteAnimationComponent.frameRangeMap = new HashMap<String, FrameRange>();
        for(int i = 0; i < vo.frameRangeMap.size(); i++) {
            spriteAnimationComponent.frameRangeMap.put(vo.frameRangeMap.get(i).name, vo.frameRangeMap.get(i));
        }
        spriteAnimationComponent.fps = vo.fps;
        spriteAnimationComponent.currentAnimation = vo.currentAnimation;

        if(vo.playMode == 0) spriteAnimationComponent.playMode = Animation.PlayMode.NORMAL;
        if(vo.playMode == 1) spriteAnimationComponent.playMode = Animation.PlayMode.REVERSED;
        if(vo.playMode == 2) spriteAnimationComponent.playMode = Animation.PlayMode.LOOP;
        if(vo.playMode == 3) spriteAnimationComponent.playMode = Animation.PlayMode.LOOP_REVERSED;
        if(vo.playMode == 4) spriteAnimationComponent.playMode = Animation.PlayMode.LOOP_PINGPONG;
        if(vo.playMode == 5) spriteAnimationComponent.playMode = Animation.PlayMode.LOOP_RANDOM;
        if(vo.playMode == 6) spriteAnimationComponent.playMode = Animation.PlayMode.NORMAL;

        Array<TextureAtlas.AtlasRegion> regions = rm.getSpriteAnimation(spriteAnimationComponent.animationName).getRegions();

        AnimationComponent animationComponent = new AnimationComponent();
        SpriteAnimationStateComponent stateComponent = new SpriteAnimationStateComponent(regions);

        if(spriteAnimationComponent.frameRangeMap.isEmpty()) {
            spriteAnimationComponent.frameRangeMap.put("Default", new FrameRange("Default", 0, regions.size-1));
        }
        if(spriteAnimationComponent.currentAnimation == null) {
            spriteAnimationComponent.currentAnimation = (String) spriteAnimationComponent.frameRangeMap.keySet().toArray()[0];
        }
        if(spriteAnimationComponent.playMode == null) {
            spriteAnimationComponent.playMode = Animation.PlayMode.LOOP;
        }

        stateComponent.set(spriteAnimationComponent);

        TextureRegionComponent textureRegionComponent = new TextureRegionComponent();
        textureRegionComponent.region = regions.get(0);
        
        entity.add(textureRegionComponent);
        entity.add(stateComponent);
        entity.add(animationComponent);
        entity.add(spriteAnimationComponent);

        return spriteAnimationComponent;
    }
}
