package com.uwsoft.editor.renderer.systems.action.data;

import com.badlogic.gdx.math.Interpolation;

/**
 * Created by ZeppLondon on 10/29/15.
 */
public class AlphaData extends TemporalData {
    public float start, end;

    public AlphaData(Interpolation interpolation, float duration, float end) {
        super(interpolation, duration);
        this.end = end;
    }
}
