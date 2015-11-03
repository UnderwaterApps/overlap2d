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

package com.uwsoft.editor.event;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.view.ui.widget.EditableSelectBox;

/**
 * Created by azakhary on 4/30/2015.
 */
public class EditableSelectBoxChangeListener extends ChangeListener {

    private final String eventName;

    private String lastSelected = "";

    public EditableSelectBoxChangeListener(String eventName) {
        this.eventName = eventName;
    }

    @Override
    public void changed(ChangeEvent changeEvent, Actor actor) {
        Overlap2DFacade facade = Overlap2DFacade.getInstance();
        String selected = ((EditableSelectBox) actor).getSelected();
        if (!lastSelected.equals(selected)) {
            lastSelected = selected;
            facade.sendNotification(eventName, selected);
        }
    }
}