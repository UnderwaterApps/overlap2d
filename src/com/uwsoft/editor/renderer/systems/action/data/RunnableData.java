package com.uwsoft.editor.renderer.systems.action.data;

import com.badlogic.gdx.math.Interpolation;

/**
 * Created by Eduard on 10/15/2015.
 */
public class RunnableData extends ActionData {
    public Runnable runnable;
    public boolean ran;

    public RunnableData(Runnable runnable) {
        super();
        this.runnable = runnable;
    }
}
