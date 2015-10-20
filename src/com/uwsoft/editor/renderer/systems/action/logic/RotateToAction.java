package com.uwsoft.editor.renderer.systems.action.logic;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.systems.action.data.RotateToData;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

/**
 * Created by Eduard on 10/16/2015.
 */
public class RotateToAction extends TemporalAction<RotateToData> {
    @Override
    protected void update(float percent, Entity entity, RotateToData actionData) {
        TransformComponent transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
        transformComponent.rotation = (actionData.start + (actionData.end - actionData.start) * percent);
    }

    @Override
    public void begin(Entity entity, RotateToData actionData) {
        TransformComponent transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
        actionData.start = transformComponent.rotation;
    }
}
