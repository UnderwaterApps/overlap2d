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

package com.uwsoft.editor.view;

import com.badlogic.ashley.core.Engine;
import com.commons.MsgAPI;
import com.puremvc.patterns.mediator.SimpleMediator;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.renderer.systems.render.Overlap2dRenderer;
import com.uwsoft.editor.view.stage.SandboxMediator;
import com.uwsoft.editor.view.ui.widget.actors.basic.SandboxBackUI;

/**
 * Created by sargis on 3/30/15.
 */
public class Overlap2DScreenMediator extends SimpleMediator<Overlap2DScreen> {
    private static final String TAG = Overlap2DScreenMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    public Overlap2DScreenMediator() {
        super(NAME, null);
    }

    @Override
    public String[] listNotificationInterests() {
        return new String[]{
                MsgAPI.CREATE,
                MsgAPI.PAUSE,
                MsgAPI.RESUME,
                MsgAPI.RENDER,
                MsgAPI.RESIZE,
                MsgAPI.DISPOSE,
                MsgAPI.SCENE_LOADED
        };
    }

    @Override
    public void handleNotification(Notification notification) {
        super.handleNotification(notification);
        switch (notification.getName()) {
            case MsgAPI.CREATE:
            	setViewComponent(new Overlap2DScreen());
            	//TODO this must be changed to Command 
            	facade = Overlap2DFacade.getInstance();
            	SandboxMediator sandboxMediator = facade.retrieveMediator(SandboxMediator.NAME);

                Engine engine = sandboxMediator.getViewComponent().getEngine();

            	getViewComponent().setEngine(engine);
                viewComponent.show();
                break;
            case MsgAPI.SCENE_LOADED:
                facade = Overlap2DFacade.getInstance();
                sandboxMediator = facade.retrieveMediator(SandboxMediator.NAME);
                engine = sandboxMediator.getViewComponent().getEngine();
                SandboxBackUI sandboxBackUI = new SandboxBackUI(engine.getSystem(Overlap2dRenderer.class).batch);
                getViewComponent().setBackUI(sandboxBackUI);
                getViewComponent().disableDrawingBgLogo();
                break;
            case MsgAPI.PAUSE:
                viewComponent.pause();
                break;
            case MsgAPI.RESUME:
                viewComponent.resume();
                break;
            case MsgAPI.RENDER:
                viewComponent.render(notification.getBody());
                break;
            case MsgAPI.RESIZE:
                int[] data = notification.getBody();
                viewComponent.resize(data[0], data[1]);
                break;
            case MsgAPI.DISPOSE:
                break;
        }
    }
}
