package com.uwsoft.editor.renderer.systems.render.logic;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.brashmonkey.spriter.Player;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.spriter.SpriterComponent;
import com.uwsoft.editor.renderer.components.spriter.SpriterDrawerComponent;

public class SpriterDrawableLogic implements Drawable {

	private ComponentMapper<SpriterDrawerComponent> spriterDrawerMapper;
	private ComponentMapper<SpriterComponent> spriterMapper;
	private ComponentMapper<TransformComponent> transformMapper;

	public SpriterDrawableLogic() {
		spriterDrawerMapper = ComponentMapper.getFor(SpriterDrawerComponent.class);
		spriterMapper = ComponentMapper.getFor(SpriterComponent.class);
		transformMapper = ComponentMapper.getFor(TransformComponent.class);
	}
	
	@Override
	public void draw(Batch batch, Entity entity, float parentAlpha) {
		TransformComponent entityTransformComponent = transformMapper.get(entity);
		SpriterDrawerComponent spriterDrawerComponent = spriterDrawerMapper.get(entity);
		SpriterComponent spriter = spriterMapper.get(entity);
		Player player = spriter.player;
		
		player.setPosition(entityTransformComponent.x, entityTransformComponent.y);
		//TODO dimentions 
		//player.setPivot(getWidth() / 2, getHeight() / 2);
		player.setScale(spriter.scale );
		player.rotate(entityTransformComponent.rotation - player.getAngle());
		player.update();
		spriterDrawerComponent.drawer.beforeDraw(player, batch);
	}

}
