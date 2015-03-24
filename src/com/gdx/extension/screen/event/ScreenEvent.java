package com.gdx.extension.screen.event;

import com.badlogic.gdx.utils.Pool.Poolable;
import com.gdx.extension.screen.BaseScreen;

public class ScreenEvent implements Poolable {

    private ScreenAction action;
    private Class<? extends BaseScreen> screen;
    private ScreenFuture<BaseScreen> future;

    public enum ScreenAction {
	ADD, REMOVE, SHOW, HIDE;
    }

    public ScreenEvent() {
    }

    public ScreenAction getAction() {
	return action;
    }

    public void setAction(ScreenAction action) {
	this.action = action;
    }

    public Class<? extends BaseScreen> getScreen() {
	return screen;
    }

    public <T extends BaseScreen> void setScreen(Class<T> screen) {
	this.screen = screen;
    }

    public ScreenFuture<BaseScreen> getFuture() {
	return future;
    }

    public void setFuture(ScreenFuture<BaseScreen> future) {
	this.future = future;
    }

    @Override
    public void reset() {
	action = null;
	screen = null;
	future = null;
    }
}