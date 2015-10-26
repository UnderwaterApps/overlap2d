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

import com.commons.IItemCommand;

/**
 * Created by azakhary on 10/23/2015.
 */
public class PluginItemCommand extends EntityModifyRevertableCommand {

    private IItemCommand command;
    private Object body;

    @Override
    public void doAction() {
        if(command == null) {
            Object[] payload = notification.getBody();
            command = (IItemCommand) payload[0];
            body = payload[1];
        }

        command.doAction(body);
    }

    @Override
    public void undoAction() {
        command.undoAction(body);
    }

    public static Object build(IItemCommand command, Object body) {
        Object[] payload = new Object[2];
        payload[0] = command;
        payload[1] = body;
        return payload;
    }
}
