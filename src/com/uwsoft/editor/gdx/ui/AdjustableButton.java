package com.uwsoft.editor.gdx.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class AdjustableButton extends ImageButton {
	
	public AdjustableButton(String name, TextureRegion region) {
		this(name, region, region);
	}

	public AdjustableButton(String name, TextureRegion unpressedRegion, TextureRegion pressedRegion) {
		super(new ImageButtonStyle(null, null, null, new TextureRegionDrawable(unpressedRegion), new TextureRegionDrawable(unpressedRegion), null));
	}
}
