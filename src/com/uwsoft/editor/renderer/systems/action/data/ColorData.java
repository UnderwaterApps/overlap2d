package com.uwsoft.editor.renderer.systems.action.data;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;

/**
 * Created by ZeppLondon on 10/28/15.
 */
public class ColorData extends TemporalData {
    public float startR, startG, startB, startA;
    public Color endColor = new Color();

    public ColorData(Interpolation interpolation, float duration, Color color) {
        super(interpolation, duration);
        this.endColor = color;
    }
}
