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

package com.uwsoft.editor.view.ui.box;

import com.badlogic.gdx.utils.Array;
import com.puremvc.patterns.mediator.SimpleMediator;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.view.stage.tools.*;

/**
 * Created by sargis on 4/9/15.
 */
public class UIToolBoxMediator extends SimpleMediator<UIToolBox> {
    private static final String TAG = UIToolBoxMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    private static final String PREFIX =  "com.uwsoft.editor.view.ui.box.UIToolBoxMediator.";
    public static final String TOOL_SELECTED = PREFIX + ".TOOL_CHANGED";


    private String currentTool;
    private Array<String> toolList;


    public UIToolBoxMediator() {
        super(NAME, new UIToolBox());
    }

    @Override
    public void onRegister() {
        facade = Overlap2DFacade.getInstance();

        toolList = getToolNameList();
        currentTool = SelectionTool.NAME;

        viewComponent.createToolButtons(toolList);
    }


    public Array<String> getToolNameList() {
        Array<String> toolNames = new Array();
        toolNames.add(SelectionTool.NAME);
        toolNames.add(TransformTool.NAME);
        toolNames.add(TextTool.NAME);
        toolNames.add(PointLightTool.NAME);
        toolNames.add(ConeLightTool.NAME);
        toolNames.add(PolygonTool.NAME);
        return toolNames;
    }

    @Override
    public String[] listNotificationInterests() {
        return new String[]{
                UIToolBox.TOOL_CLICKED
        };
    }

    @Override
    public void handleNotification(Notification notification) {
        super.handleNotification(notification);

        switch (notification.getName()) {
            case UIToolBox.TOOL_CLICKED:
                currentTool = notification.getBody();
                facade.sendNotification(TOOL_SELECTED, currentTool);
                break;
        }
    }

    public void setCurrentTool(String tool) {
        viewComponent.setCurrentTool(tool);
        currentTool = tool;
    }
}
