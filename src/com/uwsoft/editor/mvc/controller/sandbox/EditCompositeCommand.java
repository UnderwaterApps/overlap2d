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
import com.uwsoft.editor.controlles.flow.FlowActionEnum;
import com.uwsoft.editor.mvc.controller.SandboxCommand;

/**
 * Created by azakhary on 4/28/2015.
 */
public class EditCompositeCommand extends SandboxCommand {

    @Override
    public void execute(Notification notification) {
    	//TODO fix and uncomment
//        sandbox.enterIntoComposite();
//        sandbox.flow.setPendingHistory(sandbox.getCurrentScene().getDataVO(), FlowActionEnum.GET_INTO_COMPOSITE);
//        sandbox.flow.applyPendingAction();
    }
}
