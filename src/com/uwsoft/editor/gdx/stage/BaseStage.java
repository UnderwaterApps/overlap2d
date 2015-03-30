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

package com.uwsoft.editor.gdx.stage;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.uwsoft.editor.data.manager.DataManager;
import com.uwsoft.editor.data.manager.TextureManager;
import com.uwsoft.editor.renderer.Overlap2DStage;

public class BaseStage extends Overlap2DStage {

    public DataManager dataManager;
    public TextureManager textureManager;


    public BaseStage() {
        super(new ScreenViewport());
        dataManager = DataManager.getInstance();
        textureManager = dataManager.textureManager;
        initLightsConfiguration();


    }

    public void addActor(Actor actor) {
        super.addActor(actor);
    }

    public void draw() {
        super.draw();

    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public void setCursor(int cursor) {
        //UIController.instance.sendNotification(NameConstants.SET_CURSOR, cursor);
    }

    public void disableAmbience(boolean disable) {
        if (disable) {
            essentials.rayHandler.setAmbientLight(0.5f, 0.5f, 0.5f, 1f);
        } else {
            setAmbienceInfo(sceneLoader.getSceneVO());
        }
    }

    public void resize(int width, int height) {
        getViewport().update(width, height, true);
    }
}
