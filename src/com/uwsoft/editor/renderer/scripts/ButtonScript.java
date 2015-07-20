package com.uwsoft.editor.renderer.scripts;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.uwsoft.editor.renderer.components.LayerMapComponent;
import com.uwsoft.editor.renderer.data.LayerItemVO;

import java.util.HashMap;

/**
 * Created by azakhary on 6/19/2015.
 */
public class ButtonScript extends BasicScript {

    /* this are main layers */
    public static final String normalLayer = "normal";
    public static final String pressedLayer = "pressed";

    /* this are additional layers */
    public static final String checkedLayer = "checked";
    public static final String uncheckedLayer = "unchecked";
    public static final String disabledLayer = "disabled";
    public static final String overLayer = "over";


    boolean isChecked, isDisabled;

    private final DelayedRemovalArray<EventListener> listeners = new DelayedRemovalArray(0);

    private ClickListener clickListener;

    private HashMap<String, LayerItemVO> componentLayerMap = new HashMap<String, LayerItemVO>();

    public ButtonScript() {

    }

    @Override
    public void init(Entity item) {
        super.init(item);

        ComponentMapper<LayerMapComponent> layerMapComponentComponentMapper = ComponentMapper.getFor(LayerMapComponent.class);

        for(LayerItemVO vo: layerMapComponentComponentMapper.get(entity).layers) {
            componentLayerMap.put(vo.layerName, vo);
        }

        isDisabled = false;
        isChecked = false;
        addListener(clickListener = new ClickListener() {
            public void clicked (InputEvent event, float x, float y) {
                if (isDisabled()) return;
                setChecked(!isChecked);
            }
        });
    }

    @Override
    public void act(float delta) {
        boolean isPressed = isPressed();
        boolean isDisabled = isDisabled();

        String checkedLayer = normalLayer;
        String disabledLayer = normalLayer;
        String overLayer = normalLayer;
        String uncheckedLayer = normalLayer;
        // if above layers exist in composite, then substitute with actual;
        if(componentLayerMap.containsKey(ButtonScript.checkedLayer)) checkedLayer = ButtonScript.checkedLayer;
        if(componentLayerMap.containsKey(ButtonScript.disabledLayer)) disabledLayer = ButtonScript.disabledLayer;
        if(componentLayerMap.containsKey(ButtonScript.overLayer)) overLayer = ButtonScript.overLayer;
        if(componentLayerMap.containsKey(ButtonScript.uncheckedLayer)) uncheckedLayer = ButtonScript.uncheckedLayer;

        // hide all layers
        for(LayerItemVO vo: componentLayerMap.values()) {
            vo.isVisible = false;
        }

        if(isDisabled) {
            // show disabled layer
            componentLayerMap.get(disabledLayer).isVisible = true;
        } else {
            if (isPressed()) {
                // show pressed layer
                componentLayerMap.get(pressedLayer).isVisible = true;
            } else {
                if(isOver()) {
                    // show over layer
                    componentLayerMap.get(overLayer).isVisible = true;
                } else {
                    if (isChecked) {
                        //show checked layer
                        componentLayerMap.get(checkedLayer).isVisible = true;
                    } else {
                        // show unchecked layer
                        componentLayerMap.get(uncheckedLayer).isVisible = true;
                    }
                }
            }
        }
    }

    public boolean addListener (EventListener listener) {
        if (!listeners.contains(listener, true)) {
            listeners.add(listener);
            return true;
        }
        return false;
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
        return clickListener.isVisualPressed();
    }

    public boolean isOver () {
        return clickListener.isOver();
    }

    public boolean isDisabled () {
        return isDisabled;
    }

    @Override
    public void dispose() {

    }

}
