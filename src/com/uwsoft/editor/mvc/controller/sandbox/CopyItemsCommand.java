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
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.mvc.controller.SandboxCommand;

import java.util.Set;

/**
 * Created by azakhary on 4/28/2015.
 */
public class CopyItemsCommand extends SandboxCommand {

    @Override
    public void execute(Notification notification) {
        Set<Entity> items = sandbox.getSelector().getSelectedItems();
        if(items.size() > 0) {
            /*
            CompositeVO tempHolder = new CompositeVO();
            Json json = new Json();
            json.setOutputType(JsonWriter.OutputType.json);
            Actor actor = (Actor) items.get(0);
            Vector3 cameraPos = ((OrthographicCamera) getSandboxStage().getCamera()).position;
            Vector3 vector3 = new Vector3(actor.getX() - cameraPos.x, actor.getY() - cameraPos.y, 0);
            for (IBaseItem item : items) {
                tempHolder.addItem(item.getDataVO());
                actor = (Actor) item;
                if (actor.getX() - cameraPos.x < vector3.x) {
                    vector3.x = actor.getX() - cameraPos.x;
                }
                if (actor.getY() - cameraPos.y < vector3.y) {
                    vector3.y = actor.getY() - cameraPos.y;
                }
            }
            fakeClipboard = json.toJson(tempHolder);
            copedItemCameraOffset = vector3;*/
        }
    }
}
