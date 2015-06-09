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

package com.uwsoft.editor.mvc.view.stage.tools;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.uwsoft.editor.Overlap2D;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.CursorManager;
import com.uwsoft.editor.mvc.view.MidUIMediator;
import com.uwsoft.editor.mvc.view.ui.followers.BasicFollower;
import com.uwsoft.editor.mvc.view.ui.followers.FollowerTransformationListener;
import com.uwsoft.editor.mvc.view.ui.followers.NormalSelectionFollower;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.utils.runtime.ComponentRetriever;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by azakhary on 4/30/2015.
 */
public class TransformTool extends SelectionTool implements FollowerTransformationListener {

    public static final String NAME = "TRANSFORM_TOOL";

    private float lastTransformAngle = 0;
    private float lastEntityAngle = 0;

    @Override
    public void initTool() {
        Sandbox sandbox = Sandbox.getInstance();

        if(!sandbox.getSelector().selectionIsOneItem()){
            sandbox.getSelector().clearSelections();
        }

        updateListeners();

        // set cursor
        CursorManager cursorManager = Overlap2DFacade.getInstance().retrieveProxy(CursorManager.NAME);
        //cursorManager.setCursor(CursorManager.CROSS);
    }

    @Override
    public void stageMouseUp(float x, float y) {
        super.stageMouseUp(x, y);
        updateListeners();
    }

    @Override
    public void itemMouseUp(Entity entity, float x, float y) {
        super.itemMouseUp(entity, x, y);
        updateListeners();
    }

    private void updateListeners() {
        Sandbox sandbox = Sandbox.getInstance();

        Set<Entity> selectedEntities = sandbox.getSelector().getSelectedItems();

        MidUIMediator midUIMediator = Overlap2DFacade.getInstance().retrieveMediator(MidUIMediator.NAME);
        midUIMediator.clearAllListeners();

        for(Entity entity: selectedEntities) {
            midUIMediator.getFollower(entity).setFollowerListener(this);
        }
    }


    @Override
    public void anchorDown(NormalSelectionFollower follower, int anchor, float x, float y) {
        Sandbox sandbox = Sandbox.getInstance();

        if(anchor == NormalSelectionFollower.ROTATION_LT ||
                anchor == NormalSelectionFollower.ROTATION_RT ||
                anchor == NormalSelectionFollower.ROTATION_RB ||
                anchor == NormalSelectionFollower.ROTATION_LB) {

            // get mouse stage coordinates
            TransformComponent transformComponent = ComponentRetriever.get(follower.getEntity(), TransformComponent.class);
            Vector2 mousePoint = sandbox.stageToScreenCoordinates(Gdx.input.getX(), Gdx.input.getY());
            mousePoint.sub(transformComponent.x + transformComponent.originX, transformComponent.y + transformComponent.originY);

            lastTransformAngle = mousePoint.angle();
            lastEntityAngle = transformComponent.rotation;
        }
    }

    @Override
    public void anchorDragged(NormalSelectionFollower follower, int anchor, float x, float y) {
        Sandbox sandbox = Sandbox.getInstance();
        TransformComponent transformComponent = ComponentRetriever.get(follower.getEntity(), TransformComponent.class);
        DimensionsComponent dimensionsComponent = ComponentRetriever.get(follower.getEntity(), DimensionsComponent.class);

        float newX = transformComponent.x;
        float newY = transformComponent.y;
        float newWidth = dimensionsComponent.width * transformComponent.scaleX;
        float newHeight = dimensionsComponent.height * transformComponent.scaleY;

        float newOriginX = transformComponent.originX;
        float newOriginY = transformComponent.originY;

        switch (anchor) {
            case NormalSelectionFollower.ORIGIN:
                newOriginX = x - transformComponent.x;
                newOriginY = y - transformComponent.y;
                break;
            case NormalSelectionFollower.LB:
                newX = x;
                newY = y;
                newWidth = newWidth + (transformComponent.x - x);
                newHeight = newHeight + (transformComponent.y - y);
                break;
            case NormalSelectionFollower.L:
                newX = x;
                newWidth = newWidth + (transformComponent.x - x);
                break;
            case NormalSelectionFollower.LT:
                newX = x;
                newWidth = newWidth + (transformComponent.x - x);
                newHeight = y - transformComponent.y;
                break;
            case NormalSelectionFollower.T:
                newHeight = y - transformComponent.y;
                break;
            case NormalSelectionFollower.B:
                newY = y;
                newHeight = newHeight + (transformComponent.y - y);
                break;
            case NormalSelectionFollower.RB:
                newY = y;
                newWidth = x - follower.getX();
                newHeight = newHeight + (transformComponent.y - y);
                break;
            case NormalSelectionFollower.R:
                newWidth = x - transformComponent.x;
                break;
            case NormalSelectionFollower.RT:
                newHeight = y - transformComponent.y;
                newWidth = x - transformComponent.x;
                break;
        }

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

        }

        if(anchor != NormalSelectionFollower.ORIGIN) {
            newOriginX = (newWidth/(dimensionsComponent.width * transformComponent.scaleX)) * newOriginX;
            newOriginY = (newHeight/(dimensionsComponent.height * transformComponent.scaleY)) * newOriginY;
        }

        if(anchor >= NormalSelectionFollower.ROTATION_LT && anchor <= NormalSelectionFollower.ROTATION_LB) {
            // get mouse stage coordinates
            Vector2 mousePoint = sandbox.stageToScreenCoordinates(Gdx.input.getX(), Gdx.input.getY());
            mousePoint.sub(transformComponent.x + transformComponent.originX, transformComponent.y + transformComponent.originY);
            float currentAngle = mousePoint.angle();
            float angleDiff = currentAngle - lastTransformAngle;
            transformComponent.rotation = lastEntityAngle - angleDiff;
        }

        transformComponent.x = newX;
        transformComponent.y = newY;
        transformComponent.scaleX = newWidth/dimensionsComponent.width;
        transformComponent.scaleY = newHeight/dimensionsComponent.height;
        transformComponent.originX = newOriginX;
        transformComponent.originY = newOriginY;

        Overlap2DFacade.getInstance().sendNotification(Overlap2D.ITEM_DATA_UPDATED);
    }

    @Override
    public void anchorUp(NormalSelectionFollower follower, int anchor, float x, float y) {

    }

    @Override
    public void anchorMouseEnter(NormalSelectionFollower follower, int anchor, float x, float y) {

    }

    @Override
    public void anchorMouseExit(NormalSelectionFollower follower, int anchor, float x, float y) {

    }
}
