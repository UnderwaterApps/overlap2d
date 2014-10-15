package com.uwsoft.editor.gdx.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class BigImage extends Image {
	
	private boolean blending;

	public BigImage(String name, TextureRegion pRegion) {
		super(pRegion);
		blending = false;
	}

	public void draw(SpriteBatch batch, float parentAlpha) {
		if(!blending) {
			batch.disableBlending();
		}
		
		super.draw(batch, parentAlpha);
		
		if(!blending) {
			batch.enableBlending();
		}
	}
	
	public boolean getBlending() {
		return blending;
	}

	/**
	 * @param Enable blending
	 */
	public void enableBlending() {
		this.blending = true;
	}
	
	/**
	 * @param Enable blending
	 */
	public void disableBlending() {
		this.blending = false;
	}
	
	public static BigImage factory(Texture texture, float width, float height) {
		return new BigImage("bgImage",  new TextureRegion(texture, 0, 0, (int)width, (int)height));
	}
	
	public void alignCenter() {
	//	this.x = Config.SIZE[1]/2-this.width/2;
	  //  this.y = Config.SIZE[0]/2-this.height/2;
	}
	
	public void alignTop() {
	//	this.x = Config.SIZE[1]/2-this.width/2;
	 //   this.y = Config.SIZE[0]-this.height;
	}
	
	public void alignBottom() {
	//	this.x = Config.SIZE[1]/2-this.width/2;
	  //  this.y = 0;
	}	

}
