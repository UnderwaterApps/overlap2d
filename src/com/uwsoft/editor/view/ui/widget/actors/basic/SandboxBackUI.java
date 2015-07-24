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

package com.uwsoft.editor.view.ui.widget.actors.basic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.proxy.ResourceManager;
import com.uwsoft.editor.view.ui.widget.actors.GridView;

/**
 * Created by azakhary on 6/10/2015.
 */
public class SandboxBackUI {

    private Array<Actor> actors = new Array<>();

    private Batch batch;

    public SandboxBackUI(Batch batch) {
        this.batch = batch;

        GridView gridView = new GridView();
        addActor(gridView);
    }

    public void addActor(Actor actor) {
        actors.add(actor);
    }

    public void render(float delta) {
        ResourceManager resourceManager = Overlap2DFacade.getInstance().retrieveProxy(ResourceManager.NAME);
        batch.begin();
        for (Actor actor : actors) {
            actor.setScale(1f/resourceManager.getProjectVO().pixelToWorld);
            actor.act(delta);
            actor.draw(batch, 1);
        }
        batch.setColor(Color.WHITE);
        batch.end();
    }
}
