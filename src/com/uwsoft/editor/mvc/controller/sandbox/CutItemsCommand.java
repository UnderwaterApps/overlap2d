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

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.gdx.sandbox.ItemFactory;
import com.uwsoft.editor.mvc.controller.SandboxCommand;
import com.uwsoft.editor.renderer.actor.CompositeItem;
import com.uwsoft.editor.renderer.actor.IBaseItem;
import com.uwsoft.editor.renderer.data.CompositeItemVO;
import com.uwsoft.editor.renderer.data.CompositeVO;

import java.util.ArrayList;

/**
 * Created by azakhary on 4/28/2015.
 */
public class CutItemsCommand extends RevertableCommand {

    private String backup = "";

    private void backup() {
        CompositeVO tempHolder = new CompositeVO();
        ArrayList<IBaseItem> items = sandbox.getSelector().getSelectedItems();
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);

        for(int i = 0; i < items.size(); i++) {
            tempHolder.addItem(items.get(i).getDataVO());
        }
        backup = json.toJson(tempHolder);
    }

    @Override
    public void doAction() {
        backup();
        ArrayList<IBaseItem> items = sandbox.getSelector().getSelectedItems();
        sandbox.putItemsToClipboard(items);
        sandbox.getSelector().removeCurrentSelectedItems();
    }

    @Override
    public void undoAction() {
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        CompositeVO tempHolder = json.fromJson(CompositeVO.class, backup);

        CompositeItemVO fakeVO = new CompositeItemVO();

        fakeVO.composite = tempHolder;
        CompositeItem fakeItem = new CompositeItem(fakeVO, sandbox.sceneControl.getEssentials());
        ArrayList<IBaseItem> finalItems = new ArrayList<IBaseItem>();

        for (int i = 0; i < fakeItem.getItems().size(); i++) {
            IBaseItem itm = fakeItem.getItems().get(i);
            sandbox.sceneControl.getCurrentScene().addItem(itm);
            itm.updateDataVO();
            facade.sendNotification(ItemFactory.NEW_ITEM_ADDED, itm);
            finalItems.add(itm);
        }

        sandbox.getSelector().setSelections(finalItems, true);
    }
}
