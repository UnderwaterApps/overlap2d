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

package com.uwsoft.editor.view.ui.box;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.puremvc.patterns.mediator.SimpleMediator;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.proxy.ProjectManager;

/**
 * Created by azakhary on 6/17/2015.
 */
public class PanelMediator<T extends Actor> extends SimpleMediator<T> {
    /**
     * Constructor.
     *
     * @param mediatorName
     * @param viewComponent
     */
    public PanelMediator(String mediatorName, T viewComponent) {
        super(mediatorName, viewComponent);
    }


    @Override
    public String[] listNotificationInterests() {
        return new String[]{
                ProjectManager.PROJECT_OPENED
        };
    }

    @Override
    public void handleNotification(Notification notification) {
        super.handleNotification(notification);
        switch (notification.getName()) {
            case ProjectManager.PROJECT_OPENED:
                viewComponent.setVisible(true);
                break;
        }
    }
}
