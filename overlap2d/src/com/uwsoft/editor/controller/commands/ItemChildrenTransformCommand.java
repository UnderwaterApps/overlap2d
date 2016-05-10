package com.uwsoft.editor.controller.commands;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.commons.MsgAPI;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.utils.runtime.EntityUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sasun Poghosyan on 4/29/2016.
 */
public class ItemChildrenTransformCommand extends EntityModifyRevertableCommand {

    private HashMap<Integer, Vector2> prevPosMap = new HashMap<>();
    private HashMap<Integer, PositionSize> parentPositionAndSizeMap = new HashMap<>();
    private Entity entity;

    private Vector2 prevLo;

    private void doActionOnParent() {
        Array<Object[]> objects = getNotification().getBody();
        Object[] itemData = objects.get(0);
        Entity entity = (Entity) itemData[0];
        Vector2 newPos = (Vector2) itemData[1];
        Vector2 newSize = (Vector2) itemData[2];

        TransformComponent transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
        DimensionsComponent dimensionsComponent = ComponentRetriever.get(entity, DimensionsComponent.class);
        Vector2 prevLocation = new Vector2(transformComponent.x, transformComponent.y);
        Vector2 prevSize = new Vector2(dimensionsComponent.width, dimensionsComponent.height);
        PositionSize positionSize = new PositionSize(prevLocation, prevSize);
        this.parentPositionAndSizeMap.put(EntityUtils.getEntityId(entity), positionSize);
        setState(transformComponent, newPos, dimensionsComponent, newSize);
//        transformComponent.x = newPos.x;
//        transformComponent.y = newPos.y;
//
//        dimensionsComponent.width = newSize.x;
//        dimensionsComponent.height = newSize.y;
//        if (dimensionsComponent.boundBox != null) {
//            dimensionsComponent.boundBox.width = newSize.x;
//            dimensionsComponent.boundBox.height = newSize.y;
//        }
    }

    private void undoActionOnParent() {
        Map.Entry<Integer, PositionSize> entry = parentPositionAndSizeMap.entrySet().iterator().next();
        Integer entityUniqueId = entry.getKey();
        PositionSize positionSize = entry.getValue();
        Vector2 prevLocation = positionSize.position;
        Vector2 prevSize = positionSize.size;
        entity = EntityUtils.getByUniqueId(entityUniqueId);
        TransformComponent transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
        DimensionsComponent dimensionsComponent = ComponentRetriever.get(entity, DimensionsComponent.class);
        setState(transformComponent, prevLocation, dimensionsComponent, prevSize);
//        transformComponent.x = prevLocation.x;
//        transformComponent.y = prevLocation.y;
//
//
//        dimensionsComponent.width = prevSize.x;
//        dimensionsComponent.height = prevSize.y;
//        if (dimensionsComponent.boundBox != null) {
//            dimensionsComponent.boundBox.width = prevSize.x;
//            dimensionsComponent.boundBox.height = prevSize.y;
//        }
    }

    @Override
    public void doAction() {
        doActionOnParent();
        Array<Object[]> payload = notification.getBody();
        for (int i = 1; i < payload.size; i++) {
            Object[] objectData = payload.get(i);
            Entity entity = (Entity) objectData[0];
            Vector2 newPos = (Vector2) objectData[1];
            TransformComponent transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
            Vector2 prevPos = new Vector2(transformComponent.x, transformComponent.y);
            prevPosMap.put(EntityUtils.getEntityId(entity), prevPos);
//            EntityUtils.setPosition(entity, newPos);
            transformComponent.x = newPos.x;
            transformComponent.y = newPos.y;
        }
        Overlap2DFacade.getInstance().sendNotification(MsgAPI.ITEM_DATA_UPDATED, entity);
    }

    @Override
    public void undoAction() {
        undoActionOnParent();
        for (Map.Entry<Integer, Vector2> entry : prevPosMap.entrySet()) {
            Integer entityUniqueId = entry.getKey();
            Vector2 oldPosition = entry.getValue();
            Entity entity = EntityUtils.getByUniqueId(entityUniqueId);
            TransformComponent transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
            transformComponent.x = oldPosition.x;
            transformComponent.y = oldPosition.y;
        }
        Overlap2DFacade.getInstance().sendNotification(MsgAPI.ITEM_DATA_UPDATED, entity);
    }

    private void setState(TransformComponent tC, Vector2 position, DimensionsComponent dC, Vector2 size) {
        tC.x = position.x;
        tC.y = position.y;
        dC.width = size.x;
        dC.height = size.y;
        if (dC.boundBox != null) {
            dC.boundBox.width = size.x;
            dC.boundBox.height = size.y;
        }
    }

    private class PositionSize {
        Vector2 position;
        Vector2 size;

        public PositionSize(Vector2 position, Vector2 size) {
            this.position = position;
            this.size = size;
        }
    }
}
