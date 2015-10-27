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

package com.overlap2d.plugins.performance;

import com.badlogic.ashley.core.Engine;
import com.puremvc.patterns.mediator.SimpleMediator;
import com.puremvc.patterns.observer.Notification;

/**
 * Created by azakhary on 7/24/2015.
 */
public class PerformancePanelMediator extends SimpleMediator<PerformancePanel> {
    private static final String TAG = PerformancePanelMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    public static final String SCENE_LOADED = "com.uwsoft.editor.proxy.SceneDataManager.SCENE_LOADED";
    public static final String NEW_ITEM_ADDED = "com.uwsoft.editor.factory.ItemFactory.NEW_ITEM_ADDED";
    public static final String ACTION_DELETE = "com.uwsoft.editor.controller.commands.DeleteItemsCommandDONE";

    private PerformancePlugin performancePlugin;

    public PerformancePanelMediator(PerformancePlugin performancePlugin) {
        super(NAME, new PerformancePanel());
        this.performancePlugin = performancePlugin;

        viewComponent.initLockView();
    }

    @Override
    public String[] listNotificationInterests() {
        return new String[]{
                SCENE_LOADED,
                NEW_ITEM_ADDED,
                ACTION_DELETE,
                PerformancePlugin.PANEL_OPEN
        };
    }

    @Override
    public void handleNotification(Notification notification) {
        super.handleNotification(notification);
        switch (notification.getName()) {
            case SCENE_LOADED:
                viewComponent.initView();
                Engine engine = performancePlugin.getAPI().getEngine();
                viewComponent.setEngine(engine);
                break;
            case PerformancePlugin.PANEL_OPEN:
                viewComponent.show(performancePlugin.getAPI().getUIStage());
                break;
        }
    }
}
