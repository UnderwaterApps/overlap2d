package com.uwsoft.editor.data;


public interface TypeConstants {
	//Trigger types goes here
	public final int COLLISION_TRIGGER = 0;
	
	public final int DOOR_OPENED_TRIGGER = 1;
	public final int DOOR_CLOSED_TRIGGER = 2;
	
	public final int TURNED_ON_TRIGGER = 3;
	public final int TURNED_OFF_TRIGGER = 4;	
	
	public final int ON_WORLD_START = 5;	
	
	
	//---------------------------------------------------------- delimiter :P
	//Action types goes here
	public final int OPEN_ACTION = 0;
	public final int CLOSE_ACTION = 1;
	
	public final int KEY_DISSAPEAR = 2;
	public final int KEY_COMEBACK = 3;
	
	public final int TURN_ON = 4;
	public final int TURN_OFF = 5;	
	
	public final int SHOW = 6;
	public final int HIDE = 7;	
	
	public final int LOCK_ACTION = 8;
	public final int UNLOCK_ACTION = 9;
	
	
	public final int START_TWEEN = 10;
	public final int PAUSE_TWEEN = 11;
	public final int STOP_TWEEN = 12;

}
