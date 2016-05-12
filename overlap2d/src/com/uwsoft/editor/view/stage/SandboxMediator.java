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

package com.uwsoft.editor.view.stage;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.SnapshotArray;
import com.commons.MsgAPI;
import com.commons.view.tools.Tool;
import com.puremvc.patterns.mediator.SimpleMediator;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.controller.commands.AddComponentToItemCommand;
import com.uwsoft.editor.controller.commands.CompositeCameraChangeCommand;
import com.uwsoft.editor.controller.commands.RemoveComponentFromItemCommand;
import com.uwsoft.editor.proxy.CommandManager;
import com.uwsoft.editor.renderer.components.NodeComponent;
import com.uwsoft.editor.renderer.components.ViewPortComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.view.stage.input.EntityClickListener;
import com.uwsoft.editor.view.stage.input.InputListenerComponent;
import com.uwsoft.editor.view.stage.tools.*;
import com.uwsoft.editor.view.ui.box.UIToolBoxMediator;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static com.uwsoft.editor.view.ui.box.UIToolBox.TOOL_CLICKED;

/**
 * Created by sargis on 4/20/15.
 */
public class SandboxMediator extends SimpleMediator<Sandbox> {
    private static final String TAG = SandboxMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    private static final String PREFIX =  "com.uwsoft.editor.view.stage.SandboxStageMediator";
    public static final String SANDBOX_TOOL_CHANGED = PREFIX + ".SANDBOX_TOOL_CHANGED";

    private final Vector2 reducedMoveDirection = new Vector2(0, 0);

    private SandboxStageEventListener stageListener;

    private Tool hotSwapMemory;

    private HashMap<String, Tool> sandboxTools = new HashMap<>();
    private Tool currentSelectedTool;

    public SandboxMediator() {
        super(NAME, Sandbox.getInstance());
    }

    @Override
    public void onRegister() {
        super.onRegister();

        facade = Overlap2DFacade.getInstance();

        stageListener = new SandboxStageEventListener();
        getViewComponent().addListener(stageListener);

        initTools();
    }

    private void initTools() {
        sandboxTools.put(SelectionTool.NAME, new SelectionTool());
        sandboxTools.put(TransformTool.NAME, new TransformTool());
        sandboxTools.put(TextTool.NAME, new TextTool());
        sandboxTools.put(PointLightTool.NAME, new PointLightTool());
        sandboxTools.put(ConeLightTool.NAME, new ConeLightTool());
        sandboxTools.put(PanTool.NAME, new PanTool());
        sandboxTools.put(PolygonTool.NAME, new PolygonTool());

    }

    private void setCurrentTool(String toolName) {
        currentSelectedTool = sandboxTools.get(toolName);

        if (currentSelectedTool != null) {
            facade.sendNotification(SANDBOX_TOOL_CHANGED, currentSelectedTool);
            currentSelectedTool.initTool();
        }
    }

    @Override
    public String[] listNotificationInterests() {
        return new String[]{
                MsgAPI.SCENE_LOADED,
                MsgAPI.TOOL_SELECTED,
                MsgAPI.NEW_ITEM_ADDED,
                MsgAPI.NEW_TOOL_ADDED,
                CompositeCameraChangeCommand.DONE,
                AddComponentToItemCommand.DONE,
                RemoveComponentFromItemCommand.DONE,
                MsgAPI.ITEM_SELECTION_CHANGED
        };
    }

    @Override
    public void handleNotification(Notification notification) {
        super.handleNotification(notification);
        switch (notification.getName()) {
            case MsgAPI.SCENE_LOADED:
                handleSceneLoaded(notification);
                break;
            case MsgAPI.TOOL_SELECTED:
                setCurrentTool(notification.getBody());
                break;
            case MsgAPI.NEW_ITEM_ADDED:
                addListenerToItem(notification.getBody());
                break;
            case MsgAPI.NEW_TOOL_ADDED:
                addSandboxTool(notification.getBody());
                break;
            case CompositeCameraChangeCommand.DONE:
                initItemListeners();
                break;
            default:
                break;
        }
        if(currentSelectedTool != null) {
            currentSelectedTool.handleNotification(notification);
        }
    }

