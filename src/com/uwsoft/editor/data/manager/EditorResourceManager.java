package com.uwsoft.editor.data.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.Json;
import com.uwsoft.editor.data.SpineAnimData;
import com.uwsoft.editor.renderer.data.ProjectInfoVO;
import com.uwsoft.editor.renderer.data.SceneVO;
import com.uwsoft.editor.renderer.resources.IResourceRetriever;
import com.uwsoft.editor.renderer.utils.MySkin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class EditorResourceManager implements IResourceRetriever {

    private final DataManager dataManager;
    private final TextureManager textureManager;
    private HashMap<String, BitmapFont> bitmapFonts = new HashMap<String, BitmapFont>();

    public EditorResourceManager() {
        dataManager = DataManager.getInstance();
        textureManager = dataManager.textureManager;
    }

    public TextureAtlas getAtlas() {
        return textureManager.getEditorAssetsList();
    }

    @Override
    public MySkin getSkin() {
        return textureManager.editorSkin;
    }

    @Override
    public TextureRegion getTextureRegion(String name) {
        TextureAtlas atl = textureManager.getEditorAssetsList();
        return atl.findRegion(name);
    }

    @Override
    public ProjectInfoVO getProjectVO() {
        return DataManager.getInstance().getCurrentProjectInfoVO();
    }

    @Override
    public ParticleEffect getParticleEffect(String name) {
        return textureManager.getParticle(name);
    }

    public ArrayList<ParticleEffect> getParticles() {
        return textureManager.getParticles();
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

    @Override
    public FileHandle getSCMLFile(String name) {
        return textureManager.getProjectSpriterAnimationsList().get(name);
    }
}
