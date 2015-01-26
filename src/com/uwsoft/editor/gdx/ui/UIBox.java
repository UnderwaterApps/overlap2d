package com.uwsoft.editor.gdx.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.uwsoft.editor.gdx.stage.UIStage;

public class UIBox extends Group {

	protected Group backLayer;
	
	protected Group mainLayer;
	
	protected UIStage stage;
	
	protected String topImgName = "boxTop";
	
	protected Image topImg;
	
	public UIBox(UIStage s, float width, float height) {
		
		this.stage = s;
		
		backLayer = new Group();
		mainLayer = new Group();
		
		this.setWidth(width);
		this.setHeight(height);

		addActor(backLayer);
		addActor(mainLayer);
	}
	
	public void initPanel() {
		
		Image bgImg = new Image(stage.textureManager.getEditorAsset("pixel"));
		bgImg.setColor(0.325f, 0.325f, 0.325f, 1.0f);
		
		bgImg.setScaleX(getWidth());
		bgImg.setScaleY(getHeight());

		backLayer.addActor(bgImg);
		
		Image sideImgL = new Image(stage.textureManager.getEditorAsset("boxSide"));
		Image sideImgR = new Image(stage.textureManager.getEditorAsset("boxSide"));
		Image sideImgB = new Image(stage.textureManager.getEditorAsset("boxSide"));
		Image cornerImgL = new Image(stage.textureManager.getEditorAsset("boxCorner"));
		Image cornerImgR = new Image(stage.textureManager.getEditorAsset("boxCorner"));
		
		topImg = new Image(stage.textureManager.getEditorAsset(topImgName));
		
		sideImgL.setX(0);
		sideImgL.setY(0);
		sideImgL.setScaleY(getHeight()-1);
		sideImgR.setX(getWidth());
		sideImgR.setY(0);
		sideImgR.setScaleY(getHeight()-1);
		
		sideImgB.setRotation(-90);
		sideImgB.setX(1);
		sideImgB.setY(3);
		sideImgB.setScaleY(getWidth());
		
		cornerImgL.setX(0);
		cornerImgL.setY(0);
		cornerImgL.setScaleX(1);
		cornerImgR.setX(getWidth()+3);
		cornerImgR.setY(0);
		cornerImgR.setScaleX(-1);
		
		topImg.setScaleX(getWidth()-3);
		topImg.setX(3);
		topImg.setY(getHeight() - topImg.getHeight()+1);
		
		backLayer.addActor(sideImgL);
		backLayer.addActor(sideImgR);
		backLayer.addActor(sideImgB);
		backLayer.addActor(cornerImgL);
		backLayer.addActor(cornerImgR);
		backLayer.addActor(topImg);
	}
	
}
