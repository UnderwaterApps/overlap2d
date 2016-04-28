package com.uwsoft.editor.view.stage.tools.transformStrategy;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.SnapshotArray;
import com.uwsoft.editor.renderer.components.CompositeTransformComponent;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.NodeComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.utils.TransformCommandBuilder;
import com.uwsoft.editor.view.ui.followers.NormalSelectionFollower;

/**
 * Created by Sasun Poghosyan on 4/13/2016.
 */
public class CompositeStrategy extends AbstractTransformStrategy {

    @Override
    public void calculate(float mouseDx, float mouseDy, int anchor, Entity entity, TransformCommandBuilder transformCommandBuilder, Vector2 mousePointStage, float lastTransformAngle, float lastEntityAngle) {
        CompositeTransformComponent component = entity.getComponent(CompositeTransformComponent.class);
        if (!component.automaticResize) {
            TransformComponent transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
            DimensionsComponent dimensionsComponent = ComponentRetriever.get(entity, DimensionsComponent.class);

            float newWidth = dimensionsComponent.width;
            float newHeight = dimensionsComponent.height;
//        if (dimensionsComponent.boundBox != null) {
//            newWidth = (dimensionsComponent.boundBox.width + dimensionsComponent.boundBox.x) * transformComponent.scaleX;
//            newHeight = (dimensionsComponent.boundBox.height + dimensionsComponent.boundBox.y) * transformComponent.scaleY;
//        }
            float[] horizontal = calculateSizeAndXyAmount(mouseDx, mouseDy, transformComponent.rotation);
            float[] vertical = calculateSizeAndXyAmount(mouseDx, mouseDy, transformComponent.rotation + 90);
            float deltaW = horizontal[0] / transformComponent.scaleX;
            float deltaH = vertical[0] / transformComponent.scaleY;

            switch (anchor) {
                case NormalSelectionFollower.L:
                    float x = horizontal[1];
                    float y = horizontal[2];
                    newWidth = dimensionsComponent.width - deltaW;
                    transformComponent.x += x;
                    transformComponent.y += y;
                    move(entity, -deltaW, 0);

                    break;
                case NormalSelectionFollower.R:
                    newWidth = dimensionsComponent.width + deltaW;
                    break;
                case NormalSelectionFollower.B:
                    newHeight = dimensionsComponent.height - deltaH;
                    float x1 = vertical[1];
                    float y1 = vertical[2];
                    transformComponent.x += x1;
                    transformComponent.y += y1;
                    move(entity, 0, -deltaH);

                    break;
                case NormalSelectionFollower.T:
                    newHeight = dimensionsComponent.height + deltaH;
                    break;
                case NormalSelectionFollower.LT:
                    newWidth = dimensionsComponent.width - deltaW;
                    transformComponent.x += horizontal[1];
                    transformComponent.y += horizontal[2];

                    newHeight = dimensionsComponent.height + deltaH;

                    move(entity, -deltaW, 0);
                    break;
                case NormalSelectionFollower.RT:
                    newWidth = dimensionsComponent.width + deltaW;
                    newHeight = dimensionsComponent.height + deltaH;
                    break;
                case NormalSelectionFollower.RB:
                    newWidth = dimensionsComponent.width + deltaW;

                    newHeight = dimensionsComponent.height - deltaH;
                    transformComponent.x += vertical[1];
                    transformComponent.y += vertical[2];
                    move(entity, 0, -deltaH);
                    break;
                case NormalSelectionFollower.LB:
                    newWidth = dimensionsComponent.width - deltaW;
                    newHeight = dimensionsComponent.height - deltaH;

                    transformComponent.x += horizontal[1];
                    transformComponent.y += horizontal[2];
                    transformComponent.x += vertical[1];
                    transformComponent.y += vertical[2];
                    move(entity, -deltaW, -deltaH);
                    break;
            }

//        System.out.println("newWidth = " + newWidth + " newHeight = " + newHeight);

            //region proportional scaling
            // This was making sure for proportional sizing
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                float enclosingRectSize = Math.max(newWidth, newHeight);
                if (dimensionsComponent.width >= dimensionsComponent.height) {
                    newWidth = enclosingRectSize;
                    newHeight = (newWidth / dimensionsComponent.width) * dimensionsComponent.height;
                }
                if (dimensionsComponent.height > dimensionsComponent.width) {
                    newHeight = enclosingRectSize;
                    newWidth = (newHeight / dimensionsComponent.height) * dimensionsComponent.width;
                }
                System.out.println("");
            }
            //endregion

            //region rotation related stuff
            // Rotating
            if (anchor >= NormalSelectionFollower.ROTATION_LT && anchor <= NormalSelectionFollower.ROTATION_LB) {
                Vector2 originPoint = new Vector2(transformComponent.x + transformComponent.originX, transformComponent.y + transformComponent.originY);
                mousePointStage.sub(originPoint);
                float currentAngle = mousePointStage.angle();
                float angleDiff = currentAngle - lastTransformAngle;
                float newRotation = lastEntityAngle + angleDiff;
                transformComponent.rotation = newRotation;
                transformCommandBuilder.setRotation(newRotation);
                System.out.println();
            }
            //endregion

            transformCommandBuilder.setSize(newWidth, newHeight);
            dimensionsComponent.width = newWidth;
            dimensionsComponent.height = newHeight;
            dimensionsComponent.boundBox.width = newWidth;
            dimensionsComponent.boundBox.height = newHeight;
        } else {
            System.out.println("automatic resize is enabled. Uncheck and try again");
        }
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
