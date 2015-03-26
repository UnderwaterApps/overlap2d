package com.uwsoft.editor.gdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.kotcrab.vis.ui.VisUI;
import com.uwsoft.editor.data.manager.TextureManager;
import com.uwsoft.editor.gdx.screen.Overlap2DScreen;

public class Overlap2D extends ApplicationAdapter {

    public Screen currentScreen;

    public TextureManager textureManager;

    public Overlap2D() {

    }

    public void create() {
        VisUI.load();
        currentScreen = new Overlap2DScreen();
    }


    public void pause() {
        if (currentScreen == null) {
            return;
        }
        currentScreen.pause();
    }

    public void resume() {
        if (currentScreen == null) {
            return;
        }
        currentScreen.resume();
    }

    public void render() {
        if (currentScreen == null) {
            return;
        }
        currentScreen.render(Gdx.graphics.getDeltaTime());
    }

    public void resize(int width, int height) {
        if (currentScreen == null) {
            return;
        }
        currentScreen.resize(width, height);
    }

    public void dispose() {
        VisUI.dispose();
        if (currentScreen == null) {
            return;
        }
        currentScreen.dispose();
    }

}