package com.uwsoft.editor.renderer.systems.action.logic;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.systems.action.data.ActionData;
import com.uwsoft.editor.renderer.systems.action.data.RotateByData;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

/**
 * Created by Eduard on 10/16/2015.
 */
public class RotateByAction extends RelativeTemporalAction<RotateByData> {
    @Override
    protected void updateRelative(float percentDelta, Entity entity, RotateByData actionData) {
        TransformComponent transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
        transformComponent.rotation += actionData.amount * percentDelta;
    }
}
