package com.uwsoft.editor.mvc.view.stage.input;

import com.badlogic.ashley.core.Entity;

public interface InputListener {
	
	public boolean keyDown(Entity entity, int keycode);
	
	public boolean keyUp(Entity entity, int keycode);
	
	public boolean keyTyped(Entity entity, char character);

	public boolean touchDown(Entity entity, int screenX, int screenY, int pointer, int button);
	
	public boolean touchUp(Entity entity, int screenX, int screenY, int pointer, int button);
	
	public boolean touchDragged(Entity entity, int screenX, int screenY, int pointer);
	
	public boolean mouseMoved(Entity entity, int screenX, int screenY);
	
	public boolean scrolled(Entity entity, int amount);

}
