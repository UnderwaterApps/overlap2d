package com.uwsoft.editor.renderer.systems.render.logic;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.uwsoft.editor.renderer.components.spine.SpineDataComponent;

public class SpineDrawableLogic implements Drawable {
	
	private ComponentMapper<SpineDataComponent> spineMapper;
	private SkeletonRenderer skeletonRenderer;
	
	public SpineDrawableLogic() {
		spineMapper = ComponentMapper.getFor(SpineDataComponent.class);
		skeletonRenderer = new SkeletonRenderer();
	}

	@Override
	public void draw(Batch batch, Entity entity) {
		SpineDataComponent spineDataComponent = spineMapper.get(entity);
		//TODO parent alpha thing
		skeletonRenderer.draw(batch, spineDataComponent.skeleton);
	}

}
