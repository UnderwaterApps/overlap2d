package com.uwsoft.editor.view.stage.tools.transformStrategy;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.NinePatchComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.utils.TransformCommandBuilder;
import com.uwsoft.editor.view.ui.followers.NormalSelectionFollower;

/**
 * Created by Sasun Poghosyan on 4/13/2016.
 */
public class NinePatchStrategy extends AbstractTransformStrategy {

    @Override
    public void calculate(float mouseDx, float mouseDy, int anchor, Entity entity, TransformCommandBuilder transformCommandBuilder, Vector2 mousePointStage, float lastTransformAngle, float lastEntityAngle) {
        TransformComponent transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
        DimensionsComponent dimensionsComponent = ComponentRetriever.get(entity, DimensionsComponent.class);

        float newX = transformComponent.x;
        float newY = transformComponent.y;
        float newWidth = dimensionsComponent.width;
        float newHeight = dimensionsComponent.height;

        NinePatchComponent ninePatchComponent = ComponentRetriever.get(entity, NinePatchComponent.class);
        float minWidth = ninePatchComponent.ninePatch.getTotalWidth();
        float minHeight = ninePatchComponent.ninePatch.getTotalHeight();

        switch (anchor) {
            case NormalSelectionFollower.L:
                newWidth = dimensionsComponent.width + (transformComponent.x - mousePointStage.x);
                if (newWidth < minWidth) {
                    newX = mousePointStage.x - (minWidth - newWidth);
                    newWidth = minWidth;
                } else {
                    newX = mousePointStage.x;
                }
                break;
            case NormalSelectionFollower.R:
                newWidth = dimensionsComponent.width + (mousePointStage.x - (transformComponent.x + dimensionsComponent.width));
                if (newWidth < minWidth) {
                    newWidth = minWidth;
                }
                break;
            case NormalSelectionFollower.B:
                newHeight = dimensionsComponent.height + (transformComponent.y - mousePointStage.y);
                if (newHeight < minHeight) {
                    newY = mousePointStage.y - (minHeight - newHeight);
                    newHeight = minHeight;
                } else {
                    newY = mousePointStage.y;
                }
                break;
            case NormalSelectionFollower.T:
                newHeight = dimensionsComponent.height + (mousePointStage.y - (transformComponent.y + dimensionsComponent.height));
                if (newHeight < minHeight) {
                    newHeight = minHeight;
                }
                break;
            case NormalSelectionFollower.LT:
                newWidth = dimensionsComponent.width + (transformComponent.x - mousePointStage.x);
                newHeight = dimensionsComponent.height + (mousePointStage.y - (transformComponent.y + dimensionsComponent.height));
                if (newWidth < minWidth) {
                    newX = mousePointStage.x - (minWidth - newWidth);
                    newWidth = minWidth;
                } else {
                    newX = mousePointStage.x;
                }
                if (newHeight < minHeight) {
                    newHeight = minHeight;
                }
                break;
            case NormalSelectionFollower.RT:
                newWidth = dimensionsComponent.width + (mousePointStage.x - (transformComponent.x + dimensionsComponent.width));
                newHeight = dimensionsComponent.height + (mousePointStage.y - (transformComponent.y + dimensionsComponent.height));
                if (newHeight < minHeight) {
                    newHeight = minHeight;
                }
                if (newWidth < minWidth) {
                    newWidth = minWidth;
                }
                break;
            case NormalSelectionFollower.RB:
                newWidth = dimensionsComponent.width + (mousePointStage.x - (transformComponent.x + dimensionsComponent.width));
                newHeight = dimensionsComponent.height + (transformComponent.y - mousePointStage.y);
                if (newWidth < minWidth) {
                    newWidth = minWidth;
                }
                if (newHeight < minHeight) {
                    newY = mousePointStage.y - (minHeight - newHeight);
                    newHeight = minHeight;
                } else {
                    newY = mousePointStage.y;
                }
                break;
            case NormalSelectionFollower.LB:
                newWidth = dimensionsComponent.width + (transformComponent.x - mousePointStage.x);
                newHeight = dimensionsComponent.height + (transformComponent.y - mousePointStage.y);
                if (newWidth < minWidth) {
                    newX = mousePointStage.x - (minWidth - newWidth);
                    newWidth = minWidth;
                } else {
                    newX = mousePointStage.x;
                }
                if (newHeight < minHeight) {
                    newY = mousePointStage.y - (minHeight - newHeight);
                    newHeight = minHeight;
                } else {
                    newY = mousePointStage.y;
                }
                break;
        }

        transformCommandBuilder.setPos(newX, newY);
        transformCommandBuilder.setSize(newWidth, newHeight);

        transformComponent.x = newX;
        transformComponent.y = newY;
        dimensionsComponent.width = newWidth;
        dimensionsComponent.height = newHeight;
    }

}
