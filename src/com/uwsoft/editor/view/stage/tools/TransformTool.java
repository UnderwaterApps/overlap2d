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

import java.util.HashSet;
import java.util.Set;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.Overlap2D;
import com.uwsoft.editor.view.stage.Sandbox;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.factory.ItemFactory;
import com.uwsoft.editor.proxy.CursorManager;
import com.uwsoft.editor.view.MidUIMediator;
import com.uwsoft.editor.view.ui.followers.FollowerTransformationListener;
import com.uwsoft.editor.view.ui.followers.NormalSelectionFollower;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.NinePatchComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.factory.EntityFactory;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.utils.runtime.EntityUtils;

/**
 * Created by azakhary on 4/30/2015.
 */
public class TransformTool extends SelectionTool implements FollowerTransformationListener {

    public static final String NAME = "TRANSFORM_TOOL";

    private float lastTransformAngle = 0;
    private float lastEntityAngle = 0;
	private CursorManager cursorManager;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void initTool() {
        Sandbox sandbox = Sandbox.getInstance();

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
            case ItemFactory.NEW_ITEM_ADDED:
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
        MidUIMediator midUIMediator = Overlap2DFacade.getInstance().retrieveMediator(MidUIMediator.NAME);
        midUIMediator.clearAllListeners();

        for(Entity entity: entities) {
            midUIMediator.getFollower(entity).setFollowerListener(this);
        }
    }


    @Override
    public void anchorDown(NormalSelectionFollower follower, int anchor, float x, float y) {
        Sandbox sandbox = Sandbox.getInstance();
        TransformComponent transformComponent = ComponentRetriever.get(follower.getEntity(), TransformComponent.class);
        DimensionsComponent dimensionsComponent = ComponentRetriever.get(follower.getEntity(), DimensionsComponent.class);
        if(anchor == NormalSelectionFollower.ROTATION_LT ||
                anchor == NormalSelectionFollower.ROTATION_RT ||
                anchor == NormalSelectionFollower.ROTATION_RB ||
                anchor == NormalSelectionFollower.ROTATION_LB) {

            // get mouse stage coordinates
            
            Vector2 mousePoint = sandbox.worldToScreen(Gdx.input.getX(), Gdx.input.getY());
            mousePoint.sub(transformComponent.x + transformComponent.originX, transformComponent.y + transformComponent.originY);

            lastTransformAngle = mousePoint.angle();
            lastEntityAngle = transformComponent.rotation;
            
        }
        
    }
    
    @Override
    public void anchorUp(NormalSelectionFollower follower, int anchor, float x, float y) {
    	
        
    }
    
    //TODO
    //THIS PART IS SUPER VERY VERY TEMPORARRY it's need to be overwritten 

    @Override
    public void anchorDragged(NormalSelectionFollower follower, int anchor, float x, float y) {
    	 
        Sandbox sandbox = Sandbox.getInstance();

        Vector2 mousePointStage = sandbox.screenToWorld(x, y);
        x = mousePointStage.x;
        y = mousePointStage.y;

        TransformComponent transformComponent = ComponentRetriever.get(follower.getEntity(), TransformComponent.class);
        DimensionsComponent dimensionsComponent = ComponentRetriever.get(follower.getEntity(), DimensionsComponent.class);
        

        float newX = transformComponent.x;
        float newY = transformComponent.y;
        float newWidth = dimensionsComponent.width*transformComponent.scaleX;
        float newHeight = dimensionsComponent.height*transformComponent.scaleY;

        float newOriginX = transformComponent.originX;
        float newOriginY = transformComponent.originY;
        float tmpAdjustmenX = transformComponent.originX*(transformComponent.scaleX-1);
        float tmpAdjustmenY = transformComponent.originY*(transformComponent.scaleY-1);
        
        final float cos = MathUtils.cosDeg(transformComponent.rotation);
        final float sin = MathUtils.sinDeg(transformComponent.rotation);
        
        float difX = (transformComponent.x - x);
    	float difY = (transformComponent.y - y);
    	
    	difX = (difX * cos + difY * sin);
    	difY = (difX * -sin + difY * cos);
    	
        switch (anchor) {
            case NormalSelectionFollower.ORIGIN:
            	//TODO this shit is to complicated will leave it for now
//                newOriginX = x - transformComponent.x;
//                newOriginY = y - transformComponent.y;
                // TODO: adjust coordinates
                //final float cos = (float)Math.cos(transformComponent.rotation * MathUtils.degreesToRadians);
                //final float sin = (float)Math.sin(transformComponent.rotation * MathUtils.degreesToRadians);
                //final float tox = (localCoords.x - originX) * scaleX;
                //final float toy = (localCoords.y - originY) * scaleY;
                //newX = (newX * cos + newY * sin)+newX;
                //newY = (newX * -sin + newY * cos)+newY;
                break;
           
            case NormalSelectionFollower.L:
            	newWidth = dimensionsComponent.width + difX*2;
                break;
            case NormalSelectionFollower.R:
                newWidth = tmpAdjustmenX - difX;
                break;
            case NormalSelectionFollower.B:
            	newHeight = dimensionsComponent.height + difY*2;
                break;
            case NormalSelectionFollower.T:
                newHeight = tmpAdjustmenY - difY;
                break;
            case NormalSelectionFollower.LT:
            	newWidth = dimensionsComponent.width + difX*2;
            	newHeight = tmpAdjustmenY - difY;
                break;
            case NormalSelectionFollower.RT:
            	newWidth = tmpAdjustmenX - difX;
            	newHeight = tmpAdjustmenY - difY;
                break;
            case NormalSelectionFollower.RB:
            	newWidth = tmpAdjustmenX - difX;
            	newHeight = dimensionsComponent.height + difY*2;
                break;
            case NormalSelectionFollower.LB:
            	newWidth = dimensionsComponent.width + difX*2;
            	newHeight = dimensionsComponent.height + difY*2;
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
        	mousePointStage.sub(transformComponent.x + transformComponent.originX, transformComponent.y + transformComponent.originY);
            float currentAngle = mousePointStage.angle();
            float angleDiff = currentAngle - lastTransformAngle;
            transformComponent.rotation = angleDiff-lastEntityAngle;
        }

        
        //transformComponent.y = newY;
        if(EntityUtils.getType(follower.getEntity()) == EntityFactory.NINE_PATCH) {
            NinePatchComponent ninePatchComponent = ComponentRetriever.get(follower.getEntity(), NinePatchComponent.class);
            if(newWidth < ninePatchComponent.ninePatch.getTotalWidth()) newWidth = ninePatchComponent.ninePatch.getTotalWidth();
            if(newHeight < ninePatchComponent.ninePatch.getTotalHeight()) newHeight = ninePatchComponent.ninePatch.getTotalHeight();

            dimensionsComponent.width = newWidth;
            dimensionsComponent.height = newHeight;
        } else{
            transformComponent.scaleX = newWidth / dimensionsComponent.width;
            transformComponent.scaleY = newHeight / dimensionsComponent.height;            
        }
        
        transformComponent.x = newX;
        transformComponent.y = newY;
        
        //transformComponent.originX = newOriginX;
        //transformComponent.originY = newOriginY;

        Overlap2DFacade.getInstance().sendNotification(Overlap2D.ITEM_DATA_UPDATED);
      
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
