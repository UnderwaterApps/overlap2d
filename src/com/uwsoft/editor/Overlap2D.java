/*
 * ******************************************************************************
 *  * Copyright 2015 See AUTHORS file.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

package com.uwsoft.editor;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.kotcrab.vis.ui.VisUI;
import com.puremvc.patterns.proxy.Proxy;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.TextureManager;

public class Overlap2D extends ApplicationAdapter implements Proxy {
    private static final String TAG = Overlap2D.class.getCanonicalName();
    public static final String NAME = TAG;
    private static final String EVENT_PREFIX = "com.uwsoft.editor.Overlap2D";
    public static final String PAUSE = EVENT_PREFIX + ".PAUSE";
    public static final String RESUME = EVENT_PREFIX + ".RESUME";
    public static final String RENDER = EVENT_PREFIX + ".RENDER";
    public static final String RESIZE = EVENT_PREFIX + ".RESIZE";
    public static final String DISPOSE = EVENT_PREFIX + ".DISPOSE";
    public static final String CREATE = EVENT_PREFIX + ".CREATE_BTN_CLICKED";
    public static final String PROJECT_OPENED = EVENT_PREFIX + ".PROJECT_OPENED";
    //
    public TextureManager textureManager;
    private Overlap2DFacade facade;
    private Object data;

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

    @Override
    public String getProxyName() {
        return NAME;
    }

    @Override
    public Object getData() {
        return data;
    }

    @Override
    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public void onRegister() {

    }

    @Override
    public void onRemove() {

    }

    public void sendProjectOpenNotification() {
        sendNotification(PROJECT_OPENED);
    }
}