    private void addSandboxTool(Map.Entry<String, Tool> newTool) {
        sandboxTools.put(newTool.getKey(), newTool.getValue());
    }

    private void handleSceneLoaded(Notification notification) {
		//TODO fix and uncomment
        //viewComponent.addListener(stageListener);

        initItemListeners();

        setCurrentTool(SelectionTool.NAME);

        Sandbox.getInstance().getCamera().position.set(new Vector2(0, 0), 0);
        Overlap2DFacade.getInstance().sendNotification(PanTool.SCENE_PANNED);
    }

    private void initItemListeners() {
        Engine engine = getViewComponent().getEngine();
        Family rootFamily = Family.all(ViewPortComponent.class).get();
        Entity rootEntity = engine.getEntitiesFor(rootFamily).iterator().next();
        NodeComponent nodeComponent = ComponentRetriever.get(rootEntity, NodeComponent.class);
        SnapshotArray<Entity> childrenEntities = nodeComponent.children;

        for (Entity child: childrenEntities) {
            addListenerToItem(child);
        }
    }

    /**
     * TODO: this can be changed, as in ideal world entity factory should be adding listener component to ALL entities,
     * problem is currently this component is not part of runtime. but it will be.
     *
     * @param entity
     */
    private void addListenerToItem(Entity entity) {
        InputListenerComponent inputListenerComponent = entity.getComponent(InputListenerComponent.class);
        if(inputListenerComponent == null){
            inputListenerComponent = new InputListenerComponent();
            entity.add(inputListenerComponent);
        }
        inputListenerComponent.removeAllListener();
        inputListenerComponent.addListener(new SandboxItemEventListener(entity));
    }

    public Vector2 getStageCoordinates() {
        // TODO: remove this shit
        Engine engine = getViewComponent().getEngine();
        Family rootFamily = Family.all(ViewPortComponent.class).get();
        Entity rootEntity = engine.getEntitiesFor(rootFamily).iterator().next();

        ViewPortComponent viewPortComponent = ComponentRetriever.get(rootEntity, ViewPortComponent.class);
        Vector2 vec = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        viewPortComponent.viewPort.unproject(vec);

        return vec;
    }

    public class SandboxItemEventListener extends EntityClickListener {

        public SandboxItemEventListener(final Entity entity) {
        	
        }

        @Override
        public boolean touchDown(Entity entity, float x, float y, int pointer, int button) {
            super.touchDown(entity, x, y, pointer, button);

            switch (button) {
                case Input.Buttons.MIDDLE:
                    // if middle button is pressed - PAN the scene
                    toolHotSwap(sandboxTools.get(PanTool.NAME));
                    break;
            }

            Vector2 coords = getStageCoordinates();
            return currentSelectedTool != null && currentSelectedTool.itemMouseDown(entity, coords.x, coords.y);
        }

        
        @Override
        public void touchUp(Entity entity, float x, float y, int pointer, int button) {
            super.touchUp(entity, x, y, pointer, button);
            Vector2 coords = getStageCoordinates();

            if (button == Input.Buttons.MIDDLE) {
                toolHotSwapBack();
            }

            if (currentSelectedTool != null) {
                currentSelectedTool.itemMouseUp(entity, x, y);

                if (getTapCount() == 2) {
                    // this is double click
                    currentSelectedTool.itemMouseDoubleClick(entity, coords.x, coords.y);
                }
            }

            if (button == Input.Buttons.RIGHT) {
                // if right clicked on an item, drop down for current selection
                Overlap2DFacade.getInstance().sendNotification(MsgAPI.ITEM_RIGHT_CLICK);
            }
        }

