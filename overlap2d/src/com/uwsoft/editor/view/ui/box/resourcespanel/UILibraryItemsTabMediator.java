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

import com.badlogic.gdx.utils.Array;
import com.commons.MsgAPI;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.controller.commands.resource.DeleteLibraryItem;
import com.uwsoft.editor.factory.ItemFactory;
import com.uwsoft.editor.proxy.ProjectManager;
import com.uwsoft.editor.renderer.data.CompositeItemVO;
import com.uwsoft.editor.view.ui.box.resourcespanel.draggable.DraggableResource;
import com.uwsoft.editor.view.ui.box.resourcespanel.draggable.list.LibraryItemResource;
import org.apache.commons.lang3.ArrayUtils;

import java.util.HashMap;

/**
 * Created by azakhary on 4/17/2015.
 */
public class UILibraryItemsTabMediator extends UIResourcesTabMediator<UILibraryItemsTab> {

    private static final String TAG = UILibraryItemsTabMediator.class.getCanonicalName();
    public static final String NAME = TAG;


    public UILibraryItemsTabMediator() {
        super(NAME, new UILibraryItemsTab());
    }

    @Override
    public String[] listNotificationInterests() {
        String[] listNotification = super.listNotificationInterests();

        listNotification = ArrayUtils.add(listNotification, MsgAPI.LIBRARY_LIST_UPDATED);
        listNotification = ArrayUtils.add(listNotification, DeleteLibraryItem.DONE);

        return listNotification;
    }

    @Override
    public void handleNotification(Notification notification) {
        super.handleNotification(notification);
        switch (notification.getName()) {
            case MsgAPI.LIBRARY_LIST_UPDATED:
                initList(viewComponent.searchString);
                break;
            case DeleteLibraryItem.DONE:
                initList(viewComponent.searchString);
                break;
            default:
                break;
        }
    }

    @Override
    protected void initList(String searchText) {
        searchText = searchText.toLowerCase();
        ProjectManager projectManager = Overlap2DFacade.getInstance().retrieveProxy(ProjectManager.NAME);
        HashMap<String, CompositeItemVO> items = projectManager.currentProjectInfoVO.libraryItems;

        Array<DraggableResource> itemArray = new Array<>();
        for (String key : items.keySet()) {
            if(!key.toLowerCase().contains(searchText))continue;
            DraggableResource draggableResource = new DraggableResource(new LibraryItemResource(key));
            draggableResource.setFactoryFunction(ItemFactory.get()::createItemFromLibrary);
            draggableResource.initDragDrop();
            itemArray.add(draggableResource);
        }
        viewComponent.setItems(itemArray);
    }
}
