package com.uwsoft.editor.renderer.systems.action.data;

import com.badlogic.gdx.math.Interpolation;

/**
 * Created by Eduard on 10/15/2015.
 */
public class DelayData extends DelegateData {
    public float duration;
    public float passedTime;

    public DelayData(Interpolation interpolation, float duration, float passedTime) {
        super(interpolation);
        this.duration = duration;
        this.passedTime = passedTime;
    }
}
