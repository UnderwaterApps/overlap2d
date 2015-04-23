package com.uwsoft.editor.renderer.conponents.light;

import box2dLight.Light;

import com.badlogic.ashley.core.Component;
import com.uwsoft.editor.renderer.legacy.data.LightVO.LightType;

public class LightObjectComponent extends Component {
	//TODO temporarley disabled because of legasy vo
	//public static enum LightType {POINT, CONE}
	public LightType type;
	public int rays = 12;
	public float distance = 300;
	public float directionDegree = 0;
	public float coneDegree = 30;
	public boolean isStatic = true;
	public boolean isXRay = true;
	public Light lightObject = null;
	
}
