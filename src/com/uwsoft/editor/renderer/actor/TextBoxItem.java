package com.uwsoft.editor.renderer.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.uwsoft.editor.renderer.data.Essentials;
import com.uwsoft.editor.renderer.data.TextBoxVO;
import com.uwsoft.editor.renderer.resources.IResourceRetriever;
import com.uwsoft.editor.renderer.utils.CustomVariables;

public class TextBoxItem extends TextField implements IBaseItem  {

	public TextBoxVO dataVO;	
	public Essentials essentials;
	public float mulX = 1f;
	public float mulY = 1f;
	protected int layerIndex = 0;
	private boolean isLockedByLayer = false;
	private CompositeItem parentItem = null;

    private Body body;

    private CustomVariables customVariables = new CustomVariables();

	public TextBoxItem(TextBoxVO vo, Essentials rm,CompositeItem parent) {
		this(vo, rm);
		setParentItem(parent);
	}
	
	public TextBoxItem(TextBoxVO vo, Essentials e) {
		super(vo.defaultText, e.rm.getSkin(),vo.style.isEmpty()?"default":vo.style);
		
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
		
		setFocusTraversal(false);
		renew();
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
		
	
	public TextBoxVO getDataVO() {
		//updateDataVO();
		return dataVO;
	}
	
	@Override
	public void renew() {
		setText(dataVO.defaultText);
		if(dataVO.width > 0) {
			setWidth(dataVO.width);
		}
		if(dataVO.height > 0) {
			setHeight(dataVO.height);
		}
		invalidate();
		
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
	
	public CompositeItem getParentItem() {
		return parentItem;
	}
	
	public void setParentItem(CompositeItem parentItem) {
		this.parentItem = parentItem;
	}
	
	public void setStyle(TextFieldStyle lst, String styleName) {
		setStyle(lst);
		dataVO.style	=	styleName;
	}


    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public void dispose() {
        if(essentials.world != null && getBody() != null) essentials.world.destroyBody(getBody());
        setBody(null);
    }

    public CustomVariables getCustomVariables() {
        return customVariables;
    }
}
