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

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.uwsoft.editor.Overlap2D;
import com.uwsoft.editor.gdx.actors.SelectionRectangle;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.CursorManager;

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
    private Vector2 dragStartPosition;

    public SelectionTool() {

    }

    @Override
    public void initTool() {
        sandbox = Sandbox.getInstance();
        HashMap<Entity, SelectionRectangle> currSelection = sandbox.getSelector().getCurrentSelection();
        for (SelectionRectangle value : currSelection.values()) {
            value.setMode(false);
        }

        // set cursor
        CursorManager cursorManager = Overlap2DFacade.getInstance().retrieveProxy(CursorManager.NAME);
        cursorManager.setCursor(CursorManager.NORMAL);
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
      //TODO fix and uncomment
//        sandbox.getSandboxStage().selectionRec.setWidth(x - sandbox.getSandboxStage().selectionRec.getX());
//        sandbox.getSandboxStage().selectionRec.setHeight(y - sandbox.getSandboxStage().selectionRec.getY());
    }

    @Override
    public void stageMouseDoubleClick(float x, float y) {
        Overlap2DFacade.getInstance().sendNotification(Sandbox.ACTION_COMPOSITE_HIERARCHY_UP);
    }

    @Override
    public boolean itemMouseDown(Entity item, float x, float y) {
        sandbox = Sandbox.getInstance();
        Overlap2DFacade facade = Overlap2DFacade.getInstance();

      //TODO fix and uncomment
//        item.updateDataVO();
//
//        currentTouchedItemWasSelected = sandbox.getSelector().getCurrentSelection().get(item) != null;
//
//        // if shift is pressed we are in add/remove selection mode
//        if (isShiftPressed()) {
//            //TODO block selection handling (wat?)
//            if (!currentTouchedItemWasSelected) {
//                // item was not selected, adding it to selection
//                ArrayList<Entity> items = new ArrayList<>();
//                items.add(item);
//                facade.sendNotification(Sandbox.ACTION_ADD_SELECTION, items);
//            }
//        } else {
//
//            if (item.isLockedByLayer()) {
//                // this is considered empty space click and thus should release all selections
//                facade.sendNotification(Sandbox.ACTION_SET_SELECTION, null);
//                sandbox.getSelector().clearSelections();
//                return false;
//            } else {
//                // select this item and remove others from selection
//                ArrayList<Entity> items = new ArrayList<>();
//                items.add(item);
//                facade.sendNotification(Sandbox.ACTION_SET_SELECTION, items);
//            }
//        }
//
//        // remembering local touch position for each of selected boxes, if planning to drag
//        for (SelectionRectangle value : sandbox.getSelector().getCurrentSelection().values()) {
//            value.setTouchDiff(x - value.getHostAsActor().getX(), y - value.getHostAsActor().getY());
//        }
//
//        dragStartPosition = new Vector2(x, y);
//
//        // pining UI to update current item properties tools
//        Overlap2DFacade.getInstance().sendNotification(Overlap2D.ITEM_DATA_UPDATED);

        return true;
    }

    @Override
    public void itemMouseDragged(Entity item, float x, float y) {
        sandbox = Sandbox.getInstance();

        int gridSize = Sandbox.getInstance().getGridSize();

        if (isShiftPressed()) {
            // check if we have a direction vector
            if(directionVector == null) {
                directionVector = new Vector2();
                if(Math.abs(x - dragStartPosition.x) >= Math.abs(y - dragStartPosition.y)) {
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
                    newX = dragStartPosition.x;
                }
                if(directionVector.y == 0) {
                    newY = dragStartPosition.y;
                }
            }

            // Selection rectangles should move and follow along
            for (SelectionRectangle value : sandbox.getSelector().getCurrentSelection().values()) {
                float[] diff = value.getTouchDiff();

                diff[0] = MathUtils.floor(diff[0] / gridSize) * gridSize;
                diff[1] = MathUtils.floor(diff[1] / gridSize) * gridSize;
              //TODO fix and uncomment
//                value.getHostAsActor().setX(newX - diff[0]);
//                value.getHostAsActor().setY(newY - diff[1]);
                value.hide();
            }
        }

        // pining UI to update current item properties tools
        Overlap2DFacade.getInstance().sendNotification(Overlap2D.ITEM_DATA_UPDATED);
    }


    @Override
    public void itemMouseUp(Entity item, float x, float y) {
        sandbox = Sandbox.getInstance();
        Overlap2DFacade facade = Overlap2DFacade.getInstance();

        if (currentTouchedItemWasSelected && !isDragging) {
            // item was selected (and no dragging was performed), so we need to release it
            if (isShiftPressed()) {
                ArrayList<Entity> items = new ArrayList<>();
                items.add(item);
                facade.sendNotification(Sandbox.ACTION_RELEASE_SELECTION, items);
            }
        }

        sandbox.getSelector().flushAllSelectedItems();

        // re-show all selection rectangles as clicking/dragging is finished
        for (SelectionRectangle value : sandbox.getSelector().getCurrentSelection().values()) {
            value.show();
        }

        if (sandbox.dirty) {
            sandbox.saveSceneCurrentSceneData();
        }

        sandbox.dirty = false;

        // pining UI to update current item properties tools
        Overlap2DFacade.getInstance().sendNotification(Overlap2D.ITEM_DATA_UPDATED);
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
      //TODO fix and uncomment
//        SandboxStage sandboxStage = sandbox.getSandboxStage();
//        Overlap2DFacade facade = Overlap2DFacade.getInstance();
//
//        ArrayList<IBaseItem> freeItems = sandbox.getSelector().getAllFreeItems();
//
//        // when touch is up, selection process stops, and if any items got "caught" in they should be selected.
//
//        // hiding selection rectangle
//        sandboxStage.selectionRec.setOpacity(0.0f);
//        ArrayList<IBaseItem> curr = new ArrayList<IBaseItem>();
//        Rectangle sR = sandboxStage.selectionRec.getRect();
//        for (int i = 0; i < freeItems.size(); i++) {
//            Actor asActor = (Actor) freeItems.get(i);
//            if (!freeItems.get(i).isLockedByLayer() && Intersector
//                    .overlaps(sR, new Rectangle(asActor.getX(), asActor.getY(), asActor.getWidth(), asActor.getHeight()))) {
//                curr.add(freeItems.get(i));
//            }
//        }
//
//        facade.sendNotification(Sandbox.ACTION_SET_SELECTION, curr);
//
//        if (curr.size() == 0) {
//            facade.sendNotification(Overlap2D.EMPTY_SPACE_CLICKED);
//        }
    }
}
