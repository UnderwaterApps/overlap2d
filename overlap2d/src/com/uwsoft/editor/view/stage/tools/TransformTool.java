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

package com.uwsoft.editor.view.stage.tools;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.commons.MsgAPI;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.factory.ItemFactory;
import com.uwsoft.editor.proxy.CursorManager;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.NinePatchComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.factory.EntityFactory;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.utils.TransformCommandBuilder;
import com.uwsoft.editor.utils.runtime.EntityUtils;
import com.uwsoft.editor.view.stage.Sandbox;
import com.uwsoft.editor.view.ui.FollowersUIMediator;
import com.uwsoft.editor.view.ui.followers.FollowerTransformationListener;
import com.uwsoft.editor.view.ui.followers.NormalSelectionFollower;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by azakhary on 4/30/2015.
 */
public class TransformTool extends SelectionTool implements FollowerTransformationListener {

    public static final String NAME = "TRANSFORM_TOOL";

    private float lastTransformAngle = 0;
    private float lastEntityAngle = 0;
	private CursorManager cursorManager;

    private TransformCommandBuilder commandBuilder = new TransformCommandBuilder();

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void initTool() {
        sandbox = Sandbox.getInstance();

        if(!sandbox.getSelector().selectionIsOneItem()){
            sandbox.getSelector().clearSelections();
        }

        updateListeners();

        // set cursor
        cursorManager = Overlap2DFacade.getInstance().retrieveProxy(CursorManager.NAME);
        cursorManager.setCursor(CursorManager.CROSS);
    }

    @Override
    public void handleNotification(Notification notification) {
        switch (notification.getName()) {
            case MsgAPI.NEW_ITEM_ADDED:
                updateListeners((Entity) notification.getBody());
                break;
        }
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
        updateListeners(selectedEntities);
    }

    private void updateListeners(Entity entity) {
        Set<Entity> entities = new HashSet<>();
        entities.add(entity);
        updateListeners(entities);
    }

    private void updateListeners(Set<Entity> entities) {
        FollowersUIMediator followersUIMediator = Overlap2DFacade.getInstance().retrieveMediator(FollowersUIMediator.NAME);
        followersUIMediator.clearAllListeners();

        for(Entity entity: entities) {
            followersUIMediator.getFollower(entity).setFollowerListener(this);
        }
    }


    @Override
    public void anchorDown(NormalSelectionFollower follower, int anchor, float x, float y) {
        Sandbox sandbox = Sandbox.getInstance();

        commandBuilder.begin(follower.getEntity());

        TransformComponent transformComponent = ComponentRetriever.get(follower.getEntity(), TransformComponent.class);
        DimensionsComponent dimensionsComponent = ComponentRetriever.get(follower.getEntity(), DimensionsComponent.class);
        if(anchor == NormalSelectionFollower.ROTATION_LT ||
                anchor == NormalSelectionFollower.ROTATION_RT ||
                anchor == NormalSelectionFollower.ROTATION_RB ||
                anchor == NormalSelectionFollower.ROTATION_LB) {

            // get mouse stage coordinates
            
            Vector2 mousePoint = sandbox.screenToWorld(x, y);
            Vector2 originPoint = new Vector2(transformComponent.x + transformComponent.originX, transformComponent.y + transformComponent.originY);
            mousePoint.sub(originPoint);

            lastTransformAngle = mousePoint.angle();
            lastEntityAngle = transformComponent.rotation;
            
        }
        
    }
    
    @Override
    public void anchorUp(NormalSelectionFollower follower, int anchor, float x, float y) {
        commandBuilder.execute();
    }
    
