package com.uwsoft.editor.renderer.systems.render.logic;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.uwsoft.editor.renderer.components.particle.ParticleComponent;

public class ParticleDrawableLogic implements Drawable {

	private ComponentMapper<ParticleComponent> particleMapper ;

	public ParticleDrawableLogic() {
		particleMapper = ComponentMapper.getFor(ParticleComponent.class);
	}
	
	@Override
	public void draw(Batch batch, Entity entity) {
		ParticleComponent particleComponent = particleMapper.get(entity);
		particleComponent.particleEffect.draw(batch);
	}

}
