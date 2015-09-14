package com.uwsoft.editor.view.stage.input;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.TimeUtils;

public class EntityClickListener implements InputListener {
	/** Detects mouse over, mouse or finger touch presses, and clicks on an actor. A touch must go down over the actor and is
	 * considered pressed as long as it is over the actor or within the {@link #setTapSquareSize(float) tap square}. This behavior
	 * makes it easier to press buttons on a touch interface when the initial touch happens near the edge of the actor. Double clicks
	 * can be detected using {@link #getTapCount()}. Any touch (not just the first) will trigger this listener. While pressed, other
	 * touch downs are ignored.
	 **/
	
	/** Time in seconds {@link #isVisualPressed()} reports true after a press resulting in a click is released. */
	static public float visualPressedDuration = 0.1f;

	private float tapSquareSize = 14, touchDownX = -1, touchDownY = -1;
	private int pressedPointer = -1;
	private int pressedButton = -1;
	private int button;
	private boolean pressed, over, cancelled;
	private long visualPressedTime;
	private long tapCountInterval = (long)(0.4f * 1000000000l);
	private int tapCount;
	private long lastTapTime;

	/** Create a listener where {@link #clicked(float, float)} is only called for left clicks.
	 * @see #ClickListener(int) */
	public EntityClickListener () {
	}

	/** @see #setButton(int) */
	public EntityClickListener (int button) {
		this.button = button;
	}

	@Override
	public boolean touchDown (Entity entity, float x, float y, int pointer, int button) {
		if (pressed) return false;
		if (pointer == 0 && this.button != -1 && button != this.button) return false;
		pressed = true;
		pressedPointer = pointer;
		pressedButton = button;
		touchDownX = x;
		touchDownY = y;
		visualPressedTime = TimeUtils.millis() + (long)(visualPressedDuration * 1000);
		return true;
	}

	@Override
	public void touchDragged (Entity entity, float x, float y, int pointer) {
		if (pointer != pressedPointer || cancelled) return;
		pressed = isOver(entity, x, y);
		if (pressed && pointer == 0 && button != -1 && !Gdx.input.isButtonPressed(button)) pressed = false;
		if (!pressed) {
			// Once outside the tap square, don't use the tap square anymore.
			invalidateTapSquare();
		}
	}

	@Override
	public void touchUp (Entity entity, float x, float y, int pointer, int button) {
		if (pointer == pressedPointer) {
			if (!cancelled) {
				boolean touchUpOver = isOver(entity, x, y);
				// Ignore touch up if the wrong mouse button.
				if (touchUpOver && pointer == 0 && this.button != -1 && button != this.button) touchUpOver = false;
				if (touchUpOver) {
					long time = TimeUtils.nanoTime();
					if (time - lastTapTime > tapCountInterval) tapCount = 0;
					tapCount++;
					lastTapTime = time;
					clicked(entity, x, y);
				}
			}
			pressed = false;
			pressedPointer = -1;
			pressedButton = -1;
			cancelled = false;
		}
	}

	public void enter (Entity entity, float x, float y, int pointer, Actor fromActor) {
		if (pointer == -1 && !cancelled) over = true;
	}

	public void exit (Entity entity, float x, float y, int pointer, Actor toActor) {
		if (pointer == -1 && !cancelled) over = false;
	}

	/** If a touch down is being monitored, the drag and touch up events are ignored until the next touch up. */
	public void cancel () {
		if (pressedPointer == -1) return;
		cancelled = true;
		pressed = false;
	}

	public void clicked (Entity entity, float x, float y) {
	}

	/** Returns true if the specified position is over the specified actor or within the tap square. */
	public boolean isOver (Entity entity, float x, float y) {
		//TODO this part
//		Actor hit = actor.hit(x, y, true);
//		if (hit == null || !hit.isDescendantOf(actor)) return inTapSquare(x, y);
		return true;
	}

	public boolean inTapSquare (float x, float y) {
		if (touchDownX == -1 && touchDownY == -1) return false;
		return Math.abs(x - touchDownX) < tapSquareSize && Math.abs(y - touchDownY) < tapSquareSize;
	}

	/** Returns true if a touch is within the tap square. */
	public boolean inTapSquare () {
		return touchDownX != -1;
	}

	/** The tap square will not longer be used for the current touch. */
	public void invalidateTapSquare () {
		touchDownX = -1;
		touchDownY = -1;
	}

	/** Returns true if a touch is over the actor or within the tap square. */
	public boolean isPressed () {
		return pressed;
	}

	/** Returns true if a touch is over the actor or within the tap square or has been very recently. This allows the UI to show a
	 * press and release that was so fast it occurred within a single frame. */
	public boolean isVisualPressed () {
		if (pressed) return true;
		if (visualPressedTime <= 0) return false;
		if (visualPressedTime > TimeUtils.millis()) return true;
		visualPressedTime = 0;
		return false;
	}

	/** Returns true if the mouse or touch is over the actor or pressed and within the tap square. */
	public boolean isOver () {
		return over || pressed;
	}

	public void setTapSquareSize (float halfTapSquareSize) {
		tapSquareSize = halfTapSquareSize;
	}

	public float getTapSquareSize () {
		return tapSquareSize;
	}

	/** @param tapCountInterval time in seconds that must pass for two touch down/up sequences to be detected as consecutive taps. */
	public void setTapCountInterval (float tapCountInterval) {
		this.tapCountInterval = (long)(tapCountInterval * 1000000000l);
	}

	/** Returns the number of taps within the tap count interval for the most recent click event. */
	public int getTapCount () {
		return tapCount;
	}

	public float getTouchDownX () {
		return touchDownX;
	}

	public float getTouchDownY () {
		return touchDownY;
	}

	/** The button that initially pressed this button or -1 if the button is not pressed. */
	public int getPressedButton () {
		return pressedButton;
	}

	/** The pointer that initially pressed this button or -1 if the button is not pressed. */
	public int getPressedPointer () {
		return pressedPointer;
	}

	/** @see #setButton(int) */
	public int getButton () {
		return button;
	}

	/** Sets the button to listen for, all other buttons are ignored. Default is {@link Buttons#LEFT}. Use -1 for any button. */
	public void setButton (int button) {
		this.button = button;
	}

	@Override
	public boolean keyDown(Entity entity, int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(Entity entity, int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(Entity entity, char character) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean mouseMoved(Entity entity, float screenX, float screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(Entity entity, int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}
