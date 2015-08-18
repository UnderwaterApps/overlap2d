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
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Created by azakhary on 8/18/2015.
 */
public class EditingZone extends Actor {

    private ShapeRenderer shapeRenderer;
    private TextureRegion texture;

    private static final Color BG = new Color(43f / 255f, 43f / 255f, 43f / 255f, 1f);
    private static final Color GUIDE_COLOR = new Color(255f/255f, 94f/255f, 0f/255f, 0.5f);
    private static final Color OVER_GUIDE_COLOR = new Color(255f/255f, 173f/255f, 125f/255f, 1f);

    public EditingZone() {
        shapeRenderer = new ShapeRenderer();
    }

    public void setTexture(TextureRegion texture) {
        this.texture = texture;
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        drawBg(batch, parentAlpha);
        batch.draw(texture, getX() + getWidth() / 2 - texture.getRegionWidth() / 2, getY() + getHeight() / 2 - texture.getRegionHeight() / 2);
        drawSplits(batch, parentAlpha);
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
        int splits[] = ((TextureAtlas.AtlasRegion)texture).splits;

        shapeRenderer.setColor(GUIDE_COLOR);
        shapeRenderer.line(getX() + getWidth() / 2f - texture.getRegionWidth() / 2f + splits[0], getY(), getX() + getWidth() / 2f - texture.getRegionWidth() / 2f + splits[0], getY() + getHeight());

        shapeRenderer.setColor(GUIDE_COLOR);
        shapeRenderer.line(getX() + getWidth() / 2f + texture.getRegionWidth() / 2f - splits[1], getY(), getX() + getWidth() / 2f + texture.getRegionWidth() / 2f - splits[1], getY() + getHeight());

        shapeRenderer.setColor(GUIDE_COLOR);
        shapeRenderer.line(getX(), getY() + getHeight()/2f + texture.getRegionHeight()/2 - splits[2], getX()+getWidth(), getY() + getHeight()/2f + texture.getRegionHeight()/2 - splits[2]);

        shapeRenderer.setColor(GUIDE_COLOR);
        shapeRenderer.line(getX(), getY() + getHeight() / 2f - texture.getRegionHeight() / 2 + splits[3], getX() + getWidth(), getY() + getHeight() / 2f - texture.getRegionHeight() / 2 + splits[3]);

        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        batch.begin();
        batch.setColor(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, Color.WHITE.a * parentAlpha);
    }

    public void zoomBy(int amount) {
        System.out.println(amount);
    }
}
