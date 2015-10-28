package com.uwsoft.editor.renderer.systems.action.logic;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.systems.action.data.ScaleToData;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

/**
 * Created by ZeppLondon on 10/28/15.
 */
public class ScaleToAction<T extends ScaleToData> extends TemporalAction<T> {
    @Override
    protected void update(float percent, Entity entity, T actionData) {
        TransformComponent transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
        transformComponent.scaleX = actionData.startX + (actionData.endX - actionData.startX) * percent;
        transformComponent.scaleY = actionData.startY + (actionData.endY - actionData.startY) * percent;
    }

    @Override
    public void begin(Entity entity, T actionData) {
        TransformComponent transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
        actionData.startX = transformComponent.scaleX;
        actionData.startY = transformComponent.scaleY;
    }
}
