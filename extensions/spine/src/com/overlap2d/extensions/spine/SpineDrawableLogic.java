package com.overlap2d.extensions.spine;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.uwsoft.editor.renderer.systems.render.logic.Drawable;

public class SpineDrawableLogic implements Drawable {
	
	private ComponentMapper<SpineObjectComponent> spineMapper;
	private SkeletonRenderer skeletonRenderer;
	
	public SpineDrawableLogic() {
		spineMapper = ComponentMapper.getFor(SpineObjectComponent.class);
		skeletonRenderer = new SkeletonRenderer();
	}

	@Override
	public void draw(Batch batch, Entity entity, float parentAlpha) {
		SpineObjectComponent spineObjectComponent = spineMapper.get(entity);
		//TODO parent alpha thing
		skeletonRenderer.draw((PolygonSpriteBatch)batch, spineObjectComponent.skeleton);
	}

}
