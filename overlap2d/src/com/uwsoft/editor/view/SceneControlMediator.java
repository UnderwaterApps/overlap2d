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

package com.uwsoft.editor.view;

import box2dLight.RayHandler;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.proxy.ProjectManager;
import com.uwsoft.editor.proxy.ResolutionManager;
import com.uwsoft.editor.proxy.ResourceManager;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.data.CompositeItemVO;
import com.uwsoft.editor.renderer.data.ProjectInfoVO;
import com.uwsoft.editor.renderer.data.SceneVO;

/**
 * Mediates scene communication between editor and current runtime
 *
 * @author azakhary
 */
public class SceneControlMediator {

	private final Overlap2DFacade facade;
	private final ProjectManager projectManager;
	/**
	 * main holder of the scene
	 */
	public SceneLoader sceneLoader;

	/**
	 * runtime essentials
	 */
	// private Essentials essentials;

	/**
	 * current scene tools
	 */
	private SceneVO currentSceneVo;

	/**
	 * tools object of the root element of the scene
	 */
	private CompositeItemVO rootSceneVO;

	/**
	 * current scene rendering item
	 */
	private Entity currentScene;

	private boolean isLightsEnabled = false;

	public SceneControlMediator(SceneLoader sceneLoader) {
		this.sceneLoader = sceneLoader;
		// this.essentials = essentials;
		facade = Overlap2DFacade.getInstance();
		projectManager = facade.retrieveProxy(ProjectManager.NAME);
	}

	public ProjectInfoVO getProjectInfoVO() {
		return sceneLoader.getRm().getProjectVO();
	}

	public void initScene(String sceneName) {
		ResolutionManager resolutionManager = facade.retrieveProxy(ResolutionManager.NAME);
		ResourceManager resourceManager = facade.retrieveProxy(ResourceManager.NAME);

		ScreenViewport viewport = new ScreenViewport();
		// Yey to whoever made this method
		viewport.setUnitsPerPixel(1f/resourceManager.getProjectVO().pixelToWorld);

		currentSceneVo = sceneLoader.loadScene(sceneName, viewport);
		// TODO: this is now in sceneLoaader but probably will be changed
		// essentials.world = new World(new
		// Vector2(currentSceneVo.physicsPropertiesVO.gravityX,
		// currentSceneVo.physicsPropertiesVO.gravityY), true);
		// essentials.rayHandler.setWorld(essentials.world);

		rootSceneVO = new CompositeItemVO(currentSceneVo.composite);
	}

//	public CompositeItem initSceneView(CompositeItemVO compositeItemVO) {
//		disableLights(false);
//
//		if (getCurrentScene() != null)
//			getCurrentScene().dispose();
//
//		CompositeItemVO itemVo = new CompositeItemVO();
//		itemVo.composite = compositeItemVO.composite;
//		itemVo.itemIdentifier = compositeItemVO.itemIdentifier;
//		itemVo.libraryLink = compositeItemVO.libraryLink;
//		CompositeItem composite = new CompositeItem(itemVo, getEssentials());
//
//		return composite;
//	}
//
//	public void initSceneView(CompositeItem composite, boolean isRootScene) {
//		ResolutionManager resolutionManager = facade.retrieveProxy(ResolutionManager.NAME);
//		composite.applyResolution(resolutionManager.currentResolutionName);
//		currentScene = composite;
//
//		if (isRootScene) {
//			rootSceneVO = currentScene.dataVO;
//		}
//
//		if (currentSceneVo.ambientColor == null) {
//			currentSceneVo.ambientColor = new float[4];
//			currentSceneVo.ambientColor[0] = 0.5f;
//			currentSceneVo.ambientColor[1] = 0.5f;
//			currentSceneVo.ambientColor[2] = 0.5f;
//			currentSceneVo.ambientColor[3] = 1.0f;
//		}
//	}

	public void disableLights(boolean disable) {
		//TODO lights enable disable
//		ArrayList<LightActor> lights = getAllLights(currentScene);
//
//		for (int i = lights.size() - 1; i >= 0; i--) {
//			LightActor lightActor = lights.get(i);
//			if (lightActor.lightObject != null) {
//				lightActor.lightObject.setActive(!disable);
//			}
//
//		}

		isLightsEnabled = !disable;
	}

	public void setDiffuse(boolean isDiffuse) {
		if (isLightsEnabled) {
			RayHandler.useDiffuseLight(isDiffuse);
		} else {
			RayHandler.useDiffuseLight(true);
		}
	}

	public boolean isDiffuse() {
		return RayHandler.isDiffuse;
	}

	public boolean isLightsEnabled() {
		return isLightsEnabled;
	}

	public void setAmbienceInfo(SceneVO vo) {
		Color clr = new Color(vo.ambientColor[0], vo.ambientColor[1],
				vo.ambientColor[2], vo.ambientColor[3]);
		sceneLoader.rayHandler.setAmbientLight(clr);
	}

	public void disableAmbience(boolean disable) {
		if (disable) {
			sceneLoader.rayHandler.setAmbientLight(1f, 1f, 1f, 1f);
		} else {
			setAmbienceInfo(sceneLoader.getSceneVO());
		}
	}

	//TODO support function for lights on of
	
//	private ArrayList<LightActor> getAllLights(CompositeItem curComposite) {
//
//		ArrayList<LightActor> lights = new ArrayList<LightActor>();
//
//		if (curComposite == null) {
//			return lights;
//		}
//
//		ArrayList<IBaseItem> items = curComposite.getItems();
//
//		ArrayList<CompositeItem> nestedComposites = new ArrayList<CompositeItem>();
//
//		for (int i = 0; i < items.size(); i++) {
//			IBaseItem item = items.get(i);
//			if (item instanceof LightActor) {
//				lights.add((LightActor) item);
//			}
//
//			if (item instanceof CompositeItem) {
//				nestedComposites.add((CompositeItem) item);
//			}
//
//		}
//
//		for (int i = 0; i < nestedComposites.size(); i++) {
//			lights.addAll(getAllLights(nestedComposites.get(i)));
//		}
//
//		return lights;
//	}

	//TODO we don't need this anymore
//	public Essentials getEssentials() {
//		return essentials;
//	}

	public CompositeItemVO getRootSceneVO() {
		return rootSceneVO;
	}

	public Entity getCurrentScene() {
		return currentScene;
	}

	public SceneVO getCurrentSceneVO() {
		return currentSceneVo;
	}
	
	public Entity getRootEntity() {
		return sceneLoader.rootEntity;
	}

//	public CompositeItem getCompositeElement(CompositeItemVO vo) {
//		return sceneLoader.getCompositeElement(vo);
//	}

}
