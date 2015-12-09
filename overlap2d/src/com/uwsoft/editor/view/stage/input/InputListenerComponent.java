package com.uwsoft.editor.view.stage.input;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Array;

public class InputListenerComponent implements Component {
	private Array<InputListener> listeners = new Array<InputListener>(1); 
	
	public void addListener(InputListener listener){
		if (!listeners.contains(listener, true)) {
			listeners.add(listener);
		}
		
	}
	
	public void removeListener(InputListener listener){
		listeners.removeValue(listener, true);
	}
	
	public void removeAllListener(){
		listeners.clear();
	}
	
	public Array<InputListener> getAllListeners(){
		listeners.shrink();
		return listeners;
	}
	
}
