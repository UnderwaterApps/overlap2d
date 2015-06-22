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

package com.uwsoft.editor.gdx.sandbox;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.gdx.mediators.ItemControlMediator;
import com.uwsoft.editor.gdx.mediators.SceneControlMediator;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.ProjectManager;
import com.uwsoft.editor.mvc.view.stage.tools.TextTool;
import com.uwsoft.editor.renderer.legacy.data.CompositeVO;
import com.uwsoft.editor.renderer.legacy.data.LayerItemVO;
import com.uwsoft.editor.renderer.legacy.data.LightVO;
import com.uwsoft.editor.renderer.legacy.data.MainItemVO;
import com.uwsoft.editor.renderer.legacy.data.SimpleImageVO;


/**
 * Provides methods to create panels of different types based on provided tools, and adds them to the scene.
 */
public class ItemFactoryOld {

    public static final String PREFIX = "com.uwsoft.editor.gdx.sandbox.ItemFactoryOld";
    public static final String NEW_ITEM_ADDED = PREFIX + ".NEW_ITEM_ADDED";
//
    private final Overlap2DFacade facade;
    private final ProjectManager projectManager;
    private Sandbox sandbox;
    private SceneControlMediator sceneControl;
    private ItemControlMediator itemControl;

    public ItemFactoryOld(Sandbox sandbox) {
        this.sandbox = sandbox;
        sceneControl = sandbox.getSceneControl();
        facade = Overlap2DFacade.getInstance();
        projectManager = facade.retrieveProxy(ProjectManager.NAME);
    }

    private void initSpritePhysicsData(SimpleImageVO vo) {
        String regionName = vo.imageName;
      //TODO fix and uncomment
//        if (projectManager.getCurrentProjectInfoVO().assetMeshMap.containsKey(regionName)) {
//            vo.meshId = projectManager.getCurrentProjectInfoVO().assetMeshMap.get(regionName);
//            PhysicsBodyDataVO data = projectManager.getCurrentProjectInfoVO().meshes.get(vo.meshId).initialProperties;
//            if (data != null) {
//                vo.physicsBodyData = new PhysicsBodyDataVO(data);
//            } else {
//                vo.physicsBodyData = new PhysicsBodyDataVO();
//            }
//        }
    }

    public void create9patchItem(LayerItemVO layer, String regionName, float x, float y) {
    	//TODO fix and uncomment
//        sceneControl.getCurrentScene().updateDataVO();
//
//        Image9patchVO vo = new Image9patchVO();
//        vo.imageName = regionName;
//
//        prepareVO(vo, layer.layerName, x, y);
//
//        Image9patchItem itm = new Image9patchItem(vo, sceneControl.getEssentials(), sceneControl.getCurrentScene());
//
//        addItem(itm, vo);
    }


    public LayerItemVO getSelectedLayer() {
    	//TODO fix and uncomment
//        UILayerBoxMediator lbm = facade.retrieveMediator(UILayerBoxMediator.NAME);
//        int selectedLayerIndex = lbm.getCurrentSelectedLayerIndex();
//        LayerItemVO layerVO = Sandbox.getInstance().sceneControl.getCurrentScene().dataVO.composite.layers.get(selectedLayerIndex);

        return new LayerItemVO();
    }
}
