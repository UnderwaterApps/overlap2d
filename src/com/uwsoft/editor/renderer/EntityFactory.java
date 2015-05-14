package com.uwsoft.editor.renderer;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.brashmonkey.spriter.Player;
import com.brashmonkey.spriter.SCMLReader;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.BoneData;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonJson;
import com.uwsoft.editor.renderer.SceneLoader.Frames;
import com.uwsoft.editor.renderer.conponents.CompositeTransformComponent;
import com.uwsoft.editor.renderer.conponents.DimensionsComponent;
import com.uwsoft.editor.renderer.conponents.LayerMapComponent;
import com.uwsoft.editor.renderer.conponents.MainItemComponent;
import com.uwsoft.editor.renderer.conponents.NodeComponent;
import com.uwsoft.editor.renderer.conponents.ParentNodeComponent;
import com.uwsoft.editor.renderer.conponents.TextureRegionComponent;
import com.uwsoft.editor.renderer.conponents.TintComponent;
import com.uwsoft.editor.renderer.conponents.TransformComponent;
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
import com.uwsoft.editor.renderer.legacy.data.MainItemVO;
import com.uwsoft.editor.renderer.legacy.data.ParticleEffectVO;
import com.uwsoft.editor.renderer.legacy.data.SimpleImageVO;
import com.uwsoft.editor.renderer.legacy.data.SpineVO;
import com.uwsoft.editor.renderer.legacy.data.SpriteAnimationVO;
import com.uwsoft.editor.renderer.legacy.data.LightVO.LightType;
import com.uwsoft.editor.renderer.legacy.data.SpriterVO;
import com.uwsoft.editor.renderer.physics.PhysicsBodyLoader;
import com.uwsoft.editor.renderer.resources.IResourceRetriever;
import com.uwsoft.editor.renderer.resources.ResourceManager;
import com.uwsoft.editor.renderer.utils.LibGdxDrawer;
import com.uwsoft.editor.renderer.utils.LibGdxLoader;

public class EntityFactory {
	
	public static final int IMAGE_TYPE 		= 1;
	public static final int LABEL_TYPE 		= 2;
	public static final int SPRITE_TYPE 	= 3;
	public static final int SPINE_TYPE 		= 4;
	public static final int SPRITER_TYPE 	= 5;
	public static final int COMPOSITE_TYPE 	= 6;
	public static final int PARTICLE_TYPE 	= 7;
	public static final int LIGHT_TYPE 		= 8;
	public static final int NINE_PATCH 		= 9;
	
	public RayHandler rayHandler;
	public World world;
	public IResourceRetriever rm = null;
	
	private ComponentMapper<NodeComponent> nodeComponentMapper;
	
	public EntityFactory( RayHandler rayHandler, World world, IResourceRetriever rm ) {
	
		this.rayHandler = rayHandler;
		this.world = world;
		this.rm = rm;
		nodeComponentMapper = ComponentMapper.getFor(NodeComponent.class);
		
	}



	public Entity createEntity(Entity root, SimpleImageVO vo){

		Entity entity = new Entity();
		entity.flags = IMAGE_TYPE;
		
		addCommonComponents(entity,vo);
		addParentComponent(entity,root);
		addPhysicsComponents(entity, vo);
		
		TextureRegionComponent textureRegion = new TextureRegionComponent();
		textureRegion.regionName = vo.imageName;
		textureRegion.region = rm.getTextureRegion(vo.imageName);
		
		DimensionsComponent dimensionsComponent = new DimensionsComponent();
		dimensionsComponent.height = textureRegion.region.getRegionHeight();
		dimensionsComponent.width = textureRegion.region.getRegionWidth();
		
		entity.add(textureRegion);
		entity.add(dimensionsComponent);
		
		return entity;
	}
	
	public Entity createEntity(Entity root, ParticleEffectVO vo){

		Entity entity = new Entity();
		entity.flags = PARTICLE_TYPE;
		
		addCommonComponents(entity,vo);
		addParentComponent(entity,root);
		addPhysicsComponents(entity, vo);
		
		DimensionsComponent dimensionsComponent = new DimensionsComponent();
		dimensionsComponent.height = 100;
		dimensionsComponent.width = 100;
		
		ParticleCompononet particleComponent = new ParticleCompononet();
		particleComponent.particleName = vo.particleName;
		ParticleEffect particleEffect = new ParticleEffect(rm.getParticleEffect(vo.particleName));
		particleComponent.particleEffect = particleEffect;
		
		entity.add(dimensionsComponent);
		entity.add(particleComponent);
		
		return entity;
	}
	
