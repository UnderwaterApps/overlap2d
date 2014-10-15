package com.uwsoft.editor.gdx.ui;

public interface ProgressHandler {

	public void progressStarted();
	
	public void progressChanged(float value);
	
	public void progressComplete();
	
}
