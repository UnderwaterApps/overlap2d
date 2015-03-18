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

    private Sandbox sandbox;

    public InputHandler(Sandbox sandbox) {
        this.sandbox = sandbox;
    }

    public void initItemListeners(final IBaseItem iterableItem) {
        ClickListener listener = new ClickListener() {

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                iterableItem.updateDataVO();
                if (sandbox.cameraPanOn) {
                    return false;
                }

                if (sandbox.getCurrentSelection().get(iterableItem) != null && Gdx.input.isKeyPressed(59)) {
                    sandbox.releaseSelection(iterableItem);
                } else {
                    if (iterableItem.isLockedByLayer()) {
                        sandbox.clearSelections();
                    } else {
                        sandbox.setSelection(iterableItem, !Gdx.input.isKeyPressed(59));
                    }
                }

                for (SelectionRectangle value : sandbox.getCurrentSelection().values()) {
                    value.setTouchDiff(event.getStageX() - value.getHostAsActor().getX(), event.getStageY() - value.getHostAsActor().getY());
                }
                sandbox.isItemTouched = true;
                sandbox.getUIStage().updateCurrentItemState();
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                for (SelectionRectangle value : sandbox.getCurrentSelection().values()) {
                    IBaseItem item = ((IBaseItem) value.getHostAsActor());
                    item.updateDataVO();

                    // update physics objetcs
                    if (item.isComposite()) {
                        ((CompositeItem) item).positionPhysics();
                    } else if (item.getBody() != null) {
                        item.getBody().setTransform(item.getDataVO().x * sandbox.getCurrentScene().mulX * PhysicsBodyLoader.SCALE, item.getDataVO().y * sandbox.getCurrentScene().mulY * PhysicsBodyLoader.SCALE, (float) Math.toRadians(item.getDataVO().rotation));
                    }

                }

                if (sandbox.cameraPanOn) {
                    // sandbox.cameraPanOn = !(button == 2);
                    return;
                }
                if (button == 1) {
                    sandbox.getSandboxStage().frontUI.showDropDownForSelection(event.getStageX(), event.getStageY());
                }
                for (SelectionRectangle value : sandbox.getCurrentSelection().values()) {
                    value.show();
                }

                if (sandbox.dirty) {
                    sandbox.saveSceneCurrentSceneData();
                }
                sandbox.isItemTouched = false;
                sandbox.dirty = false;
                if (getTapCount() == 2) {
                    sandbox.getIntoComposite();
                    sandbox.flow.setPendingHistory(sandbox.getCurrentScene().getDataVO(), FlowActionEnum.GET_INTO_COMPOSITE);
                    sandbox.flow.applyPendingAction();
                }
                sandbox.getUIStage().updateCurrentItemState();
            }

            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (sandbox.isItemTouched && !sandbox.isResizing && Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                    sandbox.dirty = true;
                    for (SelectionRectangle value : sandbox.getCurrentSelection().values()) {
                        float[] diff = value.getTouchDiff();
                        value.getHostAsActor().setX(event.getStageX() - diff[0]);
                        value.getHostAsActor().setY(event.getStageY() - diff[1]);
                        value.hide();
                    }
                }
                sandbox.getUIStage().updateCurrentItemState();
            }
        };
        listener.setTapCountInterval(0.5f);
        ((Actor) iterableItem).addListener(listener);
    }



    public void initSandboxEvents() {
        ClickListener listener = new ClickListener() {

            private float lastX = 0,
                    lastY = 0;


            /** Called when the mouse wheel has been scrolled. When true is returned, the event is {@link com.badlogic.gdx.scenes.scene2d.Event#handle() handled}. */
            public boolean scrolled(InputEvent event, float x, float y, int amount) {
                if (amount == 0) return false;
                if (sandbox.isItemTouched) {
                    for (SelectionRectangle value : sandbox.getCurrentSelection().values()) {
                        float degreeAmmount = 1;
                        if (amount < 0) degreeAmmount = -1;
                        if (Gdx.input.isKeyPressed(59)) {
                            degreeAmmount = degreeAmmount * 30;
                        }
                        value.getHostAsActor().rotateBy(degreeAmmount);
                        value.update();
                    }
                    sandbox.dirty = true;
                }
                return false;
            }

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
//                sandbox.cameraPanOn = false;
//                if (event.getStage() == uiStage) {
//                    return false;
//                }
                lastX = Gdx.input.getX();
                lastY = Gdx.input.getY();
                sandbox.getUIStage().setKeyboardFocus();
                sandbox.getUIStage().setScrollFocus(sandbox.getSandboxStage().mainBox);
                sandbox.getSandboxStage().setKeyboardFocus();

                if (sandbox.getSandboxStage().frontUI.dropDown != null) {
                    sandbox.getSandboxStage().frontUI.dropDown.remove();
                    sandbox.getSandboxStage().frontUI.dropDown = null;
                }
                switch (button) {
                    case 2:
                        sandbox.cameraPanOn = true;
                        sandbox.clearSelections();
                        sandbox.isItemTouched = false;
                        break;
                    case 0:
                        if (!Gdx.input.isKeyPressed(62)) {
                            sandbox.getSandboxStage().selectionRec.setOpacity(0.6f);
                        }
                        sandbox.getSandboxStage().selectionRec.setWidth(0);
                        sandbox.getSandboxStage().selectionRec.setHeight(0);
                        sandbox.getSandboxStage().selectionRec.setX(x);
                        sandbox.getSandboxStage().selectionRec.setY(y);
                        break;
                }
                return !sandbox.isItemTouched;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if (button == 1 && (sandbox.getSandboxStage().frontUI.dropDown == null || sandbox.getSandboxStage().frontUI.dropDown.getParent() == null)) {
                    sandbox.clearSelections();
                    sandbox.getSandboxStage().frontUI.showDropDownForSelection(x, y);
                    return;
                }
                if (sandbox.cameraPanOn) {
                    sandbox.cameraPanOn = Gdx.input.isKeyPressed(Input.Keys.SPACE);
                    return;
                }
                sandbox.isUsingSelectionTool = false;
                sandbox.getSandboxStage().selectionRec.setOpacity(0.0f);
                ArrayList<IBaseItem> curr = new ArrayList<IBaseItem>();
                Rectangle sR = sandbox.getSandboxStage().selectionRec.getRect();
                for (int i = 0; i < sandbox.getCurrentScene().getItems().size(); i++) {
                    Actor asActor = (Actor) sandbox.getCurrentScene().getItems().get(i);
                    if (!sandbox.getCurrentScene().getItems().get(i).isLockedByLayer() && Intersector.overlaps(sR, new Rectangle(asActor.getX(), asActor.getY(), asActor.getWidth(), asActor.getHeight()))) {
                        curr.add(sandbox.getCurrentScene().getItems().get(i));
                    }
                }

                sandbox.setSelections(curr, true);

                if (curr.size() == 0) {
                    sandbox.getUIStage().emptyClick();
                }
                if (getTapCount() == 2) {
                    sandbox.getIntoPrevComposite();
                    sandbox.flow.setPendingHistory(sandbox.getCurrentScene().getDataVO(), FlowActionEnum.GET_OUT_COMPOSITE);
                    sandbox.flow.applyPendingAction();
                }

            }

            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (sandbox.isResizing) return;

                if (sandbox.cameraPanOn) {
                    float currX = sandbox.getSandboxStage().getCamera().position.x + (lastX - Gdx.input.getX());
                    float currY = sandbox.getSandboxStage().getCamera().position.y + (Gdx.input.getY() - lastY);
                    sandbox.getSandboxStage().getCamera().position.set(currX, currY, 0);
                    lastX = Gdx.input.getX();
                    lastY = Gdx.input.getY();
                } else {
                    // else using selection tool
                    sandbox.isUsingSelectionTool = true;
                    sandbox.getSandboxStage().selectionRec.setWidth(x - sandbox.getSandboxStage().selectionRec.getX());
                    sandbox.getSandboxStage().selectionRec.setHeight(y - sandbox.getSandboxStage().selectionRec.getY());
                }
            }

            public boolean keyDown(InputEvent event, int keycode) {
                float deltaMove = 1;

                if (Gdx.input.isKeyPressed(129) || Gdx.input.isKeyPressed(0)) {
                    if (keycode == 19) { // up
                        sandbox.itemControl.itemZIndexChange(sandbox.getCurrentSelection(), true);
                    }
                    if (keycode == 20) { // down
                        sandbox.itemControl.itemZIndexChange(sandbox.getCurrentSelection(), false);
                    }
                    if (keycode == 29) { // A
                        ArrayList<IBaseItem> curr = new ArrayList<IBaseItem>();
                        for (int i = 0; i < sandbox.getCurrentScene().getItems().size(); i++) {
                            curr.add(sandbox.getCurrentScene().getItems().get(i));
                        }

                        sandbox.setSelections(curr, true);
                    }

                    if (keycode == Input.Keys.NUM_1 && Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
                        sandbox.alignSelections(Align.top);
                    }
                    if (keycode == Input.Keys.NUM_2 && Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
                        sandbox.alignSelections(Align.left);
                    }
                    if (keycode == Input.Keys.NUM_3 && Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
                        sandbox.alignSelections(Align.bottom);
                    }
                    if (keycode == Input.Keys.NUM_4 && Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
                        sandbox.alignSelections(Align.right);
                    }

                    return true;
                }

                if (Gdx.input.isKeyPressed(59)) deltaMove = 20;
                if (keycode == 19) {
                    sandbox.moveSelectedItemsBy(0, deltaMove);
                }
                if (keycode == 20) {
                    sandbox.moveSelectedItemsBy(0, -deltaMove);
                }
                if (keycode == 21) {
                    sandbox.moveSelectedItemsBy(-deltaMove, 0);
                }
                if (keycode == 22) {
                    sandbox.moveSelectedItemsBy(deltaMove, 0);
                }
                if (keycode == Input.Keys.SPACE && !sandbox.isItemTouched && !sandbox.isUsingSelectionTool) {
                    sandbox.getSandboxStage().setCursor(Cursor.HAND_CURSOR);
                    sandbox.cameraPanOn = true;
                }


                return true;
            }

            public boolean keyUp(InputEvent event, int keycode) {
                if (keycode == 67) {
                    // delete
                    sandbox.removeCurrentSelectedItems();
                }
                if (keycode == 62) {
                    sandbox.getSandboxStage().setCursor(Cursor.DEFAULT_CURSOR);
                    sandbox.cameraPanOn = false;
                }
                return true;
            }
        };

        sandbox.getSandboxStage().addListener(listener);

        listener.setTapCountInterval(0.5f);


        ClickListener transformationListeners = new ClickListener() {

            private int currType;
            private Actor currHost;

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (sandbox.currTransformHost != null && sandbox.currTransformType >= 0) {
                    sandbox.isResizing = true;
                    currType = sandbox.currTransformType;
                    currHost = (Actor) sandbox.currTransformHost;
                }
                return super.touchDown(event, x, y, pointer, button);
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if (sandbox.isResizing == true && currHost != null) {
                    ((IBaseItem) currHost).updateDataVO();
                }
                sandbox.isResizing = false;
                currType = -1;
                currHost = null;
                sandbox.currTransformHost = null;
                sandbox.getSandboxStage().setCursor(Cursor.DEFAULT_CURSOR);
            }

            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (!sandbox.isResizing) {
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
                sandbox.getUIStage().updateCurrentItemState();
                SelectionRectangle selectionRectangle = sandbox.getCurrentSelection().get(currHost);
                //TODO: sometimes it is null, find out why
                if (selectionRectangle != null) {
                    selectionRectangle.update();
                }
            }
        };

        sandbox.getSandboxStage().addListener(transformationListeners);
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
