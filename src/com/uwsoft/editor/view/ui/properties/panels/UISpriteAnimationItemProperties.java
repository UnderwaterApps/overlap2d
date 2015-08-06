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

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.NumberSelector;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.uwsoft.editor.event.KeyboardListener;
import com.uwsoft.editor.event.SelectBoxChangeListener;
import com.uwsoft.editor.view.ui.properties.UIItemCollapsibleProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by azakhary on 4/16/2015.
 */
public class UISpriteAnimationItemProperties extends UIItemCollapsibleProperties {

    public static final String EDIT_ANIMATIONS_CLICKED = "com.uwsoft.editor.view.ui.properties.panels.UISpriteAnimationItemProperties" + ".EDIT_ANIMATIONS_CLICKED";


    private NumberSelector fpsSelector;
    private VisSelectBox<String> animationsSelectBox;
    private VisSelectBox<String> playModesSelectBox;
    private VisTextButton editAnimationsButton;

    public HashMap<Animation.PlayMode, String> playModes = new HashMap<>();

    public UISpriteAnimationItemProperties() {
        super("Sprite Animation");

        fpsSelector = new NumberSelector("", 0, 0, 120);
        animationsSelectBox = new VisSelectBox<>();
        playModesSelectBox = new VisSelectBox<>();
        editAnimationsButton = new VisTextButton("Edit animations");

        mainTable.add(createLabel("FPS:", Align.right)).padRight(5).fillX();
        mainTable.add(fpsSelector).width(50).left();
        mainTable.row().padTop(5);

        mainTable.add(createLabel("Play modes:", Align.right)).padRight(5).fillX();
        mainTable.add(playModesSelectBox).width(120);
        mainTable.row().padTop(5);

        mainTable.add(createLabel("Animations:", Align.right)).padRight(5).fillX();
        mainTable.add(animationsSelectBox).width(120);
        mainTable.row().padTop(5);
        mainTable.add(editAnimationsButton).right().colspan(2);

        setPlayModes();

        setListeners();
    }

    public void setFPS(int fps) {
        fpsSelector.setValue(fps);
    }

    public int getFPS() {
        return fpsSelector.getValue();
    }

    public void setPlayModes() {
        playModes.put(Animation.PlayMode.LOOP, "Loop");
        playModes.put(Animation.PlayMode.NORMAL, "Normal");
        playModes.put(Animation.PlayMode.REVERSED, "Reversed");
        playModes.put(Animation.PlayMode.LOOP_REVERSED, "Loop reversed");
        playModes.put(Animation.PlayMode.LOOP_PINGPONG, "Loop ping-pong");
        playModes.put(Animation.PlayMode.LOOP_RANDOM, "Loop random");

        String[] arr = new String[playModes.size()];
        int i = 0;
        for (Map.Entry<Animation.PlayMode, String> entry : playModes.entrySet()) {
            arr[i++] = entry.getValue();
        }
        playModesSelectBox.setItems(arr);
    }

    public Animation.PlayMode getPlayMode() {
        for (Map.Entry<Animation.PlayMode, String> entry : playModes.entrySet()) {
            Animation.PlayMode key = entry.getKey();
            String value = entry.getValue();
            if(playModesSelectBox.getSelected().equals(value)) {
                return key;
            }
        }
        return Animation.PlayMode.LOOP;
    }

    public void setPlayMode(Animation.PlayMode playMode) {
        playModesSelectBox.setSelected(playModes.get(playMode));
    }

    public Array<String> getAnimations() {
        return animationsSelectBox.getItems();
    }

    public void setAnimations(Array<String> animations) {
        animationsSelectBox.setItems(animations);
    }

    public String getSelectedAnimation() {
        return animationsSelectBox.getSelected();
    }

    public void setSelectedAnimation(String currentAnimationName) {
        animationsSelectBox.setSelected(currentAnimationName);
    }

    @Override
    public String getPrefix() {
        return this.getClass().getCanonicalName();
    }

    private void setListeners() {
        fpsSelector.addListener(new KeyboardListener(getUpdateEventName()));

        animationsSelectBox.addListener(new SelectBoxChangeListener(getUpdateEventName()));
        playModesSelectBox.addListener(new SelectBoxChangeListener(getUpdateEventName()));

        editAnimationsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                facade.sendNotification(EDIT_ANIMATIONS_CLICKED);
            }
        });
    }
}
