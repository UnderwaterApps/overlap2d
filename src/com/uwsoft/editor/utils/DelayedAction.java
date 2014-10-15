package com.uwsoft.editor.utils;

import com.uwsoft.editor.utils.UpdatebleTimer.TimerCompleteHandler;


public class DelayedAction {
	
	public UpdatebleTimer doTimer;
	
	public boolean looping;
	
	public boolean complete = false;
	
	public interface DelayedActionBody {
		public void doAction();
	}
	public DelayedActionBody delayedActionBody;	
	
	public DelayedAction(float delay, Boolean isLooping, DelayedActionBody handler) {
		delayedActionBody = handler;
        TimerCompleteHandler completeHandler = new TimerCompleteHandler(){
	    	public void complete(UpdatebleTimer timer) {
	    		delayedActionBody.doAction();
	    		if(!looping) complete = true;
			}
	    };

	    
		looping = isLooping;
		doTimer = new UpdatebleTimer(delay, isLooping, false, completeHandler);
		doTimer.start();
	}
	
	public void stopAction() {
		complete = true;
	}
}
