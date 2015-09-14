package com.uwsoft.editor.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.physics.PhysicsBodyComponent;
import com.uwsoft.editor.renderer.physics.PhysicsBodyLoader;
import com.uwsoft.editor.renderer.systems.PhysicsSystem;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

public class PhysicsAdjustSystem extends PhysicsSystem {

	private Vector2 transformVec = new Vector2();
	
	public PhysicsAdjustSystem(World world) {
		super(world);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		
		TransformComponent transformComponent =  transformComponentMapper.get(entity);
		processBody(entity);

		PhysicsBodyComponent physicsBodyComponent = ComponentRetriever.get(entity, PhysicsBodyComponent.class);

		if(physicsBodyComponent.body == null) return;

		transformVec.x = transformComponent.x * PhysicsBodyLoader.getScale();
		transformVec.y = transformComponent.y * PhysicsBodyLoader.getScale();
		physicsBodyComponent.body.setTransform(transformVec, transformComponent.rotation * MathUtils.degreesToRadians);
		
	}

}
