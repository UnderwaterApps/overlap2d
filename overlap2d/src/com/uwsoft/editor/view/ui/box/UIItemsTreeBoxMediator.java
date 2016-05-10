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

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.utils.Selection;
import com.badlogic.gdx.utils.Array;
import com.commons.MsgAPI;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.controller.commands.AddSelectionCommand;
import com.uwsoft.editor.controller.commands.ReleaseSelectionCommand;
import com.uwsoft.editor.controller.commands.SetSelectionCommand;
import com.uwsoft.editor.renderer.data.LayerItemVO;
import com.uwsoft.editor.utils.runtime.EntityUtils;
import com.uwsoft.editor.view.stage.Sandbox;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Created by sargis on 4/10/15.
 */
public class UIItemsTreeBoxMediator extends PanelMediator<UIItemsTreeBox> {
    private static final String TAG = UIItemsTreeBoxMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    public UIItemsTreeBoxMediator() {
        super(NAME, new UIItemsTreeBox());
    }

    @Override
    public String[] listNotificationInterests() {
        String[] parentNotifications = super.listNotificationInterests();
        return Stream.of(parentNotifications, new String[]{
                MsgAPI.SCENE_LOADED,
                MsgAPI.NEW_ITEM_ADDED,
                UIItemsTreeBox.ITEMS_SELECTED,
                SetSelectionCommand.DONE,
                AddSelectionCommand.DONE,
                ReleaseSelectionCommand.DONE,
                MsgAPI.DELETE_ITEMS_COMMAND_DONE
        }).flatMap(Stream::of).toArray(String[]::new);
    }

    @Override
    public void handleNotification(Notification notification) {
        super.handleNotification(notification);
        Sandbox sandbox = Sandbox.getInstance();
        switch (notification.getName()) {
            case MsgAPI.SCENE_LOADED:
            	Entity rootEntity = sandbox.getRootEntity();
                viewComponent.init(rootEntity);
                break;
            case MsgAPI.NEW_ITEM_ADDED:
                rootEntity = sandbox.getRootEntity();
                viewComponent.init(rootEntity);
                break;
            case MsgAPI.DELETE_ITEMS_COMMAND_DONE:
                rootEntity = sandbox.getRootEntity();
                viewComponent.init(rootEntity);
                break;
            case UIItemsTreeBox.ITEMS_SELECTED:
                Selection<Tree.Node> selection = notification.getBody();
                Array<Tree.Node> nodes = selection.toArray();
                Set<Entity> items = new HashSet<>();

                for (Tree.Node node : nodes) {
                    Integer entityId = (Integer) node.getObject();
                    Entity item = EntityUtils.getByUniqueId(entityId);
                    //layer lock thing
                    LayerItemVO layerItemVO = EntityUtils.getEntityLayer(item);
                    if(layerItemVO != null && layerItemVO.isLocked) {
                        continue;
                    }
                    if (item != null) {
                        items.add(item);
                    }
                }

                sendSelectionNotification(items);

                break;
            case SetSelectionCommand.DONE:
                viewComponent.setSelection(sandbox.getSelector().getSelectedItems());

                break;
            case AddSelectionCommand.DONE:
                viewComponent.setSelection(sandbox.getSelector().getSelectedItems());

                break;
            case ReleaseSelectionCommand.DONE:
                viewComponent.setSelection(sandbox.getSelector().getSelectedItems());

                break;
        }
    }

    private void sendSelectionNotification(Set<Entity> items) {
        Set<Entity> ntfItems = (items.isEmpty())? null : items;
        Overlap2DFacade.getInstance().sendNotification(MsgAPI.ACTION_SET_SELECTION, ntfItems);
    }
}
