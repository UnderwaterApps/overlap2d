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

	private Vector2 tmpOriginPoint;

	private Vector2 tmpPositionPoint;

	private float tmpAdjustX;
	private float tmpAdjustY;

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
        CursorManager cursorManager = Overlap2DFacade.getInstance().retrieveProxy(CursorManager.NAME);
        //cursorManager.setCursor(CursorManager.CROSS);
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
        
        //reset Origin Point to 0,0;
        tmpOriginPoint = new Vector2(transformComponent.originX, transformComponent.originY);
        tmpPositionPoint = new Vector2(transformComponent.x, transformComponent.y);
//        transformComponent.originX = 0;
//        transformComponent.originY = 0;
        //transformComponent.x += tmpOriginPoint.x;
        //transformComponent.y += tmpOriginPoint.y;
        //end of resseting
        tmpAdjustX = tmpOriginPoint.x*(transformComponent.scaleX-1);
        tmpAdjustY = tmpOriginPoint.y*(transformComponent.scaleY-1);
    }
    
    @Override
    public void anchorUp(NormalSelectionFollower follower, int anchor, float x, float y) {
    	TransformComponent transformComponent = ComponentRetriever.get(follower.getEntity(), TransformComponent.class);
    	DimensionsComponent dimensionsComponent = ComponentRetriever.get(follower.getEntity(), DimensionsComponent.class);
    	
    	 Vector2 mousePointStage = Sandbox.getInstance().screenToWorld(x, y);
         x = mousePointStage.x;
         y = mousePointStage.y;
         
         tmpAdjustX = tmpOriginPoint.x*(transformComponent.scaleX-1);
         tmpAdjustY = tmpOriginPoint.y*(transformComponent.scaleY-1);
    	
    	//reset Origin Point to original;
        transformComponent.originX = tmpOriginPoint.x;
        transformComponent.originY = tmpOriginPoint.y;
        float newX = tmpPositionPoint.x;
        float newY = tmpPositionPoint.y;
        switch (anchor) {
	        case NormalSelectionFollower.ORIGIN:
	           
	            break;
	        case NormalSelectionFollower.L:
	            newX = transformComponent.x - tmpAdjustX;
	            break;
	        case NormalSelectionFollower.R:
	        	newX = transformComponent.x + tmpAdjustX;
	            break;
	        case NormalSelectionFollower.T:
	        	newY = transformComponent.y - tmpAdjustY;
	            break;
	        case NormalSelectionFollower.B:
	        	newY = transformComponent.y + tmpAdjustY;
	            break;
	        case NormalSelectionFollower.LT:
	        	newX = transformComponent.x - tmpAdjustX;
	        	newY = transformComponent.y + tmpAdjustY;
	            break;
	        case NormalSelectionFollower.RT:
	        	newX = transformComponent.x + tmpAdjustX;
	        	newY = transformComponent.y + tmpAdjustY;
	            break;
	        case NormalSelectionFollower.RB:
	        	newX = transformComponent.x + tmpAdjustX;
	        	newY = transformComponent.y - tmpAdjustY;
	            break;
	        case NormalSelectionFollower.LB:
	        	newX = transformComponent.x - tmpAdjustX;
	        	newY = transformComponent.y - tmpAdjustY;
	            break;
	    }
        transformComponent.x = newX;
        transformComponent.y = newY;
        //transformComponent.x += tmpOriginPoint.x*transformComponent.scaleX-tmpOriginPoint.x;
        //transformComponent.y += tmpOriginPoint.y*transformComponent.scaleY;
        //end of resseting
    }

    @Override
    public void anchorDragged(NormalSelectionFollower follower, int anchor, float x, float y) {
    	 
        Sandbox sandbox = Sandbox.getInstance();

        Vector2 mousePointStage = sandbox.screenToWorld(x, y);
        x = mousePointStage.x;
        y = mousePointStage.y;
        
        System.out.println("asd");
        System.out.println("mousePointStage " + mousePointStage.toString());
        System.out.println("asd");

        TransformComponent transformComponent = ComponentRetriever.get(follower.getEntity(), TransformComponent.class);
        DimensionsComponent dimensionsComponent = ComponentRetriever.get(follower.getEntity(), DimensionsComponent.class);
        

        float newX = transformComponent.x;
        float newY = transformComponent.y;
        float newWidth = dimensionsComponent.width*transformComponent.scaleX;
        float newHeight = dimensionsComponent.height*transformComponent.scaleY;

        float newOriginX = transformComponent.originX;
        float newOriginY = transformComponent.originY;

        switch (anchor) {
            case NormalSelectionFollower.ORIGIN:
                newOriginX = x - transformComponent.x;
                newOriginY = y - transformComponent.y;
                // TODO: adjust coordinates
                //final float cos = (float)Math.cos(transformComponent.rotation * MathUtils.degreesToRadians);
                //final float sin = (float)Math.sin(transformComponent.rotation * MathUtils.degreesToRadians);
                //final float tox = (localCoords.x - originX) * scaleX;
                //final float toy = (localCoords.y - originY) * scaleY;
                //newX = (newX * cos + newY * sin)+newX;
                //newY = (newX * -sin + newY * cos)+newY;
                break;
           
            case NormalSelectionFollower.L:
            	transformComponent.originX = dimensionsComponent.width;
            	newX = tmpPositionPoint.x + tmpAdjustX;
                newWidth = dimensionsComponent.width + (tmpPositionPoint.x - x + tmpAdjustX);
                break;
            case NormalSelectionFollower.R:
            	transformComponent.originX = 0;
            	newX = tmpPositionPoint.x - tmpAdjustX;
                newWidth = x - newX ;
                break;
            case NormalSelectionFollower.T:
            	transformComponent.originY = 0;
            	newY = tmpPositionPoint.y - tmpAdjustY;
                newHeight = newY-y;
                break;
            case NormalSelectionFollower.B:
            	transformComponent.originY = dimensionsComponent.height;
            	newY = tmpPositionPoint.y + tmpAdjustY;
            	newHeight = dimensionsComponent.height + (tmpPositionPoint.y - y);
                break;
            case NormalSelectionFollower.LT:
            	transformComponent.originY = 0;
            	transformComponent.originX = dimensionsComponent.width;
            	newX = tmpPositionPoint.x + tmpAdjustX;
            	newWidth = dimensionsComponent.width + (tmpPositionPoint.x - x + tmpAdjustX);
            	newY = tmpPositionPoint.y - tmpAdjustY;
                newHeight = newY - y;
                break;
            case NormalSelectionFollower.RT:
            	transformComponent.originX = 0;
            	transformComponent.originY = 0;
            	newX = tmpPositionPoint.x - tmpAdjustX;
                newWidth = x - newX ;
                newY = tmpPositionPoint.y - tmpAdjustY;
                newHeight = newY - y;
                break;
            case NormalSelectionFollower.RB:
            	transformComponent.originX = 0;
            	transformComponent.originY = dimensionsComponent.height;
            	newX = tmpPositionPoint.x - tmpAdjustX;
                newWidth = x - newX ;
                newY = tmpPositionPoint.y + tmpAdjustY;
            	newHeight = dimensionsComponent.height + (tmpPositionPoint.y + y + tmpAdjustY);
                break;
            case NormalSelectionFollower.LB:
            	transformComponent.originX = dimensionsComponent.width;
            	transformComponent.originY = dimensionsComponent.height;
            	newX = tmpPositionPoint.x + tmpAdjustX;
                newWidth = dimensionsComponent.width + (tmpPositionPoint.x - x + tmpAdjustX);
                newY = tmpPositionPoint.y + tmpAdjustY;
            	newHeight = dimensionsComponent.height + (tmpPositionPoint.y + y + tmpAdjustY);
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
            Vector2 mousePoint = sandbox.worldToScreen(Gdx.input.getX(), Gdx.input.getY());
            mousePoint.sub(transformComponent.x + transformComponent.originX, transformComponent.y + transformComponent.originY);
            float currentAngle = mousePoint.angle();
            float angleDiff = currentAngle - lastTransformAngle;
            transformComponent.rotation = lastEntityAngle - angleDiff;
        }

        
        //transformComponent.y = newY;
        if(EntityUtils.getType(follower.getEntity()) == EntityFactory.NINE_PATCH) {
            NinePatchComponent ninePatchComponent = ComponentRetriever.get(follower.getEntity(), NinePatchComponent.class);
            if(newWidth < ninePatchComponent.ninePatch.getTotalWidth()) newWidth = ninePatchComponent.ninePatch.getTotalWidth();
            if(newHeight < ninePatchComponent.ninePatch.getTotalHeight()) newHeight = ninePatchComponent.ninePatch.getTotalHeight();

            dimensionsComponent.width = newWidth;
            dimensionsComponent.height = newHeight;
        } else{
        	System.out.println("newHeight " + newHeight + " / dimensionsComponent.height" + dimensionsComponent.height);
            transformComponent.scaleX = newWidth / dimensionsComponent.width;
            transformComponent.scaleY = newHeight / dimensionsComponent.height;
            System.out.println(" transformComponent.scaleY " +  transformComponent.scaleY);
        }
        
        transformComponent.x = newX;
        //transformComponent.y = newY;
        
        //transformComponent.originX = newOriginX;
        //transformComponent.originY = newOriginY;

        Overlap2DFacade.getInstance().sendNotification(Overlap2D.ITEM_DATA_UPDATED);
        
      
    }

    @Override
    public void anchorMouseEnter(NormalSelectionFollower follower, int anchor, float x, float y) {

    }

    @Override
    public void anchorMouseExit(NormalSelectionFollower follower, int anchor, float x, float y) {

    }
}
