package com.uwsoft.editor.renderer.scripts;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.uwsoft.editor.renderer.components.LayerMapComponent;

/**
 * Created by azakhary on 6/19/2015.
 */
public class ButtonScript extends BasicScript {

    public static final String normalLayer = "normal";
    public static final String overLayer = "over";
    public static final String pressedLayer = "pressed";

    boolean isChecked, isDisabled;
    boolean isOver, isVisualPressed;

    private ComponentMapper<LayerMapComponent> layerMapComponentComponentMapper;

    public ButtonScript() {
        layerMapComponentComponentMapper = ComponentMapper.getFor(LayerMapComponent.class);
    }

    @Override
    public void act(float delta) {
        LayerMapComponent layerMapComponent = layerMapComponentComponentMapper.get(entity);
        if(isPressed()) {
            //layerMapComponent.layers.get()
        }
    }

    public void setChecked (boolean isChecked) {
        this.isChecked = isChecked;
    }

    public void toggle () {
        setChecked(!isChecked);
    }

    public boolean isChecked () {
        return isChecked;
    }

    public boolean isPressed () {
        return isVisualPressed;
    }

    public boolean isOver () {
        return isOver;
    }

    public boolean isDisabled () {
        return isDisabled;
    }

    @Override
    public void dispose() {

    }

}
