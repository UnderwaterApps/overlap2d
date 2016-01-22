package com.overlap2d.extensions.spine;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.uwsoft.editor.renderer.components.SpineDataComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;

public class SpineSystem extends IteratingSystem {

	private ComponentMapper<SpineObjectComponent> spineObjectComponentMapper = ComponentMapper.getFor(SpineObjectComponent.class);
	private ComponentMapper<TransformComponent> transformComponentMapper = ComponentMapper.getFor(TransformComponent.class);
	
	public SpineSystem() {
		super(Family.all(SpineDataComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		
		TransformComponent transformComponent =  transformComponentMapper.get(entity);
		SpineObjectComponent spineObjectComponent =  spineObjectComponentMapper.get(entity);

		spineObjectComponent.skeleton.updateWorldTransform(); //
		spineObjectComponent.state.update(deltaTime); // Update the animation time.
		spineObjectComponent.state.apply(spineObjectComponent.skeleton); // Poses skeleton using current animations. This sets the bones' local SRT.
		spineObjectComponent.skeleton.setPosition(transformComponent.x - spineObjectComponent.minX, transformComponent.y - spineObjectComponent.minY);
	}
}
