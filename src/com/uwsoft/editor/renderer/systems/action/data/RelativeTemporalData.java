package com.uwsoft.editor.renderer.systems.action.data;

import com.badlogic.gdx.math.Interpolation;

/**
 * Created by ZeppLondon on 10/15/2015.
 */
public class RelativeTemporalData extends TemporalData {
    public float lastPercent;

    public RelativeTemporalData(Interpolation interpolation, float duration) {
        super(interpolation, duration);
    }
}
