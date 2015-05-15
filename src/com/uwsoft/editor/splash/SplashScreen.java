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
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

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
        NinePatch backgroundPatch = new NinePatch(atlas.findRegion("background"), atlas.findRegion("background").splits[0], atlas.findRegion("background").splits[1], atlas.findRegion("background").splits[2], atlas.findRegion("background").splits[3]);
        Image background = new Image(backgroundPatch);
        background.setWidth(stage.getWidth()+2);
        background.setX(-1);
        background.setY(-1);

        stage.addActor(background);
    }

    @Override
    public void render () {
        stage.draw();
    }
}
