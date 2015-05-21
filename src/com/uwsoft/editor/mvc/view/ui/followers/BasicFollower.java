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

package com.uwsoft.editor.mvc.view.ui.followers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.uwsoft.editor.renderer.conponents.DimensionsComponent;
import com.uwsoft.editor.renderer.conponents.TransformComponent;
import com.uwsoft.editor.renderer.utils.TransformMathUtils;
import com.uwsoft.editor.utils.runtime.ComponentRetriever;

/**
 * Created by azakhary on 5/20/2015.
 */
public abstract class BasicFollower extends Group {

    protected TransformComponent transformComponent;
    protected DimensionsComponent dimensionsComponent;
    protected Entity entity;

    public BasicFollower(Entity entity) {
        setItem(entity);
        create();
        update();
    }

    private void setItem(Entity entity) {
        transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
        dimensionsComponent = ComponentRetriever.get(entity, DimensionsComponent.class);
        this.entity = entity;
    }

    public void update() {
        // TODO: get items position based on current zoom
        // TODO: Make poolable vector
    	Vector2 localCoords = new Vector2(0, 0);
    	TransformMathUtils.localToSceneCoordinates(entity, localCoords);
        setX(localCoords.x);
        setY(localCoords.y);
        setWidth(dimensionsComponent.width * transformComponent.scaleX);
        setHeight(dimensionsComponent.height * transformComponent.scaleY);
    }

    public void show() {
        setVisible(true);
        update();
    }

    public void hide() {
        setVisible(false);
    }

    public abstract void create();
}
