package com.uwsoft.editor.renderer;

import java.util.HashMap;
import java.util.Map;

import box2dLight.RayHandler;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.uwsoft.editor.renderer.conponents.CompositeTransformComponent;
import com.uwsoft.editor.renderer.conponents.MainItemComponent;
import com.uwsoft.editor.renderer.conponents.NodeComponent;
import com.uwsoft.editor.renderer.conponents.ViewPortComponent;
import com.uwsoft.editor.renderer.legacy.data.CompositeItemVO;
import com.uwsoft.editor.renderer.legacy.data.CompositeVO;
import com.uwsoft.editor.renderer.legacy.data.SceneVO;
import com.uwsoft.editor.renderer.legacy.data.SimpleImageVO;
import com.uwsoft.editor.renderer.resources.ResourceManager;
import com.uwsoft.editor.renderer.systems.LayerSystem;
import com.uwsoft.editor.renderer.systems.LightSystem;
import com.uwsoft.editor.renderer.systems.ParticleSystem;
import com.uwsoft.editor.renderer.systems.PhysicsSystem;
import com.uwsoft.editor.renderer.systems.SpineSystem;
import com.uwsoft.editor.renderer.systems.SpriteAnimationSystem;

/**
 * SceneLoader is importatn part of runtime that utilizes provided
 * IResourceRetriever (or creates default one shipped with runtime) in order to
 * load entire scene data into viewable actors provides the functionality to get
 * root actor of scene and load scenes.
 */
public class SceneLoader {

	private String curResolution = "orig";
	private SceneVO sceneVO;
	private ResourceManager rm = null;
	public Engine engine = null;
	public RayHandler rayHandler;
	public World world;
	private EntityFactory entityFactory;

	/**
	 * Empty constructor is intended for easy use, it will create default
	 * ResourceManager, and load all possible resources into memory that have
	 * been exported with editor
	 */
	public SceneLoader(Engine engine) {
		this.engine = engine;
		rm = new ResourceManager();
		rm.initAllResources();
		// world = new World(gravity, doSleep)
		RayHandler.setGammaCorrection(true);
		RayHandler.useDiffuseLight(true);

		rayHandler = new RayHandler(world);
		rayHandler.setAmbientLight(1f, 1f, 1f, 1f);
		rayHandler.setCulling(true);
		rayHandler.setBlur(true);
		rayHandler.setBlurNum(3);
		rayHandler.setShadows(true);
		// TODO
		// rayHandler.setCombinedMatrix(getCamera().combined);
		
		addSystems();
		
		entityFactory = new EntityFactory(rayHandler, world, rm);
	}

	/**
	 * intended for easy use, it will create default ResourceManager, and load
	 * all possible resources into memory that have been exported with editor
	 * 
	 * @param resolution
	 *            - String resolution name to load everything for 9default is
	 *            "orig"
	 */
	public SceneLoader(String resolution) {
		rm = new ResourceManager();
		rm.setWorkingResolution(resolution);
		rm.initAllResources();
		curResolution = resolution;
	}

	/**
	 * Sets your implementation of IResourceRetriever, and does not load
	 * anything
	 *
	 * @param rm
	 *            - Implementation of IResourceRetriever
	 */
	// public SceneLoader(IResourceRetriever rm) {
	// this.rm = rm;
	// }

	/**
	 * Sets essentials container with or without content for later use This the
	 * most dummy contructor
	 * 
	 * @param e
	 *            - Essentials container
	 */
	// public SceneLoader(Essentials e) {
	// this.essentials = e;
	// }

	/**
	 * Sets resolution of the screen, and applies it to existing actors if
	 * already loaded
	 * 
	 * @param resolutionName
	 *            - String resolution name to load everything for 9default is
	 *            "orig"
	 */
	// public void setResolution(String resolutionName) {
	// curResolution = resolutionName;
	// if (sceneActor != null) {
	// sceneActor.applyResolution(resolutionName);
	// }
	// }

