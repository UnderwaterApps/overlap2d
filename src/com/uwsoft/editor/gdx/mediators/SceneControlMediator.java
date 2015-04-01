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

package com.uwsoft.editor.gdx.mediators;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.uwsoft.editor.controlles.ResolutionManager;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.DataManager;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.actor.CompositeItem;
import com.uwsoft.editor.renderer.actor.IBaseItem;
import com.uwsoft.editor.renderer.actor.LightActor;
import com.uwsoft.editor.renderer.data.CompositeItemVO;
import com.uwsoft.editor.renderer.data.Essentials;
import com.uwsoft.editor.renderer.data.SceneVO;

import java.util.ArrayList;

/**
 * Mediates scene communication between editor and current runtime
 *
 * @author azakhary
 */
public class SceneControlMediator {

    private final Overlap2DFacade facade;
    private final DataManager dataManager;
    /**
     * main holder of the scene
     */
    private SceneLoader sceneLoader;

    /**
     * runtime essentials
     */
    private Essentials essentials;

    /**
     * current scene data
     */
    private SceneVO currentSceneVo;

    /**
     * data object of the root element of the scene
     */
    private CompositeItemVO rootSceneVO;

    /**
     * current scene rendering item
     */
    private CompositeItem currentScene;

    public SceneControlMediator(SceneLoader sceneLoader, Essentials essentials) {
        this.sceneLoader = sceneLoader;
        this.essentials = essentials;
        facade = Overlap2DFacade.getInstance();
        dataManager = facade.retrieveProxy(DataManager.NAME);
    }

    public void initScene(String sceneName) {
        if (essentials.world != null) {
            if (currentScene != null) currentScene.dispose();
        }

        essentials.physicsStopped = true;
        sceneLoader = new SceneLoader(essentials);
        ResolutionManager resolutionManager = facade.retrieveProxy(ResolutionManager.NAME);
        sceneLoader.setResolution(resolutionManager.curResolution);

        currentSceneVo = sceneLoader.loadScene(sceneName, false);
        essentials.world = new World(new Vector2(currentSceneVo.physicsPropertiesVO.gravityX, currentSceneVo.physicsPropertiesVO.gravityY), true);
        essentials.rayHandler.setWorld(essentials.world);

        rootSceneVO = new CompositeItemVO(currentSceneVo.composite);
    }

    public CompositeItem initSceneView(CompositeItemVO compositeItemVO) {
        disableLights(false);

        if (getCurrentScene() != null) getCurrentScene().dispose();

        CompositeItemVO itemVo = new CompositeItemVO();
        itemVo.composite = compositeItemVO.composite;
        itemVo.itemIdentifier = compositeItemVO.itemIdentifier;
        itemVo.itemName = compositeItemVO.itemName;
        CompositeItem composite = new CompositeItem(itemVo, getEssentials());

        return composite;
    }

    public void initSceneView(CompositeItem composite, boolean isRootScene) {
        ResolutionManager resolutionManager = facade.retrieveProxy(ResolutionManager.NAME);
        composite.applyResolution(resolutionManager.curResolution);
        currentScene = composite;

        if (isRootScene) {
            rootSceneVO = currentScene.dataVO;
        }

        if (currentSceneVo.ambientColor == null) {
            currentSceneVo.ambientColor = new float[4];
            currentSceneVo.ambientColor[0] = 0.5f;
            currentSceneVo.ambientColor[1] = 0.5f;
            currentSceneVo.ambientColor[2] = 0.5f;
            currentSceneVo.ambientColor[3] = 1.0f;
        }
    }

    public void disableLights(boolean disable) {

        ArrayList<LightActor> lights = getAllLights(currentScene);

        for (int i = lights.size() - 1; i >= 0; i--) {
            LightActor lightActor = lights.get(i);
            if (lightActor.lightObject != null) {
                lightActor.lightObject.setActive(!disable);
            }

        }
    }

    public void setAmbienceInfo(SceneVO vo) {
        Color clr = new Color(vo.ambientColor[0], vo.ambientColor[1], vo.ambientColor[2], vo.ambientColor[3]);
        essentials.rayHandler.setAmbientLight(clr);
    }

    public void disableAmbience(boolean disable) {
        if (disable) {
            essentials.rayHandler.setAmbientLight(1f, 1f, 1f, 1f);
        } else {
            setAmbienceInfo(sceneLoader.getSceneVO());
        }
    }

    private ArrayList<LightActor> getAllLights(CompositeItem curComposite) {

        ArrayList<LightActor> lights = new ArrayList<LightActor>();

        if (curComposite == null) {
            return lights;
        }

        ArrayList<IBaseItem> items = curComposite.getItems();

        ArrayList<CompositeItem> nestedComposites = new ArrayList<CompositeItem>();


        for (int i = 0; i < items.size(); i++) {
            IBaseItem item = items.get(i);
            if (item instanceof LightActor) {
                lights.add((LightActor) item);
            }

            if (item instanceof CompositeItem) {
                nestedComposites.add((CompositeItem) item);
            }

        }

        for (int i = 0; i < nestedComposites.size(); i++) {
            lights.addAll(getAllLights(nestedComposites.get(i)));
        }

        return lights;
    }

    public Essentials getEssentials() {
        return essentials;
    }

    public CompositeItemVO getRootSceneVO() {
        return rootSceneVO;
    }

    public CompositeItem getCurrentScene() {
        return currentScene;
    }

    public SceneVO getCurrentSceneVO() {
        return currentSceneVo;
    }

    public CompositeItem getCompositeElement(CompositeItemVO vo) {
        return sceneLoader.getCompositeElement(vo);
    }

}
