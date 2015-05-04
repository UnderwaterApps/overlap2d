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

import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.uwsoft.editor.mvc.event.CheckBoxChangeListener;
import com.uwsoft.editor.mvc.view.ui.properties.UIAbstractProperties;
import com.uwsoft.editor.mvc.view.ui.properties.UIItemCollapsibleProperties;

/**
 * Created by azakhary on 4/16/2015.
 */
public class UICompositeItemProperties extends UIItemCollapsibleProperties {

    private VisCheckBox scissorsEnabledCheckBox;

    public UICompositeItemProperties() {
        super("Composite");
        scissorsEnabledCheckBox = new VisCheckBox(null);
        mainTable.add(new VisLabel("Scissors Enabled", Align.right)).padRight(5).width(55).right();
        mainTable.add(scissorsEnabledCheckBox).left();
        setListeners();
    }

    public boolean isScissorsEnabled() {
        return scissorsEnabledCheckBox.isChecked();
    }

    public void setScissorsEnabled(boolean scissorsEnabled) {
        scissorsEnabledCheckBox.setChecked(scissorsEnabled);
    }

    private void setListeners() {
        scissorsEnabledCheckBox.addListener(new CheckBoxChangeListener(UIAbstractProperties.PROPERTIES_UPDATED));
    }
}
