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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.uwsoft.editor.Overlap2D;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.CursorManager;
import com.uwsoft.editor.mvc.view.MidUIMediator;
import com.uwsoft.editor.mvc.view.ui.followers.BasicFollower;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.utils.runtime.ComponentRetriever;

/**
 * Created by azakhary on 4/30/2015.
 */
public class SelectionTool implements Tool {

    public static final String NAME = "SELECTION_TOOL";

    private Sandbox sandbox;

    private boolean isDragging = false;
    private boolean currentTouchedItemWasSelected = false;
    private boolean isCastingRectangle = false;

    private Vector2 directionVector = null;

    private Vector2 dragMouseStartPosition;
    private HashMap<Entity, Vector2> dragStartPositions = new HashMap<>();
    private HashMap<Entity, Vector2> dragTouchDiff = new HashMap<>();

	private TransformComponent transformComponent;

	private DimensionsComponent dimensionsComponent;

    public SelectionTool() {
    
    }

    @Override
    public void initTool() {
        sandbox = Sandbox.getInstance();
        Set<Entity> currSelection = sandbox.getSelector().getCurrentSelection();

        // set cursor
        CursorManager cursorManager = Overlap2DFacade.getInstance().retrieveProxy(CursorManager.NAME);
        cursorManager.setCursor(CursorManager.NORMAL);

        MidUIMediator midUIMediator = Overlap2DFacade.getInstance().retrieveMediator(MidUIMediator.NAME);
        midUIMediator.setMode(BasicFollower.FollowerMode.normal);
    }

    @Override
    public boolean stageMouseDown(float x, float y) {
        sandbox = Sandbox.getInstance();

        boolean setOpacity = false;

        //TODO: Anyone can explain what was the purpose of this?
        if (!Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            setOpacity = true;
        }

        // preparing selection tool rectangle to follow mouse
        sandbox.prepareSelectionRectangle(x, y, setOpacity);
        return true;
    }

    @Override
    public void stageMouseUp(float x, float y) {
        // selection is complete, this will check for what get caught in selection rect, and select 'em
        selectionComplete();

        isCastingRectangle = false;

    }

    @Override
    public void stageMouseDragged(float x, float y) {
        sandbox = Sandbox.getInstance();
        isCastingRectangle = true;
        sandbox.selectionRec.setWidth(x - sandbox.selectionRec.getX());
        sandbox.selectionRec.setHeight(y - sandbox.selectionRec.getY());
    }

    @Override
    public void stageMouseDoubleClick(float x, float y) {
        Overlap2DFacade.getInstance().sendNotification(Sandbox.ACTION_COMPOSITE_HIERARCHY_UP);
    }

    @Override
    public boolean itemMouseDown(Entity entity, float x, float y) {
        sandbox = Sandbox.getInstance();
        Overlap2DFacade facade = Overlap2DFacade.getInstance();

        currentTouchedItemWasSelected = sandbox.getSelector().getCurrentSelection().contains(entity);

        // if shift is pressed we are in add/remove selection mode
        if (isShiftPressed()) {
            //TODO block selection handling (wat?)
            if (!currentTouchedItemWasSelected) {
                // item was not selected, adding it to selection
                Set<Entity> items = new HashSet<>();
                items.add(entity);
                facade.sendNotification(Sandbox.ACTION_ADD_SELECTION, items);
            }
        } else {

        	//TODO fix and uncomment layer locking
//            if (item.isLockedByLayer()) {
//                // this is considered empty space click and thus should release all selections
//                facade.sendNotification(Sandbox.ACTION_SET_SELECTION, null);
//                sandbox.getSelector().clearSelections();
//                return false;
//            } else {
                if(!currentTouchedItemWasSelected) {
                    // get selection, add this item to selection
                    Set<Entity> items = new HashSet<>();
                    items.add(entity);
                    facade.sendNotification(Sandbox.ACTION_SET_SELECTION, items);
                }
            //}
        }

        // remembering local touch position for each of selected boxes, if planning to drag
        dragStartPositions.clear();
        dragTouchDiff.clear();
        for (Entity itemInstance : sandbox.getSelector().getCurrentSelection()) {
        	transformComponent = ComponentRetriever.get(itemInstance, TransformComponent.class);

            dragTouchDiff.put(itemInstance, new Vector2(x - transformComponent.x, y - transformComponent.y));
            dragStartPositions.put(itemInstance, new Vector2(transformComponent.x, transformComponent.y));
        }

        dragMouseStartPosition = new Vector2(x, y);

        // pining UI to update current item properties tools
        Overlap2DFacade.getInstance().sendNotification(Overlap2D.ITEM_DATA_UPDATED, entity);

        return true;
    }

