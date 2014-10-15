package com.uwsoft.editor.gdx.ui.properties;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.actor.CompositeItem;
import com.uwsoft.editor.renderer.actor.IBaseItem;

public class PropertyBox extends Group {

	protected IBaseItem item = null;
	private SceneLoader sceneLoader;
	protected CompositeItem ui;
	private String elemId;
	
	public PropertyBox(SceneLoader sceneLoader, String id) {
		this.sceneLoader = sceneLoader;
		elemId = id;
		initView();
	}
	
	public void initView() {
		clear();
		ui =  sceneLoader.getCompositeElementById(elemId);	
		ui.setX(0);
		ui.setY(0);		
		setWidth(ui.getWidth());
		setHeight(ui.getHeight());
		addActor(ui);
	}
	
	double roundTwoDecimals(double d) {
		//DecimalFormat twoDForm = new DecimalFormat("#.##");
		//return Double.valueOf(twoDForm.format(d));
		return Math.round(d * 100.0) / 100.0;
	}
}
