package com.uwsoft.editor.renderer.systems.action.data;

import com.badlogic.gdx.math.Interpolation;

/**
 * Created by ZeppLondon on 10/16/2015.
 */
public class RotateToData extends TemporalData {
    public float start;
    public float end;

    public RotateToData(Interpolation interpolation,float duration, float end) {
        super(interpolation, duration);
        this.end = end;
    }
}
