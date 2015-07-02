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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import com.uwsoft.editor.renderer.components.MeshComponent;
import com.uwsoft.editor.renderer.physics.PhysicsBodyLoader;
import com.uwsoft.editor.utils.runtime.ComponentRetriever;
import com.uwsoft.editor.view.stage.Sandbox;

import java.util.ArrayList;

/**
 * Created by azakhary on 7/2/2015.
 */
public class MeshFollower extends SubFollower {

    private MeshComponent meshComponent;

    private Vector2[] points = new Vector2[0];

    private ShapeRenderer shapeRenderer;
    public static final int POINT_WIDTH = 10;
    public static final int CIRCLE_RADIUS = 10;

    public MeshFollower(Entity entity) {
        super(entity);
    }

    public void create() {
        meshComponent = ComponentRetriever.get(entity, MeshComponent.class);

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
        if(meshComponent != null && meshComponent.vertices != null) {
            computePoints();
        }
    }

    private void computePoints() {
        Array<Vector2> pointList = new Array<>();
        for (Vector2[] poly : meshComponent.vertices) {
            for (int i = 0; i < poly.length; i++) {
                if (!pointList.contains(poly[i], false))
                    pointList.add(poly[i]);
            }
        }
        points = pointList.toArray(Vector2.class);
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        if(meshComponent != null && meshComponent.vertices != null) {
            computePoints();

            batch.end();

            Gdx.gl.glLineWidth(2);
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

            shapeRenderer.setProjectionMatrix(getStage().getCamera().combined);
            shapeRenderer.setTransformMatrix(batch.getTransformMatrix());

            drawPolygon();
            drawOutlines();
            drawPoints();

            Gdx.gl.glDisable(GL20.GL_BLEND);

            batch.begin();
        }
    }

    public void drawOutlines() {
        if (points.length > 0) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.RED);
            for (int i = 1; i < points.length; i++) {
                shapeRenderer.line(points[i], points[i - 1]);
                shapeRenderer.setColor(Color.BLUE);
            }
            shapeRenderer.end();
        }

    }

    public void drawPolygon() {
        if (meshComponent.vertices == null) {
            return;
        }
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.YELLOW);
        for (Vector2[] poly : meshComponent.vertices) {
            for (int i = 1; i < poly.length; i++) {
                shapeRenderer.line(poly[i - 1], poly[i]);
            }
            if (poly.length > 0)
                shapeRenderer.line(poly[poly.length - 1].x, poly[poly.length - 1].y, poly[0].x, poly[0].y);
        }
        shapeRenderer.end();
    }

    public void drawPoints() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0, 0, 0.7f, 1);
        for (int i = 0; i < points.length; i++) {
            shapeRenderer.rect(points[i].x - POINT_WIDTH / 2, points[i].y - POINT_WIDTH / 2, POINT_WIDTH, POINT_WIDTH);
        }
        shapeRenderer.end();
    }
}
