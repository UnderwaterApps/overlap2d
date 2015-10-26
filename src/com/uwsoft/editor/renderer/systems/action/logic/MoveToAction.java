package com.uwsoft.editor.renderer.systems.action.logic;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.systems.action.data.MoveToData;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

/**
 * Created by Eduard on 10/13/2015.
 */
public class MoveToAction extends TemporalAction<MoveToData> {
    @Override
    public void update(float percent, Entity entity, MoveToData actionData) {
        TransformComponent transformComponent = ComponentRetriever.get(entity, TransformComponent.class);

        float x = actionData.startX + (actionData.endX - actionData.startX) * percent;
        float y = actionData.startY + (actionData.endY - actionData.startY) * percent;

        transformComponent.x = x;
        transformComponent.y = y;
    }

    @Override
    public void begin(Entity entity, MoveToData actionData) {
        MoveToData data = actionData;
        TransformComponent transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
        data.startX = transformComponent.x;
        data.startY = transformComponent.y;
    }

    @Override
    public void end(Entity entity, MoveToData actionData) {

    }
}
