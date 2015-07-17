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

import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.uwsoft.editor.event.SelectBoxChangeListener;
import com.uwsoft.editor.view.ui.properties.UIItemCollapsibleProperties;

/**
 * Created by azakhary on 4/16/2015.
 */
public class UISpineAnimationItemProperties extends UIItemCollapsibleProperties {

    private VisSelectBox<String> animationsSelectBox;

    public UISpineAnimationItemProperties() {
        super("Spine Animations");
        animationsSelectBox = new VisSelectBox<>();
        mainTable.add(createLabel("Animations:", Align.right)).padRight(5).colspan(2).fillX();
        mainTable.add(animationsSelectBox).width(120).colspan(2);
        setListeners();
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

    @Override
    public String getPrefix() {
        return this.getClass().getCanonicalName();
    }

    private void setListeners() {
        animationsSelectBox.addListener(new SelectBoxChangeListener(getUpdateEventName()));
    }
}
