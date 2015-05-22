package com.uwsoft.editor.renderer.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.spine.SpineDataComponent;

public class SpineSystem extends IteratingSystem {

	private ComponentMapper<SpineDataComponent> spineDataComponentMapper = ComponentMapper.getFor(SpineDataComponent.class);
	private ComponentMapper<TransformComponent> transformComponentMapper = ComponentMapper.getFor(TransformComponent.class);
	
	public SpineSystem() {
		super(Family.all(SpineDataComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		
		TransformComponent transformComponent =  transformComponentMapper.get(entity);
		SpineDataComponent spineDataComponent =  spineDataComponentMapper.get(entity);
		
		spineDataComponent.skeleton.updateWorldTransform(); //
		spineDataComponent.state.update(deltaTime); // Update the animation time.
		spineDataComponent.state.apply(spineDataComponent.skeleton); // Poses skeleton using current animations. This sets the bones' local SRT.
		spineDataComponent.skeleton.setPosition(transformComponent.x - spineDataComponent.minX, transformComponent.y - spineDataComponent.minY);
	}
}
