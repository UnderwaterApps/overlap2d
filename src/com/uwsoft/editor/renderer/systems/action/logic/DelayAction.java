package com.uwsoft.editor.renderer.systems.action.logic;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.renderer.systems.action.data.ActionData;
import com.uwsoft.editor.renderer.systems.action.data.DelayData;
import com.uwsoft.editor.renderer.systems.action.data.DelegateData;

/**
 * Created by ZeppLondon on 10/15/2015.
 */
public class DelayAction<T extends DelayData>  extends DelegateAction<T>{

    @Override
    protected boolean delegate(float delta, Entity entity, T actionData) {
        if (actionData.passedTime < actionData.duration) {
            actionData.passedTime += delta;
            if (actionData.passedTime < actionData.duration) return false;
        }
        return true;
    }
}
