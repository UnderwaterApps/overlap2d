package com.uwsoft.editor.renderer.actor;

import com.uwsoft.editor.renderer.data.MainItemVO;

public interface IBaseItem {
	public MainItemVO getDataVO(); 
	public boolean isLockedByLyaer() ;
	public void setLockByLyaer(boolean isLocked);
	public boolean isComposite();
	public void renew();
	public int getLayerIndex();
	public void setLayerIndex(int index);
	public void updateDataVO();
	public void applyResolution(float mulX,float mulY);
	public void setParentItem(CompositeItem parent);
	public CompositeItem getParentItem(); 
	public void dispose();
}
