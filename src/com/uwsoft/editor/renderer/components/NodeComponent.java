package com.uwsoft.editor.renderer.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.SnapshotArray;

public class NodeComponent implements Component {
	public SnapshotArray<Entity> children = new SnapshotArray<Entity>(true, 1, Entity.class);

	public void removeChild(Entity entity) {
		children.removeValue(entity, false);
	}

	public void addChild(Entity entity) {
		children.add(entity);
	}
}
