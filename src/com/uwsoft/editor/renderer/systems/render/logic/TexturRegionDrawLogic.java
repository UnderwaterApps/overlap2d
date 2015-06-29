package com.uwsoft.editor.renderer.systems.render.logic;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.uwsoft.editor.renderer.components.TextureRegionComponent;
import com.uwsoft.editor.renderer.components.TintComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;

public class TexturRegionDrawLogic implements Drawable {

	private ComponentMapper<TintComponent> tintComponentComponentMapper;
	private ComponentMapper<TextureRegionComponent> textureRegionMapper;
	private ComponentMapper<TransformComponent> transformMapper;

	public TexturRegionDrawLogic() {
		tintComponentComponentMapper = ComponentMapper.getFor(TintComponent.class);
		textureRegionMapper = ComponentMapper.getFor(TextureRegionComponent.class);
		transformMapper = ComponentMapper.getFor(TransformComponent.class);
	}

	@Override
	public void draw(Batch batch, Entity entity) {
		TintComponent tintComponent = tintComponentComponentMapper.get(entity);
		TransformComponent entityTransformComponent = transformMapper.get(entity);
		TextureRegionComponent entityTextureRegionComponent = textureRegionMapper.get(entity);
		batch.setColor(tintComponent.color);
		batch.draw(entityTextureRegionComponent.region, entityTransformComponent.x, entityTransformComponent.y, entityTransformComponent.originX, entityTransformComponent.originY, entityTextureRegionComponent.region.getRegionWidth(), entityTextureRegionComponent.region.getRegionHeight(), entityTransformComponent.scaleX, entityTransformComponent.scaleY, entityTransformComponent.rotation);
	}

}
