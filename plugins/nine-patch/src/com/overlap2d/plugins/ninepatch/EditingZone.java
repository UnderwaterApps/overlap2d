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

package com.overlap2d.plugins.ninepatch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;


/**
 * Created by azakhary on 8/18/2015.
 */
public class EditingZone extends Actor {

    private ShapeRenderer shapeRenderer;
    private TextureRegion texture;

    private static final Color BG = new Color(43f / 255f, 43f / 255f, 43f / 255f, 1f);
    private static final Color GUIDE_COLOR = new Color(255f/255f, 94f/255f, 0f/255f, 0.5f);
    private static final Color OVER_GUIDE_COLOR = new Color(255f/255f, 173f/255f, 125f/255f, 1f);

    private float currZoom = 1f;
    private Vector2 shift = new Vector2(0, 0);

    private int mouseOverSplit = -1;

    private float[] splitPositions = new float[4];
    private int[] splits = new int[4];

    public interface PatchChangeListener {
        public void changed(int[] splits);
    }

    private PatchChangeListener listener;

    public EditingZone() {
        shapeRenderer = new ShapeRenderer();

        addListener(new InputListener() {
            private Vector2 lastPoint;
            private int selectedSplit = -1;

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                selectedSplit = splitCollision(x, y);
                if(selectedSplit >= 0) {

                } else {
                    lastPoint = new Vector2(x, y);
                }
                return true;
            }

            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (selectedSplit >= 0) {
                    if(selectedSplit == 0) { //left
                        splits[0] = (int) ((x-shift.x-getWidth()/2f)/currZoom+texture.getRegionWidth()/2f);
                        if(splits[0] > texture.getRegionWidth()-splits[1]) {
                            int tmp = splits[1]; splits[1] = texture.getRegionWidth()-splits[0]; splits[0] = texture.getRegionWidth() - tmp;
                            selectedSplit = 1;
                            mouseOverSplit = selectedSplit;
                        }
                        splitUpdate();
                        return;
                    }
                    if(selectedSplit == 1) {
                        splits[1] = -(int) ((x-shift.x-getWidth()/2f)/currZoom-texture.getRegionWidth()/2f);
                        if(texture.getRegionWidth()-splits[1] < splits[0]) {
                            int tmp = splits[0]; splits[0] = texture.getRegionWidth() - splits[1]; splits[1] = texture.getRegionWidth() - tmp;
                            selectedSplit = 0;
                            mouseOverSplit = selectedSplit;
                        }
                        splitUpdate();
                        return;
                    }
                    if(selectedSplit == 2) { // top
                        splits[2] = -(int) ((y-shift.y-getHeight()/2f)/currZoom-texture.getRegionHeight()/2f);
                        if(texture.getRegionHeight()-splits[2] < splits[3]) {
                            int tmp = splits[2]; splits[2] = texture.getRegionHeight()-splits[3]; splits[3] = texture.getRegionHeight()-tmp;
                            selectedSplit = 3;
                            mouseOverSplit = selectedSplit;
                        }
                        splitUpdate();
                        return;
                    }
                    if(selectedSplit == 3) {
                        splits[3] = (int) ((y-shift.y-getHeight()/2f)/currZoom+texture.getRegionHeight()/2f);
                        if(splits[3] > texture.getRegionHeight()-splits[2]) {
                            int tmp = splits[3]; splits[3] = texture.getRegionHeight()-splits[2]; splits[2] = texture.getRegionHeight()-tmp;
                            selectedSplit = 2;
                            mouseOverSplit = selectedSplit;
                        }
                        splitUpdate();
                        return;
                    }
                } else {
                    Vector2 diff = new Vector2(x - lastPoint.x, y - lastPoint.y);
                    shiftBy(diff);
                    lastPoint = new Vector2(x, y);
                }
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                selectedSplit = -1;

                if(listener != null) {
                    listener.changed(splits.clone());
                }
            }

            public boolean mouseMoved(InputEvent event, float x, float y) {
                mouseOverSplit = splitCollision(x, y);
                return false;
            }
        });
    }

    public void setListener(PatchChangeListener listener) {
        this.listener = listener;
    }

    public void splitUpdate() {
        if(splits[0] < 0) splits[0] = 0;
        if(splits[1] < 0) splits[1] = 0;
        if(splits[2] < 0) splits[2] = 0;
        if(splits[3] < 0) splits[3] = 0;
    }

    public int[] getSplits() {
        return splits.clone();
    }

    public void setTexture(TextureRegion texture) {
        this.texture = texture;

        splits[0] = ((TextureAtlas.AtlasRegion)texture).splits[0];
        splits[1] = ((TextureAtlas.AtlasRegion)texture).splits[1];
        splits[2] = ((TextureAtlas.AtlasRegion)texture).splits[2];
        splits[3] = ((TextureAtlas.AtlasRegion)texture).splits[3];
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        Rectangle scissors = new Rectangle();
        Rectangle clipBounds = new Rectangle(getX(),getY(),getWidth(),getHeight());
        ScissorStack.calculateScissors(getStage().getCamera(), batch.getTransformMatrix(), clipBounds, scissors);
        ScissorStack.pushScissors(scissors);

        drawBg(batch, parentAlpha);

        batch.draw(texture,
                getX() + getWidth() / 2 - texture.getRegionWidth() / 2 + shift.x,
                getY() + getHeight() / 2 - texture.getRegionHeight() / 2 + shift.y,
                texture.getRegionWidth() / 2f,
                texture.getRegionHeight() / 2f,
                texture.getRegionWidth(), texture.getRegionHeight(),
                currZoom, currZoom, 0);

        drawSplits(batch, parentAlpha);

        batch.flush();
        ScissorStack.popScissors();
    }

    public void drawBg(Batch batch, float parentAlpha) {
        batch.end();
        Gdx.gl.glLineWidth(1.0f);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.setProjectionMatrix(getStage().getCamera().combined);
        Matrix4 matrix = batch.getTransformMatrix();
        shapeRenderer.setTransformMatrix(matrix);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        BG.a = parentAlpha;
        shapeRenderer.setColor(BG);
        shapeRenderer.rect(getX(), getY(), getWidth(), getHeight());
        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);
        batch.begin();
        batch.setColor(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, Color.WHITE.a * parentAlpha);
    }

    public void drawSplits(Batch batch, float parentAlpha) {
        batch.end();
        Gdx.gl.glLineWidth(1.0f);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.setProjectionMatrix(getStage().getCamera().combined);
        Matrix4 matrix = batch.getTransformMatrix();
        shapeRenderer.setTransformMatrix(matrix);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        // left, right, top , bottom
        Color guideColor = new Color(GUIDE_COLOR);
        guideColor.a*=parentAlpha;
        Color overColor = new Color(OVER_GUIDE_COLOR);
        overColor.a*=parentAlpha;

        splitPositions[0] = shift.x + getWidth() / 2f + (-texture.getRegionWidth() / 2f + splits[0]) * currZoom;
        splitPositions[1] = shift.x + getWidth() / 2f + (texture.getRegionWidth() / 2f - splits[1]) * currZoom;
        splitPositions[2] = shift.y + getHeight()/2f + (texture.getRegionHeight()/2 - splits[2])*currZoom;
        splitPositions[3] = shift.y + getHeight() / 2f + (-texture.getRegionHeight() / 2 + splits[3]) * currZoom;


        if(mouseOverSplit == 0) shapeRenderer.setColor(overColor); else shapeRenderer.setColor(guideColor);
        shapeRenderer.line(getX() + splitPositions[0], getY(), getX() + splitPositions[0], getY() + getHeight());

        if(mouseOverSplit == 1) shapeRenderer.setColor(overColor); else shapeRenderer.setColor(guideColor);
        shapeRenderer.line(getX() + splitPositions[1], getY(), getX() + splitPositions[1], getY() + getHeight());

        if(mouseOverSplit == 2) shapeRenderer.setColor(overColor); else shapeRenderer.setColor(guideColor);
        shapeRenderer.line(getX(), getY() + splitPositions[2], getX() + getWidth(), getY() + splitPositions[2]);

        if(mouseOverSplit == 3) shapeRenderer.setColor(overColor); else shapeRenderer.setColor(guideColor);
        shapeRenderer.line(getX(), getY() + splitPositions[3], getX() + getWidth(), getY() + splitPositions[3]);

        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        batch.begin();
        batch.setColor(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, Color.WHITE.a * parentAlpha);
    }

    public void zoomBy(int amount) {
        float defaultSize = 1.0f - amount*0.12f;
        currZoom*=defaultSize;
    }

    public void shiftBy(Vector2 diff) {
        shift.add(diff);
    }

    public int splitCollision(float x, float y) {
        Circle touchCircle =  new Circle();
        touchCircle.radius = 5f;
        touchCircle.setPosition(x, y);

        if(touchCircle.contains(splitPositions[0], touchCircle.y)) {
            return 0;
        }
        if(touchCircle.contains(splitPositions[1], touchCircle.y)) {
            return 1;
        }
        if(touchCircle.contains(touchCircle.x, splitPositions[2])) {
            return 2;
        }
        if(touchCircle.contains(touchCircle.x, splitPositions[3])) {
            return 3;
        }

        return -1;
    }
}
