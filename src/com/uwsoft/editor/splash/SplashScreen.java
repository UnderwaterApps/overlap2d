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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 * Created by azakhary on 5/15/2015.
 */
public class SplashScreen extends ApplicationAdapter {

    private Stage stage;
    private TextureAtlas atlas;


    private Image progressBarBg;
    private Image progressBar;

    private Label progress;
    private Label percent;

    public interface SplashListener {
        public void loadingComplete();
    }

    public SplashListener listener;

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
        graphic.setY(stage.getHeight() - graphic.getHeight() - 37);
        stage.addActor(graphic);

        Image logo = new Image(atlas.findRegion("logo"));
        logo.setX(stage.getWidth() / 2 - logo.getWidth() / 2);
        logo.setY(graphic.getY() - logo.getHeight()-3);
        stage.addActor(logo);

        progressBarBg = new Image(getNinePatch(atlas.findRegion("progressBg")));
        progressBar = new Image(getNinePatch(atlas.findRegion("progressBar")));
        progressBarBg.setWidth(stage.getWidth()-24);
        progressBarBg.setX(stage.getWidth() / 2 - progressBarBg.getWidth() / 2);
        progressBarBg.setY(89);
        progressBar.setWidth(6);
        progressBar.setX(progressBarBg.getX());
        progressBar.setY(progressBarBg.getY());
        stage.addActor(progressBarBg);
        stage.addActor(progressBar);

        Image separator = new Image(atlas.findRegion("devider"));
        separator.setScaleX(stage.getWidth()-24);
        separator.setX(stage.getWidth() / 2 - separator.getScaleX() / 2);
        separator.setY(61);
        stage.addActor(separator);

        BitmapFont robotFont = new BitmapFont(Gdx.files.internal("splash/roboto.fnt"));
        Label.LabelStyle labelStyle = new Label.LabelStyle(robotFont, new Color(224f/255f, 224f/255f, 224f/255f, 1f));

        Label companyName = new Label("Underwater Apps LLC", labelStyle);
        companyName.setX(13);
        companyName.setY(separator.getY() - companyName.getHeight() - 7);
        stage.addActor(companyName);

        Label copyright = new Label("Copyright (c) 2015. All rights reserved.", labelStyle);
        copyright.setX(13);
        copyright.setY(companyName.getY() - 20);
        stage.addActor(copyright);

        Label version = new Label("v 0.0.9", labelStyle);
        version.setX(stage.getWidth() - 13 - version.getWidth());
        version.setY(companyName.getY());
        stage.addActor(version);

        progress = new Label("Loading fonts", labelStyle);
        progress.setX(stage.getWidth()/2 - progress.getWidth()/2);
        progress.setY(progressBar.getY() + 11);
        stage.addActor(progress);

        percent = new Label("55%", labelStyle);
        percent.setX(stage.getWidth() - 13 - percent.getWidth());
        percent.setY(progressBar.getY() + 11);
        stage.addActor(percent);

        Image logoLbl = new Image(atlas.findRegion("logoText"));
        logoLbl.setX(stage.getWidth()/2 - logoLbl.getWidth()/2);
        logoLbl.setY(stage.getHeight()-24);
        stage.addActor(logoLbl);

        setProgress(0);
        setProgressStatus("Initializing");

        loadData();
    }

    private void loadData() {
        //TODO: do some server connecting here to check for new versions.

        if(listener != null) {
            setProgress(100);

            stage.addAction(Actions.sequence(Actions.delay(0.3f), Actions.run(() -> listener.loadingComplete())));
        }
    }

    public void setProgressStatus(String status) {
        progress.setText(status);
        progress.setX(stage.getWidth()/2 - progress.getWidth()/2);
    }

    public void setProgress(float percentNum) {
        percent.setText((int)percentNum + "%");
        percent.setX(stage.getWidth() - 13 - percent.getWidth());

        float newWidth = (percentNum/100f) * progressBarBg.getWidth();
        if(newWidth < 6) newWidth = 6;
        progressBar.clearActions();
        progressBar.addAction(Actions.sizeTo(newWidth, progressBar.getHeight(), 0.2f));
    }

    private NinePatch getNinePatch(TextureAtlas.AtlasRegion region) {
        return new NinePatch(region, region.splits[0], region.splits[1], region.splits[2], region.splits[3]);
    }

    @Override
    public void render () {
        stage.act();
        stage.draw();
    }
}
