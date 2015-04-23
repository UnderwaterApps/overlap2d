package com.uwsoft.editor.renderer.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.uwsoft.editor.renderer.conponents.TransformComponent;
import com.uwsoft.editor.renderer.conponents.particle.ParticleCompononet;

public class ParticleSystem extends IteratingSystem {

	private ComponentMapper<ParticleCompononet> particleComponentMapper = ComponentMapper.getFor(ParticleCompononet.class);
    private ComponentMapper<TransformComponent> transformComponentMapper = ComponentMapper.getFor(TransformComponent.class);
    //private ComponentMapper<ParentNodeComponent> parentNodeComponentMapper = ComponentMapper.getFor(ParentNodeComponent.class);
    //private ComponentMapper<NodeComponent> nodeComponentMapper = ComponentMapper.getFor(NodeComponent.class);
	
	public ParticleSystem() {
		super(Family.all(ParticleCompononet.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		//System.out.println("DELTAING  " + entity.getId() +"  " +deltaTime);
		ParticleCompononet particleComponent = particleComponentMapper.get(entity);
		TransformComponent trnasformCompononet = transformComponentMapper.get(entity);
		ParticleEffect particleEffect = particleComponent.particleEffect;
		
		particleEffect.setPosition(trnasformCompononet.x+50, trnasformCompononet.y+50);
		particleEffect.update(deltaTime);
		//ParentNodeComponent parentNodeComponent = parentNodeComponentMapper.get(entity);
		
//		Entity parentEntity = parentNodeComponent.parentEntity;
//		while (parentEntity != null) {
//			parentNodeComponent = nodeComponentMapper.get(parentEntity);
//			parentEntity = parentNodeComponent.parentEntity;
//		}
		
	}

}
