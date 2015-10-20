package com.uwsoft.editor.renderer.systems.action.logic;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.renderer.systems.action.data.ActionData;
import com.uwsoft.editor.renderer.systems.action.data.DelayData;
import com.uwsoft.editor.renderer.systems.action.data.DelegateData;

/**
 * Created by Eduard on 10/15/2015.
 */
public class DelayAction  extends DelegateAction{

    @Override
    protected boolean delegate(float delta, Entity entity, DelegateData actionData) {
        DelayData data = cast(actionData);
        if (data.passedTime < data.duration) {
            data.passedTime += delta;
            if (data.passedTime < data.duration) return false;
            delta = data.passedTime - data.duration;
        }
        /*if (action == null) return true;
        return action.act(delta);*/
        return  true;
    }

    private DelayData cast(ActionData data) {
        return (DelayData) data;
    }
}
