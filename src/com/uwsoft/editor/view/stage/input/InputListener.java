package com.uwsoft.editor.view.stage.input;

import com.badlogic.ashley.core.Entity;

public interface InputListener {
	
	public boolean keyDown(Entity entity, int keycode);
	
	public boolean keyUp(Entity entity, int keycode);
	
	public boolean keyTyped(Entity entity, char character);

	public boolean touchDown(Entity entity, float screenX, float screenY, int pointer, int button);
	
	public void touchUp(Entity entity, float screenX, float screenY, int pointer, int button);
	
	public void touchDragged(Entity entity, float screenX, float screenY, int pointer);
	
	public boolean mouseMoved(Entity entity, float screenX, float screenY);
	
	public boolean scrolled(Entity entity, int amount);

}
