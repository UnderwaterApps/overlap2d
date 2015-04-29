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

package com.uwsoft.editor.mvc.view.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.puremvc.patterns.mediator.SimpleMediator;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.Overlap2D;
import com.uwsoft.editor.controlles.flow.FlowActionEnum;
import com.uwsoft.editor.gdx.actors.SelectionRectangle;
import com.uwsoft.editor.gdx.sandbox.EditingMode;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.SceneDataManager;
import com.uwsoft.editor.mvc.view.ui.box.UIToolBoxMediator;
import com.uwsoft.editor.mvc.view.ui.box.tools.TextToolSettings;

import java.awt.*;

/**
 * Created by sargis on 4/20/15.
 */
public class SandboxStageMediator extends SimpleMediator<SandboxStage> {
    private static final String TAG = SandboxStageMediator.class.getCanonicalName();
    public static final String NAME = TAG;
    private final Vector2 reducedMoveDirection = new Vector2(0, 0);
    private EventListener eventListener;
    private boolean reducedMouseMoveEnabled = false;
    private float lastX = 0;
    private float lastY = 0;

    public SandboxStageMediator() {
        super(NAME, new SandboxStage());
    }

    @Override
    public void onRegister() {
        super.onRegister();
        eventListener = new SandboxStageEventListener();
        facade = Overlap2DFacade.getInstance();
    }

    @Override
    public String[] listNotificationInterests() {
        return new String[]{
                SceneDataManager.SCENE_LOADED
        };
    }

    @Override
    public void handleNotification(Notification notification) {
        super.handleNotification(notification);
        switch (notification.getName()) {
            case SceneDataManager.SCENE_LOADED:
                handleSceneLoaded(notification);
                break;
            default:
                break;
        }
    }

    private void handleSceneLoaded(Notification notification) {
        viewComponent.addListener(eventListener);
    }

    private class SandboxStageEventListener extends ClickListener {
        public SandboxStageEventListener() {
            setTapCountInterval(.5f);
        }

        @Override
        public boolean keyDown(InputEvent event, int keycode) {
            boolean isControlPressed = isControlPressed();
            Sandbox sandbox = Sandbox.getInstance();
            // the amount of pixels by which to move item if moving
            float deltaMove = 1;

            // if control is pressed then z index is getting modified
            // TODO: key pressed 0 for unckown, should be removed?
            // TODO: need to make sure OSX Command button works too.

            if (isShiftKey(keycode)) {
                reducedMouseMoveEnabled = true;
                System.out.println("pressed");
            }

            // Control pressed as well
            if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(0) || Gdx.input.isKeyPressed(Input.Keys.SYM)) {
                if (keycode == Input.Keys.UP) {
                    // going to front of next item in z-index ladder
                    sandbox.itemControl.itemZIndexChange(sandbox.getSelector().getCurrentSelection(), true);
                }
                if (keycode == Input.Keys.DOWN) {
                    // going behind the next item in z-index ladder
                    sandbox.itemControl.itemZIndexChange(sandbox.getSelector().getCurrentSelection(), false);
                }
                if (keycode == Input.Keys.A) {
                    // Ctrl+A means select all
                    sandbox.getSelector().selectAllItems();
                }
                // Aligning Selections
                if (keycode == Input.Keys.NUM_1 && Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
                    sandbox.getSelector().alignSelections(Align.top);
                }
                if (keycode == Input.Keys.NUM_2 && Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
                    sandbox.getSelector().alignSelections(Align.left);
                }
                if (keycode == Input.Keys.NUM_3 && Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
                    sandbox.getSelector().alignSelections(Align.bottom);
                }
                if (keycode == Input.Keys.NUM_4 && Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
                    sandbox.getSelector().alignSelections(Align.right);
                }
                if (keycode == Input.Keys.NUM_0 || keycode == Input.Keys.NUMPAD_0) {
                    sandbox.setZoomPercent(100);
                    facade.sendNotification(Overlap2D.ZOOM_CHANGED);
                }
                if (keycode == Input.Keys.X) {
                    facade.sendNotification(Sandbox.ACTION_CUT);
                }
                if (keycode == Input.Keys.C) {
                    facade.sendNotification(Sandbox.ACTION_COPY);
                }
                if (keycode == Input.Keys.V) {
                    facade.sendNotification(Sandbox.ACTION_PASTE);
                }
                if (keycode == Input.Keys.Z) {
                    sandbox.getUac().undo();
                }
                if (keycode == Input.Keys.Y) {
                    sandbox.getUac().redo();
                }

                return true;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                // if shift is pressed, move boxes by 20 pixels instead of one
                deltaMove = 20; //pixels
            }

