package com.uwsoft.editor.renderer.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Interpolation;
import com.uwsoft.editor.renderer.systems.action.logic.Action;

/**
 * Created by Eduard on 10/13/2015.
 */
public class ActionComponent implements Component {
    public String logicType;
    public Interpolation interpolation;
    public float duration;
    public float startX;
    public float startY;
    public float endX;
    public float endY;
    public float passedTime;
    public boolean began;
    public boolean complete;
}
