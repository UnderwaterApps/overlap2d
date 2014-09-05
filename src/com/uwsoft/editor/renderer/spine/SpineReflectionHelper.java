package com.uwsoft.editor.renderer.spine;

import java.lang.reflect.Constructor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.esotericsoftware.reflectasm.ConstructorAccess;
import com.esotericsoftware.reflectasm.MethodAccess;

public class SpineReflectionHelper {
	public boolean isSpineAviable = true;
	
	public Class<?> skeletonJsonClass;
	public Class<?> skeletonClass;
	public Class<?> animationStateDataClass;
	public Class<?> stateClass;
	public Class<?> animationClass;
	public Class<?> slotClass;
	public Class<?> regionAttachmentClass;
	private Class<?> skeletonRendererClass;
	
	public Constructor<?> animationStateConstructorAccess;
	public Constructor<?> skeletonConstructorAccess;
	public Constructor<?> animationStateDataConstructorAccess;
	public Constructor<?> skeletonJsonConstructorAccess;
	private ConstructorAccess<?> skeletonRendererConstructorAccess;
	
	public MethodAccess skeletonClassMethodAccess;
	public MethodAccess stateObjectMethodAccess;
	public MethodAccess animationClassMethodAccess;
	public MethodAccess slotClassMethodAccess;
	public MethodAccess regionAttachmentMethodAccess;
	public MethodAccess skeletonRendererMethodAccess;
	
	public int updateWorldTransformMethodIndex;
	public int setAnimationMethodIndex;
	public int updateMethodIndex;
	public int applyMethodIndex;
	public int setPositionMethodIndex;
	public int getAnimNameMethodIndex;
	public int getSlotsMethodIndex;
	public int getAttachmentMethodIndex;
	public int updateWorldVerticesMethodIndex;
	public int getWorldVerticesIndex;
	public int skeletonRendererDrawMethodIndex;
	

	public Object skeletonRendererObject;

	private Class<?> skeletonDataClass;

	public int getAnimationMethodIndex;

	public MethodAccess skeletonDataClassMethodAccess;

	public SpineReflectionHelper(){
		try {
			skeletonJsonClass = Class.forName("com.esotericsoftware.spine.SkeletonJson");
		} catch (ClassNotFoundException e) {
			isSpineAviable = false;
			e.printStackTrace();
			return;
		}
		
		try {
			skeletonJsonConstructorAccess = skeletonJsonClass.getConstructor(TextureAtlas.class);
		} catch (NoSuchMethodException | SecurityException e1) {
			e1.printStackTrace();
		}
		
		try {
			skeletonClass = Class.forName("com.esotericsoftware.spine.Skeleton");
		} catch (ClassNotFoundException e) {
			isSpineAviable = false;
			e.printStackTrace();
			return;
		}
		
		try {
			skeletonDataClass = Class.forName("com.esotericsoftware.spine.SkeletonData");
		} catch (ClassNotFoundException e) {
			isSpineAviable = false;
			e.printStackTrace();
			return;
		}

		try {
			skeletonConstructorAccess = skeletonClass.getConstructor(skeletonDataClass);
		} catch (NoSuchMethodException | SecurityException e1) {
			e1.printStackTrace();
		}
		
		try {
			animationStateDataClass = Class.forName("com.esotericsoftware.spine.AnimationStateData");
		} catch (ClassNotFoundException e) {
			isSpineAviable = false;
			e.printStackTrace();
			return;
		}
		
		try {
			animationStateDataConstructorAccess = animationStateDataClass.getConstructor(skeletonDataClass);
		} catch (NoSuchMethodException | SecurityException e1) {
			e1.printStackTrace();
		}
		
		try {
			stateClass = Class.forName("com.esotericsoftware.spine.AnimationState");
		} catch (ClassNotFoundException e) {
			isSpineAviable = false;
			e.printStackTrace();
			return;
		}
		
		try {
			animationStateConstructorAccess = stateClass.getConstructor(animationStateDataClass);
		} catch (NoSuchMethodException | SecurityException e1) {
			isSpineAviable = false;
			e1.printStackTrace();
		}
		
		try {
			animationClass = Class.forName("com.esotericsoftware.spine.Animation");
		} catch (ClassNotFoundException e) {
			isSpineAviable = false;
			e.printStackTrace();
			return;
		}
		
		try {
			skeletonClassMethodAccess = MethodAccess.get(skeletonClass);
		}catch(Exception e){
			isSpineAviable = false;
			e.printStackTrace();
			return;
		}
		
		try {
			skeletonRendererClass = Class.forName("com.esotericsoftware.spine.SkeletonRenderer");
		} catch (ClassNotFoundException e) {
			isSpineAviable = false;
			e.printStackTrace();
			return;
		}
		
		try {
			slotClass = Class.forName("com.esotericsoftware.spine.Slot");
		} catch (ClassNotFoundException e) {
			isSpineAviable = false;
			e.printStackTrace();
			return;
		}
		
		updateWorldTransformMethodIndex = skeletonClassMethodAccess.getIndex("updateWorldTransform");		
		setPositionMethodIndex = skeletonClassMethodAccess.getIndex("setPosition");
		
		skeletonDataClassMethodAccess = MethodAccess.get(skeletonDataClass);
		getAnimationMethodIndex = skeletonDataClassMethodAccess.getIndex("getAnimations");
		
		
		stateObjectMethodAccess = MethodAccess.get(stateClass);
		setAnimationMethodIndex = stateObjectMethodAccess.getIndex("setAnimation",int.class,String.class,boolean.class);
		
		updateMethodIndex = stateObjectMethodAccess.getIndex("update");
		applyMethodIndex = stateObjectMethodAccess.getIndex("apply");
		
		animationClassMethodAccess = MethodAccess.get(animationClass);
		getAnimNameMethodIndex = animationClassMethodAccess.getIndex("getName");
		
		
		getSlotsMethodIndex = skeletonClassMethodAccess.getIndex("getSlots");
		
		slotClassMethodAccess = MethodAccess.get(slotClass);
		getAttachmentMethodIndex = slotClassMethodAccess.getIndex("getAttachment");
		
		try {
			regionAttachmentClass = Class.forName("com.esotericsoftware.spine.attachments.RegionAttachment");
		} catch (ClassNotFoundException e) {
			isSpineAviable = false;
			e.printStackTrace();
			return;
		}
		
		regionAttachmentMethodAccess = MethodAccess.get(regionAttachmentClass);
		updateWorldVerticesMethodIndex = regionAttachmentMethodAccess.getIndex("updateWorldVertices");
		
		getWorldVerticesIndex = regionAttachmentMethodAccess.getIndex("getWorldVertices");
		
		
		
		
		skeletonRendererConstructorAccess = ConstructorAccess.get(skeletonRendererClass);
		
		skeletonRendererMethodAccess = MethodAccess.get(skeletonRendererClass);
		skeletonRendererDrawMethodIndex = skeletonRendererMethodAccess.getIndex("draw",Batch.class, skeletonClass);
		
		skeletonRendererObject = skeletonRendererConstructorAccess.newInstance();
	}
}
