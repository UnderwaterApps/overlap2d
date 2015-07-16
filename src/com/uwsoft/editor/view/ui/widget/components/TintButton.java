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

package com.uwsoft.editor.view.ui.widget.components;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.kotcrab.vis.ui.VisUI;


/**
 * Created by azakhary on 7/8/2014.
 */
public class TintButton extends Group {

    private final Skin skin;
    private Image colorImg;

    private Color colorValue;

    public TintButton(int width, int height) {
        skin = VisUI.getSkin();
        colorImg = new Image(skin.getDrawable("white"));
        Image borderImg = new Image(skin.getDrawable("tint-border"));

        colorImg.setWidth(width - 2);
        colorImg.setHeight(height - 2);
        colorImg.setX(1);
        colorImg.setY(1);
        borderImg.setWidth(width);
        borderImg.setHeight(height);

        addActor(colorImg);
        addActor(borderImg);

        setWidth(borderImg.getWidth());
        setHeight(borderImg.getHeight());
    }

    public Color getColorValue() {
        return colorValue;
    }

    public void setColorValue(Color color) {
        colorImg.setColor(color);
        colorValue = new Color(color);
    }

}
