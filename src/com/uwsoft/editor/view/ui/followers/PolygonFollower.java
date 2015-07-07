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
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.VisUI;
import com.uwsoft.editor.renderer.components.PolygonComponent;
import com.uwsoft.editor.renderer.utils.PolygonUtils;
import com.uwsoft.editor.utils.runtime.ComponentRetriever;
import com.uwsoft.editor.view.stage.Sandbox;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by azakhary on 7/2/2015.
 */
public class PolygonFollower extends SubFollower {

    private PolygonComponent polygonComponent;

    private ArrayList<Vector2> originalPoints;
    private Vector2[] drawPoints;
    protected Actor[] anchors;

    private ShapeRenderer shapeRenderer;

    public static final int CIRCLE_RADIUS = 10;

    private static final Color outlineColor = new Color(200f / 255f, 156f / 255f, 71f / 255f, 1f);
    private static final Color innerColor = new Color(200f / 255f, 200f / 255f, 200f / 255f, 0.2f);
    private static final Color overColor = new Color(255f / 255f, 94f / 255f, 0f / 255f, 1f);

    private int lineIndex = -1;
    public int draggingAnchorId = -1;

    private int selectedAnchorId = -1;

    OrthographicCamera runtimeCamera = Sandbox.getInstance().getCamera();

    public PolygonFollower(Entity entity) {
        super(entity);
        setTouchable(Touchable.enabled);
    }

    public void create() {
        polygonComponent = ComponentRetriever.get(entity, PolygonComponent.class);
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    protected void setStage(Stage stage) {
        super.setStage(stage);
        if (stage != null) {
            shapeRenderer.setProjectionMatrix(getStage().getCamera().combined);
        }
    }

    public void update() {
        if(polygonComponent != null && polygonComponent.vertices != null) {
            computeOriginalPoints();
            computeDrawPoints();
            if(selectedAnchorId == -1) selectedAnchorId = 0;
            initAnchors();
        }
    }

    public void updateDraw() {
        computeDrawPoints();
        initAnchors();
    }

    private void computeOriginalPoints() {
        originalPoints = new ArrayList<>();
        if(polygonComponent == null) return;

        originalPoints = new ArrayList<>(Arrays.asList(PolygonUtils.mergeTouchingPolygonsToOne(polygonComponent.vertices)));

    }

    private void computeDrawPoints() {
        drawPoints = originalPoints.toArray(new Vector2[0]);
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(polygonComponent != null && polygonComponent.vertices != null) {
            positionAnchors();
            batch.end();

            Gdx.gl.glLineWidth(1.7f);
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);


            shapeRenderer.setProjectionMatrix(getStage().getCamera().combined);
            Matrix4 matrix = batch.getTransformMatrix();
            matrix.scale(1f / runtimeCamera.zoom, 1f / runtimeCamera.zoom, 1f);
            shapeRenderer.setTransformMatrix(matrix);

            drawTriangulatedPolygons();
            drawOutlines();

            Gdx.gl.glDisable(GL20.GL_BLEND);

            batch.begin();

            drawPoints(batch, parentAlpha);
        }
    }

