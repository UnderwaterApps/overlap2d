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


import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.Tooltip;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisTable;
import com.uwsoft.editor.Overlap2DFacade;

public class UIToolBox extends VisTable {

    private static final String PREFIX = "com.uwsoft.editor.view.ui.box.UIToolBox.";

    public static final String TOOL_CLICKED = PREFIX + ".TOOL_CLICKED";
    private final ButtonGroup<VisImageButton> toolsButtonGroup;

    private HashMap<String, VisImageButton> buttonMap = new HashMap<>();

    public UIToolBox() {
        toolsButtonGroup = new ButtonGroup<>();
    }

    public void createToolButtons(Map<String, String> toolList) {
    	int index = 0;
    	for (Entry<String, String> entry : toolList.entrySet()) {
    		addToolButton(entry.getKey(), entry.getValue());
    		row();
    		if(index == 1)
    			addSeparator().width(31f);
    		index++;
    	}
    }

    private void addToolButton(String name, String toolTip) {
        VisImageButton button = createButton("tool-" + name, name, toolTip);
        buttonMap.put(name, button);
        add(button).width(31).height(31);
    }

    private VisImageButton createButton(String styleName, String toolId, String toolTip) {
        VisImageButton visImageButton = new VisImageButton(styleName);
        new Tooltip(visImageButton, toolTip);
        toolsButtonGroup.add(visImageButton);
        visImageButton.addListener(new ToolboxButtonClickListener(toolId));
        return visImageButton;
    }

    private class ToolboxButtonClickListener extends ClickListener {

        private final String toolId;

        public ToolboxButtonClickListener(String toolId) {
            this.toolId = toolId;
        }

        @Override
        public void clicked(InputEvent event, float x, float y) {
            super.clicked(event, x, y);
            Overlap2DFacade facade = Overlap2DFacade.getInstance();
            facade.sendNotification(TOOL_CLICKED, toolId);
        }
    }

    public void setCurrentTool(String tool) {
        buttonMap.get(tool).setChecked(true);
    }
}
