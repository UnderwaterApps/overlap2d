package com.uwsoft.editor.renderer.systems.action.logic;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.renderer.components.ActionComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

/**
 * Created by Eduard on 10/15/2015.
 */
public abstract class RelativeTemporalAction extends TemporalAction {
    @Override
    protected void update(float percent, Entity entity) {
        ActionComponent actionComponent = ComponentRetriever.get(entity, ActionComponent.class);
        updateRelative(percent - actionComponent.lastPercent, entity);
        actionComponent.lastPercent = percent;
    }

    @Override
    public void begin(Entity entity) {
        ActionComponent actionComponent = ComponentRetriever.get(entity, ActionComponent.class);
        actionComponent.lastPercent = 0;
    }

    abstract protected void updateRelative (float percentDelta, Entity entity);
}
