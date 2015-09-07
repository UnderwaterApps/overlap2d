package com.uwsoft.editor.renderer.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.SnapshotArray;
import com.uwsoft.editor.renderer.components.CompositeTransformComponent;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.NodeComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;

public class CompositeSystem extends IteratingSystem {

	private ComponentMapper<DimensionsComponent> dimensionsMapper;
	private ComponentMapper<TransformComponent> transformMapper;
	private ComponentMapper<NodeComponent> nodeMapper;
	
	private DimensionsComponent dimensionsComponent;
	private TransformComponent transformComponent;
	private NodeComponent nodeComponent;
	
	public CompositeSystem() {
		super(Family.all(CompositeTransformComponent.class).get());
		dimensionsMapper = ComponentMapper.getFor(DimensionsComponent.class);
		transformMapper = ComponentMapper.getFor(TransformComponent.class);
		nodeMapper = ComponentMapper.getFor(NodeComponent.class);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		dimensionsComponent = dimensionsMapper.get(entity);
		nodeComponent = nodeMapper.get(entity);
//		if(dimensionsComponent.boundBox == null){
//			dimensionsComponent.boundBox = new Rectangle();
//		}
		recalculateSize();
	}
	
	public void recalculateSize() {
        float lowerX = 0, lowerY = 0, upperX = 0, upperY = 0;
        SnapshotArray<Entity> entities = nodeComponent.children;
        
        float cos = 0;
		float sin = 0;
		float x1,y1,x2,y2,x3,y3,x4,y4;
		
        for (int i = 0; i < entities.size; i++) {
            Entity entity = entities.get(i);
            transformComponent = transformMapper.get(entity);
            DimensionsComponent childDimentionsComponent = dimensionsMapper.get(entity);
            
            cos = MathUtils.cosDeg(transformComponent.rotation);
			sin = MathUtils.sinDeg(transformComponent.rotation);
			
			x1 = (-transformComponent.originX) * cos - (-transformComponent.originY) * sin+transformComponent.x+transformComponent.originX;
			y1 = (-transformComponent.originX) * sin + (-transformComponent.originY) * cos+transformComponent.y+transformComponent.originY;
			
			x2 = (-transformComponent.originX) * cos - (childDimentionsComponent.height-transformComponent.originY) * sin+transformComponent.x+transformComponent.originX;
			y2 = (-transformComponent.originX) * sin + (childDimentionsComponent.height-transformComponent.originY) * cos+transformComponent.y+transformComponent.originY;
			
			x3 = (childDimentionsComponent.width-transformComponent.originX) * cos - (-transformComponent.originY) * sin+transformComponent.x+transformComponent.originX;
			y3 = (childDimentionsComponent.width-transformComponent.originX) * sin + (-transformComponent.originY) * cos+transformComponent.y+transformComponent.originY;
			
			x4 = (childDimentionsComponent.width-transformComponent.originX) * cos - (childDimentionsComponent.height-transformComponent.originY) * sin+transformComponent.x+transformComponent.originX;
			y4 = (childDimentionsComponent.width-transformComponent.originX) * sin + (childDimentionsComponent.height-transformComponent.originY) * cos+transformComponent.y+transformComponent.originY;
			
			lowerX = Math.min(Math.min(Math.min(Math.min(x1, x2),x3),x4),lowerX);
			upperX = Math.max(Math.max(Math.max(Math.max(x1, x2),x3),x4),upperX);
			lowerY = Math.min(Math.min(Math.min(Math.min(y1, y2),y3),y4),lowerY);
			upperY = Math.max(Math.max(Math.max(Math.max(y1, y2),y3),y4),upperY);

        }

        dimensionsComponent.width = (upperX - lowerX);
        dimensionsComponent.height = (upperY - lowerY);
        dimensionsComponent.boundBox.set(lowerX, lowerY, dimensionsComponent.width, dimensionsComponent.height);
    }

}
