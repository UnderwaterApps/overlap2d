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

import com.badlogic.gdx.utils.Array;
import com.puremvc.patterns.mediator.SimpleMediator;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.Overlap2D;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.mvc.proxy.ProjectManager;
import com.uwsoft.editor.mvc.proxy.SceneDataManager;
import com.uwsoft.editor.mvc.view.ui.box.resourcespanel.draggable.list.LibraryItemThumbnailBox;
import com.uwsoft.editor.renderer.data.CompositeItemVO;

import java.util.Map;

/**
 * Created by azakhary on 4/17/2015.
 */
public class UILibraryItemsTabMediator extends SimpleMediator<UILibraryItemsTab> {

    private static final String TAG = UILibraryItemsTabMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    private Map<String, CompositeItemVO> items;

    public UILibraryItemsTabMediator() {
        super(NAME, new UILibraryItemsTab());
    }

    @Override
    public String[] listNotificationInterests() {
        return new String[]{
                SceneDataManager.SCENE_LOADED,
                ProjectManager.PROJECT_DATA_UPDATED,
                Overlap2D.LIBRARY_LIST_UPDATED
        };
    }

    @Override
    public void handleNotification(Notification notification) {
        switch (notification.getName()) {
            case SceneDataManager.SCENE_LOADED:
                initLibraryItems();
                break;
            case ProjectManager.PROJECT_DATA_UPDATED:
                initLibraryItems();
                break;
            case Overlap2D.LIBRARY_LIST_UPDATED:
                initLibraryItems();
            default:
                break;
        }
    }

    private void initLibraryItems() {
        items = Sandbox.getInstance().sceneControl.getCurrentSceneVO().libraryItems;
        Array<LibraryItemThumbnailBox> itemArray = new Array<>();
        for (String key : items.keySet()) {
            itemArray.add(new LibraryItemThumbnailBox(key, items.get(key)));
        }
        viewComponent.setItems(itemArray);
    }
}
