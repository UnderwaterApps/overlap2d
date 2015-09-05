package com.uwsoft.editor.renderer.systems.render.logic;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Batch;

public interface Drawable {
	public abstract void draw(Batch batch, Entity entity, float parentAlpha);
}
