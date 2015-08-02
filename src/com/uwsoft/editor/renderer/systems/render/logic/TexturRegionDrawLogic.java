package com.uwsoft.editor.renderer.systems.render.logic;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.TextureRegionComponent;
import com.uwsoft.editor.renderer.components.TintComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;

public class TexturRegionDrawLogic implements Drawable {

	private ComponentMapper<TintComponent> tintComponentComponentMapper;
	private ComponentMapper<TextureRegionComponent> textureRegionMapper;
	private ComponentMapper<TransformComponent> transformMapper;
	private ComponentMapper<DimensionsComponent> dimensionsComponentComponentMapper;

	public TexturRegionDrawLogic() {
		tintComponentComponentMapper = ComponentMapper.getFor(TintComponent.class);
		textureRegionMapper = ComponentMapper.getFor(TextureRegionComponent.class);
		transformMapper = ComponentMapper.getFor(TransformComponent.class);
		dimensionsComponentComponentMapper = ComponentMapper.getFor(DimensionsComponent.class);
	}

	@Override
	public void draw(Batch batch, Entity entity) {
		TintComponent tintComponent = tintComponentComponentMapper.get(entity);
		TransformComponent entityTransformComponent = transformMapper.get(entity);
		TextureRegionComponent entityTextureRegionComponent = textureRegionMapper.get(entity);
		DimensionsComponent dimensionsComponent = dimensionsComponentComponentMapper.get(entity);

        batch.setColor(tintComponent.color);

        if(entityTextureRegionComponent.polygonSprite != null) {
            entityTextureRegionComponent.polygonSprite.setPosition(
                    entityTransformComponent.x+entityTextureRegionComponent.polygonShift.x,
                    entityTransformComponent.y+entityTextureRegionComponent.polygonShift.y);
            entityTextureRegionComponent.polygonSprite.setRotation(entityTransformComponent.rotation);
            entityTextureRegionComponent.polygonSprite.draw((PolygonSpriteBatch) batch);
        } else {
            batch.draw(entityTextureRegionComponent.region,
                    entityTransformComponent.x, entityTransformComponent.y,
                    entityTransformComponent.originX, entityTransformComponent.originY,
                    dimensionsComponent.width, dimensionsComponent.height,
                    entityTransformComponent.scaleX, entityTransformComponent.scaleY,
                    entityTransformComponent.rotation);
        }
	}

}
