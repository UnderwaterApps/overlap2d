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
import com.kotcrab.vis.ui.widget.*;
import com.uwsoft.editor.mvc.view.ui.properties.UIItemProperties;

/**
 * Created by azakhary on 4/28/2015.
 */
public class UILightItemProperties extends UIItemProperties {

    private VisCheckBox isStaticCheckBox;
    private VisCheckBox isXRayCheckBox;
    private NumberSelector rayCountSelector;
    private VisSelectBox<String> lightTypeSelectBox;

    private VisValidableTextField pointLightRadiusField;

    private VisValidableTextField coneInnerAngleField;
    private VisValidableTextField coneDistanceField;

    public UILightItemProperties() {
        super();

        isStaticCheckBox = new VisCheckBox(null);
        isXRayCheckBox = new VisCheckBox(null);
        rayCountSelector = new NumberSelector(null, 3, 3, 5000, 1);
        lightTypeSelectBox = new VisSelectBox<>();
        pointLightRadiusField = new VisValidableTextField();
        coneInnerAngleField = new VisValidableTextField();
        coneDistanceField = new VisValidableTextField();


        add(new VisLabel("Is static", Align.right)).padRight(5).width(55).right();
        add(isStaticCheckBox).left();
        row().padTop(5);
        add(new VisLabel("Is static", Align.right)).padRight(5).width(55).right();
        add(isXRayCheckBox).left();
        row().padTop(5);
        add(new VisLabel("Ray Count", Align.right)).padRight(5).width(55).right();
        add(rayCountSelector).left();
        row().padTop(5);
        add(new VisLabel("Type", Align.right)).padRight(5).width(55).right();
        add(lightTypeSelectBox).left();
        row().padTop(5);

        setListeners();
    }

    private void setListeners() {

    }
}
