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

package com.uwsoft.editor.mvc.view.ui.box;

import com.puremvc.patterns.mediator.SimpleMediator;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.gdx.sandbox.EditingMode;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.view.ui.box.tools.TextToolSettings;
import com.uwsoft.editor.mvc.view.ui.box.tools.ToolSettings;

import java.util.HashMap;

/**
 * Created by sargis on 4/9/15.
 */
public class UIToolBoxMediator extends SimpleMediator<UIToolBox> {
    private static final String TAG = UIToolBoxMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    private static final String PREFIX =  "com.uwsoft.editor.mvc.view.ui.box.UIToolBoxMediator.";
    public static final String TOOL_SELECTED = PREFIX + ".TOOL_CHANGED";

    private int currentSelectedTool = UIToolBox.SELECTION_TOOL;

    private HashMap<Integer, ToolSettings> toolSettings = new HashMap<>();

    public UIToolBoxMediator() {
        super(NAME, new UIToolBox());
    }

    @Override
    public void onRegister() {
        toolSettings.put(UIToolBox.TEXT_TOOL, new TextToolSettings());

        facade = Overlap2DFacade.getInstance();
    }


    @Override
    public String[] listNotificationInterests() {
        return new String[]{
                UIToolBox.SELECTING_MODE_BTN_CLICKED,
                UIToolBox.TRANSFORMING_MODE_BTN_CLICKED,
                UIToolBox.TEXT_MODE_BTN_CLICKED
        };
    }

    @Override
    public void handleNotification(Notification notification) {
        super.handleNotification(notification);
        Sandbox sandbox = Sandbox.getInstance();
        switch (notification.getName()) {
            case UIToolBox.SELECTING_MODE_BTN_CLICKED:
                sandbox.setCurrentMode(EditingMode.SELECTION);
                onToolChanged(notification.getBody());
                break;
            case UIToolBox.TRANSFORMING_MODE_BTN_CLICKED:
                sandbox.setCurrentMode(EditingMode.TRANSFORM);
                onToolChanged(notification.getBody());
                break;
            case UIToolBox.TEXT_MODE_BTN_CLICKED:
                sandbox.setCurrentMode(EditingMode.TEXT);
                onToolChanged(notification.getBody());
                break;
        }
    }

    public void onToolChanged(int currentToolId) {
        currentSelectedTool = currentToolId;

        facade.sendNotification(TOOL_SELECTED, getCurrentSelectedToolSettings());
    }

    public ToolSettings getCurrentSelectedToolSettings() {
        ToolSettings currentToolSettings = toolSettings.get(currentSelectedTool);

        return currentToolSettings;
    }
}
