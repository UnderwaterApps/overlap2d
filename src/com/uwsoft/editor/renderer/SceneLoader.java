package com.uwsoft.editor.renderer;

import java.util.HashMap;
import java.util.Map;

import box2dLight.RayHandler;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.uwsoft.editor.renderer.components.*;
import com.uwsoft.editor.renderer.components.light.LightObjectComponent;
import com.uwsoft.editor.renderer.data.*;
import com.uwsoft.editor.renderer.factory.EntityFactory;
import com.uwsoft.editor.renderer.resources.IResourceRetriever;
import com.uwsoft.editor.renderer.resources.ResourceManager;
import com.uwsoft.editor.renderer.scripts.IScript;
import com.uwsoft.editor.renderer.systems.*;
import com.uwsoft.editor.renderer.systems.render.Overlap2dRenderer;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

/**
 * SceneLoader is important part of runtime that utilizes provided
 * IResourceRetriever (or creates default one shipped with runtime) in order to
 * load entire scene data into viewable actors provides the functionality to get
 * root actor of scene and load scenes.
 */
public class SceneLoader {

	private String curResolution = "orig";
	private SceneVO sceneVO;
	private IResourceRetriever rm = null;

    public Engine engine = null;
	public RayHandler rayHandler;
	public World world;
	public Entity rootEntity;

	public EntityFactory entityFactory;

	private float pixesPerWU = 1;
	private Overlap2dRenderer renderer;

    public SceneLoader() {
        IResourceRetriever rm = new ResourceManager();
        ((ResourceManager)rm).initAllResources();
        this.engine = new Engine();
		initSceneLoader();
    }

    public SceneLoader(IResourceRetriever rm) {
        this.engine = new Engine();
		this.rm = rm;
		initSceneLoader();
    }

	/**
	 * this method is called when rm has loaded all data
	 */
    private void initSceneLoader() {
        RayHandler.setGammaCorrection(true);
        RayHandler.useDiffuseLight(true);

        rayHandler = new RayHandler(world);
        rayHandler.setAmbientLight(1f, 1f, 1f, 1f);
        rayHandler.setCulling(true);
        rayHandler.setBlur(true);
        rayHandler.setBlurNum(3);
        rayHandler.setShadows(true);

        addSystems();

        entityFactory = new EntityFactory(rayHandler, world, rm);
    }

	public void setResolution(String resolutionName) {
		ResolutionEntryVO resolution = getRm().getProjectVO().getResolution(resolutionName);
		if(resolution != null) {
			curResolution = resolutionName;
		}
	}


	public SceneVO getSceneVO() {
		return sceneVO;
	}

	public SceneVO loadScene(String sceneName, Viewport viewport) {

		pixesPerWU = rm.getProjectVO().pixelToWorld;

		engine.removeAllEntities();

		sceneVO = rm.getSceneVO(sceneName);

		if(sceneVO.composite == null) {
			sceneVO.composite = new CompositeVO();
		}
		rootEntity = entityFactory.createRootEntity(sceneVO.composite, viewport);
		engine.addEntity(rootEntity);

		if(sceneVO.composite != null) {
			entityFactory.initAllChildren(engine, rootEntity, sceneVO.composite);
		}

		setAmbienceInfo(sceneVO);

		return sceneVO;
	}

	public SceneVO loadScene(String sceneName) {
		ProjectInfoVO projectVO = rm.getProjectVO();
		Viewport viewport = new ScalingViewport(Scaling.stretch, (float)projectVO.originalResolution.width/pixesPerWU, (float)projectVO.originalResolution.height/pixesPerWU, new OrthographicCamera());
		return loadScene(sceneName, viewport);
	} 

