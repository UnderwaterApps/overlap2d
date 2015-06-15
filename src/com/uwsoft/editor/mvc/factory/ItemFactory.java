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
import com.uwsoft.editor.gdx.mediators.SceneControlMediator;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.ResourceManager;
import com.uwsoft.editor.mvc.view.stage.tools.TextTool;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.MainItemComponent;
import com.uwsoft.editor.renderer.factory.EntityFactory;
import com.uwsoft.editor.renderer.legacy.data.*;
import com.uwsoft.editor.utils.runtime.ComponentRetriever;
import com.uwsoft.editor.utils.runtime.EntityUtils;

import java.util.HashMap;

/**
 * Created by azakhary on 6/5/2015.
 */
public class ItemFactory {

    private static final String EVENT_PREFIX = "com.uwsoft.editor.mvc.factory.ItemFactory";
    public static final String NEW_ITEM_ADDED = EVENT_PREFIX + ".NEW_ITEM_ADDED";

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

    private boolean setEssentialData(MainItemVO vo, Vector2 position) {
        // TODO: sort out how layers work now
        //LayerItemVO layer = sandbox.getSelectedLayer();
        //if (layer == null) return false;

        vo.layerName = "default";

        // This is for grid
        position.x = MathUtils.floor(position.x / sandbox.getGridSize()) * sandbox.getGridSize();
        position.y = MathUtils.floor(position.y / sandbox.getGridSize()) * sandbox.getGridSize();

        // TODO: screen to stage coordinates

        vo.x = position.x;
        vo.y = position.y;

        return true;
    }

    public boolean createSimpleImage(String regionName, Vector2 position) {
        SimpleImageVO vo = new SimpleImageVO();
        vo.imageName = regionName;

        if(!setEssentialData(vo, position)) return false;
        Entity entity = entityFactory.createEntity(sandbox.getCurrentViewingEntity(), vo);
        Overlap2DFacade.getInstance().sendNotification(Sandbox.ACTION_CREATE_ITEM, entity);

        return true;
    }

    public boolean create9Patch(String regionName, Vector2 position) {
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

    public boolean createItemFromLibrary(String libraryName, Vector2 position) {
        SceneControlMediator sceneControl = sandbox.getSceneControl();
        HashMap<String, CompositeItemVO> libraryItems = sceneControl.getCurrentSceneVO().libraryItems;

        CompositeItemVO itemVO = libraryItems.get(libraryName);
        Entity entity = createCompositeItem(itemVO, position);

        //adding library name
        MainItemComponent mainItemComponent = ComponentRetriever.get(entity, MainItemComponent.class);
        mainItemComponent.itemName = libraryName;

        Overlap2DFacade.getInstance().sendNotification(Sandbox.ACTION_CREATE_ITEM, entity);

        return true;
    }

    public Entity createCompositeItem(CompositeItemVO vo, Vector2 position) {
        if(!setEssentialData(vo, position)) return null;

        Entity entity = entityFactory.createEntity(sandbox.getCurrentViewingEntity(), vo);
        sceneLoader.initWithAshley(entity, vo.composite);

        return entity;
    }

    public Entity createCompositeItem(Vector2 position) {
        CompositeItemVO vo = new CompositeItemVO();
        Entity entity = createCompositeItem(vo, position);
        return entity;
    }

    public Entity createLightItem(LightVO vo, Vector2 position) {
        if(!setEssentialData(vo, position)) return null;
        Entity entity = entityFactory.createEntity(sandbox.getCurrentViewingEntity(), vo);

        Overlap2DFacade.getInstance().sendNotification(Sandbox.ACTION_CREATE_ITEM, entity);
        return entity;
    }

    public boolean createParticleItem(String particleName, Vector2 position) {
        ParticleEffectVO vo = new ParticleEffectVO();
        vo.particleName = particleName;

        if(!setEssentialData(vo, position)) return false;
        Entity entity = entityFactory.createEntity(sandbox.getCurrentViewingEntity(), vo);
        Overlap2DFacade.getInstance().sendNotification(Sandbox.ACTION_CREATE_ITEM, entity);

        return true;
    }

    public boolean createLabel(TextTool textSettings, Vector2 position) {
        LabelVO vo = new LabelVO();
        if(!setEssentialData(vo, position)) return false;

        Overlap2DFacade facade = Overlap2DFacade.getInstance();
        ResourceManager resourceManager = facade.retrieveProxy(ResourceManager.NAME);

        resourceManager.prepareEmbeddingFont(textSettings.getFontFamily(), textSettings.getFontSize());

        // using long unique name
        vo.style = textSettings.getFontFamily();
        vo.text = "LABEL";
        vo.size = textSettings.getFontSize();

        // need to calculate minimum bounds size here
        vo.width = 100;
        vo.height = 100;

        Entity entity = entityFactory.createEntity(sandbox.getCurrentViewingEntity(), vo);
        Overlap2DFacade.getInstance().sendNotification(Sandbox.ACTION_CREATE_ITEM, entity);

        return true;
    }
}
