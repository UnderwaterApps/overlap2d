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

package com.uwsoft.editor.splash;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.kotcrab.vis.ui.widget.VisProgressBar;

/**
 * Created by azakhary on 5/15/2015.
 */
public class SplashScreen extends ApplicationAdapter {

    private Stage stage;
    private TextureAtlas atlas;

    @Override
    public void create () {
        atlas = new TextureAtlas(Gdx.files.internal("splash/splash.atlas"));

        stage = new Stage();
        NinePatch backgroundPatch = getNinePatch(atlas.findRegion("background"));
        Image background = new Image(backgroundPatch);
        background.setWidth(stage.getWidth() + 2);
        background.setX(-1);
        background.setY(-1);
        stage.addActor(background);

        Image graphic = new Image(atlas.findRegion("graphic"));
        graphic.setX(stage.getWidth() / 2 - graphic.getWidth() / 2);
        graphic.setY(stage.getHeight() - graphic.getHeight() - 41);
        stage.addActor(graphic);

        Image logo = new Image(atlas.findRegion("logo"));
        logo.setX(stage.getWidth() / 2 - logo.getWidth() / 2);
        logo.setY(graphic.getY() - logo.getHeight() - 6);
        stage.addActor(logo);

        ProgressBar.ProgressBarStyle prBarStyle = new ProgressBar.ProgressBarStyle();
        prBarStyle.background = new NinePatchDrawable(getNinePatch(atlas.findRegion("progressBg")));
        prBarStyle.knob = new NinePatchDrawable(getNinePatch(atlas.findRegion("progressBar")));
        VisProgressBar progressBar = new VisProgressBar(0, 100, 1, false, prBarStyle);
        progressBar.setValue(30);
        stage.addActor(progressBar);

    }

    private NinePatch getNinePatch(TextureAtlas.AtlasRegion region) {
        return new NinePatch(region, region.splits[0], region.splits[1], region.splits[2], region.splits[3]);
    }

    @Override
    public void render () {
        stage.draw();
    }
}
