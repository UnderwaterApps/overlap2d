package com.uwsoft.editor.renderer.systems.action.data;

import com.badlogic.gdx.math.Interpolation;

/**
 * Created by ZeppLondon on 10/28/15.
 */
public class SizeByData extends RelativeTemporalData {
    public float amountWidth, amountHeight;

    public SizeByData(Interpolation interpolation, float duration, float amountWidth, float amountHeight) {
        super(interpolation, duration);
        this.amountWidth = amountWidth;
        this.amountHeight = amountHeight;
    }
}
