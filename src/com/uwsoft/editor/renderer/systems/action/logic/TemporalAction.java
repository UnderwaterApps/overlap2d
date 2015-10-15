package com.uwsoft.editor.renderer.systems.action.logic;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.renderer.components.ActionComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

/**
 * Created by Eduard on 10/15/2015.
 */
public abstract class TemporalAction extends Action {
    @Override
    public boolean act(float delta, Entity entity) {
        ActionComponent actionComponent = ComponentRetriever.get(entity, ActionComponent.class);

        if (actionComponent.complete) return true;

        if (!actionComponent.began) {
            begin(entity);
            actionComponent.began = true;
        }

        actionComponent.passedTime += delta;
        actionComponent.complete = actionComponent.passedTime >= actionComponent.duration;
        float percent;
        if (actionComponent.complete) {
            percent = 1;
        } else {
            percent = actionComponent.passedTime / actionComponent.duration;
            if (actionComponent.interpolation != null) percent = actionComponent.interpolation.apply(percent);
        }
        update(percent, entity);
        if (actionComponent.complete) end(entity);
        return actionComponent.complete;
    }

    abstract protected void update (float percent, Entity entity);

    @Override
    public void begin(Entity entity) {

    }

    @Override
    public void end(Entity entity) {

    }
}
