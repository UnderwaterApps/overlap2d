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

import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.mvc.controller.SandboxCommand;

/**
 * Created by azakhary on 4/28/2015.
 */
public class PasteItemsCommand extends RevertableCommand {

    private String clipboardContents;
    private ArrayList<IBaseItem> finalItems;

    @Override
    public void execute(Notification notification) {
        clipboardContents = new String(sandbox.fakeClipboard);
        super.execute(notification);
    }

    @Override
    public void doAction() {
        Vector2 pastePlace = getNotification().getBody();

        try {
            CompositeVO tempHolder;
            Json json = new Json();
            json.setOutputType(JsonWriter.OutputType.json);
            tempHolder = json.fromJson(CompositeVO.class, clipboardContents);

            if (tempHolder == null) return;

            CompositeItemVO fakeVO = new CompositeItemVO();

            fakeVO.composite = tempHolder;
            CompositeItem fakeItem = new CompositeItem(fakeVO, sandbox.sceneControl.getEssentials());

            finalItems = new ArrayList<>();
            Actor firstItem = (Actor) fakeItem.getItems().get(0);
            float offsetX = firstItem.getX() * sandbox.sceneControl.getCurrentScene().mulX;
            float offsetY = firstItem.getY() * sandbox.sceneControl.getCurrentScene().mulY;
            for (int i = 1; i < fakeItem.getItems().size(); i++) {
                Actor item = (Actor) fakeItem.getItems().get(i);
                if (item.getX() * sandbox.sceneControl.getCurrentScene().mulX < offsetX) {
                    offsetX = item.getX() * sandbox.sceneControl.getCurrentScene().mulX;
                }
                if (item.getY() * sandbox.sceneControl.getCurrentScene().mulY < offsetY) {
                    offsetY = item.getY() * sandbox.sceneControl.getCurrentScene().mulY;
                }
            }

            Vector3 cameraPos = ((OrthographicCamera) sandbox.getSandboxStage().getCamera()).position;

            for (int i = 0; i < fakeItem.getItems().size(); i++) {
                IBaseItem itm = fakeItem.getItems().get(i);
                itm.getDataVO().layerName = sandbox.getUIStage().getCurrentSelectedLayer().layerName;
                sandbox.sceneControl.getCurrentScene().addItem(itm);
                if(pastePlace == null) {
                    ((Actor) itm).setX(((Actor) itm).getX() - offsetX + (cameraPos.x + sandbox.copedItemCameraOffset.x));
                    ((Actor) itm).setY(((Actor) itm).getY() - offsetY + (cameraPos.y + sandbox.copedItemCameraOffset.y));
                } else {
                    ((Actor) itm).setX(pastePlace.x + ((Actor) itm).getX() - offsetX);
                    ((Actor) itm).setY(pastePlace.y + ((Actor) itm).getY() - offsetY);
                }
                itm.updateDataVO();
                facade.sendNotification(ItemFactory.NEW_ITEM_ADDED, itm);
                finalItems.add(itm);
            }

            sandbox.getSelector().setSelections(finalItems, true);
        } catch (Exception e) {

        }
    }

    @Override
    public void undoAction() {
        sandbox.getSelector().removeCurrentSelectedItems();
    }
}
