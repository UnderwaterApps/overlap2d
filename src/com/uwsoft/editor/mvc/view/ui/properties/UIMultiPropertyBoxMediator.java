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

package com.uwsoft.editor.mvc.view.ui.properties;

import com.puremvc.patterns.mediator.SimpleMediator;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.Overlap2D;
import com.uwsoft.editor.renderer.actor.ImageItem;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by azakhary on 4/15/2015.
 */
public class UIMultiPropertyBoxMediator extends SimpleMediator<UIMultiPropertyBox> {

    private static final String TAG = UIMultiPropertyBoxMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    private HashMap<String, ArrayList<String>> classToMediatorMap;

    public UIMultiPropertyBoxMediator() {
        super(NAME, new UIMultiPropertyBox());

        initMap();
    }

    private void initMap() {
        classToMediatorMap = new HashMap<>();

        classToMediatorMap.put(ImageItem.class.getName(), new ArrayList<>());
        classToMediatorMap.get(ImageItem.class.getName()).add(UIBasicItemPropertiesMediator.NAME);
    }

    @Override
    public String[] listNotificationInterests() {
        return new String[]{
                Overlap2D.PROJECT_OPENED,
                Overlap2D.ITEM_DATA_UPDATE,
                Overlap2D.ITEM_SELECTED
        };
    }

    @Override
    public void handleNotification(Notification notification) {
        switch (notification.getName()) {
            case Overlap2D.PROJECT_OPENED:

                break;
            case Overlap2D.ITEM_SELECTED:
                initAllPropertyBoxes(notification.getBody());
                break;
            case Overlap2D.ITEM_DATA_UPDATE:

                break;
            default:
                break;
        }
    }

    private void initAllPropertyBoxes(Object observable) {
        ArrayList<String> mediatorNames = classToMediatorMap.get(observable.getClass().getName());

        viewComponent.clearBoxes();

        for (String mediatorName : mediatorNames) {
            UIAbstractPropertiesMediator propertyBoxMediator = facade.retrieveMediator(mediatorName);
            viewComponent.addBox(propertyBoxMediator.getViewComponent());
        }
    } 
}
