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

package com.uwsoft.editor.data.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Json;
import com.uwsoft.editor.data.SpineAnimData;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.DataManager;
import com.uwsoft.editor.mvc.proxy.SceneDataManager;
import com.uwsoft.editor.mvc.proxy.TextureManager;
import com.uwsoft.editor.renderer.data.ProjectInfoVO;
import com.uwsoft.editor.renderer.data.SceneVO;
import com.uwsoft.editor.renderer.resources.IResourceRetriever;
import com.uwsoft.editor.renderer.utils.MySkin;

public class SandboxResourceManager implements IResourceRetriever {
    private final DataManager dataManager;
    private final TextureManager textureManager;
    private final Overlap2DFacade facade;

    public SandboxResourceManager() {
        facade = Overlap2DFacade.getInstance();
        dataManager = facade.retrieveProxy(DataManager.NAME);
        textureManager = facade.retrieveProxy(TextureManager.NAME);
    }

    @Override
    public MySkin getSkin() {
        return textureManager.projectSkin;
    }

    @Override
    public TextureRegion getTextureRegion(String name) {
        TextureAtlas atl = textureManager.getProjectAssetsList();
        TextureRegion reg = atl.findRegion(name);
        if (reg == null) {
            reg = textureManager.getEditorAsset(name);
        }
        return reg;
    }

    @Override
    public ProjectInfoVO getProjectVO() {
        return dataManager.getCurrentProjectInfoVO();
    }

    @Override
    public ParticleEffect getParticleEffect(String name) {
        return textureManager.getParticle(name);
    }

    @Override
    public TextureAtlas getSkeletonAtlas(String animationName) {
        SpineAnimData animData = textureManager.getProjectSpineAnimationsList().get(animationName);
        return animData.atlas;
    }

    @Override
    public TextureAtlas getSpriteAnimation(String animationName) {
        return textureManager.getProjectSpriteAnimationsList().get(animationName);
    }

    @Override
    public FileHandle getSkeletonJSON(String animationName) {
        SpineAnimData animData = textureManager.getProjectSpineAnimationsList().get(animationName);
        return animData.jsonFile;
    }


    @Override
    public BitmapFont getBitmapFont(String fontName, int fontSize) {
        return textureManager.getBitmapFont(fontName, fontSize);
    }

    @Override
    public SceneVO getSceneVO(String name) {
        SceneDataManager sceneDataManager = facade.retrieveProxy(SceneDataManager.NAME);
        FileHandle file = Gdx.files.internal(sceneDataManager.getCurrProjectScenePathByName(name));
        Json json = new Json();
        SceneVO sceneVO = json.fromJson(SceneVO.class, file.readString());

        return sceneVO;
    }

    @Override
    public FileHandle getSCMLFile(String name) {
        return textureManager.getProjectSpriterAnimationsList().get(name);
    }


}
