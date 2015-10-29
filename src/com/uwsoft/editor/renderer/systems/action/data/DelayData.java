package com.uwsoft.editor.renderer.systems.action.data;

/**
 * Created by ZeppLondon on 10/15/2015.
 */
public class DelayData extends DelegateData {
    public float duration;
    public float passedTime;

    public DelayData(float duration) {
        super();
        this.duration = duration;
    }
}
