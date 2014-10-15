package com.uwsoft.editor.gdx.ui.properties;

public interface IPropertyBox<T> {
	
	public void setObject(T object);
	
	public void initView();

    public void updateView();
	
}
