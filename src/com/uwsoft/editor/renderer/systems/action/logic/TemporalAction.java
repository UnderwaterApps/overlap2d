package com.uwsoft.editor.renderer.systems.action.logic;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.renderer.systems.action.data.TemporalData;

/**
 * Created by ZeppLondon on 10/15/2015.
 */
public abstract class TemporalAction<T extends TemporalData> extends ActionLogic<T> {
    @Override
    public boolean act(float delta, Entity entity, T actionData) {
        if (actionData.complete) return true;

        if (!actionData.began) {
            begin(entity, actionData);
            actionData.began = true;
        }

        actionData.passedTime += delta;
        actionData.complete = actionData.passedTime >= actionData.duration;
        float percent;
        if (actionData.complete) {
            percent = 1;
        } else {
            percent = actionData.passedTime / actionData.duration;
            if (actionData.interpolation != null) percent = actionData.interpolation.apply(percent);
        }
        update(percent, entity, actionData);
        if (actionData.complete) end(entity, actionData);
        return actionData.complete;
    }

    abstract protected void update (float percent, Entity entity, T actionData);

    public void begin(Entity entity, T actionData) {

    }

    public void end(Entity entity, T actionData) {

    }
}
