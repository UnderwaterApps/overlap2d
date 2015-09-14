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

import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.controller.SandboxCommand;

/**
 * Created by azakhary on 4/28/2015.
 */
public class SetGridSizeFromItemCommand extends SandboxCommand {

    @Override
    public void execute(Notification notification) {
    	//TODO fix and uncomment
        //commands.setGridSize((int) ((Actor) (commands.getSelector().getSelectedItems().get(0))).getWidth());
    }
}
