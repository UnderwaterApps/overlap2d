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

package com.uwsoft.editor.mvc.view.dialog;

import com.puremvc.patterns.mediator.SimpleMediator;
import com.uwsoft.editor.Overlap2D;

/**
 * Created by sargis on 4/1/15.
 */
public class NewProjectDialogMediator extends SimpleMediator<NewProjectDialog> {
    private static final String TAG = NewProjectDialogMediator.class.getCanonicalName();
    private static final String NAME = TAG;

    public NewProjectDialogMediator() {
        super(NAME, null);
    }

    @Override
    public String[] listNotificationInterests() {
        return new String[]{
                Overlap2D.CREATE,
                Overlap2D.PAUSE,
                Overlap2D.RESUME,
                Overlap2D.RENDER,
                Overlap2D.RESIZE,
                Overlap2D.DISPOSE,
        };
    }
}
