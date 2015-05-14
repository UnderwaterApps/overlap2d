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

package com.uwsoft.editor.mvc.controller.sandbox;

import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.renderer.actor.IBaseItem;

import java.util.ArrayList;

/**
 * Created by azakhary on 5/14/2015.
 */
public class SetSelectionCommand extends RevertableCommand {

    ArrayList<IBaseItem> previousSelection;

    @Override
    public void doAction() {
        previousSelection = Sandbox.getInstance().getSelector().getSelectedItems();

        ArrayList<IBaseItem> items = getNotification().getBody();
        Sandbox.getInstance().getSelector().setSelections(items, true);
    }

    @Override
    public void undoAction() {
        Sandbox.getInstance().getSelector().setSelections(previousSelection, true);
    }
}
