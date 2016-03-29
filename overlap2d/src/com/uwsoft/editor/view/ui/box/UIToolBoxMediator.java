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
import com.commons.MsgAPI;
import com.commons.view.tools.Tool;
import com.kotcrab.vis.ui.widget.Separator;
import com.kotcrab.vis.ui.widget.VisImageButton;
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

    private Array<String> toolList;
    private String currentTool;


    public UIToolBoxMediator() {
        super(NAME, new UIToolBox());
    }

    @Override
    public void onRegister() {
        facade = Overlap2DFacade.getInstance();

        toolList = new Array<String>();
        initToolNameList();
        currentTool = SelectionTool.NAME;

        viewComponent.createToolButtons(toolList);
    }

    private void initToolNameList() {
        toolList.add(SelectionTool.NAME);
        toolList.add(TransformTool.NAME);
        toolList.add(TextTool.NAME);
        toolList.add(PointLightTool.NAME);
        toolList.add(ConeLightTool.NAME);
        toolList.add(PolygonTool.NAME);
    }

    public void addTool(String toolName, VisImageButton.VisImageButtonStyle toolBtnStyle, boolean addSeparator, Tool tool) {
        toolList.add(toolName);
        if(addSeparator) {
            viewComponent.add(new Separator("menu")).padTop(2).padBottom(2).fill().expand().row();
        }
        viewComponent.addToolButton(toolName, toolBtnStyle);

        facade.sendNotification(toolName, tool);
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
                facade.sendNotification(MsgAPI.TOOL_SELECTED, currentTool);
                break;
        }
    }

    public void setCurrentTool(String tool) {
        viewComponent.setCurrentTool(tool);
        currentTool = tool;
    }
}
