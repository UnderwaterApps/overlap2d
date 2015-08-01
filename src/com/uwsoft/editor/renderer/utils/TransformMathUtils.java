package com.uwsoft.editor.renderer.utils;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.uwsoft.editor.renderer.components.ParentNodeComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.ViewPortComponent;

public class TransformMathUtils {
	
	/** Transforms the specified point in the scene's coordinates to the entity's local coordinate system. */
	public static Vector2 sceneToLocalCoordinates (Entity entity, Vector2 sceneCoords) {
		ParentNodeComponent parentNodeComponent = entity.getComponent(ParentNodeComponent.class);
		Entity parentEntity = null;
		if(parentNodeComponent != null){
			parentEntity = parentNodeComponent.parentEntity;
		}
		if (parentEntity != null) sceneToLocalCoordinates(parentEntity, sceneCoords);
		parentToLocalCoordinates(entity, sceneCoords);
		return sceneCoords;
	}

    public static Vector2 globalToLocalCoordinates (Entity entity, Vector2 sceneCoords) {
        ParentNodeComponent parentNodeComponent = entity.getComponent(ParentNodeComponent.class);
        Entity parentEntity = null;
        if(parentNodeComponent != null){
            ViewPortComponent viewPortComponent = ComponentRetriever.get(parentNodeComponent.parentEntity, ViewPortComponent.class);
            if(viewPortComponent == null) {
                parentEntity = parentNodeComponent.parentEntity;
            } else {
                Vector3 worldCoordinates = viewPortComponent.viewPort.getCamera().unproject(new Vector3(sceneCoords.x, sceneCoords.y,0));
                sceneCoords.x = worldCoordinates.x;
                sceneCoords.y = worldCoordinates.y;
            }
        }
        if (parentEntity != null) {
            globalToLocalCoordinates(parentEntity, sceneCoords);
        }
        parentToLocalCoordinates(entity, sceneCoords);
        return sceneCoords;
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
	
	/** Transforms the specified point in the entity's coordinates to be in the scene's coordinates.*/
	public static Vector2 localToSceneCoordinates (Entity entity, Vector2 localCoords) {
		return localToAscendantCoordinates(null, entity, localCoords);
	}
	
	/** Converts coordinates for this entity to those of a parent entity. The ascendant does not need to be a direct parent. */
	public static Vector2 localToAscendantCoordinates (Entity ascendant, Entity entity, Vector2 localCoords) {
		while (entity != null) {
			localToParentCoordinates(entity, localCoords);
			ParentNodeComponent parentNode = entity.getComponent(ParentNodeComponent.class);
			if(parentNode == null){
				break;
			}
			entity = parentNode.parentEntity;
			if (entity == ascendant) break;
		}
		return localCoords;
	}
	
	/** Transforms the specified point in the actor's coordinates to be in the parent's coordinates. */
	public static Vector2 localToParentCoordinates (Entity entity, Vector2 localCoords) {
		TransformComponent transform = entity.getComponent(TransformComponent.class);
		
		final float rotation = -transform.rotation;
		final float scaleX = transform.scaleX;
		final float scaleY = transform.scaleY;
		final float x = transform.x;
		final float y = transform.y;
		if (rotation == 0) {
			if (scaleX == 1 && scaleY == 1) {
				localCoords.x += x;
				localCoords.y += y;
			} else {
				final float originX = transform.originX;
				final float originY = transform.originY;
				localCoords.x = (localCoords.x - originX) * scaleX + originX + x;
				localCoords.y = (localCoords.y - originY) * scaleY + originY + y;
			}
		} else {
			final float cos = (float)Math.cos(rotation * MathUtils.degreesToRadians);
			final float sin = (float)Math.sin(rotation * MathUtils.degreesToRadians);
			final float originX = transform.originX;
			final float originY = transform.originY;
			final float tox = (localCoords.x - originX) * scaleX;
			final float toy = (localCoords.y - originY) * scaleY;
			localCoords.x = (tox * cos + toy * sin) + originX + x;
			localCoords.y = (tox * -sin + toy * cos) + originY + y;
		}
		return localCoords;
	}
	
}
