package com.uwsoft.editor.renderer.conponents;

import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.Matrix4;

public class CompositeTransformComponent extends TransformComponent {
	public boolean transform = true;
	public final Affine2 worldTransform = new Affine2();
	public final Matrix4 computedTransform = new Matrix4();
	public final Matrix4 oldTransform = new Matrix4();
}
