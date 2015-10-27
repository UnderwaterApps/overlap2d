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

package com.uwsoft.editor.view.ui.properties;

import com.commons.MsgAPI;
import com.puremvc.patterns.mediator.SimpleMediator;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.view.stage.Sandbox;

/**
 * Created by azakhary on 4/15/2015.
 */
public abstract class UIAbstractPropertiesMediator<T, V extends UIAbstractProperties> extends SimpleMediator<V> {
    private Sandbox sandbox;

    protected T observableReference;

    protected boolean lockUpdates = true;

    public UIAbstractPropertiesMediator(String mediatorName, V viewComponent) {
        super(mediatorName, viewComponent);

        sandbox = Sandbox.getInstance();
        facade = Overlap2DFacade.getInstance();
    }

    @Override
    public void onRegister() {
        facade = Overlap2DFacade.getInstance();
    }


    @Override
    public String[] listNotificationInterests() {
        return new String[]{
                MsgAPI.ITEM_DATA_UPDATED,
                viewComponent.getUpdateEventName()
        };
    }

    @Override
    public void handleNotification(Notification notification) {
        super.handleNotification(notification);


        if(notification.getName().equals(viewComponent.getUpdateEventName())) {
            if(!lockUpdates) {
                translateViewToItemData();
            }
        }

        switch (notification.getName()) {
            case MsgAPI.ITEM_DATA_UPDATED:
                onItemDataUpdate();
                break;
            default:
                break;
        }
    }

    public void setItem(T item) {
        observableReference = item;
        lockUpdates = true;
        translateObservableDataToView(observableReference);
        lockUpdates = false;
    }

    public void onItemDataUpdate() {
        lockUpdates = true;
        translateObservableDataToView(observableReference);
        lockUpdates = false;
    }

    protected abstract void translateObservableDataToView(T item);

    protected abstract void translateViewToItemData();
}
