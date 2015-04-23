package com.uwsoft.editor.renderer.conponents.sprite;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;

public class SpriteAnimationStateComponent extends Component {
	public Animation currentAnimation; 
	public float time = 0.0f;
	
	public Animation get() {
		return currentAnimation;
	}
	
	public void set(Animation newAnimation) {
		currentAnimation = newAnimation;
		time = 0.0f;
	}
}
