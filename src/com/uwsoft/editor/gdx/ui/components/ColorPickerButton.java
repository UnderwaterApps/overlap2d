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

package com.uwsoft.editor.gdx.ui.components;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.uwsoft.editor.data.manager.DataManager;
import com.uwsoft.editor.data.manager.TextureManager;


/**
 * Created by azakhary on 7/8/2014.
 */
public class ColorPickerButton extends Group {

    private Image colorImg;

    public ColorPickerButton() {
        colorImg = new Image(DataManager.getInstance().textureManager.getEditorAsset("pixel"));
        Image borderImg = new Image(DataManager.getInstance().textureManager.getEditorAsset("colorBox"));

        colorImg.setScale(16);
        colorImg.setX(2);
        colorImg.setY(2);

        addActor(colorImg);
        addActor(borderImg);

        setWidth(borderImg.getWidth());
        setHeight(borderImg.getHeight());
    }

    public void setColorValue(Color color) {
        colorImg.setColor(color);
    }

    public Color getColorValue() {
        return colorImg.getColor();
    }

}
