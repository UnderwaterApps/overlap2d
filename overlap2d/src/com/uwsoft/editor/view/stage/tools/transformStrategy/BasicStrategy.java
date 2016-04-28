package com.uwsoft.editor.view.stage.tools.transformStrategy;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.utils.TransformCommandBuilder;
import com.uwsoft.editor.view.ui.followers.NormalSelectionFollower;

/**
 * Created by Sasun Poghosyan on 4/13/2016.
 */
public class BasicStrategy extends AbstractTransformStrategy {

    @Override
    public void calculate(float mouseDx, float mouseDy, int anchor, Entity entity, TransformCommandBuilder transformCommandBuilder, Vector2 mousePointStage, float lastTransformAngle, float lastEntityAngle) {
        TransformComponent transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
        DimensionsComponent dimensionsComponent = ComponentRetriever.get(entity, DimensionsComponent.class);

        float newWidth = dimensionsComponent.width * transformComponent.scaleX;
        float newHeight = dimensionsComponent.height * transformComponent.scaleY;

        float[] horizontal = calculateSizeAndXyAmount(mouseDx, mouseDy, transformComponent.rotation);
        float[] vertical = calculateSizeAndXyAmount(mouseDx, mouseDy, transformComponent.rotation + 90);

        transformComponent.originX = dimensionsComponent.width * 0.5f;
        transformComponent.originY = dimensionsComponent.height * 0.5f;

        switch (anchor) {
            case NormalSelectionFollower.L:
                newWidth = dimensionsComponent.width * transformComponent.scaleX - horizontal[0];
                transformComponent.x += horizontal[1] * 0.5f;
                transformComponent.y += horizontal[2] * 0.5f;
                break;
            case NormalSelectionFollower.R:
                newWidth = dimensionsComponent.width * transformComponent.scaleX + horizontal[0];
                transformComponent.x += horizontal[1] * 0.5f;
                transformComponent.y += horizontal[2] * 0.5f;
                break;
            case NormalSelectionFollower.B:
                newHeight = dimensionsComponent.height * transformComponent.scaleY - vertical[0];
                transformComponent.x += vertical[1] * 0.5f;
                transformComponent.y += vertical[2] * 0.5f;
                break;
            case NormalSelectionFollower.T:
                newHeight = dimensionsComponent.height * transformComponent.scaleY + vertical[0];
                transformComponent.x += vertical[1] * 0.5f;
                transformComponent.y += vertical[2] * 0.5f;
                break;
            case NormalSelectionFollower.LT:
                newWidth = dimensionsComponent.width * transformComponent.scaleX - horizontal[0];
                newHeight = dimensionsComponent.height * transformComponent.scaleY + vertical[0];
                transformComponent.x += horizontal[1] * 0.5f;
                transformComponent.y += horizontal[2] * 0.5f;
                transformComponent.x += vertical[1] * 0.5f;
                transformComponent.y += vertical[2] * 0.5f;
                break;
            case NormalSelectionFollower.RT:
                newWidth = dimensionsComponent.width * transformComponent.scaleX + horizontal[0];
                newHeight = dimensionsComponent.height * transformComponent.scaleY + vertical[0];
                transformComponent.x += horizontal[1] * 0.5f;
                transformComponent.y += horizontal[2] * 0.5f;
                transformComponent.x += vertical[1] * 0.5f;
                transformComponent.y += vertical[2] * 0.5f;
                break;
            case NormalSelectionFollower.RB:
                newWidth = dimensionsComponent.width * transformComponent.scaleX + horizontal[0];
                newHeight = dimensionsComponent.height * transformComponent.scaleY - vertical[0];
                transformComponent.x += vertical[1] * 0.5f;
                transformComponent.y += vertical[2] * 0.5f;
                transformComponent.x += horizontal[1] * 0.5f;
                transformComponent.y += horizontal[2] * 0.5f;
                break;
            case NormalSelectionFollower.LB:
                newWidth = dimensionsComponent.width * transformComponent.scaleX - horizontal[0];
                newHeight = dimensionsComponent.height * transformComponent.scaleY - vertical[0];
                transformComponent.x += horizontal[1] * 0.5f;
                transformComponent.y += horizontal[2] * 0.5f;
                transformComponent.x += vertical[1] * 0.5f;
                transformComponent.y += vertical[2] * 0.5f;
                break;
        }

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

        float newScaleX = newWidth / dimensionsComponent.width;
        float newScaleY = newHeight / dimensionsComponent.height;

        transformCommandBuilder.setScale(newScaleX, newScaleY);
        transformComponent.scaleX = newScaleX;
        transformComponent.scaleY = newScaleY;
    }

}

