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

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by azakhary on 8/19/2015.
 */
public class PreviewWidget extends Group {

    private TextureAtlas.AtlasRegion region;

    float horizontalHeight, horizontalWidth;
    float verticalHeight, verticalWidth;
    float squareWidth, squareHeight;

    private Image horizontal, vertical, square;

    public PreviewWidget() {
        horizontalWidth = 200f;
        horizontalHeight = 50f;

        verticalWidth = 50f;
        verticalHeight = 150f;

        squareWidth = 145f;
        squareHeight = 150f;
    }

    public void update(TextureAtlas.AtlasRegion region, int[] splits) {
        this.region = region;
        clear();
        NinePatch horizontalPatch = new NinePatch(region, splits[0], splits[1], splits[2], splits[3]);
        NinePatch verticalPatch = new NinePatch(region, splits[0], splits[1], splits[2], splits[3]);
        NinePatch squarePatch = new NinePatch(region, splits[0], splits[1], splits[2], splits[3]);

        float minSclH = getMinScale(horizontalPatch, horizontalWidth, horizontalHeight);
        float minSclV = getMinScale(verticalPatch, verticalWidth, verticalHeight);
        float minSclS = getMinScale(squarePatch, squareWidth, squareHeight);

        float minScl = Math.min(minSclH, minSclV);
        minScl = Math.min(minScl, minSclS);

        horizontal = fitNinePatch(horizontalPatch, horizontalWidth, horizontalHeight, minScl);
        addActor(horizontal);

        vertical = fitNinePatch(verticalPatch, verticalWidth, verticalHeight, minScl);
        addActor(vertical);

        square = fitNinePatch(squarePatch, squareWidth, squareHeight, minScl);
        addActor(square);

        horizontal.setY(getHeight() - horizontalHeight);
        vertical.setY(horizontal.getY() - verticalHeight - 5);
        square.setX(verticalWidth + 5);
        square.setY(vertical.getY());
    }

    private float getMinScale(NinePatch horizontalPatch, float width, float height) {
        float scaleX = width/horizontalPatch.getTotalWidth();
        float scaleY = height/horizontalPatch.getTotalHeight();
        float scl = Math.min(scaleX, scaleY);
        if(scl > 1f) scl = 1f;

        return scl;
    }

    private Image fitNinePatch(NinePatch horizontalPatch, float width, float height, float scl) {
        horizontalPatch.scale(scl, scl);

        Image img = new Image(horizontalPatch);
        img.setScaleX(width / horizontalPatch.getTotalWidth());
        img.setScaleY(height / horizontalPatch.getTotalHeight());

        return img;
    }
}
