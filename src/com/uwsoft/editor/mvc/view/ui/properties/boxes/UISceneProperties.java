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

package com.uwsoft.editor.mvc.view.ui.properties.boxes;

import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.uwsoft.editor.mvc.event.CheckBoxChangeListener;
import com.uwsoft.editor.mvc.event.KeyboardListener;
import com.uwsoft.editor.mvc.view.ui.properties.UIAbstractProperties;

/**
 * Created by azakhary on 4/16/2015.
 */
public class UISceneProperties extends UIAbstractProperties {

    public static final String PHYSICS_ENABLED_CHECKBOX_CLICKED = "com.uwsoft.editor.mvc.view.ui.properties.depricated.UIProjectGeneralProperties" + ".PHYSICS_ENABLED_CHECKBOX_CLICKED";

    private VisCheckBox physicsEnabledCheckBox;
    private VisTextField gravityXTextField;
    private VisTextField gravityYTextField;
    private VisTextField sleepVelocityTextField;
    private VisCheckBox enableLightsCheckBox;
    private VisCheckBox diffuseCheckBox;

    public UISceneProperties() {
        super();

        pad(5);
        add(new VisLabel("Physics enabled:", Align.right)).padRight(5).width(115);
        physicsEnabledCheckBox = new VisCheckBox(null);
        physicsEnabledCheckBox.addListener(new CheckBoxChangeListener(PHYSICS_ENABLED_CHECKBOX_CLICKED));
        add(physicsEnabledCheckBox).left();
        row().padTop(5);
        add(new VisLabel("Gravity X:", Align.right)).padRight(5).width(115);
        gravityXTextField = new VisTextField();
        gravityXTextField.addListener(new KeyboardListener(PHYSICS_ENABLED_CHECKBOX_CLICKED));
        add(gravityXTextField).width(115);
        row().padTop(5);
        add(new VisLabel("Gravity Y:", Align.right)).padRight(5).width(115);
        gravityYTextField = new VisTextField();
        gravityYTextField.addListener(new KeyboardListener(PHYSICS_ENABLED_CHECKBOX_CLICKED));
        add(gravityYTextField).width(115);
        row().padTop(5);
        add(new VisLabel("Sleep velocity:", Align.right)).padRight(5).width(115);
        sleepVelocityTextField = new VisTextField();
        sleepVelocityTextField.addListener(new KeyboardListener(PHYSICS_ENABLED_CHECKBOX_CLICKED));
        add(sleepVelocityTextField).width(115);
        row().padTop(5);
        addSeparator().colspan(2).padTop(5).padBottom(5);
        add(new VisLabel("Enable lights:", Align.right)).padRight(5).width(115);
        enableLightsCheckBox = new VisCheckBox(null);
        enableLightsCheckBox.addListener(new CheckBoxChangeListener(PHYSICS_ENABLED_CHECKBOX_CLICKED));
        add(enableLightsCheckBox).left();
        row().padTop(5);
        add(new VisLabel("Diffuse:", Align.right)).padRight(5).width(115);
        diffuseCheckBox = new VisCheckBox(null);
        diffuseCheckBox.addListener(new CheckBoxChangeListener(PHYSICS_ENABLED_CHECKBOX_CLICKED));
        add(diffuseCheckBox).left();
    }
}
