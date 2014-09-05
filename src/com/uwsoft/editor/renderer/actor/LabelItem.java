package com.uwsoft.editor.renderer.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.uwsoft.editor.renderer.IResource;
import com.uwsoft.editor.renderer.data.LabelVO;
import com.uwsoft.editor.renderer.utils.CustomVariables;

public class LabelItem extends Label implements IBaseItem  {
	
	public LabelVO dataVO;	
	public IResource rm;
	public float mulX = 1f;
	public float mulY = 1f;
	protected int layerIndex = 0;
	private boolean isLockedByLayer = false;
	private CompositeItem parentItem = null;
	private String fontName;
	private int fontSize;
	private static int labelDefaultSize	=	12;
	private static BitmapFont font;
	private static LabelStyle	style;
    private CustomVariables customVariables = new CustomVariables();

	public LabelItem(LabelVO vo, IResource rm,CompositeItem parent) {
		this(vo, rm);
		setParentItem(parent);
	}
	
	public LabelItem(LabelVO vo, IResource rm) {
		//super(vo.text, rm.getSkin(), vo.style.isEmpty()?"default":vo.style);
		super(vo.text, generateStyle(rm,vo.style,vo.size));
		
		dataVO = vo;	
		
		this.rm = rm;
		setX(dataVO.x);
		setY(dataVO.y);

        customVariables.loadFromString(dataVO.customVars);
		
		this.setFontScale(dataVO.scaleX, dataVO.scaleY);
		this.setRotation(dataVO.rotation); 
		
		if(dataVO.zIndex < 0) dataVO.zIndex = 0;
				
		if(dataVO.tint == null) {			
			setTint(new Color(1, 1, 1, 1));	
		} else {
			setTint(new Color(dataVO.tint[0], dataVO.tint[1], dataVO.tint[2], dataVO.tint[3]));
		} 
		setAlignment(Align.left | Align.bottom);
		pack();layout();
	}			
	private static LabelStyle generateStyle(IResource rManager, String fontName, int size) {
		if(size==0){
			size	=	labelDefaultSize;
		}
		style = new LabelStyle(rManager.getBitmapFont(fontName,size),null);
		return style;
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
		if(!(fontName!=null && fontName.equals(dataVO.style) && fontSize==dataVO.size)){
			if(dataVO.size==0)dataVO.size	=	labelDefaultSize;
			System.out.println("__________"+dataVO.size);
			font 	= 	rm.getBitmapFont(dataVO.style,dataVO.size);
	        style 	=	new LabelStyle(font,null);		
			setStyle(style);
			fontName	=	dataVO.style;
			fontSize	=	dataVO.size;
		}

		setText(dataVO.text);
        customVariables.loadFromString(dataVO.customVars);
		setX(dataVO.x*this.mulX);
		setY(dataVO.y*this.mulY);
		setScaleX(dataVO.scaleX);
		setScaleY(dataVO.scaleY);
		setRotation(dataVO.rotation);
        setColor(dataVO.tint[0],dataVO.tint[1], dataVO.tint[2], dataVO.tint[3]);
		setFontScale(dataVO.scaleX, dataVO.scaleY);
		pack(); layout();
	}
	
	@Override
	public void setScaleX(float x) {
        if(x <= 0) return;
		super.setScaleX(x);
		setFontScaleX(x);
	}
	
	@Override
	public void setScaleY(float y) {
        if(y <= 0) return;
		super.setScaleY(y);
		setFontScaleY(y);
	}
	
	@Override
	public void setScale(float x, float y) {
        if(x <= 0 || y <= 0) return;
		super.setScaleY(y);
		setFontScaleY(y);
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

        dataVO.scaleX = getScaleX();
        dataVO.scaleY = getScaleY();
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
	
	public void setStyle(String styleName, int size) {
		dataVO.style	=	styleName;
		dataVO.size		=	size;
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


    public CustomVariables getCustomVariables() {
        return customVariables;
    }

}
