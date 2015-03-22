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
public class SandboxInputAdapter extends InputAdapter {

    private Sandbox sandbox;

	private float lastX = 0;
	private float lastY = 0;

    public SandboxInputAdapter (Sandbox sandbox) {
        this.sandbox = sandbox;
    }

	 /** When item is touched, managing selections and preparing to drag selection */
	 private boolean itemTouchDown(IBaseItem item, InputEvent event, float x, float y, int button) {
		  // Making sure we have fresh VO data
		  item.updateDataVO();

		  // If currently panning do nothing regarding this item, panning will take over
		  if (sandbox.cameraPanOn) {
				return false;
		  }

		  // if shift is pressed we are in add/remove selection mode
		  if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
				if(sandbox.getSelector().getCurrentSelection().get(item) != null) {
					 // item was selected, so we need to release it
					 sandbox.getSelector().releaseSelection(item);
				} else {
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

		  // remembering local touch position for each of selected items, if planning to drag
		  for (SelectionRectangle value : sandbox.getSelector().getCurrentSelection().values()) {
				value.setTouchDiff(event.getStageX() - value.getHostAsActor().getX(), event.getStageY() - value.getHostAsActor().getY());
		  }

		  // remembering that item was touched
		  sandbox.isItemTouched = true;

		  // pining UI to update current item properties data
		  sandbox.getUIStage().updateCurrentItemState();

		  return true;
	 }

	 private void itemTouchUp(IBaseItem item, InputEvent event, float x, float y, int button) {

		  sandbox.getSelector().flushAllSelectedItems();

		  // if panning was taking place - do nothing else. (other touch up got this)
		  if (sandbox.cameraPanOn) {
				return;
		  }

		  if (button == Input.Buttons.RIGHT) {
				// if right clicked on an item, drop down for current selection
				sandbox.showDropDown(event.getStageX(), event.getStageY());
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
		  sandbox.getUIStage().updateCurrentItemState();
	 }

	 private void itemDoubleClick(IBaseItem item, InputEvent event, float x, float y, int button) {
		  sandbox.getIntoComposite();
		  sandbox.flow.setPendingHistory(sandbox.getCurrentScene().getDataVO(), FlowActionEnum.GET_INTO_COMPOSITE);
		  sandbox.flow.applyPendingAction();
	 }

	 private void itemTouchDragged(IBaseItem item, InputEvent event, float x, float y) {

		  // if there is no resizing going on, the item was touched,
		  // the button is in and we are dragging... well you can probably be safe about saying - we do.
		  if (sandbox.isItemTouched && !sandbox.isResizing && Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
				sandbox.dirty = true;

				// Selection rectangles should move and follow along
				for (SelectionRectangle value : sandbox.getSelector().getCurrentSelection().values()) {
					 float[] diff = value.getTouchDiff();
					 value.getHostAsActor().setX(event.getStageX() - diff[0]);
					 value.getHostAsActor().setY(event.getStageY() - diff[1]);
					 value.hide();
				}
		  }

		  // pining UI to update current item properties data
		  sandbox.getUIStage().updateCurrentItemState();
	 }

	 private boolean sandboxMouseScrolled(float x, float y, float amount) {
		  // well, duh
		  if (amount == 0) return false;

		  // if item is currently being held with mouse (touched in but not touched out)
		  // mouse scroll should rotate the selection around it's origin
		  if (sandbox.isItemTouched) {
				for (SelectionRectangle value : sandbox.getSelector().getCurrentSelection().values()) {
					 float degreeAmmount = 1;
					 if (amount < 0) degreeAmmount = -1;
					 // And if shift is pressed, the rotation amount is bigger
					 if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
						  degreeAmmount = degreeAmmount * 30;
					 }
					 value.getHostAsActor().rotateBy(degreeAmmount);
					 value.update();
				}
				sandbox.dirty = true;
		  }
		  return false;
	 }

	 private boolean sandboxTouchDown(InputEvent event, float x, float y, int button) {
		  // setting key and scroll focus on main area
		  sandbox.getUIStage().setKeyboardFocus();
		  sandbox.getUIStage().setScrollFocus(sandbox.getSandboxStage().mainBox);
		  sandbox.getSandboxStage().setKeyboardFocus();

		  // if there was a drop down remove it
		  // TODO: this is job for front UI to figure out
		  if (sandbox.getSandboxStage().frontUI.dropDown != null) {
				sandbox.getSandboxStage().frontUI.dropDown.remove();
				sandbox.getSandboxStage().frontUI.dropDown = null;
		  }

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

	 private void sandboxTouchUp(InputEvent event, float x, float y, int button) {

		  if (button == Input.Buttons.RIGHT) {
				// if clicked on empty space, selections need to be cleared
				sandbox.getSelector().clearSelections();

				// show default dropdown
				if(sandbox.showDropDown(x, y)) {
					 return;
				}
		  }

		  // Basically if panning but space is not pressed, stop panning.? o_O
		  // TODO: seriously this needs to be figured out, I am not sure we need it
		  if (sandbox.cameraPanOn) {
				sandbox.cameraPanOn = Gdx.input.isKeyPressed(Input.Keys.SPACE);
				return;
		  }

		  // selection is complete, this will check for what get caught in selection rect, and select 'em
		  sandbox.selectionComplete();
	 }

	 private void sandboxDoubleClick(InputEvent event, float x, float y) {
		  // if empty space is double clicked, then get back into previous composite
		  // TODO: do not do if we are on root item ( this is somehow impossible to implement o_O )
		  sandbox.getIntoPrevComposite();
		  sandbox.flow.setPendingHistory(sandbox.getCurrentScene().getDataVO(), FlowActionEnum.GET_OUT_COMPOSITE);
		  sandbox.flow.applyPendingAction();
	 }

	 private void sandboxTouchDragged(InputEvent event, float x, float y) {
		  // if resizing is in progress we are not dealing with this
		  if (sandbox.isResizing) return;

		  if (sandbox.cameraPanOn) {
				// if panning, then just move camera
				float currX = sandbox.getSandboxStage().getCamera().position.x + (lastX - Gdx.input.getX());
				float currY = sandbox.getSandboxStage().getCamera().position.y + (Gdx.input.getY() - lastY);

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

	 private boolean sandboxKeyDown(int keycode) {
		  // the amount of pixels by which to move item if moving
		  float deltaMove = 1;

		  // if control is pressed then z index is getting modified
		  // TODO: key pressed 0 for unckown, should be removed?
		  // TODO: need to make sure OSX Command button works too.

		  if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(0)) {
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

				return true;
		  }

		  if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
				// if shift is pressed, move items by 20 pixels instead of one
				deltaMove = 20; //pixels
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

		  // if space is pressed, that means we are going to pan, so set cursor accordingly
		  // TODO: this pan is kinda different from what happens when you press middle button, so things need to merge right
		  if (keycode == Input.Keys.SPACE && !sandbox.isItemTouched && !sandbox.isUsingSelectionTool) {
				sandbox.getSandboxStage().setCursor(Cursor.HAND_CURSOR);
				sandbox.cameraPanOn = true;
		  }

		  return true;
	 }

	 private boolean sandboxKeyUp(int keycode) {
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
		  return true;
	 }

    public void initItemListeners(final IBaseItem eventItem) {
        ClickListener listener = new ClickListener() {

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);

					 return itemTouchDown(eventItem, event, x, y, button);
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

					 itemTouchUp(eventItem, event, x, y, button);

					 if (getTapCount() == 2) {
						  // this is double click
						  itemDoubleClick(eventItem, event, x, y, button);
					 }
            }

            public void touchDragged(InputEvent event, float x, float y, int pointer) {
					 itemTouchDragged(eventItem, event, x, y);
            }
        };

		  // tap count interval to correctly manage double clicks
		  // if longer then 0.5 second it's not double click
        listener.setTapCountInterval(0.5f);

		  // finally adding this huge listener
        ((Actor) eventItem).addListener(listener);
    }



    public void initSandboxEvents() {
        ClickListener listener = new ClickListener() {

            public boolean scrolled(InputEvent event, float x, float y, int amount) {
					  return sandboxMouseScrolled(x, y, amount);
            }

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);

					 lastX = Gdx.input.getX();
					 lastY = Gdx.input.getY();

					 return sandboxTouchDown(event, x, y, button);
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

					 sandboxTouchUp(event, x, y, button);

					 if (getTapCount() == 2 && button == Input.Buttons.LEFT) {
						  sandboxDoubleClick(event, x, y);
					 }
            }

            public void touchDragged(InputEvent event, float x, float y, int pointer) {
					 sandboxTouchDragged(event, x, y);
            }

            public boolean keyDown(InputEvent event, int keycode) {

					 return sandboxKeyDown(keycode);

            }

            public boolean keyUp(InputEvent event, int keycode) {
                return sandboxKeyUp(keycode);
            }
        };

