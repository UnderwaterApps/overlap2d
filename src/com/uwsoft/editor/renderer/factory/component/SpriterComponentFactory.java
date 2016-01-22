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
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.physics.box2d.World;
import com.brashmonkey.spriter.Player;
import com.brashmonkey.spriter.Rectangle;
import com.brashmonkey.spriter.SCMLReader;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.spriter.SpriterComponent;
import com.uwsoft.editor.renderer.components.spriter.SpriterDrawerComponent;
import com.uwsoft.editor.renderer.data.MainItemVO;
import com.uwsoft.editor.renderer.data.SpriterVO;
import com.uwsoft.editor.renderer.factory.EntityFactory;
import com.uwsoft.editor.renderer.resources.IResourceRetriever;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.renderer.utils.LibGdxDrawer;
import com.uwsoft.editor.renderer.utils.LibGdxLoader;

/**
 * Created by azakhary on 5/22/2015.
 */
public class SpriterComponentFactory extends ComponentFactory {

    public SpriterComponentFactory(RayHandler rayHandler, World world, IResourceRetriever rm) {
        super(rayHandler, world, rm);
    }

    @Override
    public void createComponents(Entity root, Entity entity, MainItemVO vo) {
        createSpriterDataComponent(entity, (SpriterVO) vo);
        createCommonComponents(entity, vo, EntityFactory.SPRITER_TYPE);
        createParentNodeComponent(root, entity);
        createNodeComponent(root, entity);
    }

    @Override
    protected DimensionsComponent createDimensionsComponent(Entity entity, MainItemVO vo) {
        DimensionsComponent component = new DimensionsComponent();

        SpriterComponent spriterComponent = ComponentRetriever.get(entity, SpriterComponent.class);

        Rectangle rect = spriterComponent.player.getBoundingRectangle(null);
        component.width = (int) rect.size.width;
        component.height = (int) rect.size.height;

        entity.add(component);
        return component;
    }

    protected SpriterComponent createSpriterDataComponent(Entity entity, SpriterVO vo) {
        SpriterComponent component = new SpriterComponent();
        component. entity = vo.entity;
        component.animation = vo.animation;
        component. animationName = vo.animationName;
        component.scale = vo.scale;

        FileHandle handle 	=	rm.getSCMLFile(vo.animationName);
        component.data = new SCMLReader(handle.read()).getData();
        LibGdxLoader loader = 	new LibGdxLoader(component.data);
        loader.load(handle.file());

        component.currentAnimationIndex	=	vo.animation;
        component.currentEntityIndex		=	vo.entity;

        component.player = new Player(component.data.getEntity(component.currentEntityIndex));

        component.player.setAnimation(component.currentAnimationIndex);
        component.player.setScale(component.scale);

        SpriterDrawerComponent spriterDrawer = new SpriterDrawerComponent();

        spriterDrawer.drawer = new LibGdxDrawer(loader, null);

        entity.add(component);
        entity.add(spriterDrawer);

        return component;
    }
}