        @Override
        public void touchDragged(Entity entity, float x, float y, int pointer) {
            Vector2 coords = getStageCoordinates();

            if (currentSelectedTool != null) {
                currentSelectedTool.itemMouseDragged(entity, coords.x, coords.y);
            }
        }

    }

    private class SandboxStageEventListener extends EntityClickListener {
        public SandboxStageEventListener() {
            setTapCountInterval(.5f);
        }

        @Override
        public boolean keyDown(Entity entity, int keycode) {
            boolean isControlPressed = isControlPressed();
            Sandbox sandbox = Sandbox.getInstance();

            // if control is pressed then z index is getting modified
            // TODO: key pressed 0 for unckown, should be removed?
            // TODO: need to make sure OSX Command button works too.

            if(currentSelectedTool != null) {
                currentSelectedTool.keyDown(entity, keycode);
            }

            // Control pressed as well
            if (isControlPressed()) {
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
                    facade.sendNotification(MsgAPI.ACTION_SET_SELECTION, sandbox.getSelector().getAllFreeItems());
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
                    sandbox.getCamera().position.set(0 ,0, 0);
                    facade.sendNotification(MsgAPI.ZOOM_CHANGED);
                }
                if (keycode == Input.Keys.X) {
                    facade.sendNotification(MsgAPI.ACTION_CUT);
                }
                if (keycode == Input.Keys.C) {
                    facade.sendNotification(MsgAPI.ACTION_COPY);
                }
                if (keycode == Input.Keys.V) {
                    facade.sendNotification(MsgAPI.ACTION_PASTE);
                }
                if (keycode == Input.Keys.T) {
                    facade.sendNotification(TOOL_CLICKED, TransformTool.NAME);
                    UIToolBoxMediator toolBoxMediator = facade.retrieveMediator(UIToolBoxMediator.NAME);
                    toolBoxMediator.setCurrentTool(TransformTool.NAME);
                }
                if(keycode == Input.Keys.Z) {
                    if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                        CommandManager commandManager = facade.retrieveProxy(CommandManager.NAME);
                        commandManager.redoCommand();
                    } else {
                        CommandManager commandManager = facade.retrieveProxy(CommandManager.NAME);
                        commandManager.undoCommand();
                    }
                }
            }

            if (keycode == Input.Keys.V) {
                facade.sendNotification(TOOL_CLICKED, SelectionTool.NAME);
                UIToolBoxMediator toolBoxMediator = facade.retrieveMediator(UIToolBoxMediator.NAME);
                toolBoxMediator.setCurrentTool(SelectionTool.NAME);
            }

            if (Gdx.input.isKeyPressed(Input.Keys.S) && !isControlPressed()) {
                setCurrentTool(SelectionTool.NAME);
                UIToolBoxMediator toolBoxMediator = facade.retrieveMediator(UIToolBoxMediator.NAME);
                toolBoxMediator.setCurrentTool(SelectionTool.NAME);
            }

