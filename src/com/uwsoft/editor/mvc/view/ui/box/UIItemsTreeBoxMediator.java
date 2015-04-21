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

package com.uwsoft.editor.mvc.view.ui.box;

import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.utils.Selection;
import com.badlogic.gdx.utils.Array;
import com.puremvc.patterns.mediator.SimpleMediator;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.Overlap2D;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.mvc.proxy.ProjectManager;
import com.uwsoft.editor.mvc.proxy.SceneDataManager;
import com.uwsoft.editor.renderer.actor.CompositeItem;
import com.uwsoft.editor.renderer.actor.IBaseItem;

/**
 * Created by sargis on 4/10/15.
 */
public class UIItemsTreeBoxMediator extends SimpleMediator<UIItemsTreeBox> {
    private static final String TAG = UIItemsTreeBoxMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    public UIItemsTreeBoxMediator() {
        super(NAME, new UIItemsTreeBox());
    }

    @Override
    public String[] listNotificationInterests() {
        return new String[]{
                SceneDataManager.SCENE_LOADED,
                UIItemsTreeBox.ITEMS_SELECTED
        };
    }

    @Override
    public void handleNotification(Notification notification) {
        super.handleNotification(notification);
        Sandbox sandbox = Sandbox.getInstance();
        switch (notification.getName()) {
            case SceneDataManager.SCENE_LOADED:
                CompositeItem rootScene = sandbox.getCurrentScene();
                viewComponent.init(rootScene);
                break;
            case UIItemsTreeBox.ITEMS_SELECTED:
                Selection<Tree.Node> selection = notification.getBody();
                Array<Tree.Node> nodes = selection.toArray();
                for (Tree.Node node : nodes) {
                    IBaseItem baseItem = (IBaseItem) node.getObject();
                    if (baseItem != null) {
                        addSelectionAction(baseItem);
                    }
                }

                break;
        }
    }

    private void addSelectionAction(IBaseItem iBaseItem) {
        Sandbox sandbox = Sandbox.getInstance();
        sandbox.getSelector().setSelection(iBaseItem, true);
    }
}
