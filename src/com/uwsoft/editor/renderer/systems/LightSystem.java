package com.uwsoft.editor.renderer.systems;

import box2dLight.Light;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.uwsoft.editor.renderer.components.NodeComponent;
import com.uwsoft.editor.renderer.components.ParentNodeComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.light.LightObjectComponent;
import com.uwsoft.editor.renderer.legacy.data.LightVO.LightType;
import com.uwsoft.editor.renderer.physics.PhysicsBodyLoader;

public class LightSystem extends IteratingSystem {
	private ComponentMapper<LightObjectComponent> lightObjectComponentMapper = ComponentMapper.getFor(LightObjectComponent.class);
    private ComponentMapper<TransformComponent> transformComponentMapper = ComponentMapper.getFor(TransformComponent.class);
    private ComponentMapper<ParentNodeComponent> parentNodeComponentMapper = ComponentMapper.getFor(ParentNodeComponent.class);
    private ComponentMapper<NodeComponent> nodeComponentMapper = ComponentMapper.getFor(NodeComponent.class);
	//private 
	public LightSystem() {
		super(Family.all(LightObjectComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		LightObjectComponent lightObjectComponent = lightObjectComponentMapper.get(entity);
		TransformComponent transformComponent = transformComponentMapper.get(entity);
		Light light = lightObjectComponent.lightObject;

		ParentNodeComponent parentNodeComponent = parentNodeComponentMapper.get(entity);
		
		float relativeX = transformComponent.x;
		float relativeY = transformComponent.y;
		float relativeRotation = 0;
		
		Entity parentEntity = parentNodeComponent.parentEntity;
		TransformComponent parentTransformComponent = transformComponentMapper.get(parentEntity);
		while (parentEntity != null) {
			relativeX+=parentTransformComponent.x;
			relativeY+=parentTransformComponent.y;
			relativeRotation+=parentTransformComponent.rotation;
			parentNodeComponent = parentNodeComponentMapper.get(parentEntity);
			if(parentNodeComponent == null){
				break;
			}
			parentEntity = parentNodeComponent.parentEntity;
		}
		
		if(light != null){
			
			float yy = 0;
			float xx = 0;
			
			if(relativeRotation != 0){
				xx = transformComponent.x*MathUtils.cosDeg(relativeRotation) - transformComponent.y*MathUtils.sinDeg(relativeRotation);
				yy = transformComponent.y*MathUtils.cosDeg(relativeRotation) + transformComponent.x*MathUtils.sinDeg(relativeRotation);
				yy=transformComponent.y-yy;
				xx=transformComponent.x-xx;
			}
			light.setPosition((relativeX-xx)*PhysicsBodyLoader.SCALE, (relativeY-yy)*PhysicsBodyLoader.SCALE);
		}

		if(lightObjectComponent.isDirty()) {
			light.remove();
		}

		if(lightObjectComponent.type == LightType.CONE){
			light.setDirection(lightObjectComponent.directionDegree+relativeRotation);
		}
		
	}

}
