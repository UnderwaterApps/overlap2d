package com.uwsoft.editor.gdx.ui.properties;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.actor.CompositeItem;

public class CompositeItemProperties extends PropertyBox implements IPropertyBox<CompositeItem>  {

	private 	CheckBox scissorsInabled;
	private  	CompositeItem item;
	
	public CompositeItemProperties(SceneLoader scene) {
		super(scene, "CompositeItemProperties");
	}

	@Override
	public void setObject(CompositeItem object) {
		item = object;
		
		scissorsInabled = ui.getCheckBoxById("scissorsInabled");
		
		
		setListeners();
	}


    @Override
    public void updateView() {

    }

	private void setListeners() {
		scissorsInabled.addListener(new ClickListener() {
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				
				super.touchUp(event, x, y, pointer, button);		
			}
		});		
	}

}
