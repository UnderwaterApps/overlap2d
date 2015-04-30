package com.uwsoft.editor.renderer;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.brashmonkey.spriter.Player;
import com.brashmonkey.spriter.SCMLReader;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.BoneData;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.Slot;
import com.esotericsoftware.spine.attachments.Attachment;
import com.esotericsoftware.spine.attachments.RegionAttachment;
import com.uwsoft.editor.renderer.conponents.CompositeTransformComponent;
import com.uwsoft.editor.renderer.conponents.DimensionsComponent;
import com.uwsoft.editor.renderer.conponents.LayerMapComponent;
import com.uwsoft.editor.renderer.conponents.MainItemComponent;
import com.uwsoft.editor.renderer.conponents.NodeComponent;
import com.uwsoft.editor.renderer.conponents.ParentNodeComponent;
import com.uwsoft.editor.renderer.conponents.TextureRegionComponent;
import com.uwsoft.editor.renderer.conponents.TintComponent;
import com.uwsoft.editor.renderer.conponents.TransformComponent;
import com.uwsoft.editor.renderer.conponents.ViewPortComponent;
import com.uwsoft.editor.renderer.conponents.ZindexComponent;
import com.uwsoft.editor.renderer.conponents.light.LightObjectComponent;
import com.uwsoft.editor.renderer.conponents.particle.ParticleCompononet;
import com.uwsoft.editor.renderer.conponents.physics.MeshComponont;
import com.uwsoft.editor.renderer.conponents.physics.PhysicsBodyComponent;
import com.uwsoft.editor.renderer.conponents.physics.PhysicsBodyPropertiesComponent;
import com.uwsoft.editor.renderer.conponents.spine.SpineDataComponent;
import com.uwsoft.editor.renderer.conponents.sprite.AnimationComponent;
import com.uwsoft.editor.renderer.conponents.sprite.SpriteAnimationComponent;
import com.uwsoft.editor.renderer.conponents.sprite.SpriteAnimationStateComponent;
import com.uwsoft.editor.renderer.conponents.spriter.SpriterComponent;
import com.uwsoft.editor.renderer.conponents.spriter.SpriterDrawerComponent;
import com.uwsoft.editor.renderer.legacy.data.CompositeItemVO;
import com.uwsoft.editor.renderer.legacy.data.CompositeVO;
import com.uwsoft.editor.renderer.legacy.data.LightVO;
import com.uwsoft.editor.renderer.legacy.data.LightVO.LightType;
import com.uwsoft.editor.renderer.legacy.data.ParticleEffectVO;
import com.uwsoft.editor.renderer.legacy.data.SceneVO;
import com.uwsoft.editor.renderer.legacy.data.SimpleImageVO;
import com.uwsoft.editor.renderer.legacy.data.SpineVO;
import com.uwsoft.editor.renderer.legacy.data.SpriteAnimationVO;
import com.uwsoft.editor.renderer.legacy.data.SpriterVO;
import com.uwsoft.editor.renderer.physics.PhysicsBodyLoader;
import com.uwsoft.editor.renderer.resources.ResourceManager;
import com.uwsoft.editor.renderer.systems.LayerSystem;
import com.uwsoft.editor.renderer.systems.LightSystem;
import com.uwsoft.editor.renderer.systems.ParticleSystem;
import com.uwsoft.editor.renderer.systems.PhysicsSystem;
import com.uwsoft.editor.renderer.systems.SpineSystem;
import com.uwsoft.editor.renderer.systems.SpriteAnimationSystem;
import com.uwsoft.editor.renderer.utils.LibGdxDrawer;
import com.uwsoft.editor.renderer.utils.LibGdxLoader;

/**
 * SceneLoader is importatn part of runtime that utilizes provided
 * IResourceRetriever (or creates default one shipped with runtime) in order to
 * load entire scene data into viewable actors provides the functionality to get
 * root actor of scene and load scenes.
 *
 * Created by azakhary on 9/9/2014.
 */
