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

package com.uwsoft.editor.mvc.view.ui.followers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.VisUI;
import com.uwsoft.editor.gdx.actors.basic.PixelRect;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.EditorTextureManager;

/**
 * Created by azakhary on 5/20/2015.
 */
public class NormalSelectionFollower extends BasicFollower {

    private Overlap2DFacade facade;
    private EditorTextureManager tm;

    private PixelRect pixelRect;

    private Group transformGroup;
    private Image[] miniRects;

    public static final int LT = 0;
    public static final int T = 1;
    public static final int RT = 2;
    public static final int R = 3;
    public static final int RB = 4;
    public static final int B = 5;
    public static final int LB = 6;
    public static final int L = 7;

    public static final int ORIGIN = 8;

    public NormalSelectionFollower(Entity entity) {
        super(entity);
    }

    @Override
    public void create() {
        facade = Overlap2DFacade.getInstance();
        tm = facade.retrieveProxy(EditorTextureManager.NAME);

        transformGroup = new Group();
        transformGroup.setVisible(false);

        pixelRect = new PixelRect();
        pixelRect.setOpacity(0.4f);
        addActor(pixelRect);
        addActor(transformGroup);
        initTransformGroup();

        hide();
    }

    private Image getMiniRect() {
        Image rect = new Image(VisUI.getSkin().getDrawable("selection-anchor"));
        transformGroup.addActor(rect);
        return rect;
    }

    private void positionTransformables() {
        int w = (int) (miniRects[LT].getWidth()/2);
        int h = (int) (miniRects[LT].getHeight()/2);
        miniRects[LT].setX(-w);
        miniRects[LT].setY(getHeight() - h);
        miniRects[T].setX((int)(getWidth() / 2) - w);
        miniRects[T].setY(getHeight() - h);
        miniRects[RT].setX(getWidth() - w);
        miniRects[RT].setY(getHeight() - h);
        miniRects[R].setX(getWidth() - w);
        miniRects[R].setY((int)(getHeight() / 2) - h);
        miniRects[RB].setX(getWidth() - w);
        miniRects[RB].setY(-h);
        miniRects[B].setX((int)(getWidth() / 2) - w);
        miniRects[B].setY(-h);
        miniRects[LB].setX(-w);
        miniRects[LB].setY(-h);
        miniRects[L].setX(-w);
        miniRects[L].setY((int)(getHeight() / 2) - h);

        miniRects[ORIGIN].setX((int) (getWidth() / 2 - w));
        miniRects[ORIGIN].setY((int)(getHeight()/2-h));
    }

    private void initTransformGroup() {
        miniRects = new Image[9];
        miniRects[LT] = getMiniRect();
        miniRects[T] = getMiniRect();
        miniRects[RT] = getMiniRect();
        miniRects[R] = getMiniRect();
        miniRects[RB] = getMiniRect();
        miniRects[B] = getMiniRect();
        miniRects[LB] = getMiniRect();
        miniRects[L] = getMiniRect();

        Image originAnchor = new Image(VisUI.getSkin().getDrawable("origin-anchor"));
        transformGroup.addActor(originAnchor);
        miniRects[ORIGIN] = originAnchor;

        for(int i = 0; i < miniRects.length; i++) {
            final int rectId = i;
            miniRects[i].addListener(new ClickListener() {
                @Override
                public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                    super.touchDown(event, x, y, pointer, button);
                    listener.anchorDown(rectId, event.getStageX(), event.getStageY());
                    event.stop();
                    return true;
                }
                @Override
                public void touchDragged (InputEvent event, float x, float y, int pointer) {
                    listener.anchorDragged(rectId, event.getStageX(), event.getStageY());
                    update();
                }
                @Override
                public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                    listener.anchorUp(rectId, event.getStageX(), event.getStageY());
                }
            });
        }
    }

    @Override
    public void update() {
        super.update();

        //Update actor dimensions and positions, according to this actor width and height
        pixelRect.setWidth(getWidth());
        pixelRect.setHeight(getHeight());

        positionTransformables();
    }

    @Override
    public void setMode(FollowerMode mode) {
        super.setMode(mode);

        if(mode == FollowerMode.normal) {
            transformGroup.setVisible(false);
        } else {
            transformGroup.setVisible(true);
        }
    }
}
