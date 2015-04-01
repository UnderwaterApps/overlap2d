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

package com.uwsoft.editor.gdx.ui.properties;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.uwsoft.editor.mvc.proxy.TextureManager;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.ProjectManager;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.actor.ParticleActor;
import com.uwsoft.editor.renderer.actor.ParticleItem;
import com.uwsoft.editor.renderer.data.ParticleEffectVO;
import com.uwsoft.editor.renderer.resources.IResourceRetriever;

/**
 * Created by azakhary on 7/2/2014.
 */
public class ParticleItemProperties extends Group implements IPropertyBox<ParticleItem> {

    private final Overlap2DFacade facade;
    private final ProjectManager projectManager;
    private Group mainGroup;

    private IResourceRetriever rm;

    public ParticleItemProperties(SceneLoader scene) {
        rm = scene.getRm();
        initView();
        facade = Overlap2DFacade.getInstance();
        projectManager = facade.retrieveProxy(ProjectManager.NAME);
    }

    @Override
    public void initView() {
        clear();
        TextureManager textureManager = facade.retrieveProxy(TextureManager.NAME);
        Image bgImg = new Image(textureManager.getEditorAsset("pixel"));
        bgImg.setColor(0, 0, 0, 1.0f);
        bgImg.setScale(230, 100);
        addActor(bgImg);
        setWidth(230);
        setHeight(100);

        mainGroup = new Group();
        addActor(mainGroup);
    }


    @Override
    public void updateView() {

    }

    @Override
    public void setObject(ParticleItem object) {
        mainGroup.clear();
        ParticleActor particle = new ParticleActor(rm.getParticleEffect(((ParticleEffectVO)object.getDataVO()).particleName));
        Array<ParticleEmitter> emitters = particle.getParticleEffect().getEmitters();
        for(int i = 0; i < emitters.size; i++) {
            emitters.get(i).setContinuous(true);
        }
        particle.start();
        mainGroup.addActor(particle);
        particle.setX(getWidth()/2);
        particle.setY(getHeight() / 2);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        boolean t = clipBegin(getX(),getY(), getWidth(), getHeight());
        if(t){
            super.draw(batch, parentAlpha);
            batch.flush();
            clipEnd();
            return;
        }
        super.draw(batch, parentAlpha);
    }
}
