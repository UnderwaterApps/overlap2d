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

import java.util.HashMap;

import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.PopupMenu;
import com.uwsoft.editor.mvc.event.MenuItemListener;

/**
 * Created by azakhary on 4/20/2015.
 */
public class UIDropDownMenu extends PopupMenu {

    private static final String CLASS_NAME = "com.uwsoft.editor.mvc.view.ui.UIDropDownMenu";

    public static final String ACTION_GROUP_ITEMS = "ACTION_GROUP_ITEMS";
    public static final String ACTION_EDIT_COMPOSITE = "ACTION_EDIT_COMPOSITE";
    public static final String ACTION_CONVERT_TO_BUTTON = "ACTION_CONVERT_TO_BUTTON";
    public static final String ACTION_CUT = "ACTION_CUT";
    public static final String ACTION_COPY = "ACTION_COPY";
    public static final String ACTION_PASTE = "ACTION_PASTE";
    public static final String ACTION_DELETE = "ACTION_DELETE";
    public static final String ACTION_ADD_TO_LIBRARY = "ACTION_ADD_TO_LIBRARY";
    public static final String ACTION_EDIT_PHYSICS = "ACTION_EDIT_PHYSICS";
    public static final String ACTION_EDIT_ASSET_PHYSICS = "ACTION_EDIT_ASSET_PHYSICS";
    public static final String ACTION_SET_GRID_SIZE_FROM_ITEM = "ACTION_SET_GRID_SIZE_FROM_ITEM";

    public static final String ITEM_CLICKED = CLASS_NAME + ".ACTION_CLICKED";

    private Array<String> currentActionList = new Array<>();

    private HashMap<String, String> actionNames = new HashMap<>();

    public UIDropDownMenu() {
        actionNames.put(ACTION_GROUP_ITEMS, "Convert into composite");
        actionNames.put(ACTION_EDIT_COMPOSITE, "Edit composite");
        actionNames.put(ACTION_CONVERT_TO_BUTTON, "Convert to button");
        actionNames.put(ACTION_CUT, "Cut");
        actionNames.put(ACTION_COPY, "Copy");
        actionNames.put(ACTION_PASTE, "Paste");
        actionNames.put(ACTION_DELETE, "Delete");
        actionNames.put(ACTION_ADD_TO_LIBRARY, "Add to library");
        actionNames.put(ACTION_EDIT_PHYSICS, "Edit physics");
        actionNames.put(ACTION_EDIT_ASSET_PHYSICS, "Edit physics");
        actionNames.put(ACTION_SET_GRID_SIZE_FROM_ITEM, "Set grid size");

    }

    public void setActionList(Array<String> actions) {
        currentActionList.clear();
        currentActionList.addAll(actions);

        initView();
        setListeners();
    }

    private void setListeners() {
        clearListeners();
    }

    private void initView() {
        clear();

        for(int i = 0; i < currentActionList.size; i++) {
            String itemName = actionNames.get(currentActionList.get(i));
            MenuItem menuItem = new MenuItem(itemName, new MenuItemListener(ITEM_CLICKED, currentActionList.get(i)));
            addItem(menuItem);
        }

        addListener(new ClickListener() {

        });
    }
}
