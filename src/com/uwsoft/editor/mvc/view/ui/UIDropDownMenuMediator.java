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

package com.uwsoft.editor.mvc.view.ui;

import com.badlogic.gdx.utils.Array;
import com.puremvc.patterns.mediator.SimpleMediator;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.Overlap2D;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import java.util.HashMap;

/**
 * Created by azakhary on 4/20/2015.
 */
public class UIDropDownMenuMediator extends SimpleMediator<UIDropDownMenu> {
    private static final String TAG = UIDropDownMenuMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    public static final Integer SCENE_ACTIONS_SET = 0;
    public static final Integer ITEMS_ACTIONS_SET = 1;

    public HashMap<Integer, Array<String>> actionSets = new HashMap<>();

    public UIDropDownMenuMediator() {
        super(NAME, new UIDropDownMenu());
    }

    @Override
    public void onRegister() {
        super.onRegister();

        actionSets.put(SCENE_ACTIONS_SET, new Array<>());
        actionSets.get(SCENE_ACTIONS_SET).add(UIDropDownMenu.ACTION_PASTE);

        actionSets.put(ITEMS_ACTIONS_SET, new Array<>());
        actionSets.get(ITEMS_ACTIONS_SET).add(UIDropDownMenu.ACTION_CUT);
        actionSets.get(ITEMS_ACTIONS_SET).add(UIDropDownMenu.ACTION_COPY);
        actionSets.get(ITEMS_ACTIONS_SET).add(UIDropDownMenu.ACTION_PASTE);
        actionSets.get(ITEMS_ACTIONS_SET).add(UIDropDownMenu.ACTION_DELETE);
        actionSets.get(ITEMS_ACTIONS_SET).add(UIDropDownMenu.ACTION_ADD_TO_LIBRARY);
        actionSets.get(ITEMS_ACTIONS_SET).add(UIDropDownMenu.ACTION_GROUP_ITEMS);
        actionSets.get(ITEMS_ACTIONS_SET).add(UIDropDownMenu.ACTION_SET_GRID_SIZE_FROM_ITEM);
    }

    @Override
    public String[] listNotificationInterests() {
        return new String[]{
                Overlap2D.SCENE_RIGHT_CLICK,
                Overlap2D.ITEM_RIGHT_CLICK,
                UIDropDownMenu.ITEM_CLICKED
        };
    }

    @Override
    public void handleNotification(Notification notification) {
        super.handleNotification(notification);
        Sandbox sandbox = Sandbox.getInstance();
        switch (notification.getName()) {
            case Overlap2D.SCENE_RIGHT_CLICK:
                sandbox.getUIStage().addActor(viewComponent);
                break;
            case Overlap2D.ITEM_RIGHT_CLICK:
                viewComponent.setActionList(actionSets.get(ITEMS_ACTIONS_SET));
                viewComponent.showList();
                viewComponent.setX(300);
                viewComponent.setY(300);
                break;
            case UIDropDownMenu.ITEM_CLICKED:
                break;
            default:
                break;
        }
    }
}
