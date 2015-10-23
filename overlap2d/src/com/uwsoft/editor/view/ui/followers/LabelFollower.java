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

package com.uwsoft.editor.view.ui.followers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.commons.MsgAPI;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.view.stage.Sandbox;

import java.util.HashMap;

/**
 * Created by azakhary on 5/20/2015.
 */
public class LabelFollower extends NormalSelectionFollower implements FollowerTransformationListener{

    HashMap<Integer, LabelAnchorListener> anchorListeners;

    public LabelFollower(Entity entity) {
        super(entity);
    }

    @Override
    public void create() {
        super.create();
        anchorListeners = new HashMap<>();
        for(int i = 0; i < miniRects.length; i++) {
            LabelAnchorListener listener = new LabelAnchorListener(this, this, i);
            anchorListeners.put(i, listener);
            miniRects[i].addListener(listener);
        }
        transformGroup.setVisible(true);
        miniRects[ORIGIN].setVisible(false);
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void setFollowerListener(FollowerTransformationListener listener) {
        for(int i = 0; i < miniRects.length; i++) {
            anchorListeners.get(i).setListenerTransform(listener);
        }
    }

    @Override
    public void clearFollowerListener() {

    }

    public void setMode(SelectionMode mode) {
        super.setMode(mode);
        transformGroup.setVisible(true);
        miniRects[ORIGIN].setVisible(false);
        if(mode == SelectionMode.transform) {
            miniRects[ORIGIN].setVisible(true);
        }
    }

    @Override
    public void anchorDown(NormalSelectionFollower follower, int anchor, float x, float y) {

    }

    @Override
    public void anchorDragged(NormalSelectionFollower follower, int anchor, float x, float y) {
        update();

        Vector2 stageCoordinates = Sandbox.getInstance().screenToWorld(x, y);
        x = stageCoordinates.x;
        y = stageCoordinates.y;

        TransformComponent transformComponent = ComponentRetriever.get(follower.getEntity(), TransformComponent.class);
        DimensionsComponent dimensionsComponent = ComponentRetriever.get(follower.getEntity(), DimensionsComponent.class);

        float newX = transformComponent.x;
        float newY = transformComponent.y;
        float newWidth = dimensionsComponent.width;
        float newHeight = dimensionsComponent.height;

        switch (anchor) {
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
                newWidth = x - transformComponent.x;
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

        transformComponent.x = newX;
        transformComponent.y = newY;
        dimensionsComponent.width = newWidth;
        dimensionsComponent.height = newHeight;

        Overlap2DFacade.getInstance().sendNotification(MsgAPI.ITEM_DATA_UPDATED);
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
