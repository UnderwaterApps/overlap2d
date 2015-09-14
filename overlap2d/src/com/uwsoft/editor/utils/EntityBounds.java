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
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

/**
 * Created by azakhary on 6/5/2015.
 */
public class EntityBounds extends Rectangle {

    private final float scaleX;
    private final float scaleY;

    public EntityBounds(Entity entity) {
        TransformComponent transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
        DimensionsComponent dimensionsComponent = ComponentRetriever.get(entity, DimensionsComponent.class);
        x = transformComponent.x;
        y = transformComponent.y;
        width = dimensionsComponent.width;
        height = dimensionsComponent.height;

        scaleX = transformComponent.scaleX;
        scaleY = transformComponent.scaleY;
    }

    public float getVisualX() {return Math.min(getX(), getX() + getWidth() * scaleX);}
    public float getVisualY() {return Math.min(getY(), getY() + getHeight() * scaleY);}
    public float getVisualWidth() {return Math.abs(getWidth() * scaleX);}
    public float getVisualHeight() {return Math.abs(getHeight() * scaleY);}

    public float getVisualRightX() {return Math.max(getX(), getX() + getWidth() * scaleX);}
    public float getVisualTopY() {return Math.max(getY(), getY() + getHeight() * scaleY);}
}
