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

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.puremvc.patterns.mediator.SimpleMediator;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.Overlap2D;
import com.uwsoft.editor.controlles.flow.FlowActionEnum;
import com.uwsoft.editor.gdx.actors.SelectionRectangle;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.gdx.ui.SelectionActions;
import com.uwsoft.editor.renderer.actor.CompositeItem;
import com.uwsoft.editor.renderer.actor.IBaseItem;
import com.uwsoft.editor.renderer.data.LayerItemVO;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by azakhary on 4/20/2015.
 */
public class UIDropDownMenuMediator extends SimpleMediator<UIDropDownMenu> {
    private static final String TAG = UIDropDownMenuMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    public static final Integer SCENE_ACTIONS_SET = 0;
    public static final Integer ITEMS_ACTIONS_SET = 1;

    private Sandbox sandbox;

    private Vector2 currentCoordinates;

    public HashMap<Integer, Array<String>> actionSets = new HashMap<>();

    public UIDropDownMenuMediator() {
        super(NAME, new UIDropDownMenu());
    }

    @Override
    public void onRegister() {
        super.onRegister();

        sandbox = Sandbox.getInstance();

        actionSets.put(SCENE_ACTIONS_SET, new Array<>());
        actionSets.get(SCENE_ACTIONS_SET).add(UIDropDownMenu.ACTION_PASTE);

        actionSets.put(ITEMS_ACTIONS_SET, new Array<>());
        actionSets.get(ITEMS_ACTIONS_SET).add(UIDropDownMenu.ACTION_CUT);
        actionSets.get(ITEMS_ACTIONS_SET).add(UIDropDownMenu.ACTION_COPY);
        actionSets.get(ITEMS_ACTIONS_SET).add(UIDropDownMenu.ACTION_PASTE);
        actionSets.get(ITEMS_ACTIONS_SET).add(UIDropDownMenu.ACTION_DELETE);
        actionSets.get(ITEMS_ACTIONS_SET).add(UIDropDownMenu.ACTION_ADD_TO_LIBRARY);
        actionSets.get(ITEMS_ACTIONS_SET).add(UIDropDownMenu.ACTION_GROUP_ITEMS);
        actionSets.get(ITEMS_ACTIONS_SET).add(UIDropDownMenu.ACTION_CONVERT_TO_BUTTON);
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

        switch (notification.getName()) {
            case Overlap2D.SCENE_RIGHT_CLICK:
                showPopup(SCENE_ACTIONS_SET, notification.getBody());
                break;
            case Overlap2D.ITEM_RIGHT_CLICK:
                Array<String> actionsSet = new Array<>(actionSets.get(ITEMS_ACTIONS_SET));
                if (sandbox.getSelector().getCurrentSelection().size() == 1) {
                    if(sandbox.getSelector().selectionIsComposite()) {
                        actionsSet.add(UIDropDownMenu.ACTION_ADD_TO_LIBRARY);
                        actionsSet.add(UIDropDownMenu.ACTION_EDIT_COMPOSITE);
                    }
                    actionsSet.add(UIDropDownMenu.ACTION_SET_GRID_SIZE_FROM_ITEM);
                }
                showPopup(actionsSet, notification.getBody());
                break;
            case UIDropDownMenu.ITEM_CLICKED:
                processUserAction(notification.getBody());
                break;
            default:
                break;
        }
    }

    private void showPopup(Integer actionsSet, Vector2 coordinates) {
       showPopup(actionSets.get(actionsSet), coordinates);
    }

    private void showPopup(Array<String> actionsSet, Vector2 coordinates) {
        sandbox.getUIStage().addActor(viewComponent);
        viewComponent.setActionList(actionsSet);
        viewComponent.setX(coordinates.x);
        viewComponent.setY(coordinates.y);

        currentCoordinates = new Vector2(coordinates);
    }

    private void processUserAction(String action) {
        switch (action) {
            case UIDropDownMenu.ACTION_CUT:
                sandbox.getUac().cutAction();
                sandbox.saveSceneCurrentSceneData();
                break;
            case UIDropDownMenu.ACTION_COPY:
                sandbox.getUac().copyAction();
                break;
            case UIDropDownMenu.ACTION_PASTE:
                sandbox.getUac().pasteAction(currentCoordinates.x, currentCoordinates.y, true);
                sandbox.saveSceneCurrentSceneData();
                break;
            case UIDropDownMenu.ACTION_DELETE:
                sandbox. getSelector().removeCurrentSelectedItems();
                break;
            case UIDropDownMenu.ACTION_ADD_TO_LIBRARY:
                sandbox.getItemFactory().addCompositeToLibrary();
                break;
            case UIDropDownMenu.ACTION_GROUP_ITEMS:
                sandbox.getItemFactory().groupItemsIntoComposite();
                sandbox.saveSceneCurrentSceneData();
                break;
            case UIDropDownMenu.ACTION_SET_GRID_SIZE_FROM_ITEM:
                sandbox.setGridSize((int) ((Actor) (sandbox.getSelector().getSelectedItems().get(0))).getWidth());
                break;
            case UIDropDownMenu.ACTION_EDIT_COMPOSITE:
                sandbox.enterIntoComposite();
                sandbox.flow.setPendingHistory(sandbox.getCurrentScene().getDataVO(), FlowActionEnum.GET_INTO_COMPOSITE);
                sandbox.flow.applyPendingAction();
                break;
            case UIDropDownMenu.ACTION_CONVERT_TO_BUTTON:
                // TODO: this should go to UAC
                CompositeItem btn = sandbox.getItemFactory().groupItemsIntoComposite();
                btn.getDataVO().composite.layers.add(new LayerItemVO("normal"));
                btn.getDataVO().composite.layers.add(new LayerItemVO("pressed"));
                btn.reAssembleLayers();

                sandbox.saveSceneCurrentSceneData();
                break;
            case UIDropDownMenu.ACTION_EDIT_PHYSICS:
                if (sandbox.getSelector().getCurrentSelection().size() == 1) {
                    for (SelectionRectangle value : sandbox.getSelector().getCurrentSelection().values()) {
                        IBaseItem item = value.getHost();
                        sandbox.getUIStage().editPhysics(item);
                        break;
                    }
                }
                break;
        }
    }
}
