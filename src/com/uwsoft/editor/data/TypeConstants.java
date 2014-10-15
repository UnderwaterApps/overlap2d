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
		
	//---------------------------------------------------------- delimiter :P
	//Elements types goes here	
	public final int TYPE_KEY = 1;
	public final int TYPE_GATE = 2;
	public final int TYPE_CLOSE_OPEN_DOOR = 3;
	public final int TYPE_ENERGY_FIELD = 4;
	public final int TYPE_GRAVITY_PULL = 5;
	public final int TYPE_RESISTANCE = 6;
	public final int TYPE_PORTAL = 7;
	public final int TYPE_FUELING = 8;
	public final int TYPE_GARBADGE = 9;
	public final int TYPE_SOFT_WALL = 10;
	public final int TYPE_BOUNCE_WALL = 11;
	public final int TYPE_DAMAGE_OBSTACLE = 12;
	public final int TYPE_SPEED_BOOST = 13;
	public final int TYPE_SUDDEN_STOP = 14;
	public final int TYPE_DECOR = 15;
	public final int TYPE_LANDING = 16;
	public final int TYPE_BACKGROUND = 17;
	public final int TYPE_COMPLEX_DECOR_OBSTACLE = 18;
	
	//Reserve For AI Messages Avtions 9000+9100
	public final int AI = 9000;

}
