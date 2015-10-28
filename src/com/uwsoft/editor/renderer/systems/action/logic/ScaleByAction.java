package com.uwsoft.editor.renderer.systems.action.logic;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.systems.action.data.ScaleByData;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

/**
 * Created by ZeppLondon on 10/28/15.
 */
public class ScaleByAction<T extends ScaleByData> extends RelativeTemporalAction<T> {
    @Override
    protected void updateRelative(float percent, Entity entity, T actionData) {
        TransformComponent transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
        transformComponent.scaleX += actionData.amountX * percent;
        transformComponent.scaleY += actionData.amountY * percent;
    }
}
