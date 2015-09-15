package com.uwsoft.editor.renderer.systems.render.logic;

import java.util.HashMap;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.uwsoft.editor.renderer.factory.EntityFactory;

public class DrawableLogicMapper {

	private HashMap<Integer, Drawable> logicClassMap;

	public DrawableLogicMapper() {
		logicClassMap = new HashMap<Integer, Drawable>(6);
		logicClassMap.put(EntityFactory.IMAGE_TYPE, 	new TexturRegionDrawLogic());
		logicClassMap.put(EntityFactory.LABEL_TYPE, 	new LabelDrawableLogic());
		logicClassMap.put(EntityFactory.NINE_PATCH, 	new NinePatchDrawableLogic());
		logicClassMap.put(EntityFactory.PARTICLE_TYPE, 	new ParticleDrawableLogic());
		logicClassMap.put(EntityFactory.SPRITE_TYPE, 	new SpriteDrawableLogic());
		logicClassMap.put(EntityFactory.SPRITER_TYPE, 	new SpriterDrawableLogic());
		//TODO
		logicClassMap.put(EntityFactory.LIGHT_TYPE,new Drawable() {@Override public void draw(Batch batch, Entity entity, float parentAlpha) {}}); //Empty drawable for not checking on null
	}

	public void addDrawableToMap(int type, Drawable drawable) {
		logicClassMap.put(type, drawable);
	}

	public Drawable getDrawable(int type){
		return logicClassMap.get(type);
	}
}
