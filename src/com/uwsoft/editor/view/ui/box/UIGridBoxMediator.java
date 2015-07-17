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

import com.puremvc.patterns.mediator.SimpleMediator;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.Overlap2D;
import com.uwsoft.editor.view.stage.Sandbox;
import com.uwsoft.editor.proxy.ProjectManager;

/**
 * Created by azakhary on 4/15/2015.
 */
public class UIGridBoxMediator extends SimpleMediator<UIGridBox> {
    private static final String TAG = UIGridBoxMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    public UIGridBoxMediator() {
        super(NAME, new UIGridBox());
    }

    @Override
    public String[] listNotificationInterests() {
        return new String[]{
                ProjectManager.PROJECT_OPENED,
                Overlap2D.GRID_SIZE_CHANGED,
                UIGridBox.GRID_SIZE_TEXT_FIELD_UPDATED
        };
    }

    @Override
    public void handleNotification(Notification notification) {
        super.handleNotification(notification);
        Sandbox sandbox = Sandbox.getInstance();

        switch (notification.getName()) {
            case ProjectManager.PROJECT_OPENED:
                viewComponent.update();
                viewComponent.setGridSize(sandbox.getGridSize());
                break;
            case Overlap2D.GRID_SIZE_CHANGED:
                viewComponent.setGridSize(sandbox.getGridSize());
                break;
            case UIGridBox.GRID_SIZE_TEXT_FIELD_UPDATED:
                String body = notification.getBody();
                sandbox.setGridSize(Integer.parseInt(body));
                break;
            default:
                break;
        }
    }
}
