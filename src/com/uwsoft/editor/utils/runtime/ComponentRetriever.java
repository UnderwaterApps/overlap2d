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

package com.uwsoft.editor.utils.runtime;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.renderer.conponents.*;
import com.uwsoft.editor.renderer.conponents.particle.ParticleCompononet;
import com.uwsoft.editor.renderer.conponents.physics.MeshComponont;
import com.uwsoft.editor.renderer.conponents.physics.PhysicsBodyComponent;
import com.uwsoft.editor.renderer.conponents.physics.PhysicsBodyPropertiesComponent;
import com.uwsoft.editor.renderer.conponents.spine.SpineDataComponent;
import com.uwsoft.editor.renderer.conponents.spine.SpineObjectComponent;
import com.uwsoft.editor.renderer.conponents.sprite.AnimationComponent;
import com.uwsoft.editor.renderer.conponents.sprite.SpriteAnimationComponent;
import com.uwsoft.editor.renderer.conponents.sprite.SpriteAnimationStateComponent;
import com.uwsoft.editor.renderer.conponents.spriter.SpriterComponent;
import com.uwsoft.editor.renderer.conponents.spriter.SpriterDrawerComponent;

import java.util.HashMap;
import java.util.Map;

/**
 * Component Retriever is a singleton single instance class that initialises list of
 * all component mappers on first access, and provides a retrieval methods to get {@link Component}
 * with provided class from provided {@link Entity} object
 *
 * @author azakhary on 5/19/2015.
 */
public class ComponentRetriever {

    /**
     * single static instance of this class
     */
    private static ComponentRetriever instance;

    /**
     * Unique map of mappers that can be accessed by component class
     */
    private Map<Class, ComponentMapper<? extends Component>> mappers = new HashMap<>();

    /**
     * Private constructor
     */
    private ComponentRetriever() {

    }

    /**
     * This is called only during first initialisation and populates map of mappers of all known Component mappers
     * it might be a good idea to use Reflections library later to create this list from all classes in components package of runtime, all in favour?
     */
    private void init() {
        mappers.put(ViewPortComponent.class, ComponentMapper.getFor(ViewPortComponent.class));
        mappers.put(CompositeTransformComponent.class, ComponentMapper.getFor(CompositeTransformComponent.class));
        mappers.put(NodeComponent.class, ComponentMapper.getFor(NodeComponent.class));
        mappers.put(TransformComponent.class, ComponentMapper.getFor(TransformComponent.class));
        mappers.put(TextureRegionComponent.class, ComponentMapper.getFor(TextureRegionComponent.class));
        mappers.put(ParticleCompononet.class, ComponentMapper.getFor(ParticleCompononet.class));
        mappers.put(SpriterDrawerComponent.class, ComponentMapper.getFor(SpriterDrawerComponent.class));
        mappers.put(SpriterComponent.class, ComponentMapper.getFor(SpriterComponent.class));
        mappers.put(SpineDataComponent.class, ComponentMapper.getFor(SpineDataComponent.class));
        mappers.put(MeshComponont.class, ComponentMapper.getFor(MeshComponont.class));
        mappers.put(PhysicsBodyComponent.class, ComponentMapper.getFor(PhysicsBodyComponent.class));
        mappers.put(PhysicsBodyPropertiesComponent.class, ComponentMapper.getFor(PhysicsBodyPropertiesComponent.class));
        mappers.put(SpineObjectComponent.class, ComponentMapper.getFor(SpineObjectComponent.class));
        mappers.put(AnimationComponent.class, ComponentMapper.getFor(AnimationComponent.class));
        mappers.put(AnimationComponent.class, ComponentMapper.getFor(AnimationComponent.class));
        mappers.put(SpriteAnimationComponent.class, ComponentMapper.getFor(SpriteAnimationComponent.class));
        mappers.put(SpriteAnimationStateComponent.class, ComponentMapper.getFor(SpriteAnimationStateComponent.class));
        mappers.put(DimensionsComponent.class, ComponentMapper.getFor(DimensionsComponent.class));
        mappers.put(LayerMapComponent.class, ComponentMapper.getFor(LayerMapComponent.class));
        mappers.put(NinePatchComponnent.class, ComponentMapper.getFor(NinePatchComponnent.class));
        mappers.put(ParentNodeComponent.class, ComponentMapper.getFor(ParentNodeComponent.class));
        mappers.put(ScissorComponent.class, ComponentMapper.getFor(ScissorComponent.class));
        mappers.put(TintComponent.class, ComponentMapper.getFor(TintComponent.class));
        mappers.put(ZindexComponent.class, ComponentMapper.getFor(ZindexComponent.class));
    }

    /**
     * Short version of getInstance singleton variation, but with private access,
     * as there is no reason to get instance of this class, but only use it's public methods
     *
     * @return ComponentRetriever only instance
     */
    private static synchronized ComponentRetriever self() {
        if(instance == null) {
            instance = new ComponentRetriever();

            // Important to initialize during first creation, to populate mappers map
            instance.init();
        }

        return instance;
    }

    /**
     * @return returns Map of mappers, for internal use only
     */
    private Map<Class, ComponentMapper<? extends Component>> getMappers() {
        return mappers;
    }

    /**
     * Retrieves Component of provided type from a provided entity
     * @param entity of type Entity to retrieve component from
     * @param type of the component
     * @param <T>
     *
     * @return Component subclass instance
     */
    @SuppressWarnings("unchecked")
    public static <T extends Component> T get(Entity entity, Class<T> type) {
        ComponentRetriever retriever  = self();
        return (T)retriever.getMappers().get(type).get(entity);
    }

    /**
     * This is to add a new mapper type externally, in case of for example implementing the plugin system, where components might be initialized on the fly
     *
     * @param type
     */
    @SuppressWarnings("unchecked")
    public static void addMapper(Class type) {
        self().getMappers().put(ZindexComponent.class, ComponentMapper.getFor(type));
    }
}
