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
import com.badlogic.gdx.utils.Align;
import com.commons.MsgAPI;
import com.kotcrab.vis.ui.VisUI;
import com.puremvc.patterns.proxy.Proxy;
import com.uwsoft.editor.proxy.EditorTextureManager;

public class Overlap2D extends ApplicationAdapter implements Proxy {
    private static final String TAG = Overlap2D.class.getCanonicalName();
    public static final String NAME = TAG;

    //
    public EditorTextureManager textureManager;
    private Overlap2DFacade facade;
    private Object data;

    public Overlap2D() {
    }

    public void create() {
        VisUI.load(Gdx.files.internal("style/uiskin.json"));
        VisUI.setDefaultTitleAlign(Align.center);
        facade = Overlap2DFacade.getInstance();
        facade.startup(this);
        sendNotification(MsgAPI.CREATE);
    }


    public void pause() {
        sendNotification(MsgAPI.PAUSE);
    }

    public void resume() {
        sendNotification(MsgAPI.RESUME);
    }

    public void render() {
        sendNotification(MsgAPI.RENDER, Gdx.graphics.getDeltaTime());
    }

    public void resize(int width, int height) {
        sendNotification(MsgAPI.RESIZE, new int[]{width, height});
    }

    public void dispose() {
        sendNotification(MsgAPI.DISPOSE);
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
}