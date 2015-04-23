package com.uwsoft.editor.renderer.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.uwsoft.editor.renderer.conponents.TextureRegionComponent;
import com.uwsoft.editor.renderer.conponents.sprite.SpriteAnimationStateComponent;

public class SpriteAnimationSystem extends IteratingSystem {
	private ComponentMapper<TextureRegionComponent> tm;
	private ComponentMapper<SpriteAnimationStateComponent> sm;

	public SpriteAnimationSystem() {
		super(Family.all(SpriteAnimationStateComponent.class).get());

		tm = ComponentMapper.getFor(TextureRegionComponent.class);
		sm = ComponentMapper.getFor(SpriteAnimationStateComponent.class);
	}

	@Override
	public void processEntity(Entity entity, float deltaTime) {
		TextureRegionComponent tex = tm.get(entity);
		SpriteAnimationStateComponent state = sm.get(entity);
		System.out.println("Frame " + state.currentAnimation.getKeyFrameIndex(state.time));
		tex.region = state.currentAnimation.getKeyFrame(state.time);
		System.out.println("Region " + tex.region.toString());
		state.time += deltaTime;
	}

}
