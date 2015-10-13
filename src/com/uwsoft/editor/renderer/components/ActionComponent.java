package com.uwsoft.editor.renderer.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by Eduard on 10/13/2015.
 */
public class ActionComponent implements Component {
    public int type;
    public float duration;
    public float passedDuration;
    public float x;
    public float y;
}