	/**
	 *
	 * @return SceneVO data if scene is already loaded with loadScene
	 */
	public SceneVO getSceneVO() {
		return sceneVO;
	}

	/**
	 * Asks IResourceRetriever for sceneVO data, checks scene for errors Applies
	 * resolution set previously with setResolution method or uses "orig"
	 * default resolution Sets default ambient light using scene data
	 *
	 * @param sceneName
	 *            - String scene name without ".dt" extension
	 * @param createActors
	 *            - if true the root composite with entire actor list will be
	 *            created
	 * @return SceneVO data file of loaded scene (you don't really need it at
	 *         this point though...)
	 */
	public SceneVO loadScene(String sceneName) {
		engine.removeAllEntities();
		
		sceneVO = rm.getSceneVO(sceneName);

		// init physics world
		// PhysicsPropertiesVO physicsProperties = sceneVO.physicsPropertiesVO;
		// if(sceneVO.physicsPropertiesVO != null &&
		// sceneVO.physicsPropertiesVO.enabled == true)

		// if (sceneVO.physicsPropertiesVO != null
		// && sceneVO.physicsPropertiesVO.enabled == true) {
		// essentials.world = new World(new Vector2(
		// physicsProperties.gravityX, physicsProperties.gravityY),
		// true);
		// essentials.rayHandler.setWorld(essentials.world);
		// }

		invalidateSceneVO(sceneVO);

		MainItemComponent mainComponent = new MainItemComponent();
		CompositeTransformComponent transform = new CompositeTransformComponent();
		NodeComponent node = new NodeComponent();
		
		ViewPortComponent viewPortComponent = new ViewPortComponent();
		viewPortComponent.viewPort = new ScalingViewport(Scaling.stretch, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
		viewPortComponent.viewPort.getCamera().position.set(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, 0);
		viewPortComponent.viewPort.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		
		Entity entity = new Entity();
		entity.add(mainComponent);
		entity.add(transform);
		entity.add(node);
		entity.add(viewPortComponent);
		entity.flags = EntityFactory.COMPOSITE_TYPE;

		engine.addEntity(entity);

		initWithAshley(entity, sceneVO.composite);

		// if (createActors) {
		// sceneActor = getSceneAsActor();
		// if (!curResolution.equals("orig"))
		// sceneActor.applyResolution(curResolution);
		// }
		//
		setAmbienceInfo(sceneVO);
		
		return sceneVO;
	}
	
	//TODO optimize this function 

	private void addSystems() {
		ParticleSystem particleSystem = new ParticleSystem();
		LightSystem lightSystem = new LightSystem();
		SpriteAnimationSystem animationSystem = new SpriteAnimationSystem();
		LayerSystem layerSystem = new LayerSystem();
		PhysicsSystem physicsSystem = new PhysicsSystem();
		SpineSystem spineSystem = new SpineSystem();
		engine.addSystem(animationSystem);
		engine.addSystem(particleSystem);
		engine.addSystem(lightSystem);
		engine.addSystem(layerSystem);
		engine.addSystem(physicsSystem);
		engine.addSystem(spineSystem);
	}

	//TODO this function should be changed later 
	private void initWithAshley(Entity root, CompositeVO vo) {
		//NodeComponent nodeComponent = nodeComponentMapper.get(root);
		for (int i = 0; i < vo.sImages.size(); i++) {
			Entity entity = entityFactory.createEntity(root, vo.sImages.get(i));
			engine.addEntity(entity);
		}
		
		//TODO after drawable mechanics is done
		for (int i = 0; i < vo.sImage9patchs.size(); i++) {
			// Image9patchVO imageVo = vo.sImage9patchs.get(i);
			//
			// NinePatchComponnent ninePatchComponent = new
			// NinePatchComponnent();
			// ninePatchComponent.height = imageVo.height;
			// ninePatchComponent.width = imageVo.width;
			//
			// //TODO remember nine patch region name somewhere
			// AtlasRegion atlasRegion = (TextureAtlas.AtlasRegion)
			// rm.getTextureRegion(imageVo.imageName);
			// ninePatchComponent.ninePatch = new NinePatch(atlasRegion,
			// atlasRegion.splits[0], atlasRegion.splits[1],
			// atlasRegion.splits[2], atlasRegion.splits[3]);
			
			// TODO NIN PTACH SYSTEM
		}

		for (int i = 0; i < vo.sLabels.size(); i++) {

		}

		for (int i = 0; i < vo.sParticleEffects.size(); i++) {
			Entity entity = entityFactory.createEntity(root, vo.sParticleEffects.get(i));
			engine.addEntity(entity);
		}

		for (int i = 0; i < vo.sLights.size(); i++) {
			Entity entity = entityFactory.createEntity(root, vo.sLights.get(i));
			engine.addEntity(entity);
		}

		for (int i = 0; i < vo.sSpineAnimations.size(); i++) {
			Entity entity = entityFactory.createEntity(root, vo.sSpineAnimations.get(i));
			engine.addEntity(entity);
		}

		for (int i = 0; i < vo.sSpriteAnimations.size(); i++) {			
			Entity entity = entityFactory.createEntity(root, vo.sSpriteAnimations.get(i));
			engine.addEntity(entity);
		}

		for (int i = 0; i < vo.sSpriterAnimations.size(); i++) {
			Entity entity = entityFactory.createEntity(root, vo.sSpriterAnimations.get(i));
			engine.addEntity(entity);
		}

		for (int i = 0; i < vo.layers.size(); i++) {
			//TODO wtf is this do we need this? :O
		}

		for (int i = 0; i < vo.sComposites.size(); i++) {
			Entity entity = entityFactory.createEntity(root, vo.sComposites.get(i));
			engine.addEntity(entity);
			initWithAshley(entity,  vo.sComposites.get(i).composite);
		}
	}
	
	

	/**
	 * Asks IResourceRetriever for sceneVO data, checks scene for errors and
	 * Recreates a big Actor tree that you can later add to your stage for
	 * rendering Applies resolution set previously with setResolution method or
	 * uses "orig" default resolution Sets default ambient light using scene
	 * data
	 *
	 * @param sceneName
	 *            - String scene name without ".dt" extension
	 * @return SceneVO data file of loaded scene (you don't really need it at
	 *         this point though...)
	 */
	// public SceneVO loadScene(String sceneName) {
	// return loadScene(sceneName, true);
	// }

	/**
	 * Checks scene for continuity errors
	 *
	 * @param vo
	 *            - Scene data file to invalidate
	 */
	public void invalidateSceneVO(SceneVO vo) {
		removeMissingImages(vo.composite);
	}

	/**
	 * Checks if composite data contains any scene items with images not
	 * provided by resource retriever, and removes them from composite to at
	 * least show what is not missing
	 *
	 * @param vo
	 *            - Scene data file to invalidate
	 */
	public void removeMissingImages(CompositeVO vo) {
		if (vo == null)
			return;
		for (SimpleImageVO img : vo.sImages) {
			if (rm.getTextureRegion(img.imageName) == null) {
				vo.sImages.remove(img);
			}
		}
		for (CompositeItemVO cmp : vo.sComposites) {
			removeMissingImages(cmp.composite);
		}
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
	
	public static class Frames {
        public int startFrame;
        public int endFrame;
        public String name;

        public Frames(int startFrame, int endFrame, String name) {
            this.startFrame = startFrame;
            this.endFrame = endFrame;
            this.name = name;
        }

        public Frames() {
        }

        public static String constructJsonString(Map<String, Frames> animations) {
            String str = "";
            Json json = new Json();
            json.setOutputType(JsonWriter.OutputType.json);
            str = json.toJson(animations);
            return str;
        }

        public static Map<String, Frames> constructJsonObject(String animations) {
            if (animations.isEmpty()) {
                return new HashMap<>();
            }
            Json json = new Json();
            return json.fromJson(HashMap.class, animations);
        }
    }

	/**
	 * Creates CompositeItem from sceneVo *
	 * 
	 * @return CompositeItem
	 */
	// public CompositeItem getSceneAsActor() {
	// CompositeItemVO vo = new CompositeItemVO(sceneVO.composite);
	//
	// if (vo.composite == null)
	// vo.composite = new CompositeVO();
	// CompositeItem cnt = new CompositeItem(vo, essentials);
	//
	// return cnt;
	// }

	/**
	 * Loads CompositeItem from Library by using it's library name So you can
	 * get item that is not on the scene, but is stored in library for later use
	 * Works great for dialogs and thigns like that
	 *
	 * TODO: this should be also renamed as name is confusing
	 * 
	 * @param name
	 *            String - library item name
	 * @return CompositeItem Actor
	 */
//	public CompositeItem getLibraryAsActor(String name) {
//		CompositeItemVO vo = new CompositeItemVO(sceneVO.libraryItems.get(name));
//		if (vo.composite == null)
//			vo.composite = new CompositeVO();
//		CompositeItem cnt = new CompositeItem(vo, null);
//		cnt.dataVO.itemName = name;
//		cnt.applyResolution(curResolution);
//		cnt.setX(0);
//		cnt.setY(0);
//		return cnt;
//	}
	

	/**
	 * Returns CompositeItem that is inside rootScene identified by unique id
	 * set in Editor does not perform deep search inside other composites
	 *
	 * @param id
	 *            - String uniqe identifier
	 * @return - CompositeItem
	 */
	// public CompositeItem getCompositeElementById(String id) {
	// CompositeItem cnt = getCompositeElement(sceneActor.getCompositeById(id)
	// .getDataVO());
	//
	// return cnt;
	// }

	/**
	 * Creates CompositeItem by provided CompositeItemVO data class
	 *
	 * @param vo
	 *            CompositeItemVO data class
	 * @return CompositeItem
	 */
	// public CompositeItem getCompositeElement(CompositeItemVO vo) {
	// CompositeItem cnt = new CompositeItem(vo, essentials);
	// return cnt;
	// }
	//
	// public void addScriptTo(String name, IScript iScript) {
	// sceneActor.addScriptTo(name, iScript);
	// }
	//
	// public void addScriptTo(String name, ArrayList<IScript> iScripts) {
	// sceneActor.addScriptTo(name, iScripts);
	// }

	/**
	 *
	 * @return IResourceRetriever instance to load any resources already in
	 *         memory
	 */
	// public IResourceRetriever getRm() {
	// return essentials.rm;
	// }
	//
	// /**
	// *
	// * @return CompositeItem - root element of the scene
	// */
	// public CompositeItem getRoot() {
	// return sceneActor;
	// }

	/**
	 * Injects elements loaded through this scene loader into properly annotated
	 * fields
	 * 
	 * @param object
	 */
	// @SuppressWarnings("unchecked")
	// public void inject(Object object) {
	// Class<?> cls = object.getClass();
	// // get all public fields
	// Field[] fields = cls.getDeclaredFields();
	// System.out.println(fields.length);
	// // iterate over fields, injecting values from the root composite item
	// // into the object
	// for (Field field : fields) {
	// System.out.println(field.getName());
	// if (IBaseItem.class.isAssignableFrom(field.getType())) {
	// Class<? extends IBaseItem> type = (Class<? extends IBaseItem>) field
	// .getType();
	// Class<?> realType = field.getType();
	// System.out.println(Arrays.toString(field
	// .getDeclaredAnnotations()));
	// if (field.isAnnotationPresent(Overlap2D.class)) {
	// System.out.println("annotation found");
	// String name = field.getName();
	// IBaseItem result = getRoot().getById(name, type);
	// System.out.println(result);
	// try {
	// field.set(object, realType.cast(result));
	// } catch (IllegalArgumentException | IllegalAccessException e) {
	// e.printStackTrace();
	// System.exit(-1);
	// }
	// }
	// }
	// }
	// }
}
