package com.uwsoft.editor.renderer.systems.action.data;

/**
 * Created by ZeppLondon on 10/23/15.
 */
public class ParallelData extends ActionData {
    public ActionData[] actionDatas;
    public boolean complete;

    public ParallelData(ActionData[] actionDatas) {
        this.actionDatas = actionDatas;
    }
}
