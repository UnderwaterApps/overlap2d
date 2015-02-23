package com.uwsoft.editor.renderer.utils;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.brashmonkey.spriter.Drawer;
import com.brashmonkey.spriter.Loader;
import com.brashmonkey.spriter.Player;
import com.brashmonkey.spriter.Timeline.Key.Object;

public class LibGdxDrawer extends Drawer<Sprite>{
	
	Batch batch;
	ShapeRenderer renderer;
	
	public LibGdxDrawer(Loader<Sprite> loader, ShapeRenderer renderer){
		super(loader);
		this.renderer = renderer;
	}
	
	@Override
	public void setColor(float r, float g, float b, float a) {
		renderer.setColor(r, g, b, a);
	}
	
	@Override
	public void rectangle(float x, float y, float width, float height) {
		renderer.rect(x, y, width, height);
	}
	
	@Override
	public void line(float x1, float y1, float x2, float y2) {
		renderer.line(x1, y1, x2, y2);
	}

	@Override
	public void circle(float x, float y, float radius) {
		renderer.circle(x, y, radius);
	}

	public void beforeDraw(Player player, Batch batch) {
		this.batch	=	batch;
		draw(player);
	}
	@Override
	public void draw(Object object) {
		Sprite sprite = loader.get(object.ref);

		float newPivotX = (sprite.getWidth() * object.pivot.x);
		float newX = object.position.x - newPivotX;
		float newPivotY = (sprite.getHeight() * object.pivot.y);
		float newY = object.position.y - newPivotY;
		
		sprite.setX(newX);
		sprite.setY(newY);
        sprite.setOrigin(newPivotX, newPivotY);
		sprite.setRotation(object.angle);
		
		sprite.setColor(1f, 1f, 1f, object.alpha);
		sprite.setScale(object.scale.x, object.scale.y);
		sprite.draw(batch);
	}
}
