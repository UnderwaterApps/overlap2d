package com.uwsoft.editor.renderer.utils;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.uwsoft.editor.renderer.conponents.ParentNodeComponent;
import com.uwsoft.editor.renderer.conponents.TransformComponent;

public class TransformMathUtils {
	
//	public static Vector2 screenToLocalCoordinates(Entity entity, Vector2 screenXY){
//		Vector2 localCoords = new Vector2(); 
//		Entity parentEntity = entity.getComponent(ParentNodeComponent.class).parentEntity;
//		TransformComponent parentTrnasform = entity.getComponent(TransformComponent.class);
//		localCoords = parentToLocalCoordinates(entity, new Vector2(parentTrnasform.x, parentTrnasform.y));
//		return screenToScreenCoordinates.;
//	}
	

	/** Transforms the specified point in the screen's coordinates to the entity's local coordinate system. */
	public static Vector2 stageToLocalCoordinates (Entity entity, Vector2 screenCoords) {
		ParentNodeComponent parentNodeComponent = entity.getComponent(ParentNodeComponent.class);
		Entity parentEntity = null;
		if(parentNodeComponent != null){
			parentEntity = parentNodeComponent.parentEntity;
		}
		if (parentEntity != null) stageToLocalCoordinates(parentEntity, screenCoords);
		parentToLocalCoordinates(entity, screenCoords);
		return screenCoords;
	}

	
	/** Converts the coordinates given in the parent's coordinate system to this entity's coordinate system. */
	public static Vector2 parentToLocalCoordinates (Entity childEntity, Vector2 parentCoords) {
		TransformComponent trnasform = childEntity.getComponent(TransformComponent.class); 
		
		final float rotation = trnasform.rotation;
		final float scaleX = trnasform.scaleX;
		final float scaleY = trnasform.scaleY;
		final float childX = trnasform.x;
		final float childY = trnasform.y;
		if (rotation == 0) {
			if (scaleX == 1 && scaleY == 1) {
				parentCoords.x -= childX;
				parentCoords.y -= childY;
			} else {
				//TODO origin
				final float originX = 0;
				final float originY = 0;
				parentCoords.x = (parentCoords.x - childX - originX) / scaleX + originX;
				parentCoords.y = (parentCoords.y - childY - originY) / scaleY + originY;
			}
		} else {
			final float cos = (float)Math.cos(rotation * MathUtils.degreesToRadians);
			final float sin = (float)Math.sin(rotation * MathUtils.degreesToRadians);
			//TODO origin
			final float originX = 0;
			final float originY = 0;
			final float tox = parentCoords.x - childX - originX;
			final float toy = parentCoords.y - childY - originY;
			parentCoords.x = (tox * cos + toy * sin) / scaleX + originX;
			parentCoords.y = (tox * -sin + toy * cos) / scaleY + originY;
		}
		return parentCoords;
	}
	
}
