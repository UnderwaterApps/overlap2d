package com.uwsoft.editor.gdx.tween;

import java.util.ArrayList;

public class TimeLineVO {
	public boolean yoyo = false;
	public boolean repeate = false;
	public String tweenId = ""; //object name that must be tweened;
	public String tweenObjectName = ""; //object name that must be tweened;
	public ArrayList<TweenVO> tweens = new ArrayList<TweenVO>(1);
	//public float durations[]; // for each sector
//	public ArrayList<TriggerAction> triggers;//triggers for each sector start o end //TODO this must be done later
	public int accelerationTypes[]; // acceleration types //TODO later may be
}
