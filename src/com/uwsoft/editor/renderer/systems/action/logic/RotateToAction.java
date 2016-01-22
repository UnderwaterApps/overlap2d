package com.uwsoft.editor.renderer.systems.action.logic;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.systems.action.data.RotateToData;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

/**
 * Created by ZeppLondon on 10/16/2015.
 */
public class RotateToAction<T extends RotateToData> extends TemporalAction<T> {
    @Override
    protected void update(float percent, Entity entity, T actionData) {
        TransformComponent transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
        transformComponent.rotation = (actionData.start + (actionData.end - actionData.start) * percent);
    }

    @Override
    public void begin(Entity entity, T actionData) {
        TransformComponent transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
        actionData.start = transformComponent.rotation;
    }
}
