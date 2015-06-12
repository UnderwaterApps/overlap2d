package com.uwsoft.editor.renderer.systems.render.logic;

import java.util.HashMap;

import com.uwsoft.editor.renderer.factory.EntityFactory;

public class DrawableLogicMapper {

	private HashMap<Integer, Drawable> logicClassMap;  
	
	public DrawableLogicMapper() {
		logicClassMap = new HashMap<Integer, Drawable>(6);
		logicClassMap.put(EntityFactory.IMAGE_TYPE, 	new TexturRegionDrawLogic());
		logicClassMap.put(EntityFactory.LABEL_TYPE, 	new LabelDrawableLogic());
		logicClassMap.put(EntityFactory.NINE_PATCH, 	new NinePatchDrawableLogic());
		logicClassMap.put(EntityFactory.PARTICLE_TYPE, 	new ParticleDrawableLogic());
		logicClassMap.put(EntityFactory.SPINE_TYPE, 	new SpineDrawableLogic());
		logicClassMap.put(EntityFactory.SPRITE_TYPE, 	new SpriteDrawableLogic());
		logicClassMap.put(EntityFactory.SPRITER_TYPE, 	new SpriterDrawableLogic());
	}

	public Drawable getDrawable(int type){
		return logicClassMap.get(type);
	}
}