public class SceneLoader {

	private String curResolution = "orig";
	private SceneVO sceneVO;
	private ResourceManager rm = null;
	private ComponentMapper<NodeComponent> nodeComponentMapper = ComponentMapper.getFor(NodeComponent.class);
	public Engine engine = null;
	public RayHandler rayHandler;
	public World world;

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
		NodeComponent nodeComponent = nodeComponentMapper.get(root);
		for (int i = 0; i < vo.sImages.size(); i++) {
			SimpleImageVO imageVo = vo.sImages.get(i);

			MainItemComponent mainComponent = new MainItemComponent();
			mainComponent.customVars = imageVo.customVars;
			mainComponent.itemIdentifier = imageVo.itemIdentifier;
			mainComponent.itemName = imageVo.itemName;
			mainComponent.tags = imageVo.tags;

			TransformComponent transform = new TransformComponent();
			transform.rotation = imageVo.rotation;
			transform.scaleX = imageVo.scaleX;
			transform.scaleY = imageVo.scaleY;
			transform.x = imageVo.x;
			transform.y = imageVo.y;

			TintComponent tint = new TintComponent();
			tint.tint = imageVo.tint;

			ZindexComponent layer = new ZindexComponent();
			layer.layerName = imageVo.layerName;
			layer.zIndex = imageVo.zIndex;

			TextureRegionComponent textureRegion = new TextureRegionComponent();
			textureRegion.regionName = imageVo.imageName;
			textureRegion.region = rm.getTextureRegion(imageVo.imageName);

			ParentNodeComponent parentNodeComponent = new ParentNodeComponent();
			parentNodeComponent.parentEntity = root;
			
			PhysicsBodyPropertiesComponent pysicsComponent = new PhysicsBodyPropertiesComponent();
			pysicsComponent.allowSleep = imageVo.physicsBodyData.allowSleep;
			pysicsComponent.awake = imageVo.physicsBodyData.awake;
			pysicsComponent.bodyType = imageVo.physicsBodyData.bodyType;
			pysicsComponent.bullet = imageVo.physicsBodyData.bullet;
			pysicsComponent.centerOfMass = imageVo.physicsBodyData.centerOfMass;
			pysicsComponent.damping = imageVo.physicsBodyData.damping;
			pysicsComponent.density = imageVo.physicsBodyData.density;
			pysicsComponent.friction = imageVo.physicsBodyData.friction;
			pysicsComponent.gravityScale = imageVo.physicsBodyData.gravityScale;
			pysicsComponent.mass = imageVo.physicsBodyData.mass;
			pysicsComponent.restitution = imageVo.physicsBodyData.restitution;
			pysicsComponent.rotationalInertia = imageVo.physicsBodyData.rotationalInertia;
			
			
			
			MeshComponont meshComponent = new MeshComponont();
			meshComponent.meshId = imageVo.meshId;
			meshComponent.minPolygonData = rm.getProjectVO().meshes.get(imageVo.meshId).minPolygonData; 
			
			
			PhysicsBodyComponent bodyComponent = new PhysicsBodyComponent(); 
			bodyComponent.body = PhysicsBodyLoader.createBody(world, pysicsComponent,meshComponent.minPolygonData, new Vector2(1, 1)); //TODO resolution thing 
			
			Entity entity = new Entity();
			entity.add(mainComponent);
			entity.add(transform);
			entity.add(tint);
			entity.add(layer);
			entity.add(textureRegion);
			entity.add(parentNodeComponent);
			entity.add(pysicsComponent);
			entity.add(meshComponent);

			engine.addEntity(entity);
			nodeComponent.children.add(entity);

		}
		
