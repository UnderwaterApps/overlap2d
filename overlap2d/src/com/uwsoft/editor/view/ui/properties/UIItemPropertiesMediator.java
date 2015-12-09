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

import com.badlogic.ashley.core.Entity;
import com.commons.MsgAPI;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.view.stage.Sandbox;

/**
 * Created by azakhary on 4/15/2015.
 */
public abstract class UIItemPropertiesMediator<T extends Entity, V extends UIAbstractProperties> extends UIAbstractPropertiesMediator<T, V> {

    public UIItemPropertiesMediator(String mediatorName, V viewComponent) {
        super(mediatorName, viewComponent);
    }

    @Override
    public void handleNotification(Notification notification) {

        if(notification.getName().equals(viewComponent.getUpdateEventName())) {
            if(!lockUpdates) {
                translateViewToItemData();
                afterItemDataModified();
            }
        }

        switch (notification.getName()) {
            case MsgAPI.ITEM_DATA_UPDATED:
                if(observableReference == null) return;
                onItemDataUpdate();
                break;
            default:
                break;
        }
    }

    protected void afterItemDataModified() {
    	//TODO this was needed with previous runtime don't know will it be needed for new runtime
    	//observableReference.renew();

        // TODO: add this back
        //Sandbox.getInstance().getSelector().updateSelections();
        Sandbox.getInstance().saveSceneCurrentSceneData();
    }
}
