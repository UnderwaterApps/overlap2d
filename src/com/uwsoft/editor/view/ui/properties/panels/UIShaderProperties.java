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

package com.uwsoft.editor.view.ui.properties.panels;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.event.SelectBoxChangeListener;
import com.uwsoft.editor.view.ui.properties.UIRemovableProperties;

import java.util.HashMap;

/**
 * Created by azakhary on 8/12/2015.
 */
public class UIShaderProperties extends UIRemovableProperties {

    public static final String prefix = "com.uwsoft.editor.view.ui.properties.panels.UIShaderProperties";
    public static final String CLOSE_CLICKED = prefix + ".CLOSE_CLICKED";

    private VisSelectBox<String> shadersSelector;

    public UIShaderProperties() {
        super("Custom Shader Component");
    }

    public void initView(HashMap<String, ShaderProgram> shaders) {
        mainTable.clear();

        shadersSelector = new VisSelectBox<>();
        Array<String> shaderNames = new Array<>();
        shaderNames.add("Default");
        shaders.keySet().forEach(shaderNames::add);

        shadersSelector.setItems(shaderNames);

        shadersSelector.addListener(new SelectBoxChangeListener(getUpdateEventName()));

        mainTable.add(new VisLabel("Shader: ", Align.right)).padRight(5).width(75).right();
        mainTable.add(shadersSelector).width(100).left();
    }

    @Override
    public void onClose() {
        Overlap2DFacade.getInstance().sendNotification(CLOSE_CLICKED);
    }

    public void setSelected(String currShaderName) {
        shadersSelector.setSelected(currShaderName);
    }

    public String getShader() {
        return shadersSelector.getSelected();
    }
}
