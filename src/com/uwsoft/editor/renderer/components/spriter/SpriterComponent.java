package com.uwsoft.editor.renderer.components.spriter;

import java.util.ArrayList;

import com.badlogic.ashley.core.Component;
import com.brashmonkey.spriter.Data;
import com.brashmonkey.spriter.Player;

public class SpriterComponent implements Component {
	public Player player;
	public Data data;
	public ArrayList<String> animations = new ArrayList<String>();
	public ArrayList<String> entities = new ArrayList<String>();
	public int currentEntityIndex = 0;
	public int currentAnimationIndex;
	
	public int 	entity;
	public int 	animation;
	public String 	animationName = "";    
	public float 	scale	=	1f;
}
