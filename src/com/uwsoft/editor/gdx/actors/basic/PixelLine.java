package com.uwsoft.editor.gdx.actors.basic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.uwsoft.editor.data.manager.TextureManager;

public class PixelLine extends Image {

	public PixelLine(TextureManager tm, float x, float y, float toX, float toY) {
		super(prepareTexture(tm));
		
		setPosition(x, y, toX, toY);
	}
	
	public void setPosition(float x, float y, float toX, float toY) {
		this.setX(x);
		this.setY(y);
		
		double lineLength = Math.sqrt((toX-x)*(toX-x)+(toY-y)*(toY-y));
		this.setScaleX((float) lineLength);
		
		this.setRotation(90-getAngle(x, y, toX, toY));
	}
	
	public void setOpacity(float opacity) {
		Color clr = getColor();
		clr.a = opacity;
		setColor(clr);
	}
	
	private static AtlasRegion prepareTexture(TextureManager tm) {
		AtlasRegion tx = tm.getEditorAsset("pixel");
		return tx;
	}
	
	private float getAngle(float x, float y, float toX, float toY) {
	    float angle = (float) Math.toDegrees(Math.atan2(toX - x, toY - y));

	    if(angle < 0){
	        angle += 360;
	    }

	    return angle;
	}
	
}
