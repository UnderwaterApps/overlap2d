package com.uwsoft.editor.renderer.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.uwsoft.editor.renderer.data.Essentials;
import com.uwsoft.editor.renderer.data.ParticleEffectVO;
import com.uwsoft.editor.renderer.resources.IResourceRetriever;
import com.uwsoft.editor.renderer.utils.CustomVariables;

public class ParticleItem extends Group implements IBaseItem {

	private ParticleActor particle;
	public ParticleEffectVO dataVO;	
	public Essentials essentials;
	public float mulX = 1f;
	public float mulY = 1f;
	protected int layerIndex = 0;
	private boolean isLockedByLayer = false;
	private CompositeItem parentItem = null;

    private Body body;

    private CustomVariables customVariables = new CustomVariables();
	
	public ParticleItem(ParticleEffectVO vo, Essentials e,CompositeItem parent) {
		this(vo, e);
		setParentItem(parent);
	}
	
	public ParticleItem(ParticleEffectVO vo, Essentials e) {
		
		this.essentials = e;
		dataVO = vo;
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

		particle = new ParticleActor(e.rm.getParticleEffect(vo.particleName));
		addActor(particle);
		
		particle.setX(50);
		particle.setY(50);
		
		setWidth(vo.particleWidth);
		setHeight(vo.particleHeight);
	}	
	
	
	public void start(){
		particle.start();
	}
	
	public ParticleActor getParticle() {
        return particle;
    }
	
	public void forceContinuous() {
        Array<ParticleEmitter> emitters = getParticle().getParticleEffect().getEmitters();
        for(int i = 0; i < emitters.size; i++) {
            emitters.get(i).setContinuous(true);
        }
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
	
	public ParticleEffectVO getDataVO() {
		//updateDataVO();
		return dataVO;
	}
	
	@Override
	public void renew() {		
		setX(dataVO.x*this.mulX);
		setY(dataVO.y*this.mulY);
		setScaleX(dataVO.scaleX*this.mulX);
		setScaleY(dataVO.scaleY*this.mulY);
		setRotation(dataVO.rotation);
        customVariables.loadFromString(dataVO.customVars);
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
		setX(dataVO.x*this.mulX);
		setY(dataVO.y*this.mulY);
		updateDataVO();	
		this.setScale(mulX, mulY);
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
	public float getWidth() {
		return super.getWidth()*getScaleX();
	}
	
	@Override
	public float getHeight() {
		return super.getHeight()*getScaleY();
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
}