	public Entity createEntity(Entity root, LightVO vo){

		Entity entity = new Entity();
		entity.flags = LIGHT_TYPE;
		
		addCommonComponents(entity,vo);
		addParentComponent(entity,root);
		addPhysicsComponents(entity, vo);
		
		DimensionsComponent dimensionsComponent = new DimensionsComponent();
		dimensionsComponent.height = 40;
		dimensionsComponent.width = 40;
		
		LightObjectComponent lightObjectComponent = new LightObjectComponent();
		lightObjectComponent.coneDegree = vo.coneDegree;
		lightObjectComponent.directionDegree = vo.directionDegree;
		lightObjectComponent.distance = vo.distance;
		lightObjectComponent.isStatic = vo.isStatic;
		lightObjectComponent.isXRay = vo.isXRay;
		lightObjectComponent.rays = vo.rays;
		lightObjectComponent.type = vo.type;

		if (lightObjectComponent.type == LightType.POINT) {
			lightObjectComponent.lightObject = new PointLight(rayHandler, lightObjectComponent.rays);
			lightObjectComponent.lightObject.setColor(new Color(vo.tint[0], vo.tint[1], vo.tint[2], vo.tint[3]));
			// TODO Physics and resolution part
			lightObjectComponent.lightObject.setDistance(lightObjectComponent.distance * PhysicsBodyLoader.SCALE);
			lightObjectComponent.lightObject.setPosition(vo.x * PhysicsBodyLoader.SCALE, vo.y * PhysicsBodyLoader.SCALE);
			lightObjectComponent.lightObject.setStaticLight(lightObjectComponent.isStatic);
			lightObjectComponent.lightObject.setActive(true);
			lightObjectComponent.lightObject.setXray(lightObjectComponent.isXRay);

		} else {
			lightObjectComponent.lightObject = new ConeLight(rayHandler, lightObjectComponent.rays, Color.WHITE, 1, 0, 0, 0, 0);
			// Color asd = new Color(vo.tint);
			lightObjectComponent.lightObject.setColor(new Color(vo.tint[0], vo.tint[1], vo.tint[2], vo.tint[3]));
			lightObjectComponent.lightObject.setDistance(lightObjectComponent.distance * PhysicsBodyLoader.SCALE);
			lightObjectComponent.lightObject.setPosition(vo.x * PhysicsBodyLoader.SCALE, vo.y * PhysicsBodyLoader.SCALE);
			lightObjectComponent.lightObject.setStaticLight(lightObjectComponent.isStatic);
			lightObjectComponent.lightObject.setDirection(lightObjectComponent.directionDegree);
			((ConeLight) lightObjectComponent.lightObject).setConeDegree(lightObjectComponent.coneDegree);
			lightObjectComponent.lightObject.setActive(true);
			lightObjectComponent.lightObject.setXray(lightObjectComponent.isXRay);
		}

		
		entity.add(dimensionsComponent);
		entity.add(lightObjectComponent);
		
		return entity;
	}
	
