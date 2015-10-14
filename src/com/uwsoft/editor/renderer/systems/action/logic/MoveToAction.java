package com.uwsoft.editor.renderer.systems.action.logic;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.renderer.components.ActionComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

/**
 * Created by Eduard on 10/13/2015.
 */
public class MoveToAction extends Action {
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

    @Override
    public void update(float percent, Entity entity) {
        TransformComponent transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
        ActionComponent actionComponent = ComponentRetriever.get(entity, ActionComponent.class);

        float x = actionComponent.startX + (actionComponent.endX - actionComponent.startX) * percent;
        float y = actionComponent.startY + (actionComponent.endY - actionComponent.startY) * percent;

        transformComponent.x = x;
        transformComponent.y = y;
    }

    @Override
    public void begin(Entity entity) {
        TransformComponent transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
        ActionComponent actionComponent = ComponentRetriever.get(entity, ActionComponent.class);
        actionComponent.startX = transformComponent.x;
        actionComponent.startY = transformComponent.y;
    }

    @Override
    public void end(Entity entity) {

    }
}