		//TODO after drawable mechanics is done
		for (int i = 0; i < vo.sImage9patchs.size(); i++) {
			// Image9patchVO imageVo = vo.sImage9patchs.get(i);
			//
			// MainItemComponent mainComponent = new MainItemComponent();
			// mainComponent.customVars = imageVo.customVars;
			// mainComponent.itemIdentifier = imageVo.itemIdentifier;
			// mainComponent.itemName = imageVo.itemName;
			// mainComponent.tags = imageVo.tags;
			//
			// TransformComponent transform = new TransformComponent();
			// transform.rotation = imageVo.rotation;
			// transform.scaleX = imageVo.scaleX;
			// transform.scaleY = imageVo.scaleY;
			// transform.x = imageVo.x;
			// transform.y = imageVo.y;
			//
			// TintComponent tint = new TintComponent();
			// tint.tint = imageVo.tint;
			//
			// LayerComponent layer = new LayerComponent();
			// layer.layerName = imageVo.layerName;
			// layer.zIndex = imageVo.zIndex;
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
			//
			// Entity entity = new Entity();
			// entity.add(mainComponent);
			// entity.add(transform);
			// entity.add(tint);
			// entity.add(layer);
			// entity.add(ninePatchComponent);
			//
			// engine.addEntity(entity);
			// nodeComponent.children.add(entity);
			// TODO NIN PTACH SYSTEM
		}

		for (int i = 0; i < vo.sTextBox.size(); i++) {

		}

		for (int i = 0; i < vo.sButtons.size(); i++) {

		}

		for (int i = 0; i < vo.sLabels.size(); i++) {

		}

		for (int i = 0; i < vo.sCheckBoxes.size(); i++) {

		}

		for (int i = 0; i < vo.sSelectBoxes.size(); i++) {

		}

		for (int i = 0; i < vo.sParticleEffects.size(); i++) {
			ParticleEffectVO particleVo = vo.sParticleEffects.get(i);

			MainItemComponent mainComponent = new MainItemComponent();
			mainComponent.customVars = particleVo.customVars;
			mainComponent.itemIdentifier = particleVo.itemIdentifier;
			mainComponent.itemName = particleVo.itemName;
			mainComponent.tags = particleVo.tags;

			TransformComponent transform = new TransformComponent();
			transform.rotation = particleVo.rotation;
			transform.scaleX = particleVo.scaleX;
			transform.scaleY = particleVo.scaleY;
			transform.x = particleVo.x;
			transform.y = particleVo.y;

			TintComponent tint = new TintComponent();
			tint.tint = particleVo.tint;

			ZindexComponent layer = new ZindexComponent();
			layer.layerName = particleVo.layerName;
			layer.zIndex = particleVo.zIndex;

			DimensionsComponent dimensionsComponent = new DimensionsComponent();
			dimensionsComponent.height = 100;
			dimensionsComponent.width = 100;

			// TextureRegionComponent textureRegion = new
			// TextureRegionComponent();
			// textureRegion.regionName = particleVo.imageName;
			// textureRegion.region = rm.getTextureRegion(particleVo.imageName);

			ParentNodeComponent parentNodeComponent = new ParentNodeComponent();
			parentNodeComponent.parentEntity = root;

			ParticleCompononet particleComponent = new ParticleCompononet();
			particleComponent.particleName = particleVo.particleName;
			ParticleEffect particleEffect = new ParticleEffect(
					rm.getParticleEffect(particleVo.particleName));
			particleComponent.particleEffect = particleEffect;
			Entity entity = new Entity();
			entity.add(mainComponent);
			entity.add(transform);
			entity.add(tint);
			entity.add(layer);
			entity.add(particleComponent);
			entity.add(parentNodeComponent);
			entity.add(dimensionsComponent);

			engine.addEntity(entity);
			nodeComponent.children.add(entity);
		}

