package com.uwsoft.editor.renderer.factory;

import box2dLight.RayHandler;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.World;
import com.uwsoft.editor.renderer.components.MainItemComponent;
import com.uwsoft.editor.renderer.components.light.LightObjectComponent;
import com.uwsoft.editor.renderer.factory.component.*;
import com.uwsoft.editor.renderer.legacy.data.CompositeItemVO;
import com.uwsoft.editor.renderer.legacy.data.LabelVO;
import com.uwsoft.editor.renderer.legacy.data.LightVO;
import com.uwsoft.editor.renderer.legacy.data.ParticleEffectVO;
import com.uwsoft.editor.renderer.legacy.data.SimpleImageVO;
import com.uwsoft.editor.renderer.legacy.data.SpineVO;
import com.uwsoft.editor.renderer.legacy.data.SpriteAnimationVO;
import com.uwsoft.editor.renderer.legacy.data.SpriterVO;
import com.uwsoft.editor.renderer.resources.IResourceRetriever;

import java.util.HashMap;

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

	private ComponentFactory compositeComponentFactory, lightComponentFactory, particleEffectComponentFactory,
			simpleImageComponentFactory, spineComponentFactory, spriteComponentFactory, spriterComponentFactory, labelComponentFactory;

	private int entityIterator = 0;

	private HashMap<Integer, Entity> entities = new HashMap<>();

	public ComponentFactory getCompositeComponentFactory() {
		return compositeComponentFactory;
	}

	public EntityFactory( RayHandler rayHandler, World world, IResourceRetriever rm ) {
	
		this.rayHandler = rayHandler;
		this.world = world;
		this.rm = rm;

		compositeComponentFactory = new CompositeComponentFactory(rayHandler, world, rm);
		lightComponentFactory = new LightComponentFactory(rayHandler, world, rm);
		particleEffectComponentFactory = new ParticleEffectComponentFactory(rayHandler, world, rm);
		simpleImageComponentFactory = new SimpleImageComponentFactory(rayHandler, world, rm);
		spineComponentFactory = new SpineComponentFactory(rayHandler, world, rm);
		spriteComponentFactory = new SpriteComponentFactory(rayHandler, world, rm);
		spriterComponentFactory = new SpriterComponentFactory(rayHandler, world, rm);
		labelComponentFactory = new LabelComponentFactory(rayHandler, world, rm);
	}


	public Entity createEntity(Entity root, SimpleImageVO vo){

		Entity entity = new Entity();

		simpleImageComponentFactory.createComponents(root, entity, vo);

		postProcessEntity(entity);
		
		return entity;
	}
	
	public Entity createEntity(Entity root, LabelVO vo) {
		
		Entity entity = new Entity();
		
		labelComponentFactory.createComponents(root, entity, vo);

		postProcessEntity(entity);

		return entity;
	}
	
	public Entity createEntity(Entity root, ParticleEffectVO vo){

		Entity entity = new Entity();
		
		particleEffectComponentFactory.createComponents(root, entity, vo);

		postProcessEntity(entity);
		
		return entity;
	}
	
	public Entity createEntity(Entity root, LightVO vo){

		Entity entity = new Entity();

		lightComponentFactory.createComponents(root, entity, vo);

		postProcessEntity(entity);
		
		return entity;
	}
	
	public Entity createEntity(Entity root, SpineVO vo){

		Entity entity = new Entity();

		spineComponentFactory.createComponents(root, entity, vo);

		postProcessEntity(entity);
		
		return entity;
	}
	
	public Entity createEntity(Entity root, SpriteAnimationVO vo){

		Entity entity = new Entity();

		spriteComponentFactory.createComponents(root, entity, vo);

		postProcessEntity(entity);
		
		return entity;
	}
	
	public Entity createEntity(Entity root, SpriterVO vo){

		Entity entity = new Entity();

		spriterComponentFactory.createComponents(root, entity, vo);

		postProcessEntity(entity);
		
		return entity;
	}
	
	public Entity createEntity(Entity root, CompositeItemVO vo){

		Entity entity = new Entity();

		compositeComponentFactory.createComponents(root, entity, vo);

		postProcessEntity(entity);
		
		return entity;
	}

	public Integer postProcessEntity(Entity entity) {
		ComponentMapper<MainItemComponent> mainItemComponentComponentMapper = ComponentMapper.getFor(MainItemComponent.class);
		MainItemComponent mainItemComponent = mainItemComponentComponentMapper.get(entity);
		mainItemComponent.uniqueId = entityIterator++;
		entities.put(mainItemComponent.uniqueId, entity);

		return mainItemComponent.uniqueId;
	}

	public Integer updateMap(Entity entity) {
		ComponentMapper<MainItemComponent> mainItemComponentComponentMapper = ComponentMapper.getFor(MainItemComponent.class);
		MainItemComponent mainItemComponent = mainItemComponentComponentMapper.get(entity);
		entities.put(mainItemComponent.uniqueId, entity);

		return mainItemComponent.uniqueId;
	}

	public Entity getEntityByUniqueId(Integer id) {
		return entities.get(id);
	}

	public void setResourceManager(IResourceRetriever rm) {
		this.rm = rm;

		//TODO: this is just a temporary solution caused by fact that we do not provide the correct rm at the beginning, and later change it
		// which has to be changed from the sceneLoader

		compositeComponentFactory.setResourceManager(rm);
		lightComponentFactory.setResourceManager(rm);
		particleEffectComponentFactory.setResourceManager(rm);
		simpleImageComponentFactory.setResourceManager(rm);
		spineComponentFactory.setResourceManager(rm);
		spriteComponentFactory.setResourceManager(rm);
		spriterComponentFactory.setResourceManager(rm);
		labelComponentFactory.setResourceManager(rm);
	}
	
}
