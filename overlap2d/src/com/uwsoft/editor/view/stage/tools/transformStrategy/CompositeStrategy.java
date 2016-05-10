package com.uwsoft.editor.view.stage.tools.transformStrategy;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.commons.MsgAPI;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.renderer.components.CompositeTransformComponent;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.NodeComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.utils.TransformCommandBuilder;
import com.uwsoft.editor.utils.runtime.EntityUtils;
import com.uwsoft.editor.view.ui.followers.NormalSelectionFollower;

import java.util.*;

/**
 * Created by Sasun Poghosyan on 4/13/2016.
 */
public class CompositeStrategy extends AbstractTransformStrategy {

    private ComponentMapper<NodeComponent> nodeMapper = ComponentMapper.getFor(NodeComponent.class);
    private HashMap<Integer, Vector2> childrenInitialPositions = new HashMap<>();
    private HashMap<Integer, Vector2> childrenFinalPositions = new HashMap<>();
    private final Array<Object[]> payloads = new Array<>();

    private final Vector2 parentInitialPosition = new Vector2();
    private final Vector2 parentInitialSize = new Vector2();

    private final Vector2 parentFinalPosition = new Vector2();
    private final Vector2 parentFinalSize = new Vector2();

    public void getInitialPositions(Entity entity) {
        getParentState(entity, parentInitialPosition, parentInitialSize);
        childrenInitialPositions.clear();
        getChildrenPositions(entity, childrenInitialPositions);
    }

    public void swapItemFinalAndInitialStates(Entity entity) {
        childrenFinalPositions.clear();
        getChildrenPositions(entity, childrenFinalPositions);
        getParentState(entity, parentFinalPosition, parentFinalSize);

        setEntityChildrenAtPositions(childrenInitialPositions);
        setParentState(entity, parentInitialPosition, parentInitialSize);

        sendResizePositionNotification(entity);
    }

    private void sendResizePositionNotification(Entity entity) {
        payloads.clear();
        payloads.add(parentEntity(entity));
        for (Map.Entry<Integer, Vector2> entrySet : childrenFinalPositions.entrySet()) {
            Object[] payload = new Object[2];
            payload[0] = EntityUtils.getByUniqueId(entrySet.getKey());
            payload[1] = entrySet.getValue();
            payloads.add(payload);
        }
        Overlap2DFacade.getInstance().sendNotification(MsgAPI.ACTION_ITEM_AND_CHILDREN_TO, payloads);
    }

    private void setParentState(Entity entity, Vector2 position, Vector2 size) {
        EntityUtils.setPosition(entity, position);
        EntityUtils.setSize(entity, size);
    }

    private void getParentState(Entity entity, Vector2 position, Vector2 size) {
        EntityUtils.getPosition(entity, position);
        EntityUtils.getSize(entity, size);
    }

    private Object[] parentEntity(Entity entity) {
        Object[] obj = new Object[3];
        obj[0] = entity;
        obj[1] = new Vector2(parentFinalPosition);
        obj[2] = new Vector2(parentFinalSize);
        return obj;
    }

    private void getChildrenPositions(Entity parentEntity, HashMap<Integer, Vector2> entityPos) {
        NodeComponent nodeComponent = nodeMapper.get(parentEntity);
        if (nodeComponent != null) {
            for (Entity entity : nodeComponent.children) {
                TransformComponent transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
                Vector2 currentEntityPos = new Vector2(transformComponent.x, transformComponent.y);
                entityPos.put(EntityUtils.getEntityId(entity), currentEntityPos);
            }
        }
    }

    private void setEntityChildrenAtPositions(HashMap<Integer, Vector2> posMap) {
        for (Map.Entry<Integer, Vector2> entrySet : posMap.entrySet()) {
            Integer id = entrySet.getKey();
            Vector2 position = entrySet.getValue();
            Entity entity = EntityUtils.getByUniqueId(id);
            EntityUtils.setPosition(entity, position);
        }
    }

