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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.components.*;
import com.uwsoft.editor.renderer.data.CompositeVO;
import com.uwsoft.editor.renderer.data.LayerItemVO;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.view.stage.Sandbox;

/**
 * Created by azakhary on 6/9/2015.
 */
public class EntityUtils {

    public static Integer getEntityId(Entity entity) {
        MainItemComponent mainItemComponent = ComponentRetriever.get(entity, MainItemComponent.class);
        return mainItemComponent.uniqueId;
    }

    public static Array<Integer> getEntityId(Iterable<Entity> entities) {
        Array<Integer> entityIds = new Array<>();
        int iterator = 0;
        for(Entity entity: entities) {
            MainItemComponent mainItemComponent = ComponentRetriever.get(entity, MainItemComponent.class);
            entityIds.add(mainItemComponent.uniqueId);
        }

        return entityIds;
    }

    public static Entity getByUniqueId(Integer id) {
        return Sandbox.getInstance().getSceneControl().sceneLoader.entityFactory.getEntityByUniqueId(id);
    }
    public static HashSet<Entity> getByUniqueId(Array<Integer> ids) {
        HashSet<Entity> entities = new HashSet<>();
        for(Integer id: ids) {
            Entity entity = Sandbox.getInstance().getSceneControl().sceneLoader.entityFactory.getEntityByUniqueId(id);
            entities.add(entity);
        }
        return entities;
    }

    public static HashMap<Integer, Collection<Component>> cloneEntities(Set<Entity> entities) {
        HashMap<Integer, Collection<Component>> data = new HashMap<>();

        for(Entity entity: entities) {
            Collection<Component> components = cloneEntityComponents(entity);
            data.put(EntityUtils.getEntityId(entity), components);
        }

        return data;
    }

    public static Entity cloneEntity(Entity entity) {
        Entity newEntity = new Entity();
        Collection<Component> components = cloneEntityComponents(entity);
        for(Component component: components) {
            newEntity.add(ComponentCloner.get(component));
        }

        return newEntity;
    }

    public static Collection<Component> cloneEntityComponents(Entity entity) {
        Collection<Component> components = ComponentCloner.cloneAll(ComponentRetriever.getComponents(entity));
        return components;
    }

    public static Vector2 getPosition(Entity entity) {
        TransformComponent transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
        return new Vector2(transformComponent.x, transformComponent.y);
    }

    public static Vector2 getRightTopPoint(Set<Entity> entities) {
        if(entities.size() == 0) return null;

        Vector2 rightTopPoint = getPosition(entities.stream().findFirst().get());

        for(Entity entity: entities) {
            TransformComponent transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
            DimensionsComponent dimensionsComponent = ComponentRetriever.get(entity, DimensionsComponent.class);

            if(rightTopPoint.x < transformComponent.x+dimensionsComponent.width) {
                rightTopPoint.x = transformComponent.x+dimensionsComponent.width;
            }
            if(rightTopPoint.y < transformComponent.y+dimensionsComponent.height) {
                rightTopPoint.y = transformComponent.y+dimensionsComponent.height;
            }
        }

        return rightTopPoint;
    }

    public static Vector2 getLeftBottomPoint(Set<Entity> entities) {
        if(entities.size() == 0) return null;

        Vector2 leftBottomPoint = getPosition(entities.stream().findFirst().get());

        for(Entity entity: entities) {
            TransformComponent transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
            if(leftBottomPoint.x > transformComponent.x) {
                leftBottomPoint.x = transformComponent.x;
            }
            if(leftBottomPoint.y > transformComponent.y) {
                leftBottomPoint.y = transformComponent.y;
            }
        }

        return leftBottomPoint;
    }

    public static void changeParent(HashSet<Entity> entities, Entity parent) {
        for(Entity entity: entities) {
            ParentNodeComponent parentNodeComponent = ComponentRetriever.get(entity, ParentNodeComponent.class);

            //remove me from previous parent children list
            NodeComponent nodeComponent = ComponentRetriever.get(parentNodeComponent.parentEntity, NodeComponent.class);
            nodeComponent.children.removeValue(entity, true);

            //add me to new parent child list
            NodeComponent rootNodeComponent = ComponentRetriever.get(parent, NodeComponent.class);
            rootNodeComponent.children.add(entity);

            //change my parent
            parentNodeComponent.parentEntity = parent;
        }
    }

    public static HashSet<Entity> getChildren(Entity entity) {
        HashSet<Entity> entities;
        NodeComponent nodeComponent = ComponentRetriever.get(entity, NodeComponent.class);
        Entity[] children = nodeComponent.children.toArray();
        entities = new HashSet<>(Arrays.asList(children));

        return entities;
    }

    public static int getType(Entity entity) {
        MainItemComponent mainItemComponent = ComponentRetriever.get(entity, MainItemComponent.class);
        return mainItemComponent.entityType;
    }

    public static Array<Entity> getByLibraryLink(String link) {
        Array<Entity> result = new Array<>();
        ImmutableArray<Entity> composites = Sandbox.getInstance().getEngine().getEntitiesFor(Family.all(NodeComponent.class).get());
        for(Entity composite: composites) {
            MainItemComponent mainItemComponent = ComponentRetriever.get(composite, MainItemComponent.class);
            if(mainItemComponent.libraryLink.equals(link)) {
                result.add(composite);
            }
        }

        return result;
    }

    public static void reInstantiateChildren(Entity entity) {
        NodeComponent nodeComponent = ComponentRetriever.get(entity, NodeComponent.class);
        if (nodeComponent != null) {
            CompositeVO compositeVo = new CompositeVO();
            compositeVo.loadFromEntity(entity);

            entity.remove(NodeComponent.class);
            entity.add(new NodeComponent());

            SceneLoader sceneLoader = Sandbox.getInstance().getSceneControl().sceneLoader;
            sceneLoader.entityFactory.initAllChildren(Sandbox.getInstance().getEngine(), entity, compositeVo);
        }
    }

    public static LayerItemVO getEntityLayer(Entity entity) {

        ZIndexComponent zIndexComponent = ComponentRetriever.get(entity, ZIndexComponent.class);
        LayerMapComponent layerMapComponent = ComponentRetriever.get(entity.getComponent(ParentNodeComponent.class).parentEntity, LayerMapComponent.class);

        return layerMapComponent.getLayer(zIndexComponent.layerName);
    }
}
