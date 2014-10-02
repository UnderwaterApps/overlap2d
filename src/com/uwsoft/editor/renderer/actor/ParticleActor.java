package com.uwsoft.editor.renderer.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ParticleActor extends Actor {

	private ParticleEffect particleEffect;
	private float lastDelta = 0.0f;

    public ParticleActor(ParticleEffect effect) {
		setVisible(true);
		particleEffect = effect;
	}

	public void setPosition(float xx, float yy) {
		setX(xx);
		setY(yy);
		particleEffect.setPosition(getX(), getY());
	}

	public void start() {
		particleEffect.reset();
		particleEffect.start();
	}

	public void stop() {
		for (int j = 0; j < particleEffect.getEmitters().size; j++) {
			particleEffect.getEmitters().get(j).setContinuous(false);
			particleEffect.getEmitters().get(j).allowCompletion();
		}
	}
	
	public ParticleEffect getParticleEffect() {
        return particleEffect;
    }

	public void act(float delta) {
		lastDelta = delta;
		particleEffect.setPosition(getX(), getY());

	}

	public void fastForward(float time) {
		act(time);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		particleEffect.draw(batch,lastDelta);
	}

}
