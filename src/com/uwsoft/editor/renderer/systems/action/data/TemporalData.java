package com.uwsoft.editor.renderer.systems.action.data;

import com.badlogic.gdx.math.Interpolation;

/**
 * Created by ZeppLondon on 10/15/2015.
 */
public class TemporalData extends ActionData{
    public float duration;
    public float passedTime;
    public boolean began;
    public boolean complete;
    public Interpolation interpolation;

    public TemporalData(Interpolation interpolation, float duration) {
        super();
        this.interpolation = interpolation;
        this.duration = duration;
    }
}
