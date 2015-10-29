package com.uwsoft.editor.renderer.systems.action.logic;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.systems.action.data.SizeByData;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

/**
 * Created by ZeppLondon on 10/28/15.
 */
public class SizeByAction<T extends SizeByData> extends RelativeTemporalAction<T> {
    @Override
    protected void updateRelative(float percentDelta, Entity entity, T actionData) {
        DimensionsComponent dimensionsComponent = ComponentRetriever.get(entity, DimensionsComponent.class);
        dimensionsComponent.width += actionData.amountWidth * percentDelta;
        dimensionsComponent.height += actionData.amountHeight * percentDelta;
    }
}