        sandbox.getSandboxStage().addListener(listener);

        listener.setTapCountInterval(0.5f);


        ClickListener transformationListeners = new ClickListener() {

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (sandbox.currTransformHost != null && sandbox.currTransformType >= 0) {
                    sandbox.isResizing = true;
						  sandbox.transformationHandler.setTransforming(sandbox.currTransformType, (Actor) sandbox.currTransformHost);
                }
                return super.touchDown(event, x, y, pointer, button);
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

					 if(sandbox.transformationHandler.isResizing()) {

					 }
                if (sandbox.isResizing == true && sandbox.transformationHandler.isResizing()) {
                    ((IBaseItem) sandbox.transformationHandler.getHost()).updateDataVO();
                }
                sandbox.isResizing = false;

					 sandbox.transformationHandler.clear();
                sandbox.currTransformHost = null;
                sandbox.getSandboxStage().setCursor(Cursor.DEFAULT_CURSOR);
            }

            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (!sandbox.isResizing) {
                    return;
                }

					 sandbox.transformationHandler.touchDragged(event, x, y);

                sandbox.getUIStage().updateCurrentItemState();
                SelectionRectangle selectionRectangle = sandbox.getSelector().getCurrentSelection().get(sandbox.transformationHandler.getHost());
                //TODO: sometimes it is null, find out why
                if (selectionRectangle != null) {
                    selectionRectangle.update();
                }
            }
        };

		  // add the listener
        sandbox.getSandboxStage().addListener(transformationListeners);
    }
}