		for (int i = 0; i < vo.sLights.size(); i++) {
			LightVO lightVo = vo.sLights.get(i);

			MainItemComponent mainComponent = new MainItemComponent();
			mainComponent.customVars = lightVo.customVars;
			mainComponent.itemIdentifier = lightVo.itemIdentifier;
			mainComponent.itemName = lightVo.itemName;
			mainComponent.tags = lightVo.tags;

			TransformComponent transform = new TransformComponent();
			transform.rotation = lightVo.rotation;
			transform.scaleX = lightVo.scaleX;
			transform.scaleY = lightVo.scaleY;
			transform.x = lightVo.x;
			transform.y = lightVo.y;

			TintComponent tint = new TintComponent();
			tint.tint = lightVo.tint;

			ZindexComponent layer = new ZindexComponent();
			layer.layerName = lightVo.layerName;
			layer.zIndex = lightVo.zIndex;

			DimensionsComponent dimensionsComponent = new DimensionsComponent();
			dimensionsComponent.height = 40;
			dimensionsComponent.width = 40;

			// TextureRegionComponent textureRegion = new
			// TextureRegionComponent();
			// textureRegion.regionName = particleVo.imageName;
			// textureRegion.region = rm.getTextureRegion(particleVo.imageName);

			ParentNodeComponent parentNodeComponent = new ParentNodeComponent();
			parentNodeComponent.parentEntity = root;

			LightObjectComponent lightObjectComponent = new LightObjectComponent();
			lightObjectComponent.coneDegree = lightVo.coneDegree;
			lightObjectComponent.directionDegree = lightVo.directionDegree;
			lightObjectComponent.distance = lightVo.distance;
			lightObjectComponent.isStatic = lightVo.isStatic;
			lightObjectComponent.isXRay = lightVo.isXRay;
			lightObjectComponent.rays = lightVo.rays;
			lightObjectComponent.type = lightVo.type;

			if (lightObjectComponent.type == LightType.POINT) {
				lightObjectComponent.lightObject = new PointLight(rayHandler,
						lightObjectComponent.rays);
				// Color asd = new Color(vo.tint);
				lightObjectComponent.lightObject
						.setColor(new Color(tint.tint[0], tint.tint[1],
								tint.tint[2], tint.tint[3]));
				// TODO Physics and resolution part
				lightObjectComponent.lightObject
						.setDistance(lightObjectComponent.distance
								* PhysicsBodyLoader.SCALE);
				lightObjectComponent.lightObject.setPosition(transform.x
						* PhysicsBodyLoader.SCALE, transform.y
						* PhysicsBodyLoader.SCALE);
				lightObjectComponent.lightObject
						.setStaticLight(lightObjectComponent.isStatic);
				lightObjectComponent.lightObject.setActive(true);
				lightObjectComponent.lightObject
						.setXray(lightObjectComponent.isXRay);

			} else {
				lightObjectComponent.lightObject = new ConeLight(rayHandler,
						lightObjectComponent.rays, Color.WHITE, 1, 0, 0, 0, 0);
				// Color asd = new Color(vo.tint);
				lightObjectComponent.lightObject
						.setColor(new Color(tint.tint[0], tint.tint[1],
								tint.tint[2], tint.tint[3]));
				lightObjectComponent.lightObject
						.setDistance(lightObjectComponent.distance
								* PhysicsBodyLoader.SCALE);
				lightObjectComponent.lightObject.setPosition(transform.x
						* PhysicsBodyLoader.SCALE, transform.y
						* PhysicsBodyLoader.SCALE);
				lightObjectComponent.lightObject
						.setStaticLight(lightObjectComponent.isStatic);
				lightObjectComponent.lightObject
						.setDirection(lightObjectComponent.directionDegree);
				((ConeLight) lightObjectComponent.lightObject)
						.setConeDegree(lightObjectComponent.coneDegree);
				lightObjectComponent.lightObject.setActive(true);
				lightObjectComponent.lightObject
						.setXray(lightObjectComponent.isXRay);
			}

			Entity entity = new Entity();
			entity.add(mainComponent);
			entity.add(transform);
			entity.add(tint);
			entity.add(layer);
			entity.add(lightObjectComponent);
			entity.add(parentNodeComponent);
			entity.add(dimensionsComponent);

			engine.addEntity(entity);
			nodeComponent.children.add(entity);
		}

