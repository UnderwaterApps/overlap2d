package com.uwsoft.editor.renderer.systems.action.data;

import com.badlogic.gdx.math.Interpolation;

/**
 * Created by ZeppLondon on 10/28/15.
 */
public class ScaleToData extends TemporalData {
    public float startX, startY;
    public float endX, endY;

    public ScaleToData(Interpolation interpolation, float duration, float endX, float endY) {
        super(interpolation, duration);
        this.endX = endX;
        this.endY = endY;
    }
}
