package com.uwsoft.editor.renderer.systems.action.data;

import com.badlogic.gdx.math.Interpolation;

/**
 * Created by ZeppLondon on 10/15/2015.
 */
public class MoveToData extends TemporalData{
    public float startX;
    public float startY;
    public float endX;
    public float endY;

    public MoveToData(Interpolation interpolation, float duration, float endX, float endY) {
        super(interpolation, duration);
        this.endX = endX;
        this.endY = endY;
    }
}