		for (int i = 0; i < vo.sSpineAnimations.size(); i++) {
			SpineVO spineVo = vo.sSpineAnimations.get(i);

			MainItemComponent mainComponent = new MainItemComponent();
			mainComponent.customVars = spineVo.customVars;
			mainComponent.itemIdentifier = spineVo.itemIdentifier;
			mainComponent.itemName = spineVo.itemName;
			mainComponent.tags = spineVo.tags;

			TransformComponent transform = new TransformComponent();
			transform.rotation = spineVo.rotation;
			transform.scaleX = spineVo.scaleX;
			transform.scaleY = spineVo.scaleY;
			transform.x = spineVo.x;
			transform.y = spineVo.y;

			TintComponent tint = new TintComponent();
			tint.tint = spineVo.tint;

			ZindexComponent layer = new ZindexComponent();
			layer.layerName = spineVo.layerName;
			layer.zIndex = spineVo.zIndex;

			DimensionsComponent dimensionsComponent = new DimensionsComponent();
			dimensionsComponent.height = 100;
			dimensionsComponent.width = 100;

			ParentNodeComponent parentNodeComponent = new ParentNodeComponent();
			parentNodeComponent.parentEntity = root;

			SpineDataComponent spineDataComponent = new SpineDataComponent();
			spineDataComponent.animationName = spineVo.animationName;
			spineDataComponent.skeletonJson = new SkeletonJson(rm.getSkeletonAtlas(spineDataComponent.animationName));
			spineDataComponent.skeletonData = spineDataComponent.skeletonJson.readSkeletonData((rm.getSkeletonJSON(spineDataComponent.animationName)));
			
			BoneData rootBone = spineDataComponent.skeletonData.getBones().get(0); // this has to be the root bone.
			rootBone.setScale(transform.scaleX, transform.scaleY); //TODO resolution part
	        spineDataComponent.skeleton = new Skeleton(spineDataComponent.skeletonData); // Skeleton holds skeleton state (bone positions, slot attachments, etc).
	        AnimationStateData stateData = new AnimationStateData(spineDataComponent.skeletonData); // Defines mixing (crossfading) between animations.
	        spineDataComponent.state = new AnimationState(stateData); // Holds the animation state for a skeleton (current animation, time, etc).
	        spineDataComponent.computeBoundBox(dimensionsComponent);
	        // todo: fix this, it's a temporary soluition
	        spineDataComponent.setAnimation(spineDataComponent.currentAnimationName.isEmpty() ? spineDataComponent.skeletonData.getAnimations().get(0).getName() : spineDataComponent.currentAnimationName);
			
			Entity entity = new Entity();
			entity.add(mainComponent);
			entity.add(transform);
			entity.add(tint);
			entity.add(layer);
			entity.add(spineDataComponent);
			entity.add(parentNodeComponent);
			entity.add(dimensionsComponent);

			engine.addEntity(entity);
			nodeComponent.children.add(entity);
		}

