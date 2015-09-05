package com.uwsoft.editor.renderer.systems.render.logic;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.ShaderComponent;
import com.uwsoft.editor.renderer.components.TintComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.label.LabelComponent;
import com.uwsoft.editor.renderer.components.spriter.SpriterComponent;
import com.uwsoft.editor.renderer.components.spriter.SpriterDrawerComponent;

public class LabelDrawableLogic implements Drawable {

	private ComponentMapper<LabelComponent> labelComponentMapper;
	private ComponentMapper<TintComponent> tintComponentMapper;
	private ComponentMapper<DimensionsComponent> dimensionsComponentMapper;
	private ComponentMapper<TransformComponent> transformMapper;

	private final Color tmpColor = new Color();

	public LabelDrawableLogic() {
		labelComponentMapper = ComponentMapper.getFor(LabelComponent.class);
		tintComponentMapper = ComponentMapper.getFor(TintComponent.class);
		dimensionsComponentMapper = ComponentMapper.getFor(DimensionsComponent.class);
		transformMapper = ComponentMapper.getFor(TransformComponent.class);
	}
	
	@Override
	public void draw(Batch batch, Entity entity, float parentAlpha) {
		TransformComponent entityTransformComponent = transformMapper.get(entity);
		LabelComponent labelComponent = labelComponentMapper.get(entity);
		DimensionsComponent dimenstionsComponent = dimensionsComponentMapper.get(entity);
		TintComponent tint = tintComponentMapper.get(entity);

		tmpColor.set(tint.color);

		if (labelComponent.style.background != null) {
			batch.setColor(tmpColor);
			labelComponent.style.background.draw(batch, entityTransformComponent.x, entityTransformComponent.y, dimenstionsComponent.width, dimenstionsComponent.height);
			//System.out.println("LAbel BG");
		}

		if(labelComponent.style.fontColor != null) tmpColor.mul(labelComponent.style.fontColor);
		//tmpColor.a *= TODO consider parent alpha

		labelComponent.cache.tint(tmpColor);
		labelComponent.cache.setPosition(entityTransformComponent.x, entityTransformComponent.y);
		labelComponent.cache.draw(batch);
	}

}
