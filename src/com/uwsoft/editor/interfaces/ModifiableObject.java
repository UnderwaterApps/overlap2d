package com.uwsoft.editor.interfaces;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public interface ModifiableObject {
	
	public void updateData(Actor data);
	public Rectangle getBoundRect();
}
