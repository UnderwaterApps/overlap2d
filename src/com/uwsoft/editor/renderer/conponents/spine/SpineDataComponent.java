package com.uwsoft.editor.renderer.conponents.spine;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Array;
import com.uwsoft.editor.renderer.spine.SpineDataHelper;

public class SpineDataComponent extends Component {
	public String animationName = "";
	public String currentAnimationName = "";
	public SpineDataHelper spineData;
	
	public Array<Object> getAnimations() {
		return spineData.getAnimations();
	}
	
	public void setAnimation(String animName){
		spineData.setAnimation(animName);
		this.currentAnimationName = animName;
	}

	public Object getState() {
		return spineData.stateObject;
	}
}
