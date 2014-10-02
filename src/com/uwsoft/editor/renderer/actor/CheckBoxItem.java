package com.uwsoft.editor.renderer.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.uwsoft.editor.renderer.data.CheckBoxVO;
import com.uwsoft.editor.renderer.data.Essentials;
import com.uwsoft.editor.renderer.resources.IResourceRetriever;
import com.uwsoft.editor.renderer.utils.CustomVariables;

public class CheckBoxItem extends CheckBox implements IBaseItem {
	
	public CheckBoxVO dataVO;	
	public Essentials essentials;
	public float mulX = 1f;
	public float mulY = 1f;
	protected int layerIndex = 0;

    private CustomVariables customVariables = new CustomVariables();

    private boolean isLockedByLayer = false;
	private CompositeItem parentItem = null;

    private Body body;
	
	public CheckBoxItem(CheckBoxVO vo, Essentials e,CompositeItem parent) {
		this(vo, e);
		setParentItem(parent);
	}
	
	public CheckBoxItem(CheckBoxVO vo, Essentials e) {
		super(vo.text, e.rm.getSkin(),vo.style.isEmpty()?"default":vo.style);
		dataVO = vo;
		this.essentials = e;
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
	
	public void setTint(Color tint) {
		float[] clr = new float[4]; 
		clr[0] = tint.r;
		clr[1] = tint.g;
		clr[2] = tint.b;
		clr[3] = tint.a;
		this.getDataVO().tint = clr;
		this.setColor(tint);
	}
		
	public CheckBoxVO getDataVO() {
		//updateDataVO();
		return dataVO;
	}
	
	@Override
	public void renew() {		
		setText(dataVO.text);
		pack(); layout();
		
		setX(dataVO.x*this.mulX);
		setY(dataVO.y*this.mulY);
		setScaleX(dataVO.scaleX*this.mulX);
		setScaleY(dataVO.scaleY*this.mulY);
		setRotation(dataVO.rotation);
        setColor(dataVO.tint[0],dataVO.tint[1], dataVO.tint[2], dataVO.tint[3]);

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
		setScaleX(dataVO.scaleX*mulX);
		setScaleY(dataVO.scaleY*mulY);
		
		this.mulX = mulX;
		this.mulY = mulY;
		setX(dataVO.x*this.mulX);
		setY(dataVO.y*this.mulY);
		updateDataVO();			
	}

	@Override
	public int getLayerIndex() {
		return layerIndex;
	}

	@Override
	public void setLayerIndex(int index) {
		layerIndex = index;
	}
	
	public void setParentItem(CompositeItem parentItem) {
		this.parentItem = parentItem;
	}
	
	public CompositeItem getParentItem() {
		return parentItem;
	}
	
	public void setStyle(CheckBoxStyle lst, String styleName) {		
		setStyle(lst);
		dataVO.style	=	styleName;
		
	}

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

	@Override
	public void dispose() {
        if(essentials.world != null && body != null) essentials.world.destroyBody(getBody());
        setBody(null);
	}

    public CustomVariables getCustomVariables() {
        return customVariables;
    }
}
