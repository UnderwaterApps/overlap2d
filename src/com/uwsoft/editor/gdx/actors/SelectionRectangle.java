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

package com.uwsoft.editor.gdx.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.uwsoft.editor.mvc.proxy.TextureManager;
import com.uwsoft.editor.gdx.actors.basic.PixelRect;
import com.uwsoft.editor.gdx.sandbox.EditingMode;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.ProjectManager;
import com.uwsoft.editor.renderer.actor.IBaseItem;
import com.uwsoft.editor.renderer.actor.LabelItem;

import java.awt.*;

public class SelectionRectangle extends PixelRect {

    public static final int LT = 0;
    public static final int T = 1;
    public static final int RT = 2;
    public static final int R = 3;
    public static final int RB = 4;
    public static final int B = 5;
    public static final int LB = 6;
    public static final int L = 7;
    private final Overlap2DFacade facade;
    private final ProjectManager projectManager;
    private IBaseItem host;
    private TextureManager tm;
    private Sandbox sandbox;
    private float[] touchDiff = new float[2];
    private Group transformGroup;
    private Image[] miniRects = new Image[8];
    private EditingMode mode;

    public SelectionRectangle(Sandbox sandbox) {
        super(0, 0);
        facade = Overlap2DFacade.getInstance();
        projectManager = facade.retrieveProxy(ProjectManager.NAME);
        this.tm = facade.retrieveProxy(TextureManager.NAME);
        this.sandbox = sandbox;
        setTouchable(Touchable.disabled);
        setVisible(false);
        setOpacity(0.4f);

        transformGroup = new Group();
        addActor(transformGroup);
        initTransformGroup();
    }

    @Override
    public void act(float delta) {
        super.act(delta);


		  // change size according to zoom
		  OrthographicCamera camera = (OrthographicCamera)getStage().getCamera();
		  setThickness(camera.zoom);

        if (mode != EditingMode.TRANSFORM) return;

        Vector2 mouseCoords = getMouseLocalCoordinates();

        sandbox.setCurrentlyTransforming(null, -1);

        boolean isOver = false;

        for (int i = 0; i < 8; i++) {
            final int currRectIndex = i;
            Rectangle rect = new Rectangle(miniRects[currRectIndex].getX() - 2, miniRects[currRectIndex].getY() - 2, 8, 8);
            if (rect.contains(mouseCoords) && !(getHostAsActor() instanceof LabelItem)) {
                sandbox.setCurrentlyTransforming(getHost(), currRectIndex);
                isOver = true;
                switch (currRectIndex) {
                    case LT:
                        sandbox.getSandboxStage().setCursor(Cursor.NW_RESIZE_CURSOR);
                        break;
                    case T:
                        sandbox.getSandboxStage().setCursor(Cursor.N_RESIZE_CURSOR);
                        break;
                    case RT:
                        sandbox.getSandboxStage().setCursor(Cursor.NE_RESIZE_CURSOR);
                        break;
                    case R:
                        sandbox.getSandboxStage().setCursor(Cursor.E_RESIZE_CURSOR);
                        break;
                    case RB:
                        sandbox.getSandboxStage().setCursor(Cursor.SE_RESIZE_CURSOR);
                        break;
                    case B:
                        sandbox.getSandboxStage().setCursor(Cursor.S_RESIZE_CURSOR);
                        break;
                    case LB:
                        sandbox.getSandboxStage().setCursor(Cursor.SW_RESIZE_CURSOR);
                        break;
                    case L:
                        sandbox.getSandboxStage().setCursor(Cursor.W_RESIZE_CURSOR);
                        break;
                }
            }
        }

        if (!isOver) {
            sandbox.getSandboxStage().setCursor(Cursor.DEFAULT_CURSOR);
        }
    }

