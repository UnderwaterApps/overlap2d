package com.uwsoft.editor.renderer.actor;

import com.badlogic.gdx.physics.box2d.Body;
import com.uwsoft.editor.renderer.data.MainItemVO;
import com.uwsoft.editor.renderer.utils.CustomVariables;

public interface IBaseItem {
	public MainItemVO getDataVO(); 
	public boolean isLockedByLayer() ;
	public void setLockByLayer(boolean isLocked);
	public boolean isComposite();
	public void renew();
    public CustomVariables getCustomVariables();
	public int getLayerIndex();
	public void setLayerIndex(int index);
    public Body getBody();
    public void setBody(Body body);
	public void updateDataVO();
	public void applyResolution(float mulX,float mulY);
	public void setParentItem(CompositeItem parent);
	public CompositeItem getParentItem(); 
	public void dispose();
}
