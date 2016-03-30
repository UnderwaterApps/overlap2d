/*
 * ******************************************************************************
 *  * Copyright 2015 See AUTHORS file.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

package com.uwsoft.editor.utils;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

/**
 * Created by azakhary on 6/5/2015.
 */
public class EntityBounds extends Rectangle {

    private final float scaleX;
    private final float scaleY;
    private final float originX;
    private final float originY;

    public EntityBounds(Entity entity) {
        TransformComponent transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
        DimensionsComponent dimensionsComponent = ComponentRetriever.get(entity, DimensionsComponent.class);
        x = transformComponent.x;
        y = transformComponent.y;
        width = dimensionsComponent.width;
        height = dimensionsComponent.height;

        scaleX = transformComponent.scaleX;
        scaleY = transformComponent.scaleY;

        originX = transformComponent.originX;
        originY = transformComponent.originY;
    }

    public float getVisualX() {
        return getLeftX();
    }

    public float getVisualY() {
        return getLowerY();
    }

    public float getVisualWidth() {
        return Math.abs(getWidth() * scaleX);
    }

    public float getVisualHeight() {
        return Math.abs(getHeight() * scaleY);
    }

    public float getVisualRightX() {
        return getRightX();
    }

    public float getVisualTopY() {
        return getUpperY();
    }

    private float getLeftX() {
        float x = getX() - (scaleX - 1) * originX;
        if (scaleX < 0) {
            x = x - Math.abs(getWidth() * scaleX);
        }
        return x;
    }

    private float getRightX() {
        return (getLeftX() + Math.abs(getWidth() * scaleX));
    }

    private float getLowerY() {
        float y = getY() - (scaleY - 1) * originY;
        if (scaleY < 0) {
            y = y - Math.abs(getHeight() * scaleY);
        }
        return y;
    }

    private float getUpperY() {
        return (getLowerY() + Math.abs(getHeight() * scaleY));
    }

    public boolean intersects(Vector2 touchPoint) {
        return (touchPoint.x > getVisualX() && touchPoint.x < getVisualRightX() &&
                touchPoint.y > getVisualY() && touchPoint.y < getVisualTopY());
    }
}
