package com.uwsoft.editor.renderer.systems.action.data;

/**
 * Created by ZeppLondon on 10/15/2015.
 */
public class RunnableData extends ActionData {
    public Runnable runnable;
    public boolean ran;

    public RunnableData(Runnable runnable) {
        super();
        this.runnable = runnable;
    }
}