    private void defaultAnchorDraggedLogic(Vector2 mousePointStage, int anchor, Entity entity) {
        TransformComponent transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
        DimensionsComponent dimensionsComponent = ComponentRetriever.get(entity, DimensionsComponent.class);

        float newX = transformComponent.x;
        float newY = transformComponent.y;
        float newWidth = dimensionsComponent.width*transformComponent.scaleX;
        float newHeight = dimensionsComponent.height*transformComponent.scaleY;

        float tmpAdjustmentX = transformComponent.originX*(transformComponent.scaleX-1);
        float tmpAdjustmentY = transformComponent.originY*(transformComponent.scaleY-1);

        final float cos = MathUtils.cosDeg(transformComponent.rotation);
        final float sin = MathUtils.sinDeg(transformComponent.rotation);

        float difX = (transformComponent.x - mousePointStage.x);
        float difY = (transformComponent.y - mousePointStage.y);

        difX = (difX * cos + difY * sin);
        difY = (difX * -sin + difY * cos);

        switch (anchor) {
            case NormalSelectionFollower.L:
                newWidth = dimensionsComponent.width + difX*2;
                break;
            case NormalSelectionFollower.R:
                newWidth = tmpAdjustmentX - difX;
                break;
            case NormalSelectionFollower.B:
                newHeight = dimensionsComponent.height + difY*2;
                break;
            case NormalSelectionFollower.T:
                newHeight = tmpAdjustmentY - difY;
                break;
            case NormalSelectionFollower.LT:
                newWidth = dimensionsComponent.width + difX*2;
                newHeight = tmpAdjustmentY - difY;
                break;
            case NormalSelectionFollower.RT:
                newWidth = tmpAdjustmentX - difX;
                newHeight = tmpAdjustmentY - difY;
                break;
            case NormalSelectionFollower.RB:
                newWidth = tmpAdjustmentX - difX;
                newHeight = dimensionsComponent.height + difY*2;
                break;
            case NormalSelectionFollower.LB:
                newWidth = dimensionsComponent.width + difX*2;
                newHeight = dimensionsComponent.height + difY*2;
                break;
        }


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

        }

        // Rotating
        if(anchor >= NormalSelectionFollower.ROTATION_LT && anchor <= NormalSelectionFollower.ROTATION_LB) {
            Vector2 originPoint = new Vector2(transformComponent.x + transformComponent.originX, transformComponent.y + transformComponent.originY);
            mousePointStage.sub(originPoint);
            float currentAngle = mousePointStage.angle();
            float angleDiff = currentAngle - lastTransformAngle;
            float newRotation = lastEntityAngle+angleDiff;
            transformComponent.rotation = newRotation;

            commandBuilder.setRotation(newRotation);
        }

        float newScaleX = newWidth / dimensionsComponent.width;
        float newScaleY = newHeight / dimensionsComponent.height;

        commandBuilder.setScale(newScaleX, newScaleY);
        commandBuilder.setPos(newX, newY);

