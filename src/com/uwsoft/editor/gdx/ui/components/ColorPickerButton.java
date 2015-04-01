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
import com.uwsoft.editor.mvc.proxy.TextureManager;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.DataManager;


/**
 * Created by azakhary on 7/8/2014.
 */
public class ColorPickerButton extends Group {

    private final Overlap2DFacade facade;
    private final DataManager dataManager;
    private Image colorImg;

    public ColorPickerButton() {
        facade = Overlap2DFacade.getInstance();
        dataManager = facade.retrieveProxy(DataManager.NAME);
        TextureManager textureManager = facade.retrieveProxy(TextureManager.NAME);
        colorImg = new Image(textureManager.getEditorAsset("pixel"));
        Image borderImg = new Image(textureManager.getEditorAsset("colorBox"));

        colorImg.setScale(16);
        colorImg.setX(2);
        colorImg.setY(2);

        addActor(colorImg);
        addActor(borderImg);

        setWidth(borderImg.getWidth());
        setHeight(borderImg.getHeight());
    }

    public Color getColorValue() {
        return colorImg.getColor();
    }

    public void setColorValue(Color color) {
        colorImg.setColor(color);
    }

}
