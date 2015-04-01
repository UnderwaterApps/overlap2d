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
import com.uwsoft.editor.mvc.proxy.TextureManager;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.ProjectManager;
import com.uwsoft.editor.renderer.Overlap2DStage;

public class BaseStage extends Overlap2DStage {

    private final Overlap2DFacade facade;
    public ProjectManager projectManager;
    public TextureManager textureManager;


    public BaseStage() {
        super(new ScreenViewport());
        facade = Overlap2DFacade.getInstance();
        projectManager = facade.retrieveProxy(ProjectManager.NAME);
        textureManager = facade.retrieveProxy(TextureManager.NAME);
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

    public ProjectManager getProjectManager() {
        return projectManager;
    }

    public void setCursor(int cursor) {
        //UIController.instance.sendNotification(NameConstants.SET_CURSOR, cursor);
    }

    public void resize(int width, int height) {
        getViewport().update(width, height, true);
    }
}
