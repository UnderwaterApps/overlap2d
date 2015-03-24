package com.gdx.extension.screen.event;

import com.gdx.extension.screen.BaseScreen;

public interface FutureListener<T extends BaseScreen> {

    public void done(T screen);
}