package com.uwsoft.editor.renderer.actor;

import java.lang.reflect.InvocationTargetException;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.uwsoft.editor.renderer.data.Essentials;
import com.uwsoft.editor.renderer.data.SpineVO;
import com.uwsoft.editor.renderer.spine.SpineDataHelper;
import com.uwsoft.editor.renderer.spine.SpineReflectionHelper;
import com.uwsoft.editor.renderer.utils.CustomVariables;

public class SpineActor extends Actor implements IBaseItem{
	
	public SpineVO dataVO;	
	public float mulX = 1f;
	public float mulY = 1f;
	
	protected int layerIndex = 0;
	private boolean isLockedByLayer = false;
	private CompositeItem parentItem = null;
	
	private Essentials essentials;

    private CustomVariables customVariables = new CustomVariables();
    
	private SpineReflectionHelper spineReflectionHelper;
	private SpineDataHelper spineData;
	
	private Body body;
	
	public SpineActor(SpineVO vo, Essentials e,CompositeItem parent) {
		this(vo, e);
		setParentItem(parent);
	}

	public SpineActor(SpineVO vo, Essentials e) {
		essentials = e;
		this.spineReflectionHelper = essentials.spineReflectionHelper;

        dataVO = vo;

        initSpine();

		setX(dataVO.x);
		setY(dataVO.y);
		setScaleX(dataVO.scaleX);
		setScaleY(dataVO.scaleY);
        customVariables.loadFromString(dataVO.customVars);
		this.setRotation(dataVO.rotation); 
		
		if(dataVO.zIndex < 0) dataVO.zIndex = 0;
				
		if(dataVO.tint == null) {			
			setTint(new Color(1, 1, 1, 1));	
		} else {
			setTint(new Color(dataVO.tint[0], dataVO.tint[1], dataVO.tint[2], dataVO.tint[3]));
		}
		
	}

//    private void computeBoundBox() {
//        skeleton.updateWorldTransform();
//        minX = Float.MAX_VALUE; minY = Float.MAX_VALUE;
//        float maxX = Float.MIN_VALUE, maxY = Float.MIN_VALUE;
//        for (int i = 0, n = skeleton.getSlots().size; i < n; i++) {
//            Slot slot = skeleton.getSlots().get(i);
//            Attachment attachment = slot.getAttachment();
//            if (attachment == null) continue;
//            if (!(attachment instanceof RegionAttachment)) continue;
//            RegionAttachment imageRegion = (RegionAttachment)attachment;
//            imageRegion.updateWorldVertices(slot, false);
//            float[] vertices = imageRegion.getWorldVertices();
//            for (int ii = 0, nn = vertices.length; ii < nn; ii +=5) {
//                minX = Math.min(minX, vertices[ii]);
//                minY = Math.min(minY, vertices[ii + 1]);
//                maxX = Math.max(maxX, vertices[ii]);
//                maxY = Math.max(maxY, vertices[ii + 1]);
//            }
//        }
//
//        setWidth(maxX - minX);
//        setHeight(maxY - minY);
//    }
	
	public Array<Object> getAnimations() {
		return spineData.getAnimations();
	}

    private void initSpine() {
    	spineData = new SpineDataHelper();
    	try {
			spineData.initSpine(dataVO, essentials.rm, spineReflectionHelper,mulX);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			System.out.println("Reflection problem");
			e.printStackTrace();
		}
//        skeletonJson = new SkeletonJson(essentials.rm.getSkeletonAtlas(dataVO.animationName));
//        skeletonJson.setScale(dataVO.scaleX);
//        skeletonData = skeletonJson.readSkeletonData((essentials.rm.getSkeletonJSON(dataVO.animationName)));
//
//        skeleton = new Skeleton(skeletonData); // Skeleton holds skeleton state (bone positions, slot attachments, etc).
//        AnimationStateData stateData = new AnimationStateData(skeletonData); // Defines mixing (crossfading) between animations.
//        state = new AnimationState(stateData); // Holds the animation state for a skeleton (current animation, time, etc).
//
//        computeBoundBox();
//
//        // todo: fix this, it's a temporary soluition
//        setAnimation(skeletonData.getAnimations().get(0).getName());
    	setWidth(spineData.width);
    	setHeight(spineData.height);
    }
	