            if (sandbox.getGridSize() > 1) {
                deltaMove = sandbox.getGridSize();
                if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                    // if shift is pressed, move boxes 3 times more then the grid size
                    deltaMove *= 3;
                }
            }

            if (keycode == Input.Keys.UP) {
                // moving UP
                sandbox.getSelector().moveSelectedItemsBy(0, deltaMove);
            }
            if (keycode == Input.Keys.DOWN) {
                // moving down
                sandbox.getSelector().moveSelectedItemsBy(0, -deltaMove);
            }
            if (keycode == Input.Keys.LEFT) {
                // moving left
                sandbox.getSelector().moveSelectedItemsBy(-deltaMove, 0);
            }
            if (keycode == Input.Keys.RIGHT) {
                //moving right
                sandbox.getSelector().moveSelectedItemsBy(deltaMove, 0);
            }

            // Delete
            if (keycode == Input.Keys.DEL || keycode == Input.Keys.FORWARD_DEL) {
                facade.sendNotification(Sandbox.ACTION_DELETE);
            }

            // if space is pressed, that means we are going to pan, so set cursor accordingly
            // TODO: this pan is kinda different from what happens when you press middle button, so things need to merge right
            if (keycode == Input.Keys.SPACE && !sandbox.isItemTouched && !sandbox.isUsingSelectionTool) {
                sandbox.getSandboxStage().setCursor(Cursor.HAND_CURSOR);
                sandbox.cameraPanOn = true;
            }

            // Zoom
            if (keycode == Input.Keys.MINUS && isControlPressed) {
                sandbox.zoomDevideBy(2f);
            }
            if (keycode == Input.Keys.EQUALS && isControlPressed) {
                sandbox.zoomDevideBy(0.5f);
            }

