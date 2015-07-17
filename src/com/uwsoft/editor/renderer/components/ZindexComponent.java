package com.uwsoft.editor.renderer.components;

import com.badlogic.ashley.core.Component;

public class ZindexComponent extends Component {
	private int zIndex = 0;
	public boolean needReOrder = false;
	public String layerName = "";
	public int layerIndex;
	
	public int getzIndex() {
		return zIndex;
	}
	
	public void setzIndex(int zIndex) {
		this.zIndex = zIndex;
		needReOrder = true;
	}
	
}
