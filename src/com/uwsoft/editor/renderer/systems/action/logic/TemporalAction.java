package com.uwsoft.editor.renderer.systems.action.logic;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.renderer.components.ActionComponent;
import com.uwsoft.editor.renderer.systems.action.data.TemporalData;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

/**
 * Created by Eduard on 10/15/2015.
 */
public abstract class TemporalAction extends Action {
    @Override
    public boolean act(float delta, Entity entity) {
        ActionComponent<TemporalData> actionComponent = ComponentRetriever.get(entity, ActionComponent.class);

        if (actionComponent.data.complete) return true;

        if (!actionComponent.data.began) {
            begin(entity);
            actionComponent.data.began = true;
        }

        actionComponent.data.passedTime += delta;
        actionComponent.data.complete = actionComponent.data.passedTime >= actionComponent.data.duration;
        float percent;
        if (actionComponent.data.complete) {
            percent = 1;
        } else {
            percent = actionComponent.data.passedTime / actionComponent.data.duration;
            if (actionComponent.data.interpolation != null) percent = actionComponent.data.interpolation.apply(percent);
        }
        update(percent, entity);
        if (actionComponent.data.complete) end(entity);
        return actionComponent.data.complete;
    }

    abstract protected void update (float percent, Entity entity);

    abstract public void begin(Entity entity);
    abstract public void end(Entity entity);
}
