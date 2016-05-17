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

package com.uwsoft.editor.view.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.commons.MsgAPI;
import com.puremvc.patterns.mediator.SimpleMediator;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.proxy.PluginManager;
import com.uwsoft.editor.view.stage.Sandbox;
import com.uwsoft.editor.view.ui.box.UIResourcesBoxMediator;

import java.util.HashMap;

/**
 * Created by azakhary on 4/20/2015.
 */
public class UIDropDownMenuMediator extends SimpleMediator<UIDropDownMenu> {
    private static final String TAG = UIDropDownMenuMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    public static final Integer SCENE_ACTIONS_SET = 0;
    public static final Integer ITEMS_ACTIONS_SET = 1;
    public static final Integer LIBRARY_ITEM_ACTION_SET = 2;
    public static final Integer IMAGE_RESOURCE_ACTION_SET = 3;
    public static final Integer RULER_RESOURCE_ACTION_SET = 4;
    public static final Integer SPINE_ANIMATION_ACTION_SET = 5;
    public static final Integer SPRITE_ANIMATION_ACTION_SET = 7;
    public static final Integer SPRITER_ANIMATION_ACTION_SET = 8;
    public static final Integer PARTICLE_ACTION_SET = 9;

    private Sandbox sandbox;

    private Vector2 currentCoordinates;

    private Object currentObservable;

    public HashMap<Integer, Array<String>> actionSets = new HashMap<>();

    public UIDropDownMenuMediator() {
        super(NAME, new UIDropDownMenu());
    }

    @Override
    public void onRegister() {
        super.onRegister();

        sandbox = Sandbox.getInstance();

        actionSets.put(SCENE_ACTIONS_SET, new Array<>());
        actionSets.get(SCENE_ACTIONS_SET).add(MsgAPI.ACTION_PASTE);
        actionSets.get(SCENE_ACTIONS_SET).add(MsgAPI.ACTION_CREATE_PRIMITIVE);

        actionSets.put(IMAGE_RESOURCE_ACTION_SET, new Array<>());
        actionSets.get(IMAGE_RESOURCE_ACTION_SET).add(MsgAPI.ACTION_DELETE_IMAGE_RESOURCE);
        actionSets.put(LIBRARY_ITEM_ACTION_SET, new Array<>());
        actionSets.get(LIBRARY_ITEM_ACTION_SET).add(MsgAPI.ACTION_DELETE_LIBRARY_ITEM);

        actionSets.put(SPINE_ANIMATION_ACTION_SET, new Array<>());
        actionSets.get(SPINE_ANIMATION_ACTION_SET).add(MsgAPI.ACTION_DELETE_SPINE_ANIMATION_RESOURCE);

        actionSets.put(SPRITE_ANIMATION_ACTION_SET, new Array<>());
        actionSets.get(SPRITE_ANIMATION_ACTION_SET).add(MsgAPI.ACTION_DELETE_SPRITE_ANIMATION_RESOURCE);

        actionSets.put(SPRITER_ANIMATION_ACTION_SET, new Array<>());
        actionSets.get(SPRITER_ANIMATION_ACTION_SET).add(MsgAPI.ACTION_DELETE_SPRITER_ANIMATION_RESOURCE);

        actionSets.put(PARTICLE_ACTION_SET, new Array<>());
        actionSets.get(PARTICLE_ACTION_SET).add(MsgAPI.ACTION_DELETE_PARTICLE_EFFECT);

        actionSets.put(ITEMS_ACTIONS_SET, new Array<>());
        actionSets.get(ITEMS_ACTIONS_SET).add(MsgAPI.ACTION_CUT);
        actionSets.get(ITEMS_ACTIONS_SET).add(MsgAPI.ACTION_COPY);
        actionSets.get(ITEMS_ACTIONS_SET).add(MsgAPI.ACTION_PASTE);
        actionSets.get(ITEMS_ACTIONS_SET).add(MsgAPI.ACTION_DELETE);
        actionSets.get(ITEMS_ACTIONS_SET).add(MsgAPI.ACTION_GROUP_ITEMS);
        actionSets.get(ITEMS_ACTIONS_SET).add(MsgAPI.ACTION_CONVERT_TO_BUTTON);
        
        actionSets.put(RULER_RESOURCE_ACTION_SET, new Array<>());
        actionSets.get(RULER_RESOURCE_ACTION_SET).add(MsgAPI.ACTION_UPDATE_RULER_POSITION);

        facade = Overlap2DFacade.getInstance();
    }

