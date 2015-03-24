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

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.gdx.extension.screen.event.ScreenEvent;
import com.gdx.extension.screen.event.ScreenEvent.ScreenAction;
import com.gdx.extension.screen.event.ScreenFuture;
import com.gdx.extension.screen.event.ScreenListener;

/**
 * A screen manager allow to handle severals screens at a time.<br />
 * It's not only for rendering, you can use screens as AppStates.
 */
public class ScreenManager implements Disposable {

    protected Pool<ScreenEvent> eventPool;
    protected Array<ScreenEvent> screenEvents;
    protected Array<BaseScreen> screens;
    protected Array<BaseScreen> depthList;
    private boolean needUpdate;

    /**
     * Create a new screen manager.
     * 
     * @param batch the batch to use with this manager
     * @param skin the skin to use with this manager
     */
    public ScreenManager(Batch batch, Skin skin) {
	if (skin == null) {
	    throw new IllegalArgumentException("Skin may not be null.");
	}

	eventPool = Pools.get(ScreenEvent.class, 20);
	screenEvents = new Array<ScreenEvent>();
	screens = new Array<BaseScreen>();
	depthList = new Array<BaseScreen>();

	MasterScreen.mainBatch = batch;
	MasterScreen.skin = skin;
    }

    /**
     * Render all screens.
     * 
     * @param delta time from last frame
     */
    public void render(float delta) {
	screenEvents.reverse();
	while (screenEvents.size > 0) {
	    ScreenEvent _event = screenEvents.pop();
	    Class<? extends BaseScreen> _screenType = _event.getScreen();
	    BaseScreen _screen = getScreen(_screenType);
	    switch (_event.getAction()) {
		case ADD:
		    try {
			_screen = (BaseScreen) ClassReflection.getConstructor(_screenType, ScreenManager.class).newInstance(this);
		    } catch (ReflectionException e) {
			e.printStackTrace();
			continue;
		    }
		    screens.add(_screen);
		    _event.getFuture().done(_screen);
		    _screen.create();

		    for (ScreenListener _listener : _screen.listeners) {
			_listener.onCreate();
		    }

		    break;

		case REMOVE:
		    screens.removeValue(_screen, true);
		    _screen.dispose();

		    for (ScreenListener _listener : _screen.listeners) {
			_listener.onRemove();
		    }

		    break;

		case SHOW:
		    _screen.show();

		    for (ScreenListener _listener : _screen.listeners) {
			_listener.onShow();
		    }

		    break;

		case HIDE:
		    _screen.hide();

		    for (ScreenListener _listener : _screen.listeners) {
			_listener.onHide();
		    }

		    break;

		default:
		    break;
	    }
	    eventPool.free(_event);
	}

	// Update depth
	if (needUpdate) {
	    depthList.clear();
	    for (int i = 0; i < screens.size; i++) {
		BaseScreen _screen = screens.get(i);
		if (_screen.isActive) {
		    depthList.add(_screen);
		}
	    }
	    depthList.sort();

	    Stack _stageStack = MasterScreen.stageStack;
	    _stageStack.clear();
	    for (int i = 0; i < depthList.size; i++) {
		BaseScreen _screen = depthList.get(i);
		_stageStack.add(_screen.layout);
	    }
	}

	for (int i = 0; i < depthList.size; i++) {
	    BaseScreen _screen = depthList.get(i);
	    _screen.render(delta);
	}

	MasterScreen.masterRender(delta);
    }

    /**
     * Resize all screens.
     * 
     * @param width the new width
     * @param height the new height
     */
    public void resize(int width, int height) {
	MasterScreen.masterResize(width, height);
	for (int i = 0; i < screens.size; i++) {
	    BaseScreen _screen = screens.get(i);
	    _screen.resize(width, height);
	}
    }

    /**
     * Pause all screens.
     */
    public void pause() {
	for (int i = 0; i < depthList.size; i++) {
	    BaseScreen _screen = depthList.get(i);
	    _screen.pause();
	}
    }

    /**
     * Resume all screens.
     */
    public void resume() {
	for (int i = 0; i < depthList.size; i++) {
	    BaseScreen _screen = depthList.get(i);
	    _screen.resume();
	}
    }

    /**
     * Dispose all screens.
     */
    public void dispose() {
	for (int i = 0; i < screens.size; i++) {
	    BaseScreen _screen = screens.get(i);
	    _screen.dispose();
	}

	MasterScreen.masterDispose();
    }

    /**
     * Add the screen to the manager and show it.
     * 
     * @param screen screen the screen to add
     * 
     * @return the added screen
     */
    public <T extends BaseScreen> ScreenFuture<T> addScreen(Class<T> screen) {
	return addScreen(screen, true);
    }

    /**
     * Add the screen to the manager.
     * 
     * @param screen the screen to add
     * @param show true if you you wan't to show it now or false for manual showing
     * 
     * @return the added screen
     */
    @SuppressWarnings("unchecked")
    public <T extends BaseScreen> ScreenFuture<T> addScreen(Class<T> screen, boolean show) {
	T _screen = getScreen(screen);
	if (_screen != null) {
	    return null;
	}

	ScreenFuture<T> _future = new ScreenFuture<T>();

	ScreenEvent _addEvent = eventPool.obtain();
	_addEvent.setAction(ScreenAction.ADD);
	_addEvent.setScreen(screen);
	_addEvent.setFuture((ScreenFuture<BaseScreen>) _future);
	screenEvents.add(_addEvent);

	if (show) {
	    ScreenEvent _showEvent = eventPool.obtain();
	    _showEvent.setAction(ScreenAction.SHOW);
	    _showEvent.setScreen(screen);
	    screenEvents.add(_showEvent);
	}

	return _future;
    }

    public <T extends BaseScreen> void showScreen(Class<T> screen) {
	ScreenEvent _event = eventPool.obtain();
	_event.setAction(ScreenAction.SHOW);
	_event.setScreen(screen);
	screenEvents.add(_event);
    }

    /**
     * Remove a screen from the manager and the stage and dispose it.
     * 
     * @param screen the screen type to remove
     * @return the removed screen or null if the screen was not in the manager
     */
    public <T extends BaseScreen> T removeScreen(Class<T> screen) {
	T _screen = getScreen(screen);
	if (_screen == null) {
	    return null;
	}

	ScreenEvent _removeEvent = eventPool.obtain();
	_removeEvent.setAction(ScreenAction.REMOVE);
	_removeEvent.setScreen(screen);
	screenEvents.add(_removeEvent);

	return _screen;
    }

    public <T extends BaseScreen> void hideScreen(Class<T> screen) {
	ScreenEvent _event = eventPool.obtain();
	_event.setAction(ScreenAction.HIDE);
	_event.setScreen(screen);
	screenEvents.add(_event);
    }

    /**
     * Get a screen from the manager.
     * 
     * @param screen the screen type to get.
     * @return the first screen of this type or null if no one found.
     */
    @SuppressWarnings("unchecked")
    public <T extends BaseScreen> T getScreen(Class<T> screen) {
	for (int i = 0; i < screens.size; i++) {
	    BaseScreen _screen = screens.get(i);
	    if (ClassReflection.isInstance(screen, _screen)) {
		return (T) _screen;
	    }
	}

	return null;
    }

    /**
     * Called internally when add, remove or modify screens. Call it only if you know what you do.
     */
    public void updateScreens() {
	needUpdate = true;
    }

    /**
     * @return the screens currently rendering
     */
    public Array<BaseScreen> getActiveScreen() {
	return depthList;
    }
}