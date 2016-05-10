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

package com.uwsoft.editor.view.ui.followers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.commons.MsgAPI;
import com.kotcrab.vis.ui.VisUI;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.view.ui.widget.actors.basic.PixelRect;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.proxy.CursorManager;
import com.uwsoft.editor.proxy.EditorTextureManager;
import com.uwsoft.editor.view.stage.tools.TransformTool;
import com.uwsoft.editor.view.ui.box.UIToolBoxMediator;
import com.uwsoft.editor.view.ui.widget.EmptyTarget;

/**
 * Created by azakhary on 5/20/2015.
 */
public class NormalSelectionFollower extends BasicFollower {

    private Overlap2DFacade facade;
    private EditorTextureManager tm;
    private CursorManager cursorManager;

    protected PixelRect pixelRect;

    protected Group transformGroup;
    protected Actor[] miniRects;

    public static final int LT = 0;
    public static final int T = 1;
    public static final int RT = 2;
    public static final int R = 3;
    public static final int RB = 4;
    public static final int B = 5;
    public static final int LB = 6;
    public static final int L = 7;

    public static final int ORIGIN = 8;

    public static final int ROTATION_LT = 9;
    public static final int ROTATION_RT = 10;
    public static final int ROTATION_RB = 11;
    public static final int ROTATION_LB = 12;

    public enum SelectionMode {
        normal, transform
    }
    protected SelectionMode mode = SelectionMode.normal;

    public NormalSelectionFollower(Entity entity) {
        super(entity);
    }

    @Override
    public void create() {
        facade = Overlap2DFacade.getInstance();
        tm = facade.retrieveProxy(EditorTextureManager.NAME);
        cursorManager = Overlap2DFacade.getInstance().retrieveProxy(CursorManager.NAME);

        transformGroup = new Group();
        transformGroup.setVisible(false);

        pixelRect = new PixelRect();
        pixelRect.setOpacity(0.4f);
        pixelRect.setTouchable(Touchable.disabled);
        addActor(pixelRect);
        addActor(transformGroup);
        initTransformGroup();

        hide();
    }

    private Actor getRotationAnchor() {
        EmptyTarget emptyTarget = new EmptyTarget(20, 20);
        transformGroup.addActor(emptyTarget);
        return emptyTarget;
    }

    private Image getMiniRect() {
        Image rect = new Image(VisUI.getSkin().getDrawable("selection-anchor"));
        int w = (int) (rect.getWidth()/2);
        int h = (int) (rect.getHeight()/2);
        rect.setOrigin(w, h);
        transformGroup.addActor(rect);
        return rect;
    }

    private void positionTransformables() {
        int w = (int) (miniRects[LT].getWidth()/2);
        int h = (int) (miniRects[LT].getHeight()/2);
        miniRects[LT].setX(-w);
        miniRects[LT].setY(getHeight() - h);
        miniRects[T].setX((int) (getWidth() / 2) - w);
        miniRects[T].setY(getHeight() - h);
        miniRects[RT].setX(getWidth() - w);
        miniRects[RT].setY(getHeight() - h);
        miniRects[R].setX(getWidth() - w);
        miniRects[R].setY((int) (getHeight() / 2) - h);
        miniRects[RB].setX(getWidth() - w);
        miniRects[RB].setY(-h);
        miniRects[B].setX((int) (getWidth() / 2) - w);
        miniRects[B].setY(-h);
        miniRects[LB].setX(-w);
        miniRects[LB].setY(-h);
        miniRects[L].setX(-w);
        miniRects[L].setY((int) (getHeight() / 2) - h);

        miniRects[ORIGIN].setX((int) (transformComponent.originX*transformComponent.scaleX) - w);
        miniRects[ORIGIN].setY((int) (transformComponent.originY*transformComponent.scaleY) - h);

        miniRects[ROTATION_LT].setX(-w*2);
        miniRects[ROTATION_LT].setY(getHeight());
        miniRects[ROTATION_RT].setX(getWidth());
        miniRects[ROTATION_RT].setY(getHeight());
        miniRects[ROTATION_RB].setX(getWidth());
        miniRects[ROTATION_RB].setY(-h*2);
        miniRects[ROTATION_LB].setX(-w * 2);
        miniRects[ROTATION_LB].setY(-h*2);
    }

    private void initTransformGroup() {
        miniRects = new Actor[13];

        // rotation
        miniRects[ROTATION_LT] = getRotationAnchor();
        miniRects[ROTATION_RT] = getRotationAnchor();
        miniRects[ROTATION_RB] = getRotationAnchor();
        miniRects[ROTATION_LB] = getRotationAnchor();

        //scale
        miniRects[LT] = getMiniRect();
        miniRects[T] = getMiniRect();
        miniRects[RT] = getMiniRect();
        miniRects[R] = getMiniRect();
        miniRects[RB] = getMiniRect();
        miniRects[B] = getMiniRect();
        miniRects[LB] = getMiniRect();
        miniRects[L] = getMiniRect();

        // origin point
        Image originAnchor = new Image(VisUI.getSkin().getDrawable("origin-anchor"));
        transformGroup.addActor(originAnchor);
        miniRects[ORIGIN] = originAnchor;

        //TODO: till it works bad, let's hide it
        miniRects[ORIGIN].setVisible(false);
    }

    @Override
    public void setFollowerListener(FollowerTransformationListener listener) {
        for(int i = 0; i < miniRects.length; i++) {
            final int rectId = i;
            miniRects[i].clearListeners();
            miniRects[i].addListener(new AnchorListener(this, listener, rectId) {
                @Override
                public void touchDragged(InputEvent event, float x, float y, int pointer) {
                    super.touchDragged(event, x, y, pointer);
                    update();
                }

                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    super.enter(event, x, y, pointer, fromActor);
                }

                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    super.exit(event, x, y, pointer, toActor);
                }
            });
        }
    }

    @Override
    public void clearFollowerListener() {
        for(int i = 0; i < miniRects.length; i++) {
            miniRects[i].clearListeners();
        }
    }

    @Override
    public void update() {
        super.update();

        //Update actor dimensions and positions, according to this actor width and height
        pixelRect.setWidth(getWidth());
        pixelRect.setHeight(getHeight());

        positionTransformables();

        for(int i = 0; i <= 7; i++) {
            miniRects[i].setRotation(-getRotation());
        }
    }

    @Override
    public void handleNotification(Notification notification) {
        super.handleNotification(notification);
        switch (notification.getName()) {
            case MsgAPI.TOOL_SELECTED:
                if(notification.getBody().equals(TransformTool.NAME)) {
                    setMode(SelectionMode.transform);
                } else {
                    setMode(SelectionMode.normal);
                }
                break;
        }
    }

    public void setMode(SelectionMode mode) {
        this.mode = mode;
        if(mode == SelectionMode.normal) {
            transformGroup.setVisible(false);
        } else {
            transformGroup.setVisible(true);
        }
    }

    public SelectionMode getMode() {
        return mode;
    }
}