	public Entity createEntity(Entity root, SpineVO vo){

		Entity entity = new Entity();
		entity.flags = SPINE_TYPE;
		
		addCommonComponents(entity,vo);
		addParentComponent(entity,root);
		addPhysicsComponents(entity, vo);
		
		DimensionsComponent dimensionsComponent = new DimensionsComponent();
		dimensionsComponent.height = 100;
		dimensionsComponent.width = 100;
		
		SpineDataComponent spineDataComponent = new SpineDataComponent();
		spineDataComponent.animationName = vo.animationName;
		spineDataComponent.skeletonJson = new SkeletonJson(rm.getSkeletonAtlas(spineDataComponent.animationName));
		spineDataComponent.skeletonData = spineDataComponent.skeletonJson.readSkeletonData((rm.getSkeletonJSON(spineDataComponent.animationName)));
		
		BoneData rootBone = spineDataComponent.skeletonData.getBones().get(0); // this has to be the root bone.
		rootBone.setScale(vo.scaleX, vo.scaleY); //TODO resolution part
        spineDataComponent.skeleton = new Skeleton(spineDataComponent.skeletonData); // Skeleton holds skeleton state (bone positions, slot attachments, etc).
        AnimationStateData stateData = new AnimationStateData(spineDataComponent.skeletonData); // Defines mixing (crossfading) between animations.
        spineDataComponent.state = new AnimationState(stateData); // Holds the animation state for a skeleton (current animation, time, etc).
        spineDataComponent.computeBoundBox(dimensionsComponent);
        // todo: fix this, it's a temporary solution
        spineDataComponent.setAnimation(spineDataComponent.currentAnimationName.isEmpty() ? spineDataComponent.skeletonData.getAnimations().get(0).getName() : spineDataComponent.currentAnimationName);
		
		entity.add(dimensionsComponent);
		entity.add(spineDataComponent);
		
		return entity;
	}
	
	public Entity createEntity(Entity root, SpriteAnimationVO vo){

		Entity entity = new Entity();
		entity.flags = SPRITE_TYPE;
		
		addCommonComponents(entity,vo);
		addParentComponent(entity,root);
		addPhysicsComponents(entity, vo);
		
		SpriteAnimationComponent spriteComponent = new SpriteAnimationComponent();
		spriteComponent.animationName = vo.animationName;
		spriteComponent.animations = vo.animations;
		spriteComponent.fps = vo.fps;
		
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

		TextureRegionComponent textureRegionComponent = new TextureRegionComponent();
		textureRegionComponent.region = regions.get(0);
		
		
		entity.add(spriteComponent);
		entity.add(animationComponent);
		entity.add(stateComponent);
		entity.add(textureRegionComponent);
		
		return entity;
	}
	
	public Entity createEntity(Entity root, SpriterVO vo){

		Entity entity = new Entity();
		entity.flags = SPRITER_TYPE;
		
		addCommonComponents(entity,vo);
		addParentComponent(entity,root);
		addPhysicsComponents(entity, vo);
		
		DimensionsComponent dimensionsComponent = new DimensionsComponent();
		dimensionsComponent.height = 100;
		dimensionsComponent.width = 100;
		
		SpriterComponent spriterComponent = new SpriterComponent();			
		spriterComponent. entity = vo.entity;
		spriterComponent.animation = vo.animation;
		spriterComponent. animationName = vo.animationName;
		spriterComponent.scale = vo.scale;
		
		FileHandle handle 	=	rm.getSCMLFile(vo.animationName);
		spriterComponent.data = new SCMLReader(handle.read()).getData();		
		LibGdxLoader loader = 	new LibGdxLoader(spriterComponent.data);
		loader.load(handle.file());

		spriterComponent.currentAnimationIndex	=	vo.animation;	
		spriterComponent.currentEntityIndex		=	vo.entity;
		
		spriterComponent.player = new Player(spriterComponent.data.getEntity(spriterComponent.currentEntityIndex));

		spriterComponent.player.setAnimation(spriterComponent.currentAnimationIndex);
		spriterComponent.player.setScale(spriterComponent.scale);
		
		SpriterDrawerComponent spriterDrawer = new SpriterDrawerComponent();
		
		spriterDrawer.drawer = new LibGdxDrawer(loader, null);
		
		entity.add(dimensionsComponent);
		entity.add(spriterComponent);
		entity.add(spriterDrawer);
		
		return entity;
	}
	
	public Entity createEntity(Entity root, CompositeItemVO vo){

		Entity entity = new Entity();
		entity.flags = COMPOSITE_TYPE;
		
		addCommonComponents(entity,vo,vo.composite);
		addParentComponent(entity,root);
		addPhysicsComponents(entity, vo);
		
		return entity;
	}

