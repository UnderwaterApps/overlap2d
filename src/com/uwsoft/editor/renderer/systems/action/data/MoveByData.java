package com.uwsoft.editor.renderer.systems.action.data;

import com.badlogic.gdx.math.Interpolation;

/**
 * Created by ZeppLondon on 10/15/2015.
 */
public class MoveByData extends RelativeTemporalData {
    public float amountX;
    public float amountY;


    public MoveByData(Interpolation interpolation, float duration, float amountX, float amountY) {
        super(interpolation, duration);
        this.amountX = amountX;
        this.amountY = amountY;
    }
}
