package com.uwsoft.editor.renderer.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.physics.PhysicsBodyComponent;
import com.uwsoft.editor.renderer.physics.PhysicsBodyLoader;

public class PhysicsSystem extends IteratingSystem {

	private ComponentMapper<PhysicsBodyComponent> physicsBodyComponentMapper = ComponentMapper.getFor(PhysicsBodyComponent.class);
	private ComponentMapper<TransformComponent> transformComponentMapper = ComponentMapper.getFor(TransformComponent.class);
	
	public PhysicsSystem() {
		super(Family.all(PhysicsBodyComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		
		TransformComponent transformComponent =  transformComponentMapper.get(entity);
		
		Body body = physicsBodyComponentMapper.get(entity).body;
		transformComponent.x = body.getPosition().x/ PhysicsBodyLoader.SCALE;
		transformComponent.y = body.getPosition().y/PhysicsBodyLoader.SCALE;
		transformComponent.rotation = body.getAngle() * MathUtils.radiansToDegrees;
	}

}
