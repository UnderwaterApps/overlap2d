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

package com.uwsoft.editor.mvc.view.ui.properties;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.uwsoft.editor.gdx.ui.components.ColorPickerButton;
import com.uwsoft.editor.renderer.actor.TextBoxItem;

/**
 * Created by azakhary on 4/15/2015.
 */
public class UIBasicItemProperties extends UIItemProperties {

    private TextField idBox;

    private TextField xVal;
    private TextField yVal;

    private TextField widthVal;
    private TextField heightVal;

    private TextField scalexVal;
    private TextField scaleyVal;

    private CheckBox flipV;
    private CheckBox flipH;

    private TextButton customVarsBtn;

    private ColorPickerButton tintColorComponent;

    private Actor itemActor;
    private TextBoxItem rotationVal;

    public UIBasicItemProperties() {

    }

    public void initView() {
        tintColorComponent = new ColorPickerButton();

        add(new VisLabel("Identifier:", Align.right)).padRight(5).colspan(2).fillX();
        add(new VisTextField()).width(120).colspan(2);
        row().padTop(5);
        add(new VisLabel("X:", Align.right)).padRight(5).width(50).right();
        add(new VisTextField()).width(55).padRight(5);
        add(new VisLabel("Y:", Align.right)).padRight(5).width(50).right();
        add(new VisTextField()).width(55);
        row().padTop(5);
        add(new VisLabel("Width:", Align.right)).padRight(5).width(55).right();
        add(new VisTextField()).width(55).padRight(5);
        add(new VisLabel("Height:", Align.right)).padRight(5).width(55).right();
        add(new VisTextField()).width(55);
        row().padTop(5);
        add(new VisLabel("Scale X:", Align.right)).padRight(5).width(55).right();
        add(new VisTextField()).width(55).padRight(5);
        add(new VisLabel("Scale Y:", Align.right)).padRight(5).width(55).right();
        add(new VisTextField()).width(55);
        row().padTop(5);
        add(new VisLabel("Flip X:", Align.right)).padRight(5).width(55).right();
        add(new VisCheckBox(null)).padRight(5).left();
        add(new VisLabel("Flip Y:", Align.right)).padRight(5).width(55).right();
        add(new VisCheckBox(null)).left();
        row().padTop(5);
        add(new VisLabel("Rotation:", Align.right)).padRight(5).colspan(2).fillX();
        add(new VisTextField()).width(120).colspan(2);
        row().padTop(5);
        add(new VisLabel("Rotation:", Align.right)).padRight(5).colspan(2).fillX();
        add(new VisTextField()).width(120).colspan(2);
        row().padTop(5);
        add(new VisLabel("Tint:", Align.right)).padRight(5).colspan(2).fillX();
        add(new ColorPickerButton()).width(120).colspan(2);
    }

}