		for (int i = 0; i < vo.sSpriteAnimations.size(); i++) {
			SpriteAnimationVO spriteVo = vo.sSpriteAnimations.get(i);

			MainItemComponent mainComponent = new MainItemComponent();
			mainComponent.customVars = spriteVo.customVars;
			mainComponent.itemIdentifier = spriteVo.itemIdentifier;
			mainComponent.itemName = spriteVo.itemName;
			mainComponent.tags = spriteVo.tags;

			TransformComponent transform = new TransformComponent();
			transform.rotation = spriteVo.rotation;
			transform.scaleX = spriteVo.scaleX;
			transform.scaleY = spriteVo.scaleY;
			transform.x = spriteVo.x;
			transform.y = spriteVo.y;

			TintComponent tint = new TintComponent();
			tint.tint = spriteVo.tint;

			ZindexComponent layer = new ZindexComponent();
			layer.layerName = spriteVo.layerName;
			layer.zIndex = spriteVo.zIndex;

			// DimensionsComponent dimensionsComponent = new
			// DimensionsComponent();
			// dimensionsComponent.height = 100;
			// dimensionsComponent.width = 100;

			ParentNodeComponent parentNodeComponent = new ParentNodeComponent();
			parentNodeComponent.parentEntity = root;

			SpriteAnimationComponent spriteComponent = new SpriteAnimationComponent();
			spriteComponent.animationName = spriteVo.animationName;
			spriteComponent.animations = spriteVo.animations;
			spriteComponent.fps = spriteVo.fps;
			
			Array<AtlasRegion> regions = sortAndGetRegions(spriteComponent.animationName);

			AnimationComponent animationComponent = new AnimationComponent();
			SpriteAnimationStateComponent stateComponent = new SpriteAnimationStateComponent();

			if (!spriteComponent.animations.isEmpty()) {
				spriteComponent.keyFrames = Frames
						.constructJsonObject(spriteComponent.animations);
				for (Map.Entry<String, Frames> entry : spriteComponent.keyFrames
						.entrySet()) {
					Frames keyFrame = entry.getValue();

					Array<AtlasRegion> tmpRegions = new Array<TextureAtlas.AtlasRegion>(keyFrame.endFrame - keyFrame.startFrame + 1);
					for (int r = keyFrame.startFrame; r <= keyFrame.endFrame; r++) {
						tmpRegions.add(regions.get(r));
					}
					Animation anim = new Animation(1f / spriteComponent.fps,tmpRegions, PlayMode.LOOP);
					animationComponent.animations.put(entry.getKey(), anim);
					stateComponent.set(anim);
				}

			} else {
				animationComponent.animations.put("Default", new Animation(1f / spriteComponent.fps, regions, PlayMode.LOOP));
				// spriteComponent.animation =
				stateComponent.set(animationComponent.animations.get("Default"));
			}

			TextureRegionComponent textureRegion = new TextureRegionComponent();
			textureRegion.region = regions.get(0);

			Entity entity = new Entity();
			entity.add(mainComponent);
			entity.add(transform);
			entity.add(tint);
			entity.add(layer);
			entity.add(spriteComponent);
			entity.add(stateComponent);
			entity.add(parentNodeComponent);
			entity.add(textureRegion);

			engine.addEntity(entity);
			nodeComponent.children.add(entity);
		}

		for (int i = 0; i < vo.sSpriterAnimations.size(); i++) {

			SpriterVO spriterVo = vo.sSpriterAnimations.get(i);

			MainItemComponent mainComponent = new MainItemComponent();
			mainComponent.customVars = spriterVo.customVars;
			mainComponent.itemIdentifier = spriterVo.itemIdentifier;
			mainComponent.itemName = spriterVo.itemName;
			mainComponent.tags = spriterVo.tags;

			TransformComponent transform = new TransformComponent();
			transform.rotation = spriterVo.rotation;
			transform.scaleX = spriterVo.scaleX;
			transform.scaleY = spriterVo.scaleY;
			transform.x = spriterVo.x;
			transform.y = spriterVo.y;

			TintComponent tint = new TintComponent();
			tint.tint = spriterVo.tint;

			ZindexComponent layer = new ZindexComponent();
			layer.layerName = spriterVo.layerName;
			layer.zIndex = spriterVo.zIndex;

			// DimensionsComponent dimensionsComponent = new
			// DimensionsComponent();
			// dimensionsComponent.height = 100;
			// dimensionsComponent.width = 100;

			ParentNodeComponent parentNodeComponent = new ParentNodeComponent();
			parentNodeComponent.parentEntity = root;

			SpriterComponent spriterComponent = new SpriterComponent();			
			spriterComponent. entity = spriterVo.entity;
			spriterComponent.animation = spriterVo.animation;
			spriterComponent. animationName = spriterVo.animationName;
			spriterComponent.scale = spriterVo.scale;
			
			FileHandle handle 	=	rm.getSCMLFile(spriterVo.animationName);
			spriterComponent.data = new SCMLReader(handle.read()).getData();		
			LibGdxLoader loader = 	new LibGdxLoader(spriterComponent.data);
			loader.load(handle.file());

			spriterComponent.currentAnimationIndex	=	spriterVo.animation;	
			spriterComponent.currentEntityIndex		=	spriterVo.entity;
			
			spriterComponent.player = new Player(spriterComponent.data.getEntity(spriterComponent.currentEntityIndex));

			spriterComponent.player.setAnimation(spriterComponent.currentAnimationIndex);
			spriterComponent.player.setScale(spriterComponent.scale);
			
			SpriterDrawerComponent spriterDrawer = new SpriterDrawerComponent();
			
			spriterDrawer.drawer = new LibGdxDrawer(loader, null);

			Entity entity = new Entity();
			entity.add(mainComponent);
			entity.add(transform);
			entity.add(tint);
			entity.add(layer);
			entity.add(parentNodeComponent);
			entity.add(spriterComponent);
			entity.add(spriterDrawer);

			engine.addEntity(entity);
			nodeComponent.children.add(entity);

		}

