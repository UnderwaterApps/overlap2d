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
import com.uwsoft.editor.renderer.data.*;
import com.uwsoft.editor.renderer.factory.EntityFactory;
import com.uwsoft.editor.renderer.resources.IResourceRetriever;
import com.uwsoft.editor.renderer.resources.ResourceManager;
import com.uwsoft.editor.renderer.scripts.IScript;
import com.uwsoft.editor.renderer.systems.*;
import com.uwsoft.editor.renderer.systems.render.Overlap2dRenderer;

/**
 * SceneLoader is importatn part of runtime that utilizes provided
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

    public SceneLoader() {
        IResourceRetriever rm = new ResourceManager();
        ((ResourceManager)rm).initAllResources();
        Engine engine = new Engine();
        initSceneLoader(engine, rm);
    }

    public SceneLoader(IResourceRetriever rm) {
        Engine engine = new Engine();
        initSceneLoader(engine, rm);
    }

    public SceneLoader(Engine engine) {
        IResourceRetriever rm = new ResourceManager();
        initSceneLoader(engine, rm);
    }

	public void SceneLoader(Engine engine, IResourceRetriever rm) {
        initSceneLoader(engine, rm);
	}

    private void initSceneLoader(Engine engine, IResourceRetriever rm) {
        this.engine = engine;
        this.rm = rm;

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


	public SceneVO getSceneVO() {
		return sceneVO;
	}

	public SceneVO loadScene(String sceneName) {
		engine.removeAllEntities();
		
		sceneVO = rm.getSceneVO(sceneName);

		Viewport viewport = new ScalingViewport(Scaling.stretch, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
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
		Overlap2dRenderer renderer = new Overlap2dRenderer(new PolygonSpriteBatch());
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
				ZindexComponent zindexComponent = entity.getComponent(ZindexComponent.class);
				ParentNodeComponent parentNodeComponent = entity.getComponent(ParentNodeComponent.class);
				if(parentNodeComponent != null) {
					NodeComponent nodeComponent = parentNodeComponent.parentEntity.getComponent(NodeComponent.class);
					zindexComponent.zIndex = nodeComponent.children.size;
				}

                // call init for a system
                ScriptComponent scriptComponent = entity.getComponent(ScriptComponent.class);
                if(scriptComponent != null) {
                    for (IScript script : scriptComponent.scripts) {
                        script.init(entity);
                    }
                }
			}

			@Override
			public void entityRemoved(Entity entity) {
				ParentNodeComponent parentComponent = entity.getComponent(ParentNodeComponent.class);
				
				if(parentComponent == null){
					return;
				}
				
				Entity parentEntity = parentComponent.parentEntity;
				NodeComponent parentNodeComponent = parentEntity.getComponent(NodeComponent.class);
				parentNodeComponent.removeChild(entity);

				// check if composite and remove all children
				NodeComponent nodeComponent = entity.getComponent(NodeComponent.class);
				if(nodeComponent != null) {
					// it is composite
					for(Entity node: nodeComponent.children) {
						engine.removeEntity(node);
					}
				}
			}
		});
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
	
	public void setResourceManager(IResourceRetriever rm) {
		this.rm = rm;
		entityFactory.setResourceManager(rm);
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