    public void drawOutlines() {
        if (drawPoints.length > 0) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            for (int i = 1; i < drawPoints.length; i++) {
                shapeRenderer.setColor(outlineColor);
                if (lineIndex == i) {
                    shapeRenderer.setColor(overColor);
                }
                shapeRenderer.line(drawPoints[i], drawPoints[i - 1]);
            }
            shapeRenderer.setColor(outlineColor);
            if(lineIndex == 0) {
                shapeRenderer.setColor(overColor);
            }
            shapeRenderer.line(drawPoints[drawPoints.length - 1], drawPoints[0]);
            shapeRenderer.end();
        }

    }

    public void drawTriangulatedPolygons() {
        if (polygonComponent.vertices == null) {
            return;
        }
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(innerColor);
        for (Vector2[] poly : polygonComponent.vertices) {
            for (int i = 1; i < poly.length; i++) {
                shapeRenderer.line(poly[i - 1], poly[i]);
            }
            if (poly.length > 0)
                shapeRenderer.line(poly[poly.length - 1].x, poly[poly.length - 1].y, poly[0].x, poly[0].y);
        }
        shapeRenderer.end();
    }

    public void drawPoints(Batch batch, float parentAlpha) {
        for (int i = 0; i < anchors.length; i++) {
            anchors[i].draw(batch, parentAlpha);
        }
    }

    private void positionAnchors() {
        for (int i = 0; i < anchors.length; i++) {
            anchors[i].setX(MathUtils.round(originalPoints.get(i).x - anchors[i].getWidth()/2f));
            anchors[i].setY(MathUtils.round(originalPoints.get(i).y - anchors[i].getHeight() / 2f));
            anchors[i].setScale(runtimeCamera.zoom);
        }
    }

    private void initAnchors() {
        anchors = new Actor[originalPoints.size()];
        for (int i = 0; i < originalPoints.size(); i++) {
            anchors[i] = getMiniRect();
        }
        setSelectedAnchor(selectedAnchorId);
    }

    private Image getMiniRect() {
        Image rect = new Image(VisUI.getSkin().getDrawable("selection-anchor"));
        int w = (int) (rect.getWidth()/2);
        int h = (int) (rect.getHeight()/2);
        rect.setOrigin(w, h);
        return rect;
    }

    public void setListener(final MeshTransformationListener listener) {
        clearListeners();
        addListener(new ClickListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                if(button != Input.Buttons.LEFT) return true;
                int anchorId = anchorHitTest(x, y);
                if (anchorId >= 0) {
                    draggingAnchorId = anchorId;
                    listener.anchorDown(PolygonFollower.this, anchorId, x*runtimeCamera.zoom, y*runtimeCamera.zoom);
                } else if (lineIndex > -1) {
                    // not anchor but line is selected gotta make new point
                    listener.vertexDown(PolygonFollower.this, lineIndex, x*runtimeCamera.zoom, y*runtimeCamera.zoom);
                }
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                int anchorId = draggingAnchorId;
                if (anchorId >= 0) {
                    listener.anchorDragged(PolygonFollower.this, anchorId, x*runtimeCamera.zoom, y*runtimeCamera.zoom);
                } else if (lineIndex > -1) {

                }
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(button != Input.Buttons.LEFT) return;
                int anchorId = anchorHitTest(x, y);
                lineIndex = vertexHitTest(x, y);
                if (anchorId >= 0) {
                    listener.anchorUp(PolygonFollower.this, anchorId, x*runtimeCamera.zoom, y*runtimeCamera.zoom);
                } else if (lineIndex > -1) {
                    listener.vertexUp(PolygonFollower.this, lineIndex, x*runtimeCamera.zoom, y*runtimeCamera.zoom);
                }
                draggingAnchorId = -1;
            }

            @Override
            public boolean mouseMoved(InputEvent event, float x, float y) {
                int anchorId = anchorHitTest(x, y);
                lineIndex = vertexHitTest(x, y);
                if(anchorId >= 0) {
                    lineIndex = -1;
                }
                if (lineIndex > -1) {

                }

                return super.mouseMoved(event, x, y);
            }
        });
    }

    @Override
    public Actor hit (float x, float y, boolean touchable) {
        if(anchors == null) return null;

        int anchorId = anchorHitTest(x, y);
        if(anchorId > -1) {
            return this;
        }

        // checking for vertex intersect
        lineIndex = vertexHitTest(x, y);
        if(lineIndex > -1) {
            return this;
        }

        return null;
    }

    private int vertexHitTest(float x, float y) {
        Vector2 tmpVector = new Vector2(x, y);
        int lineIndex = -1;

        for (int i = 1; i < drawPoints.length; i++) {
            Vector2 pointOne = drawPoints[i-1].cpy().scl(1f/runtimeCamera.zoom);
            Vector2 pointTwo = drawPoints[i].cpy().scl(1f/runtimeCamera.zoom);
            if (Intersector.intersectSegmentCircle(pointOne, pointTwo, tmpVector, CIRCLE_RADIUS*CIRCLE_RADIUS)) {
                lineIndex = i;
                break;
            }
        }
        Vector2 pointOne = drawPoints[drawPoints.length - 1].cpy().scl(1f/runtimeCamera.zoom);
        Vector2 pointTwo = drawPoints[0].cpy().scl(1f/runtimeCamera.zoom);
        if (drawPoints.length > 0 && Intersector.intersectSegmentCircle(pointOne, pointTwo, tmpVector, CIRCLE_RADIUS*CIRCLE_RADIUS)) {
            lineIndex = 0;
        }

        if(lineIndex > -1) {
            return lineIndex;
        }

        return -1;
    }

    private int anchorHitTest(float x, float y) {
        for (int i = 0; i < drawPoints.length; i++) {
            Circle pointCircle = new Circle(drawPoints[i].x/runtimeCamera.zoom, drawPoints[i].y/runtimeCamera.zoom, CIRCLE_RADIUS);
            if(pointCircle.contains(x, y)) {
                return i;
            }
        }
        return -1;
    }

    public Entity getEntity() {
        return entity;
    }

    public ArrayList<Vector2> getOriginalPoints() {
        return originalPoints;
    }

    public void setSelectedAnchor(int anchorId) {
        if(anchorId == -1) return;

        selectedAnchorId = anchorId;
        for (int i = 0; i < anchors.length; i++) {
            anchors[i].setColor(Color.WHITE);
        }
        anchors[selectedAnchorId].setColor(Color.ORANGE);
    }

    public int getSelectedAnchorId() {
        return selectedAnchorId;
    }

    public void getSelectedAnchorId(int id) {
        if(id < 0) id = 0;
        selectedAnchorId = id;
    }
}
