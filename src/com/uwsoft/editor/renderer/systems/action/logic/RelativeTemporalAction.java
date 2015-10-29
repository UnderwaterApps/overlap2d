package com.uwsoft.editor.renderer.systems.action.logic;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.renderer.systems.action.data.RelativeTemporalData;

/**
 * Created by ZeppLondon on 10/15/2015.
 */
public abstract class RelativeTemporalAction<T extends RelativeTemporalData> extends TemporalAction<T> {
    @Override
    protected void update(float percent, Entity entity, T actionData) {
        updateRelative(percent - actionData.lastPercent, entity, actionData);
        actionData.lastPercent = percent;
    }

    @Override
    public void begin(Entity entity, T actionData) {
        RelativeTemporalData data = actionData;
        data.lastPercent = 0;
    }

    abstract protected void updateRelative (float percentDelta, Entity entity, T actionData);
}
