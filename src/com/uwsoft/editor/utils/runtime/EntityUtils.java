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
import com.badlogic.gdx.utils.Array;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.renderer.components.MainItemComponent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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


}
