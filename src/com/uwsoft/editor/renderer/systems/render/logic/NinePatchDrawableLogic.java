package com.uwsoft.editor.renderer.systems.render.logic;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.NinePatchComponnent;
import com.uwsoft.editor.renderer.components.TransformComponent;

public class NinePatchDrawableLogic implements Drawable {
	
	private ComponentMapper<TransformComponent> transformMapper;
	private ComponentMapper<DimensionsComponent> dimensionsMapper;
	private ComponentMapper<NinePatchComponnent> ninePatchMapper;

	public NinePatchDrawableLogic() {
		transformMapper = ComponentMapper.getFor(TransformComponent.class);
		dimensionsMapper = ComponentMapper.getFor(DimensionsComponent.class);
		ninePatchMapper = ComponentMapper.getFor(NinePatchComponnent.class);
	}

	@Override
	public void draw(Batch batch, Entity entity) {
		//Image a;
		TransformComponent entityTransformComponent = transformMapper.get(entity);
		DimensionsComponent entityDimentionsComponent = dimensionsMapper.get(entity);
		NinePatchComponnent entityNinePatchComponent = ninePatchMapper.get(entity);
		entityNinePatchComponent.ninePatch.draw(batch, entityTransformComponent.x, entityTransformComponent.y, entityDimentionsComponent.width, entityDimentionsComponent.height);
	}

}
