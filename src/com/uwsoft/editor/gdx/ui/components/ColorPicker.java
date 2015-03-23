package com.uwsoft.editor.gdx.ui.components;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.uwsoft.editor.data.manager.DataManager;
import com.uwsoft.editor.data.manager.TextureManager;


/**
 * Created by azakhary on 7/8/2014.
 */
public class ColorPicker extends Group {

    private Image colorImg;

    public ColorPicker() {
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
