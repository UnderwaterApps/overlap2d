package com.uwsoft.editor.renderer.systems.action.data;

import com.badlogic.gdx.math.Interpolation;

/**
 * Created by ZeppLondon on 10/28/15.
 */
public class SizeToData extends TemporalData{
    public float startWidth, startHeight;
    public float endWidth, endHeight;

    public SizeToData(Interpolation interpolation, float duration, float endWidth, float endHeight) {
        super(interpolation, duration);
        this.endWidth = endWidth;
        this.endHeight = endHeight;
    }
}
