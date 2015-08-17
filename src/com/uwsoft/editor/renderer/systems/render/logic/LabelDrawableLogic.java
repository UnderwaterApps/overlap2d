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
	private ComponentMapper<ShaderComponent> shaderComponentMapper;
	
	public LabelDrawableLogic() {
		labelComponentMapper = ComponentMapper.getFor(LabelComponent.class);
		tintComponentMapper = ComponentMapper.getFor(TintComponent.class);
		dimensionsComponentMapper = ComponentMapper.getFor(DimensionsComponent.class);
		transformMapper = ComponentMapper.getFor(TransformComponent.class);
		shaderComponentMapper = ComponentMapper.getFor(ShaderComponent.class);
	}
	
	@Override
	public void draw(Batch batch, Entity entity) {
		TransformComponent entityTransformComponent = transformMapper.get(entity);
		LabelComponent labelComponent = labelComponentMapper.get(entity);
		DimensionsComponent dimenstionsComponent = dimensionsComponentMapper.get(entity);
		TintComponent tint = tintComponentMapper.get(entity);
		//TODO parent alpha thing
		//tint.color.a *= parentAlpha;
		if (labelComponent.style.background != null) {
			batch.setColor(tint.color.r, tint.color.g, tint.color.b, tint.color.a);
			labelComponent.style.background.draw(batch, entityTransformComponent.x, entityTransformComponent.y, dimenstionsComponent.width, dimenstionsComponent.height);
			//System.out.println("LAbel BG");
		}
		//TODO we need tmp color here
		//if (labelComponent.style.fontColor != null) tint.color.mul(labelComponent.style.fontColor);
		//labelComponent.cache.tint(tint.color);
		
		labelComponent.cache.tint(Color.WHITE);
		labelComponent.cache.setPosition(entityTransformComponent.x, entityTransformComponent.y);
		labelComponent.cache.draw(batch);
	}

}