        transformComponent.scaleX = newScaleX;
        transformComponent.scaleY = newScaleY;
        transformComponent.x = newX;
        transformComponent.y = newY;
    }

    private void ninePatchAnchorDraggedLogic(Vector2 mousePointStage, int anchor, Entity entity) {
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
                newWidth = dimensionsComponent.width + (transformComponent.x-mousePointStage.x);
                if(newWidth < minWidth) {
                    newX = mousePointStage.x - (minWidth-newWidth);
                    newWidth = minWidth;
                } else {
                    newX = mousePointStage.x;
                }
                break;
            case NormalSelectionFollower.R:
                newWidth = dimensionsComponent.width + (mousePointStage.x - (transformComponent.x+dimensionsComponent.width));
                if(newWidth < minWidth) {
                    newWidth = minWidth;
                }
                break;
            case NormalSelectionFollower.B:
                newHeight = dimensionsComponent.height + (transformComponent.y-mousePointStage.y);
                if(newHeight < minHeight) {
                    newY = mousePointStage.y - (minHeight-newHeight);
                    newHeight = minHeight;
                } else {
                    newY = mousePointStage.y;
                }
                break;
            case NormalSelectionFollower.T:
                newHeight = dimensionsComponent.height + (mousePointStage.y - (transformComponent.y+dimensionsComponent.height));
                if(newHeight < minHeight) {
                    newHeight = minHeight;
                }
                break;
            case NormalSelectionFollower.LT:
                newWidth = dimensionsComponent.width + (transformComponent.x-mousePointStage.x);
                newHeight = dimensionsComponent.height + (mousePointStage.y - (transformComponent.y+dimensionsComponent.height));
                if(newWidth < minWidth) {
                    newX = mousePointStage.x - (minWidth-newWidth);
                    newWidth = minWidth;
                } else {
                    newX = mousePointStage.x;
                }
                if(newHeight < minHeight) {
                    newHeight = minHeight;
                }
                break;
            case NormalSelectionFollower.RT:
                newWidth = dimensionsComponent.width + (mousePointStage.x - (transformComponent.x+dimensionsComponent.width));
                newHeight = dimensionsComponent.height + (mousePointStage.y - (transformComponent.y+dimensionsComponent.height));
                if(newHeight < minHeight) {
                    newHeight = minHeight;
                }
                if(newWidth < minWidth) {
                    newWidth = minWidth;
                }
                break;
            case NormalSelectionFollower.RB:
                newWidth = dimensionsComponent.width + (mousePointStage.x - (transformComponent.x+dimensionsComponent.width));
                newHeight = dimensionsComponent.height + (transformComponent.y-mousePointStage.y);
                if(newWidth < minWidth) {
                    newWidth = minWidth;
                }
                if(newHeight < minHeight) {
                    newY = mousePointStage.y - (minHeight-newHeight);
                    newHeight = minHeight;
                } else {
                    newY = mousePointStage.y;
                }
                break;
            case NormalSelectionFollower.LB:
                newWidth = dimensionsComponent.width + (transformComponent.x-mousePointStage.x);
                newHeight = dimensionsComponent.height + (transformComponent.y-mousePointStage.y);
                if(newWidth < minWidth) {
                    newX = mousePointStage.x - (minWidth-newWidth);
                    newWidth = minWidth;
                } else {
                    newX = mousePointStage.x;
                }
                if(newHeight < minHeight) {
                    newY = mousePointStage.y - (minHeight-newHeight);
                    newHeight = minHeight;
                } else {
                    newY = mousePointStage.y;
                }
                break;
        }


        commandBuilder.setPos(newX, newY);
        commandBuilder.setSize(newWidth, newHeight);

        transformComponent.x = newX;
        transformComponent.y = newY;
        dimensionsComponent.width = newWidth;
        dimensionsComponent.height = newHeight;
    }

    @Override
    public void anchorDragged(NormalSelectionFollower follower, int anchor, float x, float y) {
        Sandbox sandbox = Sandbox.getInstance();

        Vector2 mousePointStage = sandbox.screenToWorld(x, y);

        if(EntityUtils.getType(follower.getEntity()) == EntityFactory.NINE_PATCH) {
            ninePatchAnchorDraggedLogic(mousePointStage, anchor, follower.getEntity());
        } else {
            defaultAnchorDraggedLogic(mousePointStage, anchor, follower.getEntity());
        }

        Overlap2DFacade.getInstance().sendNotification(MsgAPI.ITEM_DATA_UPDATED);
      
    }

    @Override
    public void anchorMouseEnter(NormalSelectionFollower follower, int anchor, float x, float y) {
        switch (anchor) {
            case NormalSelectionFollower.ROTATION_LB:
                cursorManager.setCursor(CursorManager.ROTATION_LB);
                break;
            case NormalSelectionFollower.ROTATION_LT:
                cursorManager.setCursor(CursorManager.ROTATION_LT);
                break;
            case NormalSelectionFollower.ROTATION_RT:
                cursorManager.setCursor(CursorManager.ROTATION_RT);
                break;
            case NormalSelectionFollower.ROTATION_RB:
                cursorManager.setCursor(CursorManager.ROTATION_RB);
                break;
            case NormalSelectionFollower.LB:
                cursorManager.setCursor(CursorManager.TRANSFORM_LEFT_RIGHT);
                break;
            case NormalSelectionFollower.L:
                cursorManager.setCursor(CursorManager.TRANSFORM_HORIZONTAL);
                break;
            case NormalSelectionFollower.LT:
                cursorManager.setCursor(CursorManager.TRANSFORM_RIGHT_LEFT);
                break;
            case NormalSelectionFollower.T:
                cursorManager.setCursor(CursorManager.TRANSFORM_VERTICAL);
                break;
            case NormalSelectionFollower.RT:
                cursorManager.setCursor(CursorManager.TRANSFORM_LEFT_RIGHT);
                break;
            case NormalSelectionFollower.R:
                cursorManager.setCursor(CursorManager.TRANSFORM_HORIZONTAL);
                break;
            case NormalSelectionFollower.RB:
                cursorManager.setCursor(CursorManager.TRANSFORM_RIGHT_LEFT);
                break;
            case NormalSelectionFollower.B:
                cursorManager.setCursor(CursorManager.TRANSFORM_VERTICAL);
                break;
            default:
                cursorManager.setCursor(CursorManager.NORMAL);
                break;
        }
    }

    @Override
    public void anchorMouseExit(NormalSelectionFollower follower, int anchor, float x, float y) {
        cursorManager.setCursor(CursorManager.CROSS);
    }
}
