package com.uwsoft.editor.renderer.systems;

import box2dLight.ConeLight;
import box2dLight.Light;
import box2dLight.PointLight;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.uwsoft.editor.renderer.components.ParentNodeComponent;
import com.uwsoft.editor.renderer.components.TintComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.light.LightObjectComponent;
import com.uwsoft.editor.renderer.data.LightVO;
import com.uwsoft.editor.renderer.data.LightVO.LightType;
import com.uwsoft.editor.renderer.physics.PhysicsBodyLoader;

public class LightSystem extends IteratingSystem {
	private ComponentMapper<LightObjectComponent> lightObjectComponentMapper = ComponentMapper.getFor(LightObjectComponent.class);
    private ComponentMapper<TransformComponent> transformComponentMapper = ComponentMapper.getFor(TransformComponent.class);
    private ComponentMapper<ParentNodeComponent> parentNodeComponentMapper = ComponentMapper.getFor(ParentNodeComponent.class);
    private ComponentMapper<TintComponent> tintComponentMapper = ComponentMapper.getFor(TintComponent.class);

	public LightSystem() {
		super(Family.all(LightObjectComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		LightObjectComponent lightObjectComponent = lightObjectComponentMapper.get(entity);
		TransformComponent transformComponent = transformComponentMapper.get(entity);
		TintComponent tintComponent = tintComponentMapper.get(entity);
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
			light.setPosition((relativeX-xx)*PhysicsBodyLoader.getScale(), (relativeY-yy)*PhysicsBodyLoader.getScale());
			light.setSoftnessLength(lightObjectComponent.softnessLength);
		}

		if(lightObjectComponent.getType() == LightType.CONE){
			light.setDirection(lightObjectComponent.directionDegree+relativeRotation);
		}
		
		
		if (lightObjectComponent.getType() == LightVO.LightType.POINT) {
			lightObjectComponent.lightObject.setColor(new Color(tintComponent.color));
            // TODO Physics and resolution part
            lightObjectComponent.lightObject.setDistance(lightObjectComponent.distance * PhysicsBodyLoader.getScale());
            lightObjectComponent.lightObject.setStaticLight(lightObjectComponent.isStatic);
            lightObjectComponent.lightObject.setActive(true);
            lightObjectComponent.lightObject.setXray(lightObjectComponent.isXRay);

        } else {
        	lightObjectComponent.lightObject.setColor(new Color(tintComponent.color));
            lightObjectComponent.lightObject.setDistance(lightObjectComponent.distance * PhysicsBodyLoader.getScale());
            lightObjectComponent.lightObject.setStaticLight(lightObjectComponent.isStatic);
            lightObjectComponent.lightObject.setDirection(lightObjectComponent.directionDegree);
            ((ConeLight) lightObjectComponent.lightObject).setConeDegree(lightObjectComponent.coneDegree);
            lightObjectComponent.lightObject.setXray(lightObjectComponent.isXRay);
        }
		
	}

}
