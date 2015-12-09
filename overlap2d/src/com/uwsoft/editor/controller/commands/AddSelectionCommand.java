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

package com.uwsoft.editor.controller.commands;

import java.util.Set;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.uwsoft.editor.view.stage.Sandbox;
import com.uwsoft.editor.utils.runtime.EntityUtils;

/**
 * Created by azakhary on 5/14/2015.
 */
public class AddSelectionCommand extends RevertableCommand {

    private static final String CLASS_NAME = "com.uwsoft.editor.controller.commands.AddSelectionCommand";
    public static final String DONE = CLASS_NAME + "DONE";

    private Array<Integer> entityIds;

    @Override
    public void doAction() {
        if(entityIds == null) {
            Set<Entity> items = getNotification().getBody();
            entityIds = EntityUtils.getEntityId(items);
        }

        Set<Entity> items = EntityUtils.getByUniqueId(entityIds);
        Sandbox.getInstance().getSelector().addSelections(items);
        facade.sendNotification(DONE);
    }

    @Override
    public void undoAction() {
        Set<Entity> items = EntityUtils.getByUniqueId(entityIds);
        Sandbox.getInstance().getSelector().releaseSelections(items);
        facade.sendNotification(DONE);
    }

}
