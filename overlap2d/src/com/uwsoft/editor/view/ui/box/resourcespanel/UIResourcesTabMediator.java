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

package com.uwsoft.editor.view.ui.box.resourcespanel;

import com.puremvc.patterns.mediator.SimpleMediator;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.proxy.ProjectManager;

/**
 * Created by sargis on 5/12/15.
 */
public abstract class UIResourcesTabMediator<T extends UIResourcesTab> extends SimpleMediator<T> {
    /**
     * Constructor.
     *
     * @param mediatorName
     * @param viewComponent
     */
    public UIResourcesTabMediator(String mediatorName, T viewComponent) {
        super(mediatorName, viewComponent);
    }

    @Override
    public void onRegister() {
        super.onRegister();
        facade = Overlap2DFacade.getInstance();
    }

    @Override
    public String[] listNotificationInterests() {
        return new String[]{
                ProjectManager.PROJECT_OPENED,
                ProjectManager.PROJECT_DATA_UPDATED,
                UIResourcesTab.SEARCH
        };
    }


    @Override
    public void handleNotification(Notification notification) {
        switch (notification.getName()) {
            case ProjectManager.PROJECT_OPENED:
            case ProjectManager.PROJECT_DATA_UPDATED:
                initList(viewComponent.searchString);
                break;
            case UIResourcesTab.SEARCH:
                initList(viewComponent.searchString);
            default:
                break;
        }
    }

    protected abstract void initList(String searchText);

}
