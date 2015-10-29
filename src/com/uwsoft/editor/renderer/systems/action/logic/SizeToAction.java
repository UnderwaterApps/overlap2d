package com.uwsoft.editor.renderer.systems.action.logic;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.systems.action.data.SizeToData;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

/**
 * Created by ZeppLondon on 10/28/15.
 */
public class SizeToAction <T extends SizeToData> extends TemporalAction<T>  {
    @Override
    protected void update(float percent, Entity entity, T actionData) {
        DimensionsComponent dimensionsComponent = ComponentRetriever.get(entity, DimensionsComponent.class);
        dimensionsComponent.width = actionData.startWidth + (actionData.endHeight - actionData.startWidth) * percent;
        dimensionsComponent.height = actionData.startHeight + (actionData.endHeight - actionData.startHeight) * percent;
    }

    @Override
    public void begin(Entity entity, T actionData) {
        DimensionsComponent dimensionsComponent = ComponentRetriever.get(entity, DimensionsComponent.class);
        actionData.startWidth = dimensionsComponent.width;
        actionData.startHeight = dimensionsComponent.height;
    }
}
