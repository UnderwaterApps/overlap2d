package com.uwsoft.editor.renderer.conponents.sprite;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.ashley.core.Component;
import com.uwsoft.editor.renderer.SceneLoader;

public class SpriteAnimationComponent extends Component {
	public String animationName = "";
	public int fps = 24;
	public String animations = "";
	public Map<String, SceneLoader.Frames> keyFrames = new HashMap<>(); 
	
}
