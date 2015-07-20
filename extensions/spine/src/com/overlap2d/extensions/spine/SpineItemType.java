package com.overlap2d.extensions.spine;/*
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

import box2dLight.RayHandler;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.World;
import com.uwsoft.editor.renderer.commons.IExternalItemType;
import com.uwsoft.editor.renderer.components.SpineDataComponent;
import com.uwsoft.editor.renderer.factory.EntityFactory;
import com.uwsoft.editor.renderer.factory.component.ComponentFactory;
import com.uwsoft.editor.renderer.resources.IResourceRetriever;
import com.uwsoft.editor.renderer.systems.render.logic.Drawable;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

/**
 * Created by azakhary on 7/20/2015.
 */
public class SpineItemType implements IExternalItemType {

    private ComponentFactory factory;
    private IteratingSystem system;
    private Drawable drawable;

    public SpineItemType() {
        factory = new SpineComponentFactory();
        system = new SpineSystem();
        drawable = new SpineDrawableLogic();
    }

    @Override
    public int getTypeId() {
        return EntityFactory.SPINE_TYPE;
    }

    @Override
    public Drawable getDrawable() {
        return drawable;
    }

    @Override
    public IteratingSystem getSystem() {
        return system;
    }

    @Override
    public ComponentFactory getComponentFactory() {
        return factory;
    }

    @Override
    public void injectMappers() {
        ComponentRetriever.addMapper(SpineDataComponent.class);
        ComponentRetriever.addMapper(SpineObjectComponent.class);
    }

    @Override
    public void injectDependencies(RayHandler rayHandler, World world, IResourceRetriever rm) {
        factory.injectDependencies(rayHandler, world, rm);
    }
}
