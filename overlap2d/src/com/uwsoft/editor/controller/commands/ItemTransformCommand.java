package com.uwsoft.editor.controller.commands;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.commons.MsgAPI;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.NodeComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.utils.runtime.EntityUtils;

import java.util.ArrayList;

/**
 * Created by Osman on 01.08.2015.
 */
public class ItemTransformCommand extends EntityModifyRevertableCommand {

    private Array<Object> payload;

    private Integer entityId;

    @Override
    public void doAction() {
        payload = getNotification().getBody();
        Entity entity = (Entity) payload.get(0);
        Object[] newData = (Object[]) payload.get(2);

        entityId = EntityUtils.getEntityId(entity);

        Vector2 newPos = (Vector2) newData[0];
        Vector2 newSize = (Vector2) newData[1];
        Vector2 newScale = (Vector2) newData[2];
        Float newRotation = (Float) newData[3];

        TransformComponent transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
        DimensionsComponent dimensionsComponent = ComponentRetriever.get(entity, DimensionsComponent.class);

        if(newPos != null) transformComponent.x = newPos.x;
        if(newPos != null) transformComponent.y = newPos.y;
        if(newSize != null) dimensionsComponent.width = newSize.x;
        if(newSize != null) dimensionsComponent.height = newSize.y;
        if(newScale != null) transformComponent.scaleX = newScale.x;
        if(newScale != null) transformComponent.scaleY = newScale.y;
        if(newRotation != null) transformComponent.rotation = newRotation;

        Overlap2DFacade.getInstance().sendNotification(MsgAPI.ITEM_DATA_UPDATED, entity);
    }

    @Override
    public void undoAction() {

        Entity entity = EntityUtils.getByUniqueId(entityId);
        Object[] prevData = (Object[]) payload.get(1);

        Vector2 prevPos = (Vector2) prevData[0];
        Vector2 prevSize = (Vector2) prevData[1];
        Vector2 prevScale = (Vector2) prevData[2];
        Float prevRotation = (Float) prevData[3];

        TransformComponent transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
        DimensionsComponent dimensionsComponent = ComponentRetriever.get(entity, DimensionsComponent.class);

        transformComponent.x = prevPos.x;
        transformComponent.y = prevPos.y;
        dimensionsComponent.width = prevSize.x;
        dimensionsComponent.height = prevSize.y;
        if (dimensionsComponent.boundBox != null) {
            dimensionsComponent.boundBox.width = dimensionsComponent.width;
            dimensionsComponent.boundBox.height = dimensionsComponent.height;
        }
        transformComponent.scaleX = prevScale.x;
        transformComponent.scaleY = prevScale.y;
        transformComponent.rotation = prevRotation;

        Overlap2DFacade.getInstance().sendNotification(MsgAPI.ITEM_DATA_UPDATED, entity);
    }
}
