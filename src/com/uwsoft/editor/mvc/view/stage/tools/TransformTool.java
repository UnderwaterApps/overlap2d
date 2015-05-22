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
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.uwsoft.editor.Overlap2D;
import com.uwsoft.editor.gdx.actors.SelectionRectangle;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.CursorManager;
import com.uwsoft.editor.mvc.view.MidUIMediator;
import com.uwsoft.editor.mvc.view.ui.followers.BasicFollower;
import com.uwsoft.editor.mvc.view.ui.followers.FollowerTransformationListener;
import com.uwsoft.editor.mvc.view.ui.followers.NormalSelectionFollower;
import com.uwsoft.editor.renderer.conponents.DimensionsComponent;
import com.uwsoft.editor.renderer.conponents.TransformComponent;
import com.uwsoft.editor.utils.runtime.ComponentRetriever;

/**
 * Created by azakhary on 4/30/2015.
 */
public class TransformTool extends SelectionTool {

    public static final String NAME = "TRANSFORM_TOOL";

    private BasicFollower selectionFollower;
    private Entity currentEntity;

    private TransformComponent transformComponent;
    private DimensionsComponent dimensionsComponent;

    @Override
    public void initTool() {
        Sandbox sandbox = Sandbox.getInstance();

        if(!sandbox.getSelector().selectionIsOneItem()){
            sandbox.getSelector().clearSelections();
        }

        // set cursor
        CursorManager cursorManager = Overlap2DFacade.getInstance().retrieveProxy(CursorManager.NAME);
        //cursorManager.setCursor(CursorManager.CROSS);

        MidUIMediator midUIMediator = Overlap2DFacade.getInstance().retrieveMediator(MidUIMediator.NAME);
        midUIMediator.setMode(BasicFollower.FollowerMode.transform);
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

        currentEntity = entity;
        transformComponent = ComponentRetriever.get(currentEntity, TransformComponent.class);
        dimensionsComponent = ComponentRetriever.get(currentEntity, DimensionsComponent.class);

        MidUIMediator midUIMediator = Overlap2DFacade.getInstance().retrieveMediator(MidUIMediator.NAME);
        selectionFollower = midUIMediator.getFollower(entity);
        setListeners();

        return true;
    }

    private void setListeners() {
        selectionFollower.setFollowerListener(new FollowerTransformationListener() {

            private int anchorId;

            @Override
            public void anchorDown(int anchor, float x, float y) {
                Sandbox sandbox = Sandbox.getInstance();
                this.anchorId = anchor;
            }

            @Override
            public void anchorDragged(int anchor, float x, float y) {

                float newX = transformComponent.x;
                float newY = transformComponent.y;
                float newWidth = dimensionsComponent.width * transformComponent.scaleX;
                float newHeight = dimensionsComponent.height * transformComponent.scaleY;

                float newOriginX;
                float newOriginY;

                switch (anchorId) {
                    case NormalSelectionFollower.ORIGIN:
                        newOriginX = x;
                        newOriginY = y;
                        break;
                    case NormalSelectionFollower.LB:
                        newX = x;
                        newY = y;
                        newWidth = newWidth + (transformComponent.x - x);
                        newHeight = newHeight + (transformComponent.y - y);
                        break;
                    case NormalSelectionFollower.L:
                        newX = x;
                        newWidth = newWidth + (transformComponent.x - x);
                        break;
                    case NormalSelectionFollower.LT:
                        newX = x;
                        newWidth = newWidth + (transformComponent.x - x);
                        newHeight = y - transformComponent.y;
                        break;
                    case NormalSelectionFollower.T:
                        newHeight = y - transformComponent.y;
                        break;
                    case NormalSelectionFollower.B:
                        newY = y;
                        newHeight = newHeight + (transformComponent.y - y);
                        break;
                    case NormalSelectionFollower.RB:
                        newY = y;
                        newWidth = x - selectionFollower.getX();
                        newHeight = newHeight + (transformComponent.y - y);
                        break;
                    case NormalSelectionFollower.R:
                        newWidth = x - transformComponent.x;
                        break;
                    case NormalSelectionFollower.RT:
                        newHeight = y - transformComponent.y;
                        newWidth = x - transformComponent.x;
                        break;
                    default:
                        return;
                }


                if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                    newWidth = Math.max(newWidth, newHeight);
                    newHeight = Math.max(newWidth, newHeight);
                }


                transformComponent.x = newX;
                transformComponent.y = newY;
                transformComponent.scaleX = newWidth/dimensionsComponent.width;
                transformComponent.scaleY = newHeight/dimensionsComponent.height;

                Overlap2DFacade.getInstance().sendNotification(Overlap2D.ITEM_DATA_UPDATED);

            }

            @Override
            public void anchorUp(int anchor, float x, float y) {

            }

            @Override
            public void mouseEnter(int anchor, float x, float y) {

            }

            @Override
            public void mouseExit(int anchor, float x, float y) {

            }
        });
    }
}
