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

package com.uwsoft.editor.gdx.sandbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.uwsoft.editor.Overlap2D;
import com.uwsoft.editor.controlles.flow.FlowActionEnum;
import com.uwsoft.editor.gdx.actors.SelectionRectangle;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.view.ui.UIDropDownMenu;
import com.uwsoft.editor.renderer.actor.IBaseItem;

/**
 * Adds listeners to everything sandbox related, including
 * boxes, entire scene, mouse, keys e.g.
 * Communicates user actions/intentions to Sandbox or UAC
 *
 * @author azakhary
 */
public class SandboxInputAdapter {

    private final Vector2 dragStartPosition = new Vector2();
    private final Vector2 reducedMoveDirection = new Vector2(0, 0);
    private Sandbox sandbox;
    private boolean isDragging = false;
    private boolean currentTouchedItemWasSelected = false;
    private boolean reducedMouseMoveEnabled = false;

    private Overlap2DFacade facade;

    public SandboxInputAdapter(Sandbox sandbox) {
        this.sandbox = sandbox;

        facade = Overlap2DFacade.getInstance();
    }

    /**
     * When item is touched, managing selections and preparing to drag selection
     */
    private boolean itemTouchDown(IBaseItem item, InputEvent event, float x, float y, int button) {
        // Making sure we have fresh VO data
        item.updateDataVO();

        currentTouchedItemWasSelected = sandbox.getSelector().getCurrentSelection().get(item) != null;

        // if shift is pressed we are in add/remove selection mode
        if (isShiftPressed()) {

            //TODO block selection handling
            if (!currentTouchedItemWasSelected) {
                // item was not selected, adding it to selection
                sandbox.getSelector().setSelection(item, false);
            }
        } else {

            if (item.isLockedByLayer()) {
                // this is considered empty space click and thus should release all selections
                sandbox.getSelector().clearSelections();
            } else {
                // select this item and remove others from selection
                sandbox.getSelector().setSelection(item, true);
            }
        }

        // If currently panning do nothing regarding this item, panning will take over
        if (sandbox.cameraPanOn) {
            return false;
        }

        // remembering local touch position for each of selected boxes, if planning to drag
        for (SelectionRectangle value : sandbox.getSelector().getCurrentSelection().values()) {
            value.setTouchDiff(event.getStageX() - value.getHostAsActor().getX(), event.getStageY() - value.getHostAsActor().getY());
        }

        // remembering that item was touched
        sandbox.isItemTouched = true;

        // pining UI to update current item properties data
        facade.sendNotification(Overlap2D.ITEM_DATA_UPDATED);

        return true;
    }

    private void itemTouchUp(IBaseItem item, InputEvent event, float x, float y, int button) {


        if (currentTouchedItemWasSelected && !isDragging) {
            // item was selected (and no dragging was performed), so we need to release it
            if (isShiftPressed()) {
                sandbox.getSelector().releaseSelection(item);
            }
        }


        sandbox.getSelector().flushAllSelectedItems();


        // if panning was taking place - do nothing else. (other touch up got this)
        if (sandbox.cameraPanOn) {
            return;
        }

        if (button == Input.Buttons.RIGHT) {
            // if right clicked on an item, drop down for current selection
            facade.sendNotification(Overlap2D.ITEM_RIGHT_CLICK, new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY()));
        }

        // re-show all selection rectangles as clicking/dragging is finished
        for (SelectionRectangle value : sandbox.getSelector().getCurrentSelection().values()) {
            value.show();
        }

        if (sandbox.dirty) {
            sandbox.saveSceneCurrentSceneData();
        }
        sandbox.isItemTouched = false;
        sandbox.dirty = false;

        // pining UI to update current item properties data
        facade.sendNotification(Overlap2D.ITEM_DATA_UPDATED);
    }

    private void itemDoubleClick(IBaseItem item, InputEvent event, float x, float y, int button) {
        sandbox.enterIntoComposite();
        sandbox.flow.setPendingHistory(sandbox.getCurrentScene().getDataVO(), FlowActionEnum.GET_INTO_COMPOSITE);
        sandbox.flow.applyPendingAction();
    }

    private void itemTouchDragged(IBaseItem item, InputEvent event, float x, float y, Vector2 useReducedMoveFixPoint, boolean isFirstDragCall) {
        int gridSize = Sandbox.getInstance().getGridSize();

        // if there is no resizing going on, the item was touched,
        // the button is in and we are dragging... well you can probably be safe about saying - we do.
        if (sandbox.isItemTouched && !sandbox.isResizing && Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            sandbox.dirty = true;

            float newX;
            float newY;

            if (useReducedMoveFixPoint != null) {
                Vector2 reducedMoveDiff = useReducedMoveFixPoint.cpy().scl(-1).add(event.getStageX(), event.getStageY());
                if (isFirstDragCall) {
                    int moveHorizontallyRatio = (Math.abs(reducedMoveDiff.x) >= Math.abs(reducedMoveDiff.y)) ? 1 : 0;
                    reducedMoveDirection.set(moveHorizontallyRatio, moveHorizontallyRatio ^ 1);
                }
                newX = useReducedMoveFixPoint.x + reducedMoveDirection.x * reducedMoveDiff.x;
                newY = useReducedMoveFixPoint.y + reducedMoveDirection.y * reducedMoveDiff.y;
            } else {
                newX = MathUtils.floor(event.getStageX() / gridSize) * gridSize;
                newY = MathUtils.floor(event.getStageY() / gridSize) * gridSize;
            }


            // Selection rectangles should move and follow along
            for (SelectionRectangle value : sandbox.getSelector().getCurrentSelection().values()) {
                float[] diff = value.getTouchDiff();

                diff[0] = MathUtils.floor(diff[0] / gridSize) * gridSize;
                diff[1] = MathUtils.floor(diff[1] / gridSize) * gridSize;

                value.getHostAsActor().setX(newX - diff[0]);
                value.getHostAsActor().setY(newY - diff[1]);
                value.hide();
            }
        }

        // pining UI to update current item properties data
        facade.sendNotification(Overlap2D.ITEM_DATA_UPDATED);
    }


    private boolean isShiftPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)
                || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT);
    }

    public void initItemListeners(final IBaseItem eventItem) {
        ClickListener listener = new ClickListener() {

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);

                dragStartPosition.set(event.getStageX(), event.getStageY());
                return itemTouchDown(eventItem, event, x, y, button);
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                itemTouchUp(eventItem, event, x, y, button);

                if (getTapCount() == 2) {
                    // this is double click
                    itemDoubleClick(eventItem, event, x, y, button);
                }
                isDragging = false;
            }

            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                boolean isFirstDrag = !isDragging;
                isDragging = true;
                Vector2 useReducedMoveFixPoint = (reducedMouseMoveEnabled) ? dragStartPosition : null;
                itemTouchDragged(eventItem, event, x, y, useReducedMoveFixPoint, isFirstDrag);
            }
        };

        // tap count interval to correctly manage double clicks
        // if longer then 0.5 second it's not double click
        // listener.setTapCountInterval(0.5f);

        // finally adding this huge listener
        ((Actor) eventItem).addListener(listener);
    }
}