    @Override
    public void calculate(float mouseDx, float mouseDy, int anchor, Entity entity, TransformCommandBuilder transformCommandBuilder, Vector2 mousePointStage, float lastTransformAngle, float lastEntityAngle) {
        CompositeTransformComponent component = entity.getComponent(CompositeTransformComponent.class);
        TransformComponent transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
        if (!component.automaticResize) {
            DimensionsComponent dimensionsComponent = ComponentRetriever.get(entity, DimensionsComponent.class);

            float newWidth = dimensionsComponent.width;
            float newHeight = dimensionsComponent.height;

            float[] horizontal = calculateSizeAndXyAmount(mouseDx, mouseDy, transformComponent.rotation);
            float[] vertical = calculateSizeAndXyAmount(mouseDx, mouseDy, transformComponent.rotation + 90);
            float deltaW = horizontal[0] / transformComponent.scaleX;
            float deltaH = vertical[0] / transformComponent.scaleY;

            switch (anchor) {
                case NormalSelectionFollower.L:
                    float x = horizontal[1];
                    float y = horizontal[2];
                    move(entity, -deltaW, 0);
                    if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                        deltaW *= 2;
                    }
                    newWidth = dimensionsComponent.width - deltaW;
                    transformComponent.x += x;
                    transformComponent.y += y;
                    break;
                case NormalSelectionFollower.R:
                    if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                        move(entity, deltaW, 0);
                        deltaW *= 2;
                        transformComponent.x -= horizontal[1];
                        transformComponent.y -= horizontal[2];
                    }
                    newWidth = dimensionsComponent.width + deltaW;
                    break;
                case NormalSelectionFollower.B:
                    float x1 = vertical[1];
                    float y1 = vertical[2];
                    move(entity, 0, -deltaH);
                    if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                        deltaH *= 2;
                    }
                    newHeight = dimensionsComponent.height - deltaH;
                    transformComponent.x += x1;
                    transformComponent.y += y1;
                    break;
                case NormalSelectionFollower.T:
                    if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                        move(entity, 0, deltaH);
                        deltaH *= 2;
                        transformComponent.x -= vertical[1];
                        transformComponent.y -= vertical[2];
                    }
                    newHeight = dimensionsComponent.height + deltaH;
                    break;
                case NormalSelectionFollower.LT:
                    if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                        move(entity, -deltaW, deltaH);
                        deltaW *= 2;
                        deltaH *= 2;
                        transformComponent.x -= vertical[1];
                        transformComponent.y -= vertical[2];
                    } else {
                        move(entity, -deltaW, 0);
                    }
                    newWidth = dimensionsComponent.width - deltaW;
                    newHeight = dimensionsComponent.height + deltaH;
                    transformComponent.x += horizontal[1];
                    transformComponent.y += horizontal[2];

                    break;
                case NormalSelectionFollower.RT:
                    if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                        move(entity, deltaW, deltaH);
                        deltaH *= 2;
                        deltaW *= 2;
                        transformComponent.x -= horizontal[1];
                        transformComponent.y -= horizontal[2];
                        transformComponent.x -= vertical[1];
                        transformComponent.y -= vertical[2];
                    }
                    newWidth = dimensionsComponent.width + deltaW;
                    newHeight = dimensionsComponent.height + deltaH;
                    break;
                case NormalSelectionFollower.RB:
                    if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                        move(entity, deltaW, -deltaH);
                        deltaW *= 2;
                        deltaH *= 2;
                        transformComponent.x -= horizontal[1];
                        transformComponent.y -= horizontal[2];
                    } else {
                        move(entity, 0, -deltaH);
                    }
                    newWidth = dimensionsComponent.width + deltaW;
                    newHeight = dimensionsComponent.height - deltaH;
                    transformComponent.x += vertical[1];
                    transformComponent.y += vertical[2];
                    break;
                case NormalSelectionFollower.LB:
                    move(entity, -deltaW, -deltaH);
                    if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                        deltaW *= 2;
                        deltaH *= 2;
                    }
                    newWidth = dimensionsComponent.width - deltaW;
                    newHeight = dimensionsComponent.height - deltaH;
                    transformComponent.x += horizontal[1];
                    transformComponent.y += horizontal[2];
                    transformComponent.x += vertical[1];
                    transformComponent.y += vertical[2];
                    break;
            }
            dimensionsComponent.width = newWidth;
            dimensionsComponent.height = newHeight;
            dimensionsComponent.boundBox.width = newWidth;
            dimensionsComponent.boundBox.height = newHeight;
        } else {
            System.out.println("Automatic resize is enabled. Uncheck and try again.");
        }
        // Rotating
        rotating(anchor, transformCommandBuilder, mousePointStage, lastTransformAngle, lastEntityAngle, transformComponent);
    }

    private void move(Entity node, float x, float y) {
        ComponentMapper<TransformComponent> transMapper = ComponentMapper.getFor(TransformComponent.class);
        SnapshotArray<Entity> nodeEntity = node.getComponent(NodeComponent.class).children;
        for (Entity child : nodeEntity) {
            TransformComponent transformComponent = transMapper.get(child);
            transformComponent.x += x;
            transformComponent.y += y;
        }
    }

}
