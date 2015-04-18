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

package com.uwsoft.editor.gdx.ui;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.uwsoft.editor.gdx.ui.thumbnailbox.ParticleThumbnailBox;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.TextureManager;
import com.uwsoft.editor.mvc.view.stage.UIStage;

import java.util.HashMap;

public class ParticleList extends Group {

    private final Overlap2DFacade facade;
    private final TextureManager textureManager;

    public ParticleList(float width, float height) {
        facade = Overlap2DFacade.getInstance();
        textureManager = facade.retrieveProxy(TextureManager.NAME);
        this.setWidth(width);
        this.setHeight(height);
        final Table container = new Table();
        Table table = new Table();
        container.setX(0);
        container.setY(0);
        container.setWidth(getWidth() - 1);
        container.setHeight(getHeight() - 20);
        final ScrollPane scroll = new ScrollPane(table, textureManager.editorSkin);
        container.add(scroll).colspan(4).width(getWidth());
        container.row();
        scroll.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                event.stop();
                return true;
            }
        });
        scroll.setHeight(getHeight() - 20);
        scroll.setFlickScroll(false);

        // change to particle list
        HashMap<String, ParticleEffect> particles = textureManager.getProjectParticleList();

        if (particles == null) return;

        int i = 0;
        for (String key : particles.keySet()) {
            final ParticleThumbnailBox thumb = new ParticleThumbnailBox(key);

            final String pname = key;

            table.add(thumb).pad(3);
            if ((i - 7) % 4 == 0) {

                table.row();
            }
            i++;
        }


        addActor(container);
    }
}
