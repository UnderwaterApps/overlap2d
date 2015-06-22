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

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.uwsoft.editor.Overlap2DFacade;

/**
 * Created by azakhary on 4/15/2015.
 */
public class KeyboardListener implements EventListener {

    private final String eventName;

    private String lastValue;

    public KeyboardListener(String eventName) {
        this.eventName = eventName;
    }

    @Override
    public boolean handle(Event event) {
        if (event instanceof FocusListener.FocusEvent) {
            handleFocusListener((FocusListener.FocusEvent) event);
            return true;
        }
        if (event instanceof InputEvent) {
            handleInputListener((InputEvent) event);
            return true;
        }
        return false;
    }

    private void handleInputListener(InputEvent event) {
        switch (event.getType()) {
            case keyUp:
                if (event.getKeyCode() == Input.Keys.ENTER) {
                    keyboardHandler((VisTextField) event.getTarget());
                    VisTextField field = (VisTextField) event.getTarget();
                    lastValue = field.getText();
                }
                break;
        }
    }

    private void handleFocusListener(FocusListener.FocusEvent event) {
        VisTextField field = (VisTextField) event.getTarget();
        if(event.isFocused()) {
            //it was a focus in event, which is no change
            lastValue = field.getText();
            return;
        }
        switch (event.getType()) {
            case keyboard:
                keyboardHandler(field);
                break;
            case scroll:
                break;
        }

    }

    private void keyboardHandler(VisTextField target) {
        if(!target.isInputValid()) {
            return;
        }
        // check for change
        if(lastValue.equals(target.getText())) {
            // no change = no event;
            return;
        }

        Overlap2DFacade facade = Overlap2DFacade.getInstance();
        facade.sendNotification(eventName, target.getText());
    }
}