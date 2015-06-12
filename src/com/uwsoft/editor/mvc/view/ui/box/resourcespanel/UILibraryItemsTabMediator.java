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

package com.uwsoft.editor.mvc.view.ui.box.resourcespanel;

import java.util.HashMap;

import org.apache.commons.lang3.ArrayUtils;

import com.badlogic.gdx.utils.Array;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.Overlap2D;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.mvc.factory.ItemFactory;
import com.uwsoft.editor.mvc.view.ui.box.resourcespanel.draggable.DraggableResource;
import com.uwsoft.editor.mvc.view.ui.box.resourcespanel.draggable.list.LibraryItemResource;
import com.uwsoft.editor.renderer.legacy.data.CompositeItemVO;

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
        return ArrayUtils.add(listNotification, Overlap2D.LIBRARY_LIST_UPDATED);
    }

    @Override
    public void handleNotification(Notification notification) {
        super.handleNotification(notification);
        switch (notification.getName()) {
            case Overlap2D.LIBRARY_LIST_UPDATED:
                initList(viewComponent.searchString);

            default:
                break;
        }
    }

    @Override
    protected void initList(String searchText) {
        Sandbox sandbox = Sandbox.getInstance();
        HashMap<String, CompositeItemVO> items = Sandbox.getInstance().sceneControl.getCurrentSceneVO().libraryItems;
        Array<DraggableResource> itemArray = new Array<>();
        for (String key : items.keySet()) {
            if(!key.contains(searchText))continue;
            DraggableResource draggableResource = new DraggableResource(new LibraryItemResource(key));
            draggableResource.setFactoryFunction(ItemFactory.get()::createItemFromLibrary);
            draggableResource.initDragDrop();
            itemArray.add(draggableResource);
        }
        viewComponent.setItems(itemArray);
    }
}