            // if space is pressed, that means we are going to pan, so set cursor accordingly
            // TODO: this pan is kinda different from what happens when you press middle button, so things need to merge right
            if (keycode == Input.Keys.SPACE) {
                sandbox.setCursor(Cursor.HAND_CURSOR);
                toolHotSwap(sandboxTools.get(PanTool.NAME));
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
        public boolean keyUp(Entity entity, int keycode) {
            Sandbox sandbox = Sandbox.getInstance();
            if (keycode == Input.Keys.DEL) {
                // delete selected item
                sandbox.getSelector().removeCurrentSelectedItems();
            }
            if (keycode == Input.Keys.SPACE) {
                // if pan mode is disabled set cursor back
                sandbox.setCursor(Cursor.DEFAULT_CURSOR);
                toolHotSwapBack();
            }

            if(currentSelectedTool != null) {
                currentSelectedTool.keyUp(entity, keycode);
            }

            return true;
        }


        @Override
        public boolean touchDown(Entity entity, float x, float y, int pointer, int button) {
            super.touchDown(entity, x, y, pointer, button);

            Sandbox sandbox = Sandbox.getInstance();

            // setting key and scroll focus on main area
            sandbox.getUIStage().setKeyboardFocus();
            sandbox.getUIStage().setScrollFocus(sandbox.getUIStage().midUI);
            sandbox.setKeyboardFocus();

            // if there was a drop down remove it
            // TODO: this is job for front UI to figure out
            //commands.getUIStage().mainDropDown.hide();

            switch (button) {
                case Input.Buttons.MIDDLE:
                    // if middle button is pressed - PAN the scene
                    toolHotSwap(sandboxTools.get(PanTool.NAME));
                    break;
            }

            if (currentSelectedTool != null) {
                currentSelectedTool.stageMouseDown(x, y);
            }

            return true;
        }

        @Override
        public void touchUp(Entity entity, float x, float y, int pointer, int button) {
            super.touchUp(entity, x, y, pointer, button);

            if(currentSelectedTool != null) {
                currentSelectedTool.stageMouseUp(x, y);
            }

            Sandbox sandbox = Sandbox.getInstance();
            if (button == Input.Buttons.RIGHT) {
                // if clicked on empty space, selections need to be cleared
                sandbox.getSelector().clearSelections();

                // show default dropdown
                facade.sendNotification(MsgAPI.SCENE_RIGHT_CLICK, new Vector2(x, y));

                return;
            }

            if (button == Input.Buttons.MIDDLE) {
                toolHotSwapBack();
            }

            if (getTapCount() == 2 && button == Input.Buttons.LEFT) {
                doubleClick(entity, x, y);
            }

        }

        private void doubleClick(Entity entity, float x, float y) {
            if (currentSelectedTool != null) {
                Sandbox sandbox = Sandbox.getInstance();
                currentSelectedTool.stageMouseDoubleClick(x, y);
            }
        }

        @Override
        public void touchDragged(Entity entity, float x, float y, int pointer) {
            if (currentSelectedTool != null) {
                Sandbox sandbox = Sandbox.getInstance();
                currentSelectedTool.stageMouseDragged(x, y);
            }
        }


        @Override
        public boolean scrolled(Entity entity, int amount) {
            Sandbox sandbox = Sandbox.getInstance();
            // well, duh
            if (amount == 0) return false;

            // Control pressed as well
            if (isControlPressed()) {
                float zoomPercent = sandbox.getZoomPercent();
                zoomPercent-=amount*4f;
                if(zoomPercent < 5 ) zoomPercent = 5;
                sandbox.setZoomPercent(zoomPercent);

                facade.sendNotification(MsgAPI.ZOOM_CHANGED);
            }
            // if item is currently being held with mouse (touched in but not touched out)
            // mouse scroll should rotate the selection around it's origin
            /*
            if (commands.isItemTouched) {
                for (SelectionRectangle value : commands.getSelector().getCurrentSelection().values()) {
                    float degreeAmount = 1;
                    if (amount < 0) degreeAmount = -1;
                    // And if shift is pressed, the rotation amount is bigger
                    if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                        degreeAmount = degreeAmount * 30;
                    }
                    value.getHostAsActor().rotateBy(degreeAmount);
                    value.update();
                }
                facade.sendNotification(MsgAPI.ITEM_DATA_UPDATED);
                commands.dirty = true;
            } else if (Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT)) {
                // if not item is touched then we can use this for zoom
                commands.zoomBy(amount);
            }
            */

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

    public void toolHotSwap(Tool tool) {
        hotSwapMemory = currentSelectedTool;
        currentSelectedTool = tool;
    }

    public void toolHotSwapBack() {
        currentSelectedTool = hotSwapMemory;
        hotSwapMemory = null;
    }

    public String getCurrentSelectedToolName() {
        return currentSelectedTool != null ? currentSelectedTool.getName() : "";
    }
}
