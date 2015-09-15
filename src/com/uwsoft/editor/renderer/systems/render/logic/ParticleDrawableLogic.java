package com.uwsoft.editor.renderer.systems.render.logic;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Matrix4;
import com.uwsoft.editor.renderer.components.particle.ParticleComponent;

public class ParticleDrawableLogic implements Drawable {

	private ComponentMapper<ParticleComponent> particleMapper ;

	public ParticleDrawableLogic() {
		particleMapper = ComponentMapper.getFor(ParticleComponent.class);
	}
	
	@Override
	public void draw(Batch batch, Entity entity, float parentAlpha) {
		ParticleComponent particleComponent = particleMapper.get(entity);
		Matrix4 matrix = batch.getTransformMatrix().scl(particleComponent.worldMultiplyer);
		batch.setTransformMatrix(matrix);
		particleComponent.particleEffect.draw(batch);
		batch.setTransformMatrix(batch.getTransformMatrix().scl(1f/particleComponent.worldMultiplyer));
	}

}
