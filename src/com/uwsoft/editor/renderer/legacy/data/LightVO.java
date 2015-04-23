package com.uwsoft.editor.renderer.data;

public class LightVO extends MainItemVO{
	//public int itemId = -1;
	public static enum LightType {POINT, CONE}
	public LightType type;
	public int rays = 12;
	public float distance = 300;
	public float directionDegree = 0;
	public float coneDegree = 30;
	public boolean isStatic = true;
	public boolean isXRay = true;
	
	public LightVO() {
		tint = new float[4];
		tint[0] = 1f;
		tint[1] = 1f;
		tint[2] = 1f;
		tint[3] = 1f;
	}
	
	public LightVO(LightVO vo) {
		super(vo);
		type = vo.type;
		rays = vo.rays;
		distance = vo.distance;
		directionDegree = vo.directionDegree;
		coneDegree = vo.coneDegree;
		isStatic = vo.isStatic;
		isXRay = vo.isXRay;
	}
	
}
