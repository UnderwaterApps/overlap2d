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

package com.uwsoft.editor.mvc.proxy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Json;
import com.uwsoft.editor.data.SpineAnimData;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.renderer.data.ProjectInfoVO;
import com.uwsoft.editor.renderer.data.SceneVO;
import com.uwsoft.editor.renderer.resources.IResourceRetriever;
import com.uwsoft.editor.renderer.utils.MySkin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class EditorResourceManager implements IResourceRetriever {

    private final ProjectManager projectManager;
    private final Overlap2DFacade facade;
    private HashMap<String, BitmapFont> bitmapFonts = new HashMap<String, BitmapFont>();

    public EditorResourceManager() {
        facade = Overlap2DFacade.getInstance();
        projectManager = facade.retrieveProxy(ProjectManager.NAME);
    }

    public TextureAtlas getAtlas() {
        TextureManager textureManager = facade.retrieveProxy(TextureManager.NAME);
        return textureManager.getEditorAssetsList();
    }

    @Override
    public MySkin getSkin() {
        TextureManager textureManager = facade.retrieveProxy(TextureManager.NAME);
        return textureManager.editorSkin;
    }

    @Override
    public TextureRegion getTextureRegion(String name) {
        TextureManager textureManager = facade.retrieveProxy(TextureManager.NAME);
        TextureAtlas atl = textureManager.getEditorAssetsList();
        return atl.findRegion(name);
    }

    @Override
    public ProjectInfoVO getProjectVO() {
        return projectManager.getCurrentProjectInfoVO();
    }

    @Override
    public ParticleEffect getParticleEffect(String name) {
        TextureManager textureManager = facade.retrieveProxy(TextureManager.NAME);
        return textureManager.getParticle(name);
    }

    public ArrayList<ParticleEffect> getParticles() {
        TextureManager textureManager = facade.retrieveProxy(TextureManager.NAME);
        return textureManager.getParticles();
    }

    @Override
    public TextureAtlas getSkeletonAtlas(String animationName) {
        TextureManager textureManager = facade.retrieveProxy(TextureManager.NAME);
        SpineAnimData animData = textureManager.getProjectSpineAnimationsList().get(animationName);
        return animData.atlas;
    }

    @Override
    public TextureAtlas getSpriteAnimation(String animationName) {
        TextureManager textureManager = facade.retrieveProxy(TextureManager.NAME);
        return textureManager.getProjectSpriteAnimationsList().get(animationName);
    }

    @Override
    public FileHandle getSkeletonJSON(String animationName) {
        TextureManager textureManager = facade.retrieveProxy(TextureManager.NAME);
        SpineAnimData animData = textureManager.getProjectSpineAnimationsList().get(animationName);
        return animData.jsonFile;
    }


    @Override
    public BitmapFont getBitmapFont(String fontName, int fontSize) {
        String fontpair = fontName + "_" + fontSize;
        if (bitmapFonts.containsKey(fontpair)) {
            return bitmapFonts.get(fontpair);
        } else {
            FileHandle fontFile;
            fontFile = Gdx.files.internal("freetypefonts" + File.separator + fontName + ".ttf");
            if (!fontFile.exists()) fontFile = Gdx.files.internal("freetypefonts" + File.separator + "arial.ttf");
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
            FreeTypeFontParameter parameter = new FreeTypeFontParameter();
            parameter.size = fontSize;
            BitmapFont font = generator.generateFont(parameter);
            bitmapFonts.put(fontpair, font);
            return font;
        }
    }

    @Override
    public SceneVO getSceneVO(String name) {
        FileHandle file = Gdx.files.internal("scenes" + File.separator + name + ".dt");
        Json json = new Json();
        SceneVO sceneVO = json.fromJson(SceneVO.class, file.readString());

        return sceneVO;
    }

    public Skin getNewEditorSkin() {
        TextureManager textureManager = facade.retrieveProxy(TextureManager.NAME);
        return textureManager.newEditorSkin;
    }

    @Override
    public FileHandle getSCMLFile(String name) {
        TextureManager textureManager = facade.retrieveProxy(TextureManager.NAME);
        return textureManager.getProjectSpriterAnimationsList().get(name);
    }
}