    @Override
    public void itemMouseDragged(Entity entity, float x, float y) {
        sandbox = Sandbox.getInstance();

        int gridSize = Sandbox.getInstance().getGridSize();

        if (isShiftPressed()) {
            // check if we have a direction vector
            if(directionVector == null) {
                directionVector = new Vector2();
                if(Math.abs(x - dragMouseStartPosition.x) >= Math.abs(y - dragMouseStartPosition.y)) {
                    directionVector.x = 1;
                    directionVector.y = 0;
                } else {
                    directionVector.x = 0;
                    directionVector.y = 1;
                }
            }
        } else {
            directionVector = null;
        }

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            sandbox.dirty = true;

            float newX;
            float newY;

            newX = MathUtils.floor(x / gridSize) * gridSize;
            newY = MathUtils.floor(y / gridSize) * gridSize;

            if (isShiftPressed()) {
                if(directionVector.x == 0) {
                    newX = dragMouseStartPosition.x;
                }
                if(directionVector.y == 0) {
                    newY = dragMouseStartPosition.y;
                }
            }

            // Selection rectangles should move and follow along
            for (Entity itemInstance : sandbox.getSelector().getCurrentSelection()) {
            	transformComponent = ComponentRetriever.get(itemInstance, TransformComponent.class);


                Vector2 diff = new Vector2(dragTouchDiff.get(itemInstance));
                diff.x = MathUtils.floor(diff.x / gridSize) * gridSize;
                diff.y = MathUtils.floor(diff.y/ gridSize) * gridSize;
              
                transformComponent.x = (newX - diff.x);
                transformComponent.y = (newY - diff.y);
                //value.hide();

                // pining UI to update current item properties tools
                Overlap2DFacade.getInstance().sendNotification(Overlap2D.ITEM_DATA_UPDATED, itemInstance);
            }
        }

    }


    @Override
    public void itemMouseUp(Entity entity, float x, float y) {
        sandbox = Sandbox.getInstance();
        Overlap2DFacade facade = Overlap2DFacade.getInstance();

        if (currentTouchedItemWasSelected && !isDragging) {
            // item was selected (and no dragging was performed), so we need to release it
            if (isShiftPressed()) {
                Set<Entity> items = new HashSet<>();
                items.add(entity);
                facade.sendNotification(Sandbox.ACTION_RELEASE_SELECTION, items);
            }
        }

        // re-show all selection rectangles as clicking/dragging is finished
        // TODO: this has to be done via notification
        //for (SelectionRectangle value : sandbox.getSelector().getCurrentSelection().values()) {
        //   value.show();
        //}

        if (sandbox.dirty) {
            sandbox.saveSceneCurrentSceneData();
        }

        sandbox.dirty = false;

        // sets item position, and puts things into undo-redo que
        Array<Object[]> payloads = new Array<>();
        for (Entity itemInstance : sandbox.getSelector().getCurrentSelection()) {
            transformComponent = ComponentRetriever.get(itemInstance, TransformComponent.class);
            Vector2 newPosition = new Vector2(transformComponent.x, transformComponent.y);
            Vector2 oldPosition = dragStartPositions.get(itemInstance);

            Object payload[] = new Object[3];
            payload[0] = itemInstance; payload[1] = newPosition; payload[2] = oldPosition;
            payloads.add(payload);
        }

        Overlap2DFacade.getInstance().sendNotification(Sandbox.ACTION_ITEMS_MOVE_TO, payloads);

    }

    @Override
    public void itemMouseDoubleClick(Entity item, float x, float y) {
        Overlap2DFacade.getInstance().sendNotification(Sandbox.ACTION_EDIT_COMPOSITE);
    }

    private boolean isShiftPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)
                || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT);
    }


    private void selectionComplete() {
        sandbox = Sandbox.getInstance();
       
        Overlap2DFacade facade = Overlap2DFacade.getInstance();

        SnapshotArray<Entity> freeItems = sandbox.getSelector().getAllFreeItems();

        // when touch is up, selection process stops, and if any items got "caught" in they should be selected.

        // hiding selection rectangle
        sandbox.selectionRec.setOpacity(0.0f);
        //ArrayList<Entity> curr = new ArrayList<Entity>();
        Set<Entity> curr = new HashSet<>();
        Rectangle sR = sandbox.selectionRec.getRect();
         
        for (int i = 0; i < freeItems.size; i++) {
            Entity entity = freeItems.get(i);
            transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
            dimensionsComponent = ComponentRetriever.get(entity, DimensionsComponent.class);
            //TODO fix layer lock thing
            //if (!freeItems.get(i).isLockedByLayer() && Intersector.overlaps(sR, new Rectangle(entity.getX(), entity.getY(), entity.getWidth(), entity.getHeight()))) {
            if (Intersector.overlaps(sR, new Rectangle(transformComponent.x, transformComponent.y, dimensionsComponent.width, dimensionsComponent.height))) {
                curr.add(freeItems.get(i));
            }
        }

        if (curr.size() == 0) {
            facade.sendNotification(Overlap2D.EMPTY_SPACE_CLICKED);

            //remove visual selection command
            facade.sendNotification(Sandbox.ACTION_SET_SELECTION, null);
            return;
        }
        
        facade.sendNotification(Sandbox.ACTION_SET_SELECTION, curr);
    }

}
