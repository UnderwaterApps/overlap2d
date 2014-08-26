package com.uwsoft.editor.renderer.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.uwsoft.editor.renderer.IResource;
import com.uwsoft.editor.renderer.data.LabelVO;

public class LabelItem extends Label implements IBaseItem  {
	
	public LabelVO dataVO;	
	public IResource rm;
	public float mulX = 1f;
	public float mulY = 1f;
	protected int layerIndex = 0;
	private boolean isLockedByLayer = false;
	private CompositeItem parentItem = null;

	public LabelItem(LabelVO vo, IResource rm,CompositeItem parent) {
		this(vo, rm);
		setParentItem(parent);
	}
	
	public LabelItem(LabelVO vo, IResource rm) {
		super(vo.text, rm.getSkin(), vo.style.isEmpty()?"default":vo.style);
		
		dataVO = vo;	
		
		this.rm = rm;
		setX(dataVO.x);
		setY(dataVO.y);
		
//		setScaleX(dataVO.scaleX*this.mulX);
//		setScaleY(dataVO.scaleY*this.mulY);
		
		this.setFontScale(dataVO.scaleX, dataVO.scaleY);
		this.setRotation(dataVO.rotation); 
		
		if(dataVO.zIndex < 0) dataVO.zIndex = 0;
				
		if(dataVO.tint == null) {			
			setTint(new Color(1, 1, 1, 1));	
		} else {
			setTint(new Color(dataVO.tint[0], dataVO.tint[1], dataVO.tint[2], dataVO.tint[3]));
		} 
		setAlignment(Align.bottom);
		pack();layout();
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
	
	public LabelVO getDataVO() {
		//updateDataVO();
		return dataVO;
	}
	
	@Override
	public void renew() {		
		setText(dataVO.text);

		setX(dataVO.x*this.mulX);
		setY(dataVO.y*this.mulY);
//		setScaleX(dataVO.scaleX*this.mulX);
//		setScaleY(dataVO.scaleY*this.mulY);
		setRotation(dataVO.rotation);
        setColor(dataVO.tint[0],dataVO.tint[1], dataVO.tint[2], dataVO.tint[3]);
		this.setFontScale(dataVO.scaleX*mulX, dataVO.scaleY*mulY);
		pack(); layout();
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
	}
	
	public void applyResolution(float mulX, float mulY) {		
		
		this.mulX = mulX;
		this.mulY = mulY;
		setX(dataVO.x*this.mulX);
		setY(dataVO.y*this.mulY);
//		setScaleX(dataVO.scaleX*this.mulX);
//		setScaleY(dataVO.scaleY*this.mulY);
		updateDataVO();	
		this.setFontScale(dataVO.scaleX*mulX, dataVO.scaleY*mulY);
		pack(); layout();
	}
	

	@Override
	public int getLayerIndex() {
		return layerIndex;
	}

	@Override
	public void setLayerIndex(int index) {
		layerIndex = index;
	}
	
	public void setStyle(LabelStyle lst, String styleName) {
		setStyle(lst);
		dataVO.style	=	styleName;
	}
	
	public CompositeItem getParentItem() {
		return parentItem;
	}
	
	public void setParentItem(CompositeItem parentItem) {
		this.parentItem = parentItem;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}
