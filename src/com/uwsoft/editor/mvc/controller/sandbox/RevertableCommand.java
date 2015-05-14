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
import com.uwsoft.editor.mvc.proxy.CommandManager;

/**
 * Created by azakhary on 5/14/2015.
 */
public abstract class RevertableCommand extends SandboxCommand {

    private CommandManager commandManager;
    private Notification notification;

    @Override
    public void execute(Notification notification) {
        commandManager = facade.retrieveProxy(CommandManager.NAME);
        this.notification = notification;
        doAction();
        commandManager.addCommand(this);
    }

    public abstract void doAction();
    public abstract void undoAction();

    public Notification getNotification() {
        return notification;
    }
}
