package com.uwsoft.editor.renderer.conponents.physics;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class PhysicsBodyPropertiesComponent extends Component {
	public int bodyType = 0;

	public float mass;
	public Vector2 centerOfMass;
	public float rotationalInertia;
	public float damping;
	public float gravityScale;
	public boolean allowSleep;
	public boolean awake;
	public boolean bullet;

	public float density;
	public float friction;
	public float restitution;
}
