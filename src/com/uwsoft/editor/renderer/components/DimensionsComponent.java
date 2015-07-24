package com.uwsoft.editor.renderer.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Rectangle;

public class DimensionsComponent extends Component {
	public float width = 0;
	public float height = 0;

	public Rectangle boundBox;

	public boolean hit(float x, float y){
		if(boundBox == null) {
			return (x >= 0 && x < width && y >= 0 && y < height);
		} else {
			return (x >= boundBox.x && x < boundBox.x+boundBox.width && y >= boundBox.y && y < boundBox.y+boundBox.height);
		}
	}
}
