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
import com.uwsoft.editor.renderer.components.CompositeTransformComponent;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.LayerMapComponent;
import com.uwsoft.editor.renderer.components.NodeComponent;
import com.uwsoft.editor.renderer.data.CompositeItemVO;
import com.uwsoft.editor.renderer.data.CompositeVO;
import com.uwsoft.editor.renderer.data.LayerItemVO;
import com.uwsoft.editor.renderer.data.MainItemVO;
import com.uwsoft.editor.renderer.factory.EntityFactory;
import com.uwsoft.editor.renderer.resources.IResourceRetriever;

/**
 * Created by azakhary on 5/22/2015.
 */
public class CompositeComponentFactory extends ComponentFactory {

    public CompositeComponentFactory(RayHandler rayHandler, World world, IResourceRetriever rm) {
        super(rayHandler, world, rm);
    }

    @Override
    public void createComponents(Entity root, Entity entity, MainItemVO vo) {
        createCommonComponents(entity, vo, EntityFactory.COMPOSITE_TYPE);
        if(root != null) {
            createParentNodeComponent(root, entity);
        }
        createNodeComponent(root, entity);
        createPhysicsComponents(entity, vo);
        createCompositeComponents(entity, (CompositeItemVO) vo);
    }

    @Override
    protected DimensionsComponent createDimensionsComponent(Entity entity, MainItemVO vo) {
        DimensionsComponent component = new DimensionsComponent();
        component.width = ((CompositeItemVO) vo).width;
        component.height = ((CompositeItemVO) vo).height;

        entity.add(component);
        return component;
    }

    @Override
    protected void createNodeComponent(Entity root, Entity entity) {
        if(root != null) {
            super.createNodeComponent(root, entity);
        }

        NodeComponent node = new NodeComponent();
        entity.add(node);
    }

    protected void createCompositeComponents(Entity entity, CompositeItemVO vo) {
        CompositeTransformComponent compositeTransform = new CompositeTransformComponent();

        LayerMapComponent layerMap = new LayerMapComponent();
        if(vo.composite.layers.size() == 0) {
            vo.composite.layers.add(LayerItemVO.createDefault());
        }
        layerMap.setLayers(vo.composite.layers);

        entity.add(compositeTransform);
        entity.add(layerMap);
    }
}
