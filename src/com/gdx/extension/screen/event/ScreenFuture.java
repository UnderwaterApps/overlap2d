package com.gdx.extension.screen.event;

import com.badlogic.gdx.utils.Array;
import com.gdx.extension.screen.BaseScreen;

public class ScreenFuture<T extends BaseScreen> {

    private Array<FutureListener<T>> listeners;

    public ScreenFuture() {
	listeners = new Array<FutureListener<T>>(1);
    }

    public void done(T screen) {
	for (FutureListener<T> _listener : listeners) {
	    _listener.done(screen);
	}
    }

    public void addListener(FutureListener<T> listener) {
	listeners.add(listener);
    }
}