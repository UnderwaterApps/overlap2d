package com.uwsoft.editor;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.kotcrab.vis.ui.VisUI;
import com.puremvc.patterns.observer.Notifier;
import com.uwsoft.editor.data.manager.TextureManager;
import com.uwsoft.editor.mvc.Overlap2DFacade;

public class Overlap2D extends ApplicationAdapter implements Notifier {
    private static final String EVENT_PREFIX = "com.uwsoft.editor.Overlap2D";
    public static final String PAUSE = EVENT_PREFIX + ".PAUSE";
    public static final String RESUME = EVENT_PREFIX + ".RESUME";
    public static final String RENDER = EVENT_PREFIX + ".RENDER";
    public static final String RESIZE = EVENT_PREFIX + ".RESIZE";
    public static final String DISPOSE = EVENT_PREFIX + ".DISPOSE";
    public static final String CREATE = EVENT_PREFIX + ".CREATE";
    //
    public TextureManager textureManager;
    private Overlap2DFacade facade;

    public Overlap2D() {

    }

    public void create() {
        VisUI.load();
        facade = Overlap2DFacade.getInstance();
        facade.startup(this);
        sendNotification(CREATE);
    }


    public void pause() {
        sendNotification(PAUSE);
    }

    public void resume() {
        sendNotification(RESUME);
    }

    public void render() {
        sendNotification(RENDER, Gdx.graphics.getDeltaTime());
    }

    public void resize(int width, int height) {
        sendNotification(RESIZE, new int[]{width, height});
    }

    public void dispose() {
        sendNotification(DISPOSE);
        VisUI.dispose();
    }

    @Override
    public void sendNotification(String notificationName, Object body, String type) {
        facade.sendNotification(notificationName, body, type);
    }

    @Override
    public void sendNotification(String notificationName, Object body) {
        facade.sendNotification(notificationName, body);
    }

    @Override
    public void sendNotification(String notificationName) {
        facade.sendNotification(notificationName);
    }
}