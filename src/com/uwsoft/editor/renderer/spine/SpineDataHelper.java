package com.uwsoft.editor.renderer.spine;

import java.lang.reflect.InvocationTargetException;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.reflectasm.MethodAccess;
import com.uwsoft.editor.renderer.data.SpineVO;
import com.uwsoft.editor.renderer.resources.IResourceRetriever;

public class SpineDataHelper {
	private Object renderer; // SkeletonRenderer
	private Object skeletonObject; // Skeleton
	public Object stateObject; // AnimationState
	private Object skeletonData;
	
	private float minX;
	private float minY;
	private SpineReflectionHelper reflectionData;
	public float width;
	public float height;
	
	public void initSpine(SpineVO dataVO, IResourceRetriever rm, SpineReflectionHelper refData, float mulX) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		renderer = refData.skeletonRendererObject;
		reflectionData = refData;
		
		Object skeletonJsonObject = reflectionData.skeletonJsonConstructorAccess.newInstance(rm.getSkeletonAtlas(dataVO.animationName));
		
		MethodAccess methodAccess = MethodAccess.get(reflectionData.skeletonJsonClass);
		methodAccess.invoke(skeletonJsonObject, "setScale", dataVO.scaleX * mulX);
		
		skeletonData = methodAccess.invoke(skeletonJsonObject, "readSkeletonData", rm.getSkeletonJSON(dataVO.animationName));
		
		skeletonObject = reflectionData.skeletonConstructorAccess.newInstance(skeletonData);
		
		Object animationStateDataObject = reflectionData.animationStateDataConstructorAccess.newInstance(skeletonData);
		
		computeBoundBox();

		stateObject = reflectionData.animationStateConstructorAccess.newInstance(animationStateDataObject);
		
		Array<Object> anims = (Array<Object>) reflectionData.skeletonDataClassMethodAccess.invoke(skeletonData, reflectionData.getAnimationMethodIndex);
		
		Object animObj = anims.get(0);
		String animName = (String) reflectionData.animationClassMethodAccess.invoke(animObj, reflectionData.getAnimNameMethodIndex);
		setAnimation(animName);
	}

	private void computeBoundBox() {
		reflectionData.skeletonClassMethodAccess.invoke(skeletonObject, reflectionData.updateWorldTransformMethodIndex);
		minX = Float.MAX_VALUE;
		minY = Float.MAX_VALUE;
		float maxX = Float.MIN_VALUE, maxY = Float.MIN_VALUE;
		
		Array<Object> slots = (Array<Object>) reflectionData.skeletonClassMethodAccess.invoke(skeletonObject, reflectionData.getSlotsMethodIndex);
		
		for (int i = 0, n = slots.size; i < n; i++) {
			Object slot = slots.get(i);
			Object attachment = reflectionData.slotClassMethodAccess.invoke(slot, reflectionData.getAttachmentMethodIndex);
			if (attachment == null)
				continue;
			if (!(reflectionData.regionAttachmentClass.isInstance(attachment)))
				continue;

			reflectionData.regionAttachmentMethodAccess.invoke(attachment, reflectionData.updateWorldVerticesMethodIndex,slot,false);
			
			float[] vertices = (float[]) reflectionData.regionAttachmentMethodAccess.invoke(attachment, reflectionData.getWorldVerticesIndex);
			for (int ii = 0, nn = vertices.length; ii < nn; ii += 5) {
				minX = Math.min(minX, vertices[ii]);
				minY = Math.min(minY, vertices[ii + 1]);
				maxX = Math.max(maxX, vertices[ii]);
				maxY = Math.max(maxY, vertices[ii + 1]);
			}
		}

		width = (maxX - minX);
		height = (maxY - minY);
	}

	public void setAnimation(String animName) {
		if(animName == null){
			System.out.println("NO ANIM NAME");
			return;
		}
		reflectionData.stateObjectMethodAccess.invoke(stateObject,reflectionData.setAnimationMethodIndex,0, animName, true);
	}
	public Array<Object> getAnimations(){
		return (Array<Object>) reflectionData.skeletonDataClassMethodAccess.invoke(skeletonData, reflectionData.getAnimationMethodIndex);
	}

	public void draw(Batch batch, float parentAlpha) {
		reflectionData.skeletonRendererMethodAccess.invoke(renderer, reflectionData.skeletonRendererDrawMethodIndex, batch, skeletonObject);
	}

	public void act(float delta, float x, float y) {
		reflectionData.skeletonClassMethodAccess.invoke(skeletonObject, reflectionData.updateWorldTransformMethodIndex);
		reflectionData.stateObjectMethodAccess.invoke(stateObject,reflectionData.updateMethodIndex,delta);
		reflectionData.stateObjectMethodAccess.invoke(stateObject,reflectionData.applyMethodIndex,skeletonObject);
		reflectionData.skeletonClassMethodAccess.invoke(skeletonObject,reflectionData.setPositionMethodIndex,x - minX, y - minY);
	}
}
