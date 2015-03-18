package com.uwsoft.editor.gdx.sandbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.uwsoft.editor.controlles.flow.FlowActionEnum;
import com.uwsoft.editor.gdx.actors.SelectionRectangle;
import com.uwsoft.editor.renderer.actor.CompositeItem;
import com.uwsoft.editor.renderer.actor.IBaseItem;
import com.uwsoft.editor.renderer.actor.LabelItem;
import com.uwsoft.editor.renderer.physics.PhysicsBodyLoader;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by CyberJoe on 3/18/2015.
 */
public class InputHandler extends InputAdapter {

    public InputHandler() {

    }

    public void initItemListeners(final IBaseItem iterableItem) {
        ClickListener listener = new ClickListener() {

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                iterableItem.updateDataVO();
                if (cameraPanOn) {
                    return false;
                }

                if (currentSelection.get(iterableItem) != null && Gdx.input.isKeyPressed(59)) {
                    releaseSelection(iterableItem);
                } else {
                    if (iterableItem.isLockedByLayer()) {
                        clearSelections();
                    } else {
                        setSelection(iterableItem, !Gdx.input.isKeyPressed(59));
                    }
                }

                for (SelectionRectangle value : currentSelection.values()) {
                    value.setTouchDiff(event.getStageX() - value.getHostAsActor().getX(), event.getStageY() - value.getHostAsActor().getY());
                }
                isItemTouched = true;
                uiStage.updateCurrentItemState();
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                for (SelectionRectangle value : currentSelection.values()) {
                    IBaseItem item = ((IBaseItem) value.getHostAsActor());
                    item.updateDataVO();

                    // update physics objetcs
                    if (item.isComposite()) {
                        ((CompositeItem) item).positionPhysics();
                    } else if (item.getBody() != null) {
                        item.getBody().setTransform(item.getDataVO().x * currentScene.mulX * PhysicsBodyLoader.SCALE, item.getDataVO().y * currentScene.mulY * PhysicsBodyLoader.SCALE, (float) Math.toRadians(item.getDataVO().rotation));
                    }

                }

                if (cameraPanOn) {
                    // cameraPanOn = !(button == 2);
                    return;
                }
                if (button == 1) {
                    showDropDownForSelection(event.getStageX(), event.getStageY());
                }
                for (SelectionRectangle value : currentSelection.values()) {
                    value.show();
                }

                if (dirty) {
                    saveSceneCurrentSceneData();
                }
                isItemTouched = false;
                dirty = false;
                if (getTapCount() == 2) {
                    getIntoComposite();
                    flow.setPendingHistory(getCurrentScene().getDataVO(), FlowActionEnum.GET_INTO_COMPOSITE);
                    flow.applyPendingAction();
                }
                uiStage.updateCurrentItemState();
            }

            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (isItemTouched && !isResizing && Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                    dirty = true;
                    for (SelectionRectangle value : currentSelection.values()) {
                        float[] diff = value.getTouchDiff();
                        value.getHostAsActor().setX(event.getStageX() - diff[0]);
                        value.getHostAsActor().setY(event.getStageY() - diff[1]);
                        value.hide();
                    }
                }
                uiStage.updateCurrentItemState();
            }
        };
        listener.setTapCountInterval(0.5f);
        ((Actor) iterableItem).addListener(listener);
    }

    private void initSandboxEvents() {
        ClickListener listener = new ClickListener() {

            private float lastX = 0
                    ,
                    lastY = 0;


            /** Called when the mouse wheel has been scrolled. When true is returned, the event is {@link com.badlogic.gdx.scenes.scene2d.Event#handle() handled}. */
            public boolean scrolled(InputEvent event, float x, float y, int amount) {
                if (amount == 0) return false;
                if (isItemTouched) {
                    for (SelectionRectangle value : currentSelection.values()) {
                        float degreeAmmount = 1;
                        if (amount < 0) degreeAmmount = -1;
                        if (Gdx.input.isKeyPressed(59)) {
                            degreeAmmount = degreeAmmount * 30;
                        }
                        value.getHostAsActor().rotateBy(degreeAmmount);
                        value.update();
                    }
                    dirty = true;
                }
                return false;
            }

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
//                cameraPanOn = false;
//                if (event.getStage() == uiStage) {
//                    return false;
//                }
                lastX = Gdx.input.getX();
                lastY = Gdx.input.getY();
                uiStage.setKeyboardFocus();
                uiStage.setScrollFocus(mainBox);
                setKeyboardFocus();
                if (dropDown != null) {
                    dropDown.remove();
                    dropDown = null;
                }
                switch (button) {
                    case 2:
                        cameraPanOn = true;
                        clearSelections();
                        isItemTouched = false;
                        break;
                    case 0:
                        if (!Gdx.input.isKeyPressed(62)) {
                            selectionRec.setOpacity(0.6f);
                        }
                        selectionRec.setWidth(0);
                        selectionRec.setHeight(0);
                        selectionRec.setX(x);
                        selectionRec.setY(y);
                        break;
                }
                return !isItemTouched;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if (button == 1 && (dropDown == null || dropDown.getParent() == null)) {
                    clearSelections();
                    showDropDownForSelection(x, y);
                    return;
                }
                if (cameraPanOn) {
                    cameraPanOn = Gdx.input.isKeyPressed(Input.Keys.SPACE);
                    return;
                }
                isUsingSelectionTool = false;
                selectionRec.setOpacity(0.0f);
                ArrayList<IBaseItem> curr = new ArrayList<IBaseItem>();
                Rectangle sR = selectionRec.getRect();
                for (int i = 0; i < currentScene.getItems().size(); i++) {
                    Actor asActor = (Actor) currentScene.getItems().get(i);
                    if (!currentScene.getItems().get(i).isLockedByLayer() && Intersector.overlaps(sR, new Rectangle(asActor.getX(), asActor.getY(), asActor.getWidth(), asActor.getHeight()))) {
                        curr.add(currentScene.getItems().get(i));
                    }
                }

                setSelections(curr, true);

                if (curr.size() == 0) {
                    uiStage.emptyClick();
                }
                if (getTapCount() == 2) {
                    getIntoPrevComposite();
                    flow.setPendingHistory(getCurrentScene().getDataVO(), FlowActionEnum.GET_OUT_COMPOSITE);
                    flow.applyPendingAction();
                }

            }

            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (isResizing) return;

                if (cameraPanOn) {
                    float currX = getCamera().position.x + (lastX - Gdx.input.getX());
                    float currY = getCamera().position.y + (Gdx.input.getY() - lastY);
                    getCamera().position.set(currX, currY, 0);
                    lastX = Gdx.input.getX();
                    lastY = Gdx.input.getY();
                } else {
                    // else using selection tool
                    isUsingSelectionTool = true;
                    selectionRec.setWidth(x - selectionRec.getX());
                    selectionRec.setHeight(y - selectionRec.getY());
                }
            }

            public boolean keyDown(InputEvent event, int keycode) {
                float deltaMove = 1;

                if (Gdx.input.isKeyPressed(129) || Gdx.input.isKeyPressed(0)) {
                    if (keycode == 19) { // up
                        itemZeIndexChange(true);
                    }
                    if (keycode == 20) { // down
                        itemZeIndexChange(false);
                    }
                    if (keycode == 29) { // A
                        ArrayList<IBaseItem> curr = new ArrayList<IBaseItem>();
                        for (int i = 0; i < currentScene.getItems().size(); i++) {
                            curr.add(currentScene.getItems().get(i));
                        }

                        setSelections(curr, true);
                    }

                    if (keycode == Input.Keys.NUM_1 && Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
                        alignSelections(Align.top);
                    }
                    if (keycode == Input.Keys.NUM_2 && Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
                        alignSelections(Align.left);
                    }
                    if (keycode == Input.Keys.NUM_3 && Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
                        alignSelections(Align.bottom);
                    }
                    if (keycode == Input.Keys.NUM_4 && Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
                        alignSelections(Align.right);
                    }

                    return true;
                }

                if (Gdx.input.isKeyPressed(59)) deltaMove = 20;
                if (keycode == 19) {
                    moveSelectedItemsBy(0, deltaMove);
                }
                if (keycode == 20) {
                    moveSelectedItemsBy(0, -deltaMove);
                }
                if (keycode == 21) {
                    moveSelectedItemsBy(-deltaMove, 0);
                }
                if (keycode == 22) {
                    moveSelectedItemsBy(deltaMove, 0);
                }
                if (keycode == Input.Keys.SPACE && !isItemTouched && !isUsingSelectionTool) {
                    setCursor(Cursor.HAND_CURSOR);
                    cameraPanOn = true;
                }


                return true;
            }

            public boolean keyUp(InputEvent event, int keycode) {
                if (keycode == 67) {
                    // delete
                    removeCurrentSelectedItems();
                }
                if (keycode == 62) {
                    setCursor(Cursor.DEFAULT_CURSOR);
                    cameraPanOn = false;
                }
                return true;
            }
        };

        this.addListener(listener);

        listener.setTapCountInterval(0.5f);


        ClickListener transformationListeners = new ClickListener() {

            private int currType;
            private Actor currHost;

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (currTransformHost != null && currTransformType >= 0) {
                    isResizing = true;
                    currType = currTransformType;
                    currHost = (Actor) currTransformHost;
                }
                return super.touchDown(event, x, y, pointer, button);
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if (isResizing == true && currHost != null) {
                    ((IBaseItem) currHost).updateDataVO();
                }
                isResizing = false;
                currType = -1;
                currHost = null;
                currTransformHost = null;
                setCursor(Cursor.DEFAULT_CURSOR);
            }

            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (!isResizing) {
                    return;
                }
                float pseudoWidth = currHost.getWidth() * (currHost instanceof LabelItem ? 1 : currHost.getScaleX());
                float pseudoHeight = currHost.getHeight() * (currHost instanceof LabelItem ? 1 : currHost.getScaleY());
                float currPseudoWidth = pseudoWidth;
                float currPseudoHeight = pseudoHeight;
                switch (currType) {
                    case SelectionRectangle.LB:
                        pseudoWidth = (currHost.getX() + currPseudoWidth) - event.getStageX();
                        pseudoHeight = (currHost.getY() + currPseudoHeight) - event.getStageY();
                        currHost.setX(currHost.getX() - (pseudoWidth - currPseudoWidth));
                        currHost.setY(currHost.getY() - (pseudoHeight - currPseudoHeight));
                        break;
                    case SelectionRectangle.L:
                        pseudoWidth = (currHost.getX() + currPseudoWidth) - event.getStageX();
                        currHost.setX(currHost.getX() - (pseudoWidth - currPseudoWidth));
                        break;
                    case SelectionRectangle.LT:
                        pseudoWidth = (currHost.getX() + currPseudoWidth) - event.getStageX();
                        pseudoHeight = event.getStageY() - currHost.getY();
                        currHost.setX(currHost.getX() - (pseudoWidth - currPseudoWidth));
                        break;
                    case SelectionRectangle.T:
                        pseudoHeight = event.getStageY() - currHost.getY();
                        break;
                    case SelectionRectangle.B:
                        pseudoHeight = (currHost.getY() + currPseudoHeight) - event.getStageY();
                        currHost.setY(currHost.getY() - (pseudoHeight - currPseudoHeight));
                        break;
                    case SelectionRectangle.RB:
                        pseudoWidth = event.getStageX() - currHost.getX();
                        pseudoHeight = (currHost.getY() + currPseudoHeight) - event.getStageY();
                        currHost.setY(currHost.getY() - (pseudoHeight - currPseudoHeight));
                        break;
                    case SelectionRectangle.R:
                        pseudoWidth = event.getStageX() - currHost.getX();
                        break;
                    case SelectionRectangle.RT:
                        pseudoWidth = event.getStageX() - currHost.getX();
                        pseudoHeight = event.getStageY() - currHost.getY();
                        break;
                    default:
                        return;
                }
                if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                    float enclosingRectSize = Math.max(pseudoWidth, pseudoHeight);
                    if (currHost.getWidth() >= currHost.getHeight()) {
                        pseudoWidth = enclosingRectSize;
                        pseudoHeight = (pseudoWidth / currHost.getWidth()) * currHost.getHeight();
                    }
                    if (currHost.getHeight() > currHost.getWidth()) {
                        pseudoHeight = enclosingRectSize;
                        pseudoWidth = (pseudoHeight / currHost.getHeight()) * currHost.getWidth();
                    }

                }
                currHost.setScale(pseudoWidth / currHost.getWidth(), pseudoHeight / currHost.getHeight());
                uiStage.updateCurrentItemState();
                SelectionRectangle selectionRectangle = currentSelection.get(currHost);
                //TODO: sometimes it is null, find out why
                if (selectionRectangle != null) {
                    selectionRectangle.update();
                }
            }
        };

        this.addListener(transformationListeners);
    }

    public void copyAction() {

    }

    public void cutAction() {

    }

    public void pasteAction() {

    }

    public void saveAction() {

    }
}