            return true;
        }

        @Override
        public boolean keyUp(InputEvent event, int keycode) {
            Sandbox sandbox = Sandbox.getInstance();
            if (keycode == Input.Keys.DEL) {
                // delete selected item
                sandbox.getSelector().removeCurrentSelectedItems();
            }
            if (keycode == Input.Keys.SPACE) {
                // if pan mode is disabled set cursor back
                // TODO: this should go to sandbox as well
                sandbox.getSandboxStage().setCursor(Cursor.DEFAULT_CURSOR);
                sandbox.cameraPanOn = false;
            }
            if (!isShiftPressed()) {
                reducedMoveDirection.setZero();
                reducedMouseMoveEnabled = false;
            }
            return true;
        }


        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            super.touchDown(event, x, y, pointer, button);
            Sandbox sandbox = Sandbox.getInstance();
            lastX = Gdx.input.getX();
            lastY = Gdx.input.getY();
            // setting key and scroll focus on main area
            sandbox.getUIStage().setKeyboardFocus();
            sandbox.getUIStage().setScrollFocus(sandbox.getSandboxStage().mainBox);
            sandbox.getSandboxStage().setKeyboardFocus();

            // if there was a drop down remove it
            // TODO: this is job for front UI to figure out
            sandbox.getUIStage().mainDropDown.hide();

            switch (button) {
                case Input.Buttons.MIDDLE:
                    // if middle button is pressed - PAN the scene
                    sandbox.enablePan();
                    break;
                case Input.Buttons.LEFT:
                    boolean setOpacity = false;

                    //TODO: Anyone can explain what was the purpose of this?
                    if (!Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                        setOpacity = true;
                    }

                    // preparing selection tool rectangle to follow mouse
                    sandbox.prepareSelectionRectangle(x, y, setOpacity);

                    break;
            }
            return !sandbox.isItemTouched;
        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            super.touchUp(event, x, y, pointer, button);
            Sandbox sandbox = Sandbox.getInstance();
            if (button == Input.Buttons.RIGHT) {
                // if clicked on empty space, selections need to be cleared
                sandbox.getSelector().clearSelections();

                // show default dropdown
                facade.sendNotification(Overlap2D.SCENE_RIGHT_CLICK, new Vector2(event.getStageX(), event.getStageY()));

                return;
            }

            // Basically if panning but space is not pressed, stop panning.? o_O
            // TODO: seriously this needs to be figured out, I am not sure we need it
            if (sandbox.cameraPanOn) {
                sandbox.cameraPanOn = Gdx.input.isKeyPressed(Input.Keys.SPACE);
                return;
            }

            // selection is complete, this will check for what get caught in selection rect, and select 'em
            sandbox.selectionComplete();
            if (getTapCount() == 2 && button == Input.Buttons.LEFT) {
                doubleClick(event, x, y);
            }

            if(sandbox.editingMode == EditingMode.TEXT) {
                UIToolBoxMediator toolBox = facade.retrieveMediator(UIToolBoxMediator.NAME);
                TextToolSettings textToolSettings = (TextToolSettings) toolBox.getCurrentSelectedToolSettings();

                sandbox.getItemFactory().createLabel(textToolSettings, Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());

                return;
            }
        }

        private void doubleClick(InputEvent event, float x, float y) {
            Sandbox sandbox = Sandbox.getInstance();
            // if empty space is double clicked, then get back into previous composite
            // TODO: do not do if we are on root item ( this is somehow impossible to implement o_O )
            sandbox.enterIntoPrevComposite();
            sandbox.flow.setPendingHistory(sandbox.getCurrentScene().getDataVO(), FlowActionEnum.GET_OUT_COMPOSITE);
            sandbox.flow.applyPendingAction();
        }

        @Override
        public void touchDragged(InputEvent event, float x, float y, int pointer) {
            Sandbox sandbox = Sandbox.getInstance();
            // if resizing is in progress we are not dealing with this
            if (sandbox.isResizing) return;

            if (sandbox.cameraPanOn) {
                // if panning, then just move camera
                OrthographicCamera camera = (OrthographicCamera) (sandbox.getSandboxStage().getCamera());

                float currX = camera.position.x + (lastX - Gdx.input.getX()) * camera.zoom;
                float currY = camera.position.y + (Gdx.input.getY() - lastY) * camera.zoom;

                sandbox.getSandboxStage().getCamera().position.set(currX, currY, 0);

                lastX = Gdx.input.getX();
                lastY = Gdx.input.getY();
            } else {
                // else - using selection tool
                sandbox.isUsingSelectionTool = true;
                sandbox.getSandboxStage().selectionRec.setWidth(x - sandbox.getSandboxStage().selectionRec.getX());
                sandbox.getSandboxStage().selectionRec.setHeight(y - sandbox.getSandboxStage().selectionRec.getY());
            }
        }


        @Override
        public boolean scrolled(InputEvent event, float x, float y, int amount) {
            Sandbox sandbox = Sandbox.getInstance();
            // well, duh
            if (amount == 0) return false;

            // if item is currently being held with mouse (touched in but not touched out)
            // mouse scroll should rotate the selection around it's origin
            if (sandbox.isItemTouched) {
                for (SelectionRectangle value : sandbox.getSelector().getCurrentSelection().values()) {
                    float degreeAmount = 1;
                    if (amount < 0) degreeAmount = -1;
                    // And if shift is pressed, the rotation amount is bigger
                    if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                        degreeAmount = degreeAmount * 30;
                    }
                    value.getHostAsActor().rotateBy(degreeAmount);
                    value.update();
                }
                facade.sendNotification(Overlap2D.ITEM_DATA_UPDATED);
                sandbox.dirty = true;
            } else if (Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT)) {
                // if not item is touched then we can use this for zoom
                sandbox.zoomBy(amount);
            }
            return false;
        }

        private boolean isControlPressed() {
            return Gdx.input.isKeyPressed(Input.Keys.SYM)
                    || Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)
                    || Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT);
        }

        private boolean isShiftKey(int keycode) {
            return keycode == Input.Keys.SHIFT_LEFT
                    || keycode == Input.Keys.SHIFT_RIGHT;
        }

        private boolean isShiftPressed() {
            return Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)
                    || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT);
        }
    }
}
