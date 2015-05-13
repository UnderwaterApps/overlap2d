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

package com.uwsoft.editor.mvc.view.ui.properties.panels;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.NumberSelector;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.uwsoft.editor.mvc.event.SelectBoxChangeListener;
import com.uwsoft.editor.mvc.view.ui.properties.UIAbstractProperties;
import com.uwsoft.editor.mvc.view.ui.properties.UIItemCollapsibleProperties;

/**
 * Created by azakhary on 4/16/2015.
 */
public class UISpriteAnimationItemProperties extends UIItemCollapsibleProperties {

    public static final String EDIT_ANIMATIONS_CLICKED = "com.uwsoft.editor.mvc.view.ui.properties.panels.UISpriteAnimationItemProperties" + ".EDIT_ANIMATIONS_CLICKED";


    private NumberSelector fpsSelector;
    private VisSelectBox<String> animationsSelectBox;
    private VisTextButton editAnimationsButton;


    public UISpriteAnimationItemProperties() {
        super("Sprite Animation");

        fpsSelector = new NumberSelector("", 0, 0, 120);
        animationsSelectBox = new VisSelectBox<>();
        editAnimationsButton = new VisTextButton("Edit animations");

        mainTable.add(new VisLabel("FPS:", Align.right)).padRight(5).fillX();
        mainTable.add(fpsSelector).width(50).left();
        mainTable.row().padTop(5);

        mainTable.add(new VisLabel("Animations:", Align.right)).padRight(5).fillX();
        mainTable.add(animationsSelectBox).width(120);
        mainTable.row().padTop(5);
        mainTable.add(editAnimationsButton).right().colspan(2);
        setListeners();
    }

    public void setFPS(int fps) {
        fpsSelector.setValue(fps);
    }

    public int getFPS() {
        return fpsSelector.getValue();
    }

    public Array<String> getAnimations() {
        return animationsSelectBox.getItems();
    }

    public void setAnimations(Array<String> animations) {
        animationsSelectBox.setItems(animations);
    }

    public String getSelected() {
        return animationsSelectBox.getSelected();
    }

    public void setSelectedAnimation(String currentAnimationName) {
        animationsSelectBox.setSelected(currentAnimationName);
    }

    private void setListeners() {
        fpsSelector.addChangeListener(number -> facade.sendNotification(PROPERTIES_UPDATED));

        animationsSelectBox.addListener(new SelectBoxChangeListener(UIAbstractProperties.PROPERTIES_UPDATED));
        editAnimationsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                facade.sendNotification(EDIT_ANIMATIONS_CLICKED);
            }
        });
    }
}
