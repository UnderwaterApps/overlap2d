package com.uwsoft.editor.renderer.systems.action.logic;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.systems.action.data.MoveByData;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

/**
 * Created by Eduard on 10/15/2015.
 */
public class MoveByAction extends RelativeTemporalAction<MoveByData> {
    @Override
    protected void updateRelative(float percentDelta, Entity entity, MoveByData actionData) {
        TransformComponent transformComponent = ComponentRetriever.get(entity, TransformComponent.class);

        float amountX = actionData.amountX*percentDelta;
        float amountY = actionData.amountY*percentDelta;

        transformComponent.x += amountX;
        transformComponent.y += amountY;
    }
}
