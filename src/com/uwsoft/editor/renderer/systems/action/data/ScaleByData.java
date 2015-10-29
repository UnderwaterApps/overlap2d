package com.uwsoft.editor.renderer.systems.action.data;

import com.badlogic.gdx.math.Interpolation;

/**
 * Created by ZeppLondon on 10/28/15.
 */
public class ScaleByData extends RelativeTemporalData {
    public float amountX, amountY;

    public ScaleByData(Interpolation interpolation, float duration, float amountX, float amountY) {
        super(interpolation, duration);
        this.amountX = amountX;
        this.amountY = amountY;
    }
}