		for (int i = 0; i < vo.layers.size(); i++) {
			//TODO wtf is this do we need this? :O
		}

		for (int i = 0; i < vo.sComposites.size(); i++) {
			CompositeItemVO compositeVo = vo.sComposites.get(i);

			MainItemComponent mainComponent = new MainItemComponent();
			mainComponent.customVars = compositeVo.customVars;
			mainComponent.itemIdentifier = compositeVo.itemIdentifier;
			mainComponent.itemName = compositeVo.itemName;
			mainComponent.tags = compositeVo.tags;

			CompositeTransformComponent transform = new CompositeTransformComponent();
			transform.rotation = compositeVo.rotation;
			transform.scaleX = compositeVo.scaleX;
			transform.scaleY = compositeVo.scaleY;
			transform.x = compositeVo.x;
			transform.y = compositeVo.y;

			TintComponent tint = new TintComponent();
			tint.tint = compositeVo.tint;

			ZindexComponent zComponent = new ZindexComponent();
			zComponent.layerName = compositeVo.layerName;
			zComponent.zIndex = compositeVo.zIndex;
			
			LayerMapComponent layerMap = new LayerMapComponent();
			layerMap.layers = vo.layers;

			NodeComponent node = new NodeComponent();
			node.parentEntity = root;

			Entity entity = new Entity();
			entity.add(mainComponent);
			entity.add(transform);
			entity.add(tint);
			entity.add(zComponent);
			entity.add(layerMap);
			entity.add(node);

			engine.addEntity(entity);
			nodeComponent.children.add(entity);
			initWithAshley(entity, compositeVo.composite);
		}
	}
	
	private Array<AtlasRegion> sortAndGetRegions(String animationName) {
		Array<TextureAtlas.AtlasRegion> regions = rm.getSpriteAnimation(animationName).getRegions();
		AtlasRegion[] animationAtlasRegions = new TextureAtlas.AtlasRegion[regions.size];
		for (int ri = 0; ri < regions.size; ri++) {
			String regName = regions.get(ri).name;
			animationAtlasRegions[regNameToFrame(regName) - 1] = regions.get(ri);
		}
		return new Array<TextureAtlas.AtlasRegion>(animationAtlasRegions);
	}

	private int regNameToFrame(String name) {
		final Pattern lastIntPattern = Pattern.compile("[^0-9]+([0-9]+)$");
		Matcher matcher = lastIntPattern.matcher(name);
		if (matcher.find()) {
			String someNumberStr = matcher.group(1);
			return Integer.parseInt(someNumberStr);
		}
		throw new RuntimeException(
				"Frame name should be something like this '*0001', but not "
						+ name + ".");
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
