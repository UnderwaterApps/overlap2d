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

package com.uwsoft.editor.mvc.controller;

import com.puremvc.patterns.command.SimpleCommand;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.controller.sandbox.*;

/**
 * Created by azakhary on 4/28/2015.
 */
public class BootstrapCommand extends SimpleCommand {

    public void execute(Notification notification) {
        super.execute(notification);
        facade = Overlap2DFacade.getInstance();
        facade.registerCommand(Sandbox.ACTION_CUT, CutItemsCommand.class);
        facade.registerCommand(Sandbox.ACTION_COPY, CopyItemsCommand.class);
        facade.registerCommand(Sandbox.ACTION_PASTE, PasteItemsCommand.class);
        facade.registerCommand(Sandbox.ACTION_DELETE, DeleteItemsCommand.class);
        facade.registerCommand(Sandbox.ACTION_EDIT_COMPOSITE, EditCompositeCommand.class);
        facade.registerCommand(Sandbox.ACTION_UPDATE_ITEM_DATA, UpdateEntityComponentsCommand.class);
        facade.registerCommand(Sandbox.ACTION_COMPOSITE_HIERARCHY_UP, CompositeHierarchyUpCommand.class);
        facade.registerCommand(Sandbox.ACTION_ADD_TO_LIBRARY, AddToLibraryCommand.class);
        facade.registerCommand(Sandbox.ACTION_CONVERT_TO_BUTTON, ConvertToButtonCommand.class);
        facade.registerCommand(Sandbox.ACTION_GROUP_ITEMS, GroupItemsCommand.class);
        facade.registerCommand(Sandbox.ACTION_SET_GRID_SIZE_FROM_ITEM, SetGridSizeFromItemCommand.class);

        facade.registerCommand(Sandbox.ACTION_SET_SELECTION, SetSelectionCommand.class);
        facade.registerCommand(Sandbox.ACTION_ADD_SELECTION, AddSelectionCommand.class);
        facade.registerCommand(Sandbox.ACTION_RELEASE_SELECTION, ReleaseSelectionCommand.class);
    }
}
