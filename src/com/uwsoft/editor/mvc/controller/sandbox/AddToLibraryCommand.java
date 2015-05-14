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

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.Overlap2D;
import com.uwsoft.editor.gdx.mediators.SceneControlMediator;

/**
 * Created by azakhary on 4/28/2015.
 */
public class AddToLibraryCommand extends RevertableCommand {

    private String createdLibraryItemName;

    private Entity item = null;

    @Override
    public void doAction() {
        Object[] payload = getNotification().getBody();

        if(item == null){
            item = ((Entity) payload[0]);
            createdLibraryItemName = (String) payload[1];
        }
        //TODO fix and uncomment
//        SceneControlMediator sceneControl = sandbox.getSceneControl();
//        sceneControl.getCurrentSceneVO().libraryItems.put(createdLibraryItemName, item.getDataVO());
//        facade.sendNotification(Overlap2D.LIBRARY_LIST_UPDATED);
    }

    @Override
    public void undoAction() {
        SceneControlMediator sceneControl = sandbox.getSceneControl();
        sceneControl.getCurrentSceneVO().libraryItems.remove(createdLibraryItemName);
        facade.sendNotification(Overlap2D.LIBRARY_LIST_UPDATED);
    }
}
