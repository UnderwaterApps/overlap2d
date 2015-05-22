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
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.uwsoft.editor.renderer.conponents.*;
import com.uwsoft.editor.renderer.conponents.physics.MeshComponent;
import com.uwsoft.editor.renderer.conponents.physics.PhysicsBodyComponent;
import com.uwsoft.editor.renderer.conponents.physics.PhysicsBodyPropertiesComponent;
import com.uwsoft.editor.renderer.factory.EntityFactory;
import com.uwsoft.editor.renderer.legacy.data.MainItemVO;
import com.uwsoft.editor.renderer.physics.PhysicsBodyLoader;
import com.uwsoft.editor.renderer.resources.IResourceRetriever;

/**
 * Created by azakhary on 5/22/2015.
 */
public abstract class ComponentFactory {

    protected IResourceRetriever rm;
    protected RayHandler rayHandler;
    protected World world;

    protected ComponentMapper<NodeComponent> nodeComponentMapper;

    public ComponentFactory(RayHandler rayHandler, World world, IResourceRetriever rm) {
        this.rayHandler = rayHandler;
        this.world = world;
        this.rm = rm;
        nodeComponentMapper = ComponentMapper.getFor(NodeComponent.class);
    }

    public abstract void createComponents(Entity root, Entity entity, MainItemVO vo);

    protected void createCommonComponents(Entity root, Entity entity, MainItemVO vo) {
        createMainItemComponent(entity, vo);
        createTransformComponent(entity, vo);
        createTintComponent(entity, vo);
        createZIndexComponent(entity, vo);
        createDimensionsComponent(entity, vo);
    }

    protected MainItemComponent createMainItemComponent(Entity entity, MainItemVO vo) {
        MainItemComponent component = new MainItemComponent();
        component.customVars = vo.customVars;
        component.itemIdentifier = vo.itemIdentifier;
        component.itemName = vo.itemName;
        component.tags = vo.tags;

        entity.add(component);

        return component;
    }

    protected TransformComponent createTransformComponent(Entity entity, MainItemVO vo) {
        TransformComponent component = new TransformComponent();
        component.rotation = vo.rotation;
        component.scaleX = vo.scaleX;
        component.scaleY = vo.scaleY;
        component.x = vo.x;
        component.y = vo.y;

        entity.add(component);

        return component;
    }

    protected abstract DimensionsComponent createDimensionsComponent(Entity entity, MainItemVO vo);

    protected TintComponent createTintComponent(Entity entity, MainItemVO vo) {
        TintComponent component = new TintComponent();
        component.color.set(vo.tint[0], vo.tint[1], vo.tint[2], vo.tint[3]);

        entity.add(component);

        return component;
    }

    protected ZindexComponent createZIndexComponent(Entity entity, MainItemVO vo) {
        ZindexComponent component = new ZindexComponent();
        component.layerName = vo.layerName;
        component.zIndex = vo.zIndex;

        entity.add(component);

        return component;
    }

    protected ParentNodeComponent createParentNodeComponent(Entity root, Entity entity) {
        ParentNodeComponent component = new ParentNodeComponent();
        component.parentEntity = root;
        entity.add(component);

        return component;
    }

    protected NodeComponent createNodeComponent(Entity root, Entity entity) {
        NodeComponent component = nodeComponentMapper.get(root);
        component.children.add(entity);

        return component;
    }

    protected void createPhysicsComponents(Entity entity, MainItemVO vo) {
        if(vo.physicsBodyData == null){
            return;
        }
        //TODO: it's better to move mesh logic out of the physics in future

        PhysicsBodyPropertiesComponent physicsBodyPropertiesComponent = createPhysicsBodyPropertiesComponent(entity, vo);
        MeshComponent meshComponent = createMeshComponent(entity, vo);
        createPhysicsBodyComponent(entity, physicsBodyPropertiesComponent, meshComponent);
    }

    protected PhysicsBodyPropertiesComponent createPhysicsBodyPropertiesComponent(Entity entity, MainItemVO vo) {
        PhysicsBodyPropertiesComponent component = new PhysicsBodyPropertiesComponent();
        component.allowSleep = vo.physicsBodyData.allowSleep;
        component.awake = vo.physicsBodyData.awake;
        component.bodyType = vo.physicsBodyData.bodyType;
        component.bullet = vo.physicsBodyData.bullet;
        component.centerOfMass = vo.physicsBodyData.centerOfMass;
        component.damping = vo.physicsBodyData.damping;
        component.density = vo.physicsBodyData.density;
        component.friction = vo.physicsBodyData.friction;
        component.gravityScale = vo.physicsBodyData.gravityScale;
        component.mass = vo.physicsBodyData.mass;
        component.restitution = vo.physicsBodyData.restitution;
        component.rotationalInertia = vo.physicsBodyData.rotationalInertia;

        entity.add(component);

        return component;
    }

    protected PhysicsBodyComponent createPhysicsBodyComponent(Entity entity, PhysicsBodyPropertiesComponent physicsBodyPropertiesComponent, MeshComponent meshComponent) {
		PhysicsBodyComponent component = new PhysicsBodyComponent();
        component.body = PhysicsBodyLoader.createBody(world, physicsBodyPropertiesComponent, meshComponent.minPolygonData, new Vector2(1, 1)); //TODO resolution thing

        entity.add(component);

        return component;
    }

    protected MeshComponent createMeshComponent(Entity entity, MainItemVO vo) {
        MeshComponent component = new MeshComponent();
        component.meshId = vo.meshId;
        component.minPolygonData = rm.getProjectVO().meshes.get(vo.meshId).minPolygonData;

        entity.add(component);

        return component;
    }

    public void setResourceManager(IResourceRetriever rm) {
        this.rm = rm;
    }

}
