package com.uwsoft.editor.gdx.tween;

public interface Triggerable {
	public abstract int[] getTriggerTypes();
	
	public abstract int[] getActionTypes();
	
	public abstract void doAction(int actionType);
}