	public void addCommonComponents(Entity entity, MainItemVO vo){
		MainItemComponent mainComponent = new MainItemComponent();
		mainComponent.customVars = vo.customVars;
		mainComponent.itemIdentifier = vo.itemIdentifier;
		mainComponent.itemName = vo.itemName;
		mainComponent.tags = vo.tags;

		TransformComponent transform = new TransformComponent();
		transform.rotation = vo.rotation;
		transform.scaleX = vo.scaleX;
		transform.scaleY = vo.scaleY;
		transform.x = vo.x;
		transform.y = vo.y;

		TintComponent tint = new TintComponent();
		tint.tint = vo.tint;

		ZindexComponent zComponent = new ZindexComponent();
		zComponent.layerName = vo.layerName;
		zComponent.zIndex = vo.zIndex;
		
		entity.add(mainComponent);
		entity.add(transform);
		entity.add(tint);
		entity.add(zComponent);
		
	}
	
	public void addCommonComponents(Entity entity, CompositeItemVO vo, CompositeVO composite){
		MainItemComponent mainComponent = new MainItemComponent();
		mainComponent.customVars = vo.customVars;
		mainComponent.itemIdentifier = vo.itemIdentifier;
		mainComponent.itemName = vo.itemName;
		mainComponent.tags = vo.tags;

		CompositeTransformComponent transform = new CompositeTransformComponent();
		transform.rotation = vo.rotation;
		transform.scaleX = vo.scaleX;
		transform.scaleY = vo.scaleY;
		transform.x = vo.x;
		transform.y = vo.y;

		TintComponent tint = new TintComponent();
		tint.tint = vo.tint;

		ZindexComponent zComponent = new ZindexComponent();
		zComponent.layerName = vo.layerName;
		zComponent.zIndex = vo.zIndex;
		
		LayerMapComponent layerMap = new LayerMapComponent();
		layerMap.layers = composite.layers;
		
		entity.add(mainComponent);
		entity.add(transform);
		entity.add(tint);
		entity.add(zComponent);
		entity.add(layerMap);
		
	}
	
	public void addPhysicsComponents(Entity entity, MainItemVO vo){
		if(vo.physicsBodyData == null){
			return;
		}
		PhysicsBodyPropertiesComponent pysicsComponent = new PhysicsBodyPropertiesComponent();
		pysicsComponent.allowSleep = vo.physicsBodyData.allowSleep;
		pysicsComponent.awake = vo.physicsBodyData.awake;
		pysicsComponent.bodyType = vo.physicsBodyData.bodyType;
		pysicsComponent.bullet = vo.physicsBodyData.bullet;
		pysicsComponent.centerOfMass = vo.physicsBodyData.centerOfMass;
		pysicsComponent.damping = vo.physicsBodyData.damping;
		pysicsComponent.density = vo.physicsBodyData.density;
		pysicsComponent.friction = vo.physicsBodyData.friction;
		pysicsComponent.gravityScale = vo.physicsBodyData.gravityScale;
		pysicsComponent.mass = vo.physicsBodyData.mass;
		pysicsComponent.restitution = vo.physicsBodyData.restitution;
		pysicsComponent.rotationalInertia = vo.physicsBodyData.rotationalInertia;
		
		MeshComponont meshComponent = new MeshComponont();
		meshComponent.meshId = vo.meshId;
		meshComponent.minPolygonData = rm.getProjectVO().meshes.get(vo.meshId).minPolygonData; 
		
		PhysicsBodyComponent bodyComponent = new PhysicsBodyComponent(); 
		bodyComponent.body = PhysicsBodyLoader.createBody(world, pysicsComponent,meshComponent.minPolygonData, new Vector2(1, 1)); //TODO resolution thing 
		
		entity.add(pysicsComponent);
		entity.add(meshComponent);
		entity.add(bodyComponent);
	}
	
	
	public void addParentComponent(Entity entity, Entity root) {
		ParentNodeComponent parentNodeComponent = new ParentNodeComponent();
		parentNodeComponent.parentEntity = root;
		entity.add(parentNodeComponent);
		
		NodeComponent nodeComponent = nodeComponentMapper.get(root);
		nodeComponent.children.add(entity);
		
		if(entity.flags == COMPOSITE_TYPE){
			NodeComponent node = new NodeComponent();
			entity.add(node);
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



	public void setResourceManager(IResourceRetriever rm) {
		this.rm = rm;
	}
	
}
