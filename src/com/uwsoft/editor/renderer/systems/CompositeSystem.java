package com.uwsoft.editor.renderer.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
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
			
			//if(transformComponent.rotation != 0){
				System.out.println("X1=" + x1 + " X2=" + x2 + " X3=" + x3 + " X4=" + x4);
				System.out.println("Y1=" + y1 + " Y2=" + y2 + " Y3=" + y3 + " Y4=" + y4);
			//}
			
			lowerX = Math.min(Math.min(Math.min(Math.min(x1, x2),x3),x4),lowerX);
			upperX = Math.max(Math.max(Math.max(Math.max(x1, x2),x3),x4),upperX);
			lowerY = Math.min(Math.min(Math.min(Math.min(y1, y2),y3),y4),lowerY);
			upperY = Math.max(Math.max(Math.max(Math.max(y1, y2),y3),y4),upperY);
			
			System.out.println("lowerX=" + lowerX + " upperX=" + upperX + " lowerY=" + lowerY + " upperY=" + upperY);
			
//            if (i == 0) {
//                if (transformComponent.scaleX > 0 && childDimentionsComponent.width * transformComponent.scaleX > 0) {
//                    lowerX = transformComponent.x;
//                    upperX = transformComponent.x + childDimentionsComponent.width * transformComponent.scaleX;
//                } else {
//                    upperX = transformComponent.x;
//                    lowerX = transformComponent.x + childDimentionsComponent.width * transformComponent.scaleX;
//                }
//
//                if (transformComponent.scaleY > 0 && childDimentionsComponent.height * transformComponent.scaleY > 0) {
//                    lowerY = transformComponent.y;
//                    upperY = transformComponent.y + childDimentionsComponent.height * transformComponent.scaleY;
//                } else {
//                    upperY = transformComponent.y;
//                    lowerY = transformComponent.y + childDimentionsComponent.height * transformComponent.scaleY;
//                }
//            }
//            if (transformComponent.scaleX > 0 && childDimentionsComponent.width > 0) {
//                if (lowerX > transformComponent.x) lowerX = transformComponent.x;
//                if (upperX < transformComponent.x + childDimentionsComponent.width * transformComponent.scaleX) upperX = transformComponent.x + childDimentionsComponent.width * transformComponent.scaleX;
//            } else {
//                if (upperX < transformComponent.x) upperX = transformComponent.x;
//                if (lowerX > transformComponent.x + childDimentionsComponent.width * transformComponent.scaleX) lowerX = transformComponent.x + childDimentionsComponent.width * transformComponent.scaleX;
//            }
//            if (transformComponent.scaleY > 0 && childDimentionsComponent.height * transformComponent.scaleY > 0) {
//                if (lowerY > transformComponent.y) lowerY = transformComponent.y;
//                if (upperY < transformComponent.y + childDimentionsComponent.height * transformComponent.scaleY) upperY = transformComponent.y + childDimentionsComponent.height * transformComponent.scaleY;
//            } else {
//                if (upperY < transformComponent.y) upperY = transformComponent.y;
//                if (lowerY > transformComponent.y + childDimentionsComponent.height * transformComponent.scaleY) lowerY = transformComponent.y + childDimentionsComponent.height * transformComponent.scaleY;
//            }

        }

        dimensionsComponent.width = (upperX - 0);
        dimensionsComponent.height = (upperY - 0);
        System.out.println("AFTER RESIZE width=" + dimensionsComponent.width + " height=" + dimensionsComponent.height);
        System.out.println(" ");
    }

}
