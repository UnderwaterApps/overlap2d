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
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.ViewPortComponent;
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
        OrthographicCamera camera = Sandbox.getInstance().getCamera();
        Viewport viewport = Sandbox.getInstance().getViewport();

        // TODO: Make poolable vector
    	Vector2 localCoords = new Vector2(0, 0);
    	TransformMathUtils.localToSceneCoordinates(entity, localCoords);

        setX((int)(localCoords.x + (viewport.getScreenWidth()/2 - camera.position.x)));
        setY((int)(localCoords.y + (viewport.getScreenHeight()/2 - camera.position.y)));
        setWidth(dimensionsComponent.width * transformComponent.scaleX * camera.zoom);
        setHeight(dimensionsComponent.height * transformComponent.scaleY * camera.zoom);
    }

    public void show() {
        setVisible(true);
        update();
    }

    public void hide() {
        setVisible(false);
    }

    public abstract void create();


    public void setFollowerListener(FollowerTransformationListener listener) {

    }

    public void clearFollowerListener() {

    }

    @Override
    public Actor hit (float x, float y, boolean touchable) {
        Actor hitActor = super.hit(x, y, touchable);
        if(hitActor == null) return null;
        if(hitActor.equals(this)) return null;

        return hitActor;
    }

    public void handleNotification(Notification notification) {
        // This method is meant to be overridden.
    }

    public Entity getEntity() {
        return entity;
    }
}
