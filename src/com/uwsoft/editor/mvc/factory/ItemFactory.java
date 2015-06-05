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

package com.uwsoft.editor.mvc.factory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.factory.EntityFactory;
import com.uwsoft.editor.renderer.legacy.data.LayerItemVO;
import com.uwsoft.editor.renderer.legacy.data.SimpleImageVO;

/**
 * Created by azakhary on 6/5/2015.
 */
public class ItemFactory {

    private EntityFactory entityFactory;
    private SceneLoader sceneLoader;
    private Sandbox sandbox;

    private static ItemFactory instance;

    private ItemFactory(SceneLoader sceneLoader) {
        this.sceneLoader = sceneLoader;
        entityFactory = sceneLoader.getEntityFactory();
        sandbox = Sandbox.getInstance();
    }

    public static ItemFactory get() {
        if(instance == null) {
            instance = new ItemFactory(Sandbox.getInstance().sceneControl.sceneLoader);
        }

        return instance;
    }

    public boolean createSimpleImage(String regionName, Vector2 position) {
        LayerItemVO layer = sandbox.getSelectedLayer();
        if (layer == null) return false;
        position.x = MathUtils.floor(position.x / sandbox.getGridSize()) * sandbox.getGridSize();
        position.y = MathUtils.floor(position.y / sandbox.getGridSize()) * sandbox.getGridSize();

        SimpleImageVO vo = new SimpleImageVO();
        vo.imageName = regionName;
        vo.layerName = layer.layerName;
        // TODO: screent o stage coordinates
        vo.x = position.x;
        vo.y = position.y;

        Entity entity = entityFactory.createEntity(sceneLoader.rootEntity, vo);

        sandbox.getEngine().addEntity(entity);

        return true;
    }

    public boolean createSpriteAnimation(String regionName, Vector2 position) {
        return true;
    }

    public boolean createSpineAnimation(String regionName, Vector2 position) {
        return true;
    }

    public boolean createSpriterAnimation(String regionName, Vector2 position) {
        return true;
    }

    public boolean createItemFromLibrary(String regionName, Vector2 position) {
        return true;
    }

    public boolean createParticleItem(String regionName, Vector2 position) {
        return true;
    }
}
