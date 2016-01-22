package com.uwsoft.editor.renderer.systems.render.logic;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Matrix4;
import com.uwsoft.editor.renderer.components.*;

public class NinePatchDrawableLogic implements Drawable {

	private ComponentMapper<TintComponent> tintComponentComponentMapper;
	private ComponentMapper<TransformComponent> transformMapper;
	private ComponentMapper<DimensionsComponent> dimensionsMapper;
	private ComponentMapper<NinePatchComponent> ninePatchMapper;


	public NinePatchDrawableLogic() {
		tintComponentComponentMapper = ComponentMapper.getFor(TintComponent.class);
		transformMapper = ComponentMapper.getFor(TransformComponent.class);
		dimensionsMapper = ComponentMapper.getFor(DimensionsComponent.class);
		ninePatchMapper = ComponentMapper.getFor(NinePatchComponent.class);
	}

	@Override
	public void draw(Batch batch, Entity entity, float parentAlpha) {
		TintComponent tintComponent = tintComponentComponentMapper.get(entity);
		TransformComponent entityTransformComponent = transformMapper.get(entity);
		DimensionsComponent entityDimensionsComponent = dimensionsMapper.get(entity);
		NinePatchComponent entityNinePatchComponent = ninePatchMapper.get(entity);
		batch.setColor(tintComponent.color);

		entityNinePatchComponent.ninePatch.draw(batch, entityTransformComponent.x, entityTransformComponent.y, entityDimensionsComponent.width, entityDimensionsComponent.height);
	}

}
