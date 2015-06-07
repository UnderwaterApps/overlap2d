package com.uwsoft.editor.renderer.spine;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Constructor;
import com.badlogic.gdx.utils.reflect.Method;
import com.badlogic.gdx.utils.reflect.ReflectionException;

public class SpineReflectionHelper {
	public boolean isSpineAviable = true;
	
	public Class<?> skeletonJsonClass;
	public Class<?> skeletonClass;
	public Class<?> animationStateDataClass;
	public Class<?> stateClass;
	public Class<?> animationClass;
	public Class<?> slotClass;
	public Class<?> regionAttachmentClass;
	private Class<?> skeletonDataClass;
	private Class<?> skeletonRendererClass;
	
	public Constructor animationStateConstructorAccess;
	public Constructor skeletonConstructorAccess;
	public Constructor animationStateDataConstructorAccess;
	public Constructor skeletonJsonConstructorAccess;
	private Constructor skeletonRendererConstructorAccess;
	
	public Method updateWorldTransformMethodIndex;
	public Method setAnimationMethodIndex;
	public Method updateMethodIndex;
	public Method applyMethodIndex;
	public Method setPositionMethodIndex;
	public Method getAnimNameMethodIndex;
	public Method getSlotsMethodIndex;
	public Method getAttachmentMethodIndex;
	public Method updateWorldVerticesMethodIndex;
	public Method getWorldVerticesIndex;
	public Method skeletonRendererDrawMethodIndex;
	public Method getAnimationMethodIndex;

	public Object skeletonRendererObject;

	public SpineReflectionHelper(){
		try{
			//all needed classes
			skeletonJsonClass = ClassReflection.forName("com.esotericsoftware.spine.SkeletonJson");
			skeletonClass = ClassReflection.forName("com.esotericsoftware.spine.Skeleton");
			skeletonDataClass = ClassReflection.forName("com.esotericsoftware.spine.SkeletonData");
			animationStateDataClass = ClassReflection.forName("com.esotericsoftware.spine.AnimationStateData");
			stateClass = ClassReflection.forName("com.esotericsoftware.spine.AnimationState");
			animationClass = ClassReflection.forName("com.esotericsoftware.spine.Animation");
			skeletonRendererClass = ClassReflection.forName("com.esotericsoftware.spine.SkeletonRenderer");
			slotClass = ClassReflection.forName("com.esotericsoftware.spine.Slot");
			regionAttachmentClass = ClassReflection.forName("com.esotericsoftware.spine.attachments.RegionAttachment");
			
			//all needed constructors
			skeletonJsonConstructorAccess = ClassReflection.getConstructor(skeletonJsonClass, TextureAtlas.class);
			skeletonConstructorAccess = ClassReflection.getConstructor(skeletonClass, skeletonDataClass);
			animationStateDataConstructorAccess = ClassReflection.getConstructor(animationStateDataClass, skeletonDataClass);
			animationStateConstructorAccess = ClassReflection.getConstructor(stateClass, animationStateDataClass);
			skeletonRendererConstructorAccess = ClassReflection.getConstructor(skeletonRendererClass);
			
			//all needed methods
			updateWorldTransformMethodIndex = ClassReflection.getMethod(skeletonClass, "updateWorldTransform");		
			setPositionMethodIndex = ClassReflection.getMethod(skeletonClass,"setPosition", float.class, float.class);
			getSlotsMethodIndex = ClassReflection.getMethod(skeletonClass, "getSlots");
			getAnimationMethodIndex = ClassReflection.getMethod(skeletonDataClass,"getAnimations");
			setAnimationMethodIndex = ClassReflection.getMethod(stateClass, "setAnimation", int.class, String.class, boolean.class);
			updateMethodIndex = ClassReflection.getMethod(stateClass,"update", float.class);
			applyMethodIndex = ClassReflection.getMethod(stateClass,"apply", skeletonClass);
			getAnimNameMethodIndex = ClassReflection.getMethod(animationClass, "getName");
			getAttachmentMethodIndex = ClassReflection.getMethod(slotClass, "getAttachment");
			updateWorldVerticesMethodIndex = ClassReflection.getMethod(regionAttachmentClass, "updateWorldVertices", slotClass, boolean.class);
			getWorldVerticesIndex = ClassReflection.getMethod(regionAttachmentClass, "getWorldVertices");
			
			//skeletonRendererMethodAccess = MethodAccess.get(skeletonRendererClass);
			skeletonRendererDrawMethodIndex = ClassReflection.getMethod(skeletonRendererClass, "draw", Batch.class, skeletonClass);
			
			//instance of SkeletonRenderrer
			skeletonRendererObject = skeletonRendererConstructorAccess.newInstance();
			
		}catch (ReflectionException e) {
			isSpineAviable = false;
			//e.printStackTrace();
			return;
		}
		
	}
}
