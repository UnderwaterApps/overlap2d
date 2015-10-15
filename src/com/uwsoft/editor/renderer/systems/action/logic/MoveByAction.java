package com.uwsoft.editor.renderer.systems.action.logic;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.renderer.components.ActionComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

/**
 * Created by Eduard on 10/15/2015.
 */
public class MoveByAction extends RelativeTemporalAction {
    @Override
    protected void updateRelative(float percentDelta, Entity entity) {
        TransformComponent transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
        ActionComponent actionComponent = ComponentRetriever.get(entity, ActionComponent.class);

        float amountX = actionComponent.amountX*percentDelta;
        float amountY = actionComponent.amountY*percentDelta;

        transformComponent.x += amountX;
        transformComponent.y += amountY;
    }
}
