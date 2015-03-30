package com.uwsoft.editor.gdx.ui.components;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.DataManager;


/**
 * Created by azakhary on 7/8/2014.
 */
public class ColorPicker extends Group {

    private final Overlap2DFacade facade;
    private final DataManager dataManager;
    private Image colorImg;

    public ColorPicker() {
        facade = Overlap2DFacade.getInstance();
        dataManager = facade.retrieveProxy(DataManager.NAME);
        colorImg = new Image(dataManager.textureManager.getEditorAsset("pixel"));
        Image borderImg = new Image(dataManager.textureManager.getEditorAsset("colorBox"));

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