    private Vector2 getMouseLocalCoordinates() {
        Vector2 vec = new Vector2();

        vec.x = Gdx.input.getX() - (-sandbox.getSandboxStage().getCamera().position.x + sandbox.getSandboxStage().getWidth() / 2) - getX();
        vec.y = (sandbox.getSandboxStage().getHeight() - Gdx.input.getY()) - (-sandbox.getSandboxStage().getCamera().position.y + sandbox.getSandboxStage().getHeight() / 2) - getY();

        return vec;
    }

    private void initTransformGroup() {
        miniRects[LT] = getMiniRect();
        miniRects[T] = getMiniRect();
        miniRects[RT] = getMiniRect();
        miniRects[R] = getMiniRect();
        miniRects[RB] = getMiniRect();
        miniRects[B] = getMiniRect();
        miniRects[LB] = getMiniRect();
        miniRects[L] = getMiniRect();
    }

    private void positionTransformables() {
        miniRects[LT].setX(-3);
        miniRects[LT].setY(getHeight() - 2);
        miniRects[T].setX(getWidth() / 2 - 3);
        miniRects[T].setY(getHeight() - 2);
        miniRects[RT].setX(getWidth() - 3);
        miniRects[RT].setY(getHeight() - 2);
        miniRects[R].setX(getWidth() - 3);
        miniRects[R].setY(getHeight() / 2 - 2);
        miniRects[RB].setX(getWidth() - 3);
        miniRects[RB].setY(-2);
        miniRects[B].setX(getWidth() / 2 - 3);
        miniRects[B].setY(-2);
        miniRects[LB].setX(-3);
        miniRects[LB].setY(-2);
        miniRects[L].setX(-3);
        miniRects[L].setY(getHeight() / 2 - 2);
    }

    private Image getMiniRect() {
        Image rect = new Image(tm.getEditorAsset("pixel"));
        rect.setScale(6);
        rect.setColor(new Color(1, 1, 1, 1));

        transformGroup.addActor(rect);

        return rect;
    }

    public void claim(IBaseItem itm) {
        host = itm;
        Actor hostAsActor = getHostAsActor();
        setX(hostAsActor.getX());
        setY(hostAsActor.getY());
        setRotation(hostAsActor.getRotation());
        setWidth(hostAsActor.getWidth() * (hostAsActor instanceof LabelItem ? 1 : hostAsActor.getScaleX()));
        setHeight(hostAsActor.getHeight() * (hostAsActor instanceof LabelItem ? 1 : hostAsActor.getScaleY()));
    }

    public void update() {
        //setX(getHostAsActor().getX() - ((getHostAsActor().getScaleX()-1)*getHostAsActor().getWidth()/2));
        //setY(getHostAsActor().getY() - ((getHostAsActor().getScaleY()-1)*getHostAsActor().getHeight()/2));
        Actor hostAsActor = getHostAsActor();
        setX(hostAsActor.getX());
        setY(hostAsActor.getY());
        setOriginX(hostAsActor.getOriginX());
        setOriginY(hostAsActor.getOriginY());
        setRotation(hostAsActor.getRotation());
        setWidth(hostAsActor.getWidth() * (hostAsActor instanceof LabelItem ? 1 : hostAsActor.getScaleX()));
        setHeight(hostAsActor.getHeight() * (hostAsActor instanceof LabelItem ? 1 : hostAsActor.getScaleY()));

        positionTransformables();
    }

    public void show() {
        update();

        setVisible(true);
    }

    public void hide() {
        setVisible(false);
    }

    public void release() {
        host = null;
        setVisible(false);
        remove();
    }

    public IBaseItem getHost() {
        return host;
    }

    public Actor getHostAsActor() {
        return (Actor) host;
    }

    public void setTouchDiff(float x, float y) {
        touchDiff[0] = x;
        touchDiff[1] = y;
    }

    public float[] getTouchDiff() {
        return touchDiff;
    }

    public void setMode(EditingMode mode) {
        this.mode = mode;
        if (mode == EditingMode.TRANSFORM && !(getHostAsActor() instanceof LabelItem)) {
            transformGroup.setVisible(true);
        } else {
            transformGroup.setVisible(false);
        }
    }

}
