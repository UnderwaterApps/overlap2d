package com.uwsoft.editor.gdx.ui.properties;

import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.actor.SelectBoxItem;

public class SelectBoxItemProperties extends PropertyBox implements IPropertyBox<SelectBoxItem<String>> {

	
	
	public SelectBoxItemProperties(SceneLoader scene) {
		super(scene, "SelectBoxItemProperties");
	}

	@Override
	public void setObject(SelectBoxItem<String> object) {
		item = object;		
		
		setListeners();
	}


    @Override
    public void updateView() {

    }

	private void setListeners() {
				
	}
	
}
