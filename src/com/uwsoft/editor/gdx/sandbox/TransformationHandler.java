package com.uwsoft.editor.gdx.sandbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.uwsoft.editor.gdx.actors.SelectionRectangle;
import com.uwsoft.editor.renderer.actor.LabelItem;

/**
 * Provides logic for item size/scale/rotation visual transformation by dragging from selection rectangle corners
 */
public class TransformationHandler {

	 private int type;

	 private Actor host;

	 public TransformationHandler() {

	 }

	 public void setTransforming(int type, Actor host) {
			this.type = type;
		   this.host = host;
	 }

	 public void touchDragged(InputEvent event, float x, float y) {
		  float pseudoWidth = host.getWidth() * (host instanceof LabelItem ? 1 : host.getScaleX());
		  float pseudoHeight = host.getHeight() * (host instanceof LabelItem ? 1 : host.getScaleY());
		  float currPseudoWidth = pseudoWidth;
		  float currPseudoHeight = pseudoHeight;
		  switch (type) {
		  case SelectionRectangle.LB:
				pseudoWidth = (host.getX() + currPseudoWidth) - event.getStageX();
				pseudoHeight = (host.getY() + currPseudoHeight) - event.getStageY();
				host.setX(host.getX() - (pseudoWidth - currPseudoWidth));
				host.setY(host.getY() - (pseudoHeight - currPseudoHeight));
				break;
		  case SelectionRectangle.L:
				pseudoWidth = (host.getX() + currPseudoWidth) - event.getStageX();
				host.setX(host.getX() - (pseudoWidth - currPseudoWidth));
				break;
		  case SelectionRectangle.LT:
				pseudoWidth = (host.getX() + currPseudoWidth) - event.getStageX();
				pseudoHeight = event.getStageY() - host.getY();
				host.setX(host.getX() - (pseudoWidth - currPseudoWidth));
				break;
		  case SelectionRectangle.T:
				pseudoHeight = event.getStageY() - host.getY();
				break;
		  case SelectionRectangle.B:
				pseudoHeight = (host.getY() + currPseudoHeight) - event.getStageY();
				host.setY(host.getY() - (pseudoHeight - currPseudoHeight));
				break;
		  case SelectionRectangle.RB:
				pseudoWidth = event.getStageX() - host.getX();
				pseudoHeight = (host.getY() + currPseudoHeight) - event.getStageY();
				host.setY(host.getY() - (pseudoHeight - currPseudoHeight));
				break;
		  case SelectionRectangle.R:
				pseudoWidth = event.getStageX() - host.getX();
				break;
		  case SelectionRectangle.RT:
				pseudoWidth = event.getStageX() - host.getX();
				pseudoHeight = event.getStageY() - host.getY();
				break;
		  default:
				return;
		  }
		  if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
				float enclosingRectSize = Math.max(pseudoWidth, pseudoHeight);
				if (host.getWidth() >= host.getHeight()) {
					 pseudoWidth = enclosingRectSize;
					 pseudoHeight = (pseudoWidth / host.getWidth()) * host.getHeight();
				}
				if (host.getHeight() > host.getWidth()) {
					 pseudoHeight = enclosingRectSize;
					 pseudoWidth = (pseudoHeight / host.getHeight()) * host.getWidth();
				}

		  }

		  host.setScale(pseudoWidth / host.getWidth(), pseudoHeight / host.getHeight());
	 }

	 public boolean isResizing() {
		  if(host != null) {
				return true;
		  }

		  return false;
	 }

	 public Actor getHost() {
		  return host;
	 }

	 public void clear() {
		  type = -1;
		  host = null;
	 }
}
