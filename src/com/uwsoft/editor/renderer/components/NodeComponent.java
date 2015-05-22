package com.uwsoft.editor.renderer.components;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.SnapshotArray;

public class NodeComponent extends ParentNodeComponent {
	public SnapshotArray<Entity> children = new SnapshotArray<Entity>(true, 1, Entity.class);
}
