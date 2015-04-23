package com.uwsoft.editor.renderer.spine;

import java.lang.reflect.InvocationTargetException;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Method;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.uwsoft.editor.renderer.legacy.data.SpineVO;
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
	
	public void initSpine(SpineVO dataVO, IResourceRetriever rm, SpineReflectionHelper refData, float mulX) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ReflectionException {
		
		renderer = refData.skeletonRendererObject;
		reflectionData = refData;
		
		Object skeletonJsonObject = reflectionData.skeletonJsonConstructorAccess.newInstance(rm.getSkeletonAtlas(dataVO.animationName));
		
		Method setScaleMethodAccess = ClassReflection.getMethod(reflectionData.skeletonJsonClass, "setScale", float.class);	
		setScaleMethodAccess.invoke(skeletonJsonObject, dataVO.scaleX * mulX);
		
		Method readSkeletonData = ClassReflection.getMethod(reflectionData.skeletonJsonClass, "readSkeletonData", FileHandle.class);	
		skeletonData = readSkeletonData.invoke(skeletonJsonObject, rm.getSkeletonJSON(dataVO.animationName));
		
		skeletonObject = reflectionData.skeletonConstructorAccess.newInstance(skeletonData);
		
		Object animationStateDataObject = reflectionData.animationStateDataConstructorAccess.newInstance(skeletonData);
		
		
		computeBoundBox();
		

		stateObject = reflectionData.animationStateConstructorAccess.newInstance(animationStateDataObject);
		
		//Array<Object> anims = (Array<Object>) reflectionData.skeletonDataClassMethodAccess.invoke(skeletonData, reflectionData.getAnimationMethodIndex);
		Array<Object> anims = (Array<Object>) reflectionData.getAnimationMethodIndex.invoke(skeletonData);
		
		Object animObj = anims.get(0);
		//String animName = (String) reflectionData.animationClassMethodAccess.invoke(animObj, reflectionData.getAnimNameMethodIndex);
		String animName = (String) reflectionData.getAnimNameMethodIndex.invoke(animObj);
		setAnimation(animName);
	}

	private void computeBoundBox() throws ReflectionException{
		//reflectionData.skeletonClassMethodAccess.invoke(skeletonObject, reflectionData.updateWorldTransformMethodIndex);
		reflectionData.updateWorldTransformMethodIndex.invoke(skeletonObject);
		
		minX = Float.MAX_VALUE;
		minY = Float.MAX_VALUE;
		float maxX = Float.MIN_VALUE, maxY = Float.MIN_VALUE;
		
		//Array<Object> slots = (Array<Object>) reflectionData.skeletonClassMethodAccess.invoke(skeletonObject, reflectionData.getSlotsMethodIndex);
		Array<Object> slots = (Array<Object>) reflectionData.getSlotsMethodIndex.invoke(skeletonObject);
		
		for (int i = 0, n = slots.size; i < n; i++) {
			Object slot = slots.get(i);
			//Object attachment = reflectionData.slotClassMethodAccess.invoke(slot, reflectionData.getAttachmentMethodIndex);
			Object attachment = reflectionData.getAttachmentMethodIndex.invoke(slot);
			if (attachment == null)
				continue;
			if (!(reflectionData.regionAttachmentClass.isInstance(attachment)))
				continue;

			//reflectionData.regionAttachmentMethodAccess.invoke(attachment, reflectionData.updateWorldVerticesMethodIndex,slot,false);
			reflectionData.updateWorldVerticesMethodIndex.invoke(attachment, slot, false);
			
			//float[] vertices = (float[]) reflectionData.regionAttachmentMethodAccess.invoke(attachment, reflectionData.getWorldVerticesIndex);
			float[] vertices = (float[]) reflectionData.getWorldVerticesIndex.invoke(attachment);
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
		//reflectionData.stateObjectMethodAccess.invoke(stateObject,reflectionData.setAnimationMethodIndex,0, animName, true);
		try {
			reflectionData.setAnimationMethodIndex.invoke(stateObject,0, animName, true);
		} catch (ReflectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Array<Object> getAnimations(){
		try {
			return (Array<Object>) reflectionData.getAnimationMethodIndex.invoke(skeletonData);
		} catch (ReflectionException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void draw(Batch batch, float parentAlpha) {
		//reflectionData.skeletonRendererMethodAccess.invoke(renderer, reflectionData.skeletonRendererDrawMethodIndex, batch, skeletonObject);
		try {
			reflectionData.skeletonRendererDrawMethodIndex.invoke(renderer, batch, skeletonObject);
		} catch (ReflectionException e) {
		}
	}

	public void act(float delta, float x, float y) {
		try {
			reflectionData.updateWorldTransformMethodIndex.invoke(skeletonObject);
			reflectionData.updateMethodIndex.invoke(stateObject, delta);
			reflectionData.applyMethodIndex.invoke(stateObject, skeletonObject);
			reflectionData.setPositionMethodIndex.invoke(skeletonObject, x - minX, y - minY);
		}catch (ReflectionException e) {
		}
	}
}