	private void addSystems() {
		ParticleSystem particleSystem = new ParticleSystem();
		LightSystem lightSystem = new LightSystem();
		SpriteAnimationSystem animationSystem = new SpriteAnimationSystem();
		LayerSystem layerSystem = new LayerSystem();
		PhysicsSystem physicsSystem = new PhysicsSystem();
		SpineSystem spineSystem = new SpineSystem();
		CompositeSystem compositeSystem = new CompositeSystem();
		LabelSystem labelSystem = new LabelSystem();
        ScriptSystem scriptSystem = new ScriptSystem();
		renderer = new Overlap2dRenderer(new PolygonSpriteBatch());
		renderer.setRayHandler(rayHandler);
		
		engine.addSystem(animationSystem);
		engine.addSystem(particleSystem);
		engine.addSystem(lightSystem);
		engine.addSystem(layerSystem);
		engine.addSystem(physicsSystem);
		engine.addSystem(spineSystem);
		engine.addSystem(compositeSystem);
		engine.addSystem(labelSystem);
        engine.addSystem(scriptSystem);
		engine.addSystem(renderer);

		addEntityRemoveListener();
	}

	private void addEntityRemoveListener() {
		engine.addEntityListener(new EntityListener() {
			@Override
			public void entityAdded(Entity entity) {
				// TODO: Gev knows what to do. (do this for all entities)

				// mae sure we assign correct z-index here
				/*
				ZindexComponent zindexComponent = ComponentRetriever.get(entity, ZindexComponent.class);
				ParentNodeComponent parentNodeComponent = ComponentRetriever.get(entity, ParentNodeComponent.class);
				if (parentNodeComponent != null) {
					NodeComponent nodeComponent = parentNodeComponent.parentEntity.getComponent(NodeComponent.class);
					zindexComponent.setZIndex(nodeComponent.children.size);
					zindexComponent.needReOrder = false;
				}*/

				// call init for a system
				ScriptComponent scriptComponent = entity.getComponent(ScriptComponent.class);
				if (scriptComponent != null) {
					for (IScript script : scriptComponent.scripts) {
						script.init(entity);
					}
				}
			}

			@Override
			public void entityRemoved(Entity entity) {
				ParentNodeComponent parentComponent = ComponentRetriever.get(entity, ParentNodeComponent.class);

				if (parentComponent == null) {
					return;
				}

				Entity parentEntity = parentComponent.parentEntity;
				NodeComponent parentNodeComponent = ComponentRetriever.get(parentEntity, NodeComponent.class);
				parentNodeComponent.removeChild(entity);

				// check if composite and remove all children
				NodeComponent nodeComponent = ComponentRetriever.get(entity, NodeComponent.class);
				if (nodeComponent != null) {
					// it is composite
					for (Entity node : nodeComponent.children) {
						engine.removeEntity(node);
					}
				}

                // check if it is light
                LightObjectComponent lightObjectComponent = ComponentRetriever.get(entity, LightObjectComponent.class);
                if(lightObjectComponent != null) {
                    lightObjectComponent.lightObject.remove(true);
                }
			}
		});
	}

	public Entity loadFromLibrary(String libraryName) {
		ProjectInfoVO projectInfoVO = getRm().getProjectVO();
		CompositeItemVO compositeItemVO = projectInfoVO.libraryItems.get(libraryName);

		if(compositeItemVO != null) {
			Entity entity = entityFactory.createEntity(null, compositeItemVO);
			return entity;
		}

		return null;
	}

	/**
	 * Sets ambient light to the one specified in scene from editor
	 *
	 * @param vo
	 *            - Scene data file to invalidate
	 */
	public void setAmbienceInfo(SceneVO vo) {
		if (vo.ambientColor != null) {
			Color clr = new Color(vo.ambientColor[0], vo.ambientColor[1],
					vo.ambientColor[2], vo.ambientColor[3]);
			rayHandler.setAmbientLight(clr);
		}
	}


	public EntityFactory getEntityFactory() {
		return entityFactory;
	}

	 public IResourceRetriever getRm() {
	 	return rm;
	 }

    public Engine getEngine() {
        return engine;
    }
}
