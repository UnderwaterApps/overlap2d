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

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.gdx.actors.SelectionRectangle;
import com.uwsoft.editor.gdx.sandbox.Sandbox;

/**
 * Created by azakhary on 4/30/2015.
 */
public class TransformTool extends SelectionTool {

    public static final String NAME = "TRANSFORM_TOOL";

    SelectionRectangle currentSelection;

    @Override
    public void initTool() {
        Sandbox sandbox = Sandbox.getInstance();

        if(sandbox.getSelector().selectionIsOneItem()){
            sandbox.getSelector().getSelectedItemSelectionRectangle().setMode(true);
        } else {
            sandbox.getSelector().clearSelections();
        }
    }

    @Override
    public boolean stageMouseDown(float x, float y) {
        return false;
    }

    @Override
    public void stageMouseUp(float x, float y) {

    }

    @Override
    public void stageMouseDragged(float x, float y) {

    }

    @Override
    public void stageMouseDoubleClick(float x, float y) {

    }

    @Override
    public boolean itemMouseDown(Entity entity, float x, float y) {
        super.itemMouseDown(entity, x, y);
        Sandbox sandbox = Sandbox.getInstance();

        currentSelection = sandbox.getSelector().getSelectedItemSelectionRectangle();

        currentSelection.setMode(true);
        setListeners(currentSelection);

        return true;
    }

    private void setListeners(SelectionRectangle selectionRect) {
        selectionRect.setListener(new SelectionRectangle.SelectionRectangleListener() {

            private int anchorId;
            private Entity host;

            @Override
            public void anchorDown(int anchor, float x, float y) {
                Sandbox sandbox = Sandbox.getInstance();
                this.anchorId = anchor;
                //TODO fix and uncomment
                //this.host = (Actor) sandbox.getSelector().getSelectedItem();
            }

            @Override
            public void anchorDragged(int anchor, float x, float y) {
            	//TODO fix and uncomment
//                float pseudoWidth = host.getWidth() * (host instanceof LabelItem ? 1 : host.getScaleX());
//                float pseudoHeight = host.getHeight() * (host instanceof LabelItem ? 1 : host.getScaleY());
//                float currPseudoWidth = pseudoWidth;
//                float currPseudoHeight = pseudoHeight;
//                switch (anchorId) {
//                    case SelectionRectangle.LB:
//                        pseudoWidth = (host.getX() + currPseudoWidth) - x;
//                        pseudoHeight = (host.getY() + currPseudoHeight) - y;
//                        host.setX(host.getX() - (pseudoWidth - currPseudoWidth));
//                        host.setY(host.getY() - (pseudoHeight - currPseudoHeight));
//                        break;
//                    case SelectionRectangle.L:
//                        pseudoWidth = (host.getX() + currPseudoWidth) - x;
//                        host.setX(host.getX() - (pseudoWidth - currPseudoWidth));
//                        break;
//                    case SelectionRectangle.LT:
//                        pseudoWidth = (host.getX() + currPseudoWidth) - x;
//                        pseudoHeight = y - host.getY();
//                        host.setX(host.getX() - (pseudoWidth - currPseudoWidth));
//                        break;
//                    case SelectionRectangle.T:
//                        pseudoHeight = y - host.getY();
//                        break;
//                    case SelectionRectangle.B:
//                        pseudoHeight = (host.getY() + currPseudoHeight) - y;
//                        host.setY(host.getY() - (pseudoHeight - currPseudoHeight));
//                        break;
//                    case SelectionRectangle.RB:
//                        pseudoWidth = x - host.getX();
//                        pseudoHeight = (host.getY() + currPseudoHeight) - y;
//                        host.setY(host.getY() - (pseudoHeight - currPseudoHeight));
//                        break;
//                    case SelectionRectangle.R:
//                        pseudoWidth = x - host.getX();
//                        break;
//                    case SelectionRectangle.RT:
//                        pseudoWidth = x - host.getX();
//                        pseudoHeight = y - host.getY();
//                        break;
//                    default:
//                        return;
//                }
//                if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
//                    float enclosingRectSize = Math.max(pseudoWidth, pseudoHeight);
//                    if (host.getWidth() >= host.getHeight()) {
//                        pseudoWidth = enclosingRectSize;
//                        pseudoHeight = (pseudoWidth / host.getWidth()) * host.getHeight();
//                    }
//                    if (host.getHeight() > host.getWidth()) {
//                        pseudoHeight = enclosingRectSize;
//                        pseudoWidth = (pseudoHeight / host.getHeight()) * host.getWidth();
//                    }
//
//                }
//
//                host.setScale(pseudoWidth / host.getWidth(), pseudoHeight / host.getHeight());
//                ((IBaseItem)host).updateDataVO();
//                Overlap2DFacade.getInstance().sendNotification(Overlap2D.ITEM_DATA_UPDATED);
            }

            @Override
            public void anchorUp(int anchor, float x, float y) {

            }
        });
    }
}