	public void setAnimation(String animName){
		spineData.setAnimation(animName);
		dataVO.currentAnimationName = animName;
	}

	public Object getState() {
		return spineData.stateObject;
	}
//	private void drawRect() {
//		Texture pixmapBoundTexture = null;
//	
//		Pixmap pixmapRect = new Pixmap( (int)getWidth(), (int) getHeight(), Format.RGBA8888 );
//		pixmapRect.setColor( 1f, 1f, 1f, 1f );
//		pixmapRect.drawRectangle(0, 0, (int) getWidth(), (int) getHeight());
//		pixmapBoundTexture = new Texture( pixmapRect );
//		pixmapRect.dispose();
//	
//		com.badlogic.gdx.scenes.scene2d.ui.Image boundingActor = new com.badlogic.gdx.scenes.scene2d.ui.Image( pixmapBoundTexture);
//		addActor(boundingActor);
//		boundingActor.setX(-55);
//		boundingActor.setY(-55);
//	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		spineData.draw(batch, parentAlpha);
		//renderer.draw(batch, skeleton);
		super.draw(batch, parentAlpha);
	}

	@Override
	public void act(float delta) {
//		skeleton.updateWorldTransform(); // 
//		state.update(delta); // Update the animation time.
//		state.apply(skeleton); // Poses skeleton using current animations. This sets the bones' local SRT.
//		skeleton.setPosition(getX()-minX, getY()-minY);
		spineData.act(delta, getX(), getY());
		super.act(delta);
	}
	
	public void setTint(Color tint) {
		float[] clr = new float[4]; 
		clr[0] = tint.r;
		clr[1] = tint.g;
		clr[2] = tint.b;
		clr[3] = tint.a;
		this.getDataVO().tint = clr;
		this.setColor(tint);
	}
	
	public SpineVO getDataVO() {
		return dataVO;
	}
	


	public void renew() {
		setX(dataVO.x*this.mulX);
		setY(dataVO.y*this.mulY);
        setScaleX(dataVO.scaleX);
        setScaleY(dataVO.scaleY);
		setRotation(dataVO.rotation);
        setColor(dataVO.tint[0],dataVO.tint[1], dataVO.tint[2], dataVO.tint[3]);
        customVariables.loadFromString(dataVO.customVars);
        initSpine();
	}

	@Override
	public boolean isLockedByLayer() {
		return isLockedByLayer;
	}

	@Override
	public void setLockByLayer(boolean isLocked) {
		isLockedByLayer = isLocked;
	}

	@Override
	public boolean isComposite() {
		return false;
	}
	
	public void updateDataVO() {
		dataVO.x = getX()/this.mulX;
		dataVO.y = getY()/this.mulY;
		dataVO.rotation = getRotation();
		
		if(getZIndex()>=0){
			dataVO.zIndex = getZIndex();
		}
		
		if(dataVO.layerName == null || dataVO.layerName.equals("")) {
			dataVO.layerName = "Default";
		}

        dataVO.customVars = customVariables.saveAsString();
	}

    public void applyResolution(float mulX, float mulY) {
        this.mulX = mulX;
        this.mulY = mulY;
        setX(dataVO.x * this.mulX);
        setY(dataVO.y * this.mulY);
        updateDataVO();
        initSpine();
    }

	@Override
	public int getLayerIndex() {
		return layerIndex;
	}

	@Override
	public void setLayerIndex(int index) {
		layerIndex = index;
	}
	
	public CompositeItem getParentItem() {
		return parentItem;
	}
	
	public void setParentItem(CompositeItem parentItem) {
		this.parentItem = parentItem;
	}


    @Override
    public void setScale(float scale) {
        super.setScale(scale, scale);
        dataVO.scaleX = scale;
        renew();
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public void dispose() {
        if(essentials.world != null && getBody() != null)essentials.world.destroyBody(getBody());
        	setBody(null);
    }

    public CustomVariables getCustomVariables() {
        return customVariables;
    }

    public String getCurrentAnimationName() {
        return dataVO.currentAnimationName;
    }
}
