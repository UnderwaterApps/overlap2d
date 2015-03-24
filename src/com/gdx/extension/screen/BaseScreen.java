/** Copyright 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gdx.extension.screen;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.gdx.extension.screen.event.ScreenListener;

/**
 * Create an empty screen with a layout table expanding the whole stage viewport.
 * Add your actors to the layout to automatically handle the screen add/remove/show/hide actions.
 */
public abstract class BaseScreen extends MasterScreen implements Comparable<BaseScreen> {

    /**
     * Internal screen layout
     */
    protected Table layout;

    /**
     * If true, rendered by the {@link ScreenManager}
     */
    protected boolean isActive;

    /**
     * Depth of the screen
     */
    protected int depth;

    /**
     * The {@link ScreenManager screen manager} of this screen
     */
    protected ScreenManager screenManager;

    /**
     * Listeners listening this screen
     */
    protected Array<ScreenListener> listeners;

    /**
     * <b>Don't override it.</b>
     * Used internally to create a new screen.<br />
     * 
     * @param screenManager the {@link ScreenManager} who's adding the {@link BaseScreen}
     * @param depth the depth you want screen to be rendered
     */
    public BaseScreen(ScreenManager screenManager) {
	layout = new Table();

	this.screenManager = screenManager;
	listeners = new Array<ScreenListener>(4);
    }

    @Override
    public void render(float delta) {
    }

    @Override
    public void resize(int width, int height) {
    }

    /**
     * Show the screen.
     */
    @Override
    public void show() {
	if (isActive) {
	    return;
	}

	setActive(true);
    }

    /**
     * Hide the screen.
     */
    @Override
    public void hide() {
	if (!isActive) {
	    return;
	}

	setActive(false);
    }

    /**
     * Called when the screen is paused (on android).
     */
    @Override
    public void pause() {
    }

    /**
     * Called when the screen is resumed (on android).
     */
    @Override
    public void resume() {
    }

    /**
     * Called when the screen need to be disposed.
     */
    @Override
    public void dispose() {
	hide();
    }

    /**
     * @return if screen is rendered
     */
    public boolean isActive() {
	return isActive;
    }

    /**
     * @param isActive rendered or not
     */
    private void setActive(boolean isActive) {
	if (this.isActive == isActive) {
	    return;
	}

	this.isActive = isActive;

	if (isActive) {
	    stageStack.addActor(layout);
	} else {
	    stageStack.removeActor(layout);
	}

	screenManager.updateScreens();
    }

    /**
     * Show a modal popup on top of all the screens children
     * 
     * @param title the title of the popup
     * @param message text you want to display in the popup
     */
    public void showPopup(String title, String message) {
	final Dialog _dialog = new Dialog(title, skin);
	Label _message = new Label(message, skin);
	_message.setAlignment(Align.center);

	TextButton _okButton = new TextButton("Ok", skin);
	_okButton.addListener(new ChangeListener() {

	    @Override
	    public void changed(ChangeEvent event, Actor actor) {
		_dialog.hide();
	    }

	});

	_dialog.getContentTable().add(_message).minWidth(300f).pad(10f);
	_dialog.getButtonTable().add(_okButton).minWidth(100f).pad(10f);
	_dialog.setTitleAlignment(Align.center);
	_dialog.setMovable(false);
	_dialog.center();
	_dialog.show(stage);
    }

    /**
     * Override to set the depth of the screen
     * 
     * @return
     */
    public abstract int getDepth();

    /**
     * Get the screen manager of this screen.
     * 
     * @return the screen manager
     */
    public ScreenManager getScreenManager() {
	return screenManager;
    }

    /**
     * Add a listener.
     * 
     * @param listener an implemented listener to add
     */
    public void addListener(ScreenListener listener) {
	listeners.add(listener);
    }

    /**
     * Remove a listener.
     * 
     * @param listener the listener to remove
     */
    public void removeListener(ScreenListener listener) {
	listeners.removeValue(listener, true);
    }

    /**
     * Get the listeners of this screen.
     * 
     * @return the list of listeners
     */
    public Array<ScreenListener> getListeners() {
	return listeners;
    }

    /**
     * Used internally to sort screen by depth.
     */
    @Override
    public int compareTo(BaseScreen c) {
	int value = 0;
	if (depth > c.depth)
	    value = 1;
	else if (depth < c.depth)
	    value = -1;
	else if (depth == c.depth)
	    value = 0;

	return value;
    }

}
