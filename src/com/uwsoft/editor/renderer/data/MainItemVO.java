package com.uwsoft.editor.renderer.data;

import java.util.Arrays;


public class MainItemVO {
	public String itemIdentifier = "";
	public String itemName = "";
	public float x; 
	public float y;
	public float scaleX	=	1f; 
	public float scaleY	=	1f;
	public float rotation;
	public int zIndex = 0;
	public String layerName = "";
	public float[] tint = null;
	public boolean isFlipedH = false;
	public boolean isFlipedV = false;
	
	public MainItemVO() {
		
	}
	
	public MainItemVO(MainItemVO vo) {
		itemIdentifier = new String(vo.itemIdentifier);
		itemName = new String(vo.itemName);
		x = vo.x; 
		y = vo.y;
		rotation = vo.rotation;
		zIndex = vo.zIndex;
		layerName = new String(vo.layerName);
		tint = Arrays.copyOf(vo.tint, vo.tint.length);
		isFlipedH 	= vo.isFlipedH;
		isFlipedV 	= vo.isFlipedV;
		scaleX 		= vo.scaleX;
		scaleY 		= vo.scaleY;
	}

}
