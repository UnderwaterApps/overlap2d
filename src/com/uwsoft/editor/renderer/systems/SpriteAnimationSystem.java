package com.uwsoft.editor.renderer.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.uwsoft.editor.renderer.components.TextureRegionComponent;
import com.uwsoft.editor.renderer.components.sprite.SpriteAnimationComponent;
import com.uwsoft.editor.renderer.components.sprite.SpriteAnimationStateComponent;

public class SpriteAnimationSystem extends IteratingSystem {
	private ComponentMapper<TextureRegionComponent> tm;
	private ComponentMapper<SpriteAnimationStateComponent> sm;
    private ComponentMapper<SpriteAnimationComponent> sa;

	public SpriteAnimationSystem() {
		super(Family.all(SpriteAnimationStateComponent.class).get());

		tm = ComponentMapper.getFor(TextureRegionComponent.class);
		sm = ComponentMapper.getFor(SpriteAnimationStateComponent.class);
        sa = ComponentMapper.getFor(SpriteAnimationComponent.class);
	}

	@Override
	public void processEntity(Entity entity, float deltaTime) {
		TextureRegionComponent tex = tm.get(entity);
		SpriteAnimationStateComponent state = sm.get(entity);
        state.currentAnimation.setFrameDuration(1f/sa.get(entity).fps);
		tex.region = state.currentAnimation.getKeyFrame(state.time);

        if(!state.paused) {
            state.time += deltaTime;
        }
	}
}
