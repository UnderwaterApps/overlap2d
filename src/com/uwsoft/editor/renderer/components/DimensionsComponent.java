package com.uwsoft.editor.renderer.components;

import com.badlogic.ashley.core.Component;

public class DimensionsComponent extends Component {
	public float width = 0;
	public float height = 0;
	
	public boolean hit(float x, float y){
//		System.out.println("******** DimensionsComponent hit********");
//		System.out.println("Loacal X="+ x + " Local Y=" + y);
//		System.out.println("width="+ width + " height=" + height);
//		System.out.println("****************************************");
		return (x >= 0 && x < width && y >= 0 && y < height);
	}
}
