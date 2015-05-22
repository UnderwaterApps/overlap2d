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
import com.esotericsoftware.spine.*;
import com.esotericsoftware.spine.Animation;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.conponents.DimensionsComponent;
import com.uwsoft.editor.renderer.conponents.TextureRegionComponent;
import com.uwsoft.editor.renderer.conponents.spine.SpineDataComponent;
import com.uwsoft.editor.renderer.conponents.sprite.AnimationComponent;
import com.uwsoft.editor.renderer.conponents.sprite.SpriteAnimationComponent;
import com.uwsoft.editor.renderer.conponents.sprite.SpriteAnimationStateComponent;
import com.uwsoft.editor.renderer.legacy.data.MainItemVO;
import com.uwsoft.editor.renderer.legacy.data.SpineVO;
import com.uwsoft.editor.renderer.legacy.data.SpriteAnimationVO;
import com.uwsoft.editor.renderer.resources.IResourceRetriever;

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
        createCommonComponents(root, entity, vo);
        createParentNodeComponent(root, entity);
        createNodeComponent(root, entity);
        createPhysicsComponents(entity, vo);
        createSriteAnimationDataComponent(entity, (SpriteAnimationVO) vo);
    }

    @Override
    protected DimensionsComponent createDimensionsComponent(Entity entity, MainItemVO vo) {
        DimensionsComponent component = new DimensionsComponent();
        component.height = 100;
        component.width = 100;

        entity.add(component);
        return component;
    }

    protected SpriteAnimationComponent createSriteAnimationDataComponent(Entity entity, SpriteAnimationVO vo) {
        SpriteAnimationComponent component = new SpriteAnimationComponent();
        component.animationName = vo.animationName;
        component.animations = vo.animations;
        component.fps = vo.fps;

        DimensionsComponent dimensionsComponent = new DimensionsComponent();
        dimensionsComponent.height = 100;
        dimensionsComponent.width = 100;

        Array<TextureAtlas.AtlasRegion> regions = sortAndGetRegions(component.animationName);

        AnimationComponent animationComponent = new AnimationComponent();
        SpriteAnimationStateComponent stateComponent = new SpriteAnimationStateComponent();

        if (!component.animations.isEmpty()) {
            component.keyFrames = SceneLoader.Frames
                    .constructJsonObject(component.animations);
            for (Map.Entry<String, SceneLoader.Frames> entry : component.keyFrames
                    .entrySet()) {
                SceneLoader.Frames keyFrame = entry.getValue();

                Array<TextureAtlas.AtlasRegion> tmpRegions = new Array<TextureAtlas.AtlasRegion>(keyFrame.endFrame - keyFrame.startFrame + 1);
                for (int r = keyFrame.startFrame; r <= keyFrame.endFrame; r++) {
                    tmpRegions.add(regions.get(r));
                }
                com.badlogic.gdx.graphics.g2d.Animation anim = new com.badlogic.gdx.graphics.g2d.Animation(1f / component.fps,tmpRegions, com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP);
                animationComponent.animations.put(entry.getKey(), anim);
                stateComponent.set(anim);
            }

        } else {
            animationComponent.animations.put("Default", new com.badlogic.gdx.graphics.g2d.Animation(1f / component.fps, regions, com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP));
            // spriteComponent.animation =
            stateComponent.set(animationComponent.animations.get("Default"));
        }

        TextureRegionComponent textureRegionComponent = new TextureRegionComponent();
        textureRegionComponent.region = regions.get(0);

        entity.add(component);

        return component;
    }

    private Array<TextureAtlas.AtlasRegion> sortAndGetRegions(String animationName) {
        Array<TextureAtlas.AtlasRegion> regions = rm.getSpriteAnimation(animationName).getRegions();
        TextureAtlas.AtlasRegion[] animationAtlasRegions = new TextureAtlas.AtlasRegion[regions.size];
        for (int ri = 0; ri < regions.size; ri++) {
            String regName = regions.get(ri).name;
            animationAtlasRegions[regNameToFrame(regName) - 1] = regions.get(ri);
        }
        return new Array<TextureAtlas.AtlasRegion>(animationAtlasRegions);
    }

    private int regNameToFrame(String name) {
        final Pattern lastIntPattern = Pattern.compile("[^0-9]+([0-9]+)$");
        Matcher matcher = lastIntPattern.matcher(name);
        if (matcher.find()) {
            String someNumberStr = matcher.group(1);
            return Integer.parseInt(someNumberStr);
        }
        throw new RuntimeException(
                "Frame name should be something like this '*0001', but not "
                        + name + ".");
    }
}
