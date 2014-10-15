package com.uwsoft.editor.utils;

import java.util.ArrayList;

public class DelayManager {

	public static DelayManager instance;
	
	public ArrayList<DelayedAction> actions = new ArrayList<DelayedAction>();
	
	public DelayManager() {
		
	}
	
	public static DelayManager getInstance() {
		if(instance == null) {
			instance = new DelayManager();
		}
		
		return instance;
	}
	
	public static DelayedAction scheduleAction(DelayedAction a) {
		getInstance().actions.add(a);
		
		return a;
	}
	
	public void act(float delta) {
		for(int i = 0; i < actions.size(); i++) {
			if(actions.get(i).complete) {
				actions.remove(i);
			} else {
				actions.get(i).doTimer.update(delta);
			}
		}
	}
}