    @Override
    public String[] listNotificationInterests() {
        return new String[]{
                MsgAPI.SCENE_RIGHT_CLICK,
                MsgAPI.ITEM_RIGHT_CLICK,
                UIDropDownMenu.ITEM_CLICKED,
                UIResourcesBoxMediator.IMAGE_RIGHT_CLICK,
                UIResourcesBoxMediator.SPINE_ANIMATION_RIGHT_CLICK,
                UIResourcesBoxMediator.SPRITE_ANIMATION_RIGHT_CLICK,
                UIResourcesBoxMediator.SPRITER_ANIMATION_RIGHT_CLICK,
                UIResourcesBoxMediator.LIBRARY_ITEM_RIGHT_CLICK,
                UIResourcesBoxMediator.PARTICLE_EFFECT_RIGHT_CLICK,
                RulersUI.RIGHT_CLICK_RULER,
        };
    }

    private void applyItemTypeMutators(Array<String> actionsSet) {
        // generic mutators
        if (sandbox.getSelector().getCurrentSelection().size() == 1) {
            if(sandbox.getSelector().selectionIsComposite()) {
                actionsSet.add(MsgAPI.SHOW_ADD_LIBRARY_DIALOG);
                actionsSet.add(MsgAPI.ACTION_CAMERA_CHANGE_COMPOSITE);
            }
            actionsSet.add(MsgAPI.ACTION_SET_GRID_SIZE_FROM_ITEM);
        }

        // external plugin mutators
        PluginManager pluginManager = facade.retrieveProxy(PluginManager.NAME);
        pluginManager.dropDownActionSets(sandbox.getSelector().getCurrentSelection(), actionsSet);
    }

    @Override
    public void handleNotification(Notification notification) {
        super.handleNotification(notification);

        Array<String> actionsSet;

        switch (notification.getName()) {
            case MsgAPI.SCENE_RIGHT_CLICK:
                Vector2 stageCoords = notification.getBody();
                actionsSet = new Array<>(actionSets.get(SCENE_ACTIONS_SET));
                applyItemTypeMutators(actionsSet);
                showPopup(actionsSet, stageCoords);
                break;
            case MsgAPI.ITEM_RIGHT_CLICK:
                actionsSet = new Array<>(actionSets.get(ITEMS_ACTIONS_SET));
                applyItemTypeMutators(actionsSet);
                showPopup(actionsSet, sandbox.getSelector().getSelectedItem());
                break;
            case UIResourcesBoxMediator.IMAGE_RIGHT_CLICK:
                showPopup(IMAGE_RESOURCE_ACTION_SET, notification.getBody());
                break;
            case UIResourcesBoxMediator.SPINE_ANIMATION_RIGHT_CLICK:
                showPopup(SPINE_ANIMATION_ACTION_SET, notification.getBody());
                break;
            case UIResourcesBoxMediator.SPRITE_ANIMATION_RIGHT_CLICK:
                showPopup(SPRITE_ANIMATION_ACTION_SET, notification.getBody());
                break;
            case UIResourcesBoxMediator.SPRITER_ANIMATION_RIGHT_CLICK:
                showPopup(SPRITER_ANIMATION_ACTION_SET, notification.getBody());
                break;
            case UIResourcesBoxMediator.LIBRARY_ITEM_RIGHT_CLICK:
                showPopup(LIBRARY_ITEM_ACTION_SET, notification.getBody());
                break;
            case UIResourcesBoxMediator.PARTICLE_EFFECT_RIGHT_CLICK:
                showPopup(PARTICLE_ACTION_SET, notification.getBody());
                break;
            case RulersUI.RIGHT_CLICK_RULER:
            	showPopup(RULER_RESOURCE_ACTION_SET, notification.getBody());
            	break;
            case UIDropDownMenu.ITEM_CLICKED:
                facade.sendNotification(notification.getBody(), currentObservable);
                break;
            default:
                break;
        }
    }

    private void showPopup(Integer actionsSet, Object observable) {
        showPopup(actionSets.get(actionsSet), observable);
    }

    private void showPopup(Array<String> actionsSet, Object observable) {
        Vector2 coordinates = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());

        sandbox.getUIStage().addActor(viewComponent);
        viewComponent.setActionList(actionsSet);
        viewComponent.setX(coordinates.x);
        viewComponent.setY(coordinates.y - viewComponent.getHeight());

        currentCoordinates = new Vector2(coordinates);

        currentObservable = observable;
    }

    public void setCurrentObservable(Object currentObservable) {
        this.currentObservable = currentObservable;
    }
}
