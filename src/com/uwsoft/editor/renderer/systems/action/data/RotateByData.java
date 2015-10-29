package com.uwsoft.editor.renderer.systems.action.data;

import com.badlogic.gdx.math.Interpolation;

/**
 * Created by ZeppLondon on 10/16/2015.
 */
public class RotateByData extends RelativeTemporalData {
    public float amount;

    public RotateByData(Interpolation interpolation, float duration, float amount) {
        super(interpolation, duration);
        this.amount = amount;
    }
}
