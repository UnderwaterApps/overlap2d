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

package com.uwsoft.editor.view.ui.box.resourcespanel;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.function.BiFunction;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.uwsoft.editor.view.stage.Sandbox;
import com.uwsoft.editor.factory.ItemFactory;
import com.uwsoft.editor.proxy.ResourceManager;
import com.uwsoft.editor.view.ui.box.resourcespanel.draggable.DraggableResource;
import com.uwsoft.editor.view.ui.box.resourcespanel.draggable.DraggableResourceView;
import com.uwsoft.editor.view.ui.box.resourcespanel.draggable.box.SpineResource;
import com.uwsoft.editor.view.ui.box.resourcespanel.draggable.box.SpriteResource;
import com.uwsoft.editor.view.ui.box.resourcespanel.draggable.box.SpriterResource;

/**
 * Created by azakhary on 4/17/2015.
 */
public class UIAnimationsTabMediator extends UIResourcesTabMediator<UIAnimationsTab> {

    private static final String TAG = UIAnimationsTabMediator.class.getCanonicalName();
    public static final String NAME = TAG;
    private Array<DraggableResource> animationBoxes;

    public UIAnimationsTabMediator() {
        super(NAME, new UIAnimationsTab());
        animationBoxes = new Array<>();
    }


    @Override
    protected void initList(String searchText) {
        animationBoxes.clear();
        Sandbox sandbox = Sandbox.getInstance();
        ResourceManager resourceManager = facade.retrieveProxy(ResourceManager.NAME);
        createAnimationResources(resourceManager.getProjectSpineAnimationsList().keySet(), SpineResource.class, ItemFactory.get()::createSpineAnimation, searchText);
        createAnimationResources(resourceManager.getProjectSpriteAnimationsList().keySet(), SpriteResource.class, ItemFactory.get()::createSpriteAnimation, searchText);
        createAnimationResources(resourceManager.getProjectSpriterAnimationsList().keySet(), SpriterResource.class, ItemFactory.get()::createSpriterAnimation, searchText);
        viewComponent.setThumbnailBoxes(animationBoxes);
    }

    private void createAnimationResources(Set<String> strings, Class resourceClass, BiFunction<String, Vector2, Boolean> factoryFunction, String searchText) {
        for (String animationName : strings) {
            if(!animationName.contains(searchText))continue;
            try {
                Constructor constructor = resourceClass.getConstructor(String.class);
                DraggableResource draggableResource = new DraggableResource((DraggableResourceView) constructor.newInstance(animationName));
                draggableResource.initDragDrop();
                draggableResource.setFactoryFunction(factoryFunction);
                animationBoxes.add(draggableResource);
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
