package com.uwsoft.editor.data.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.Json;
import com.uwsoft.editor.data.SpineAnimData;
import com.uwsoft.editor.renderer.resources.IResourceRetriever;
import com.uwsoft.editor.renderer.utils.MySkin;
import com.uwsoft.editor.renderer.data.ProjectInfoVO;
import com.uwsoft.editor.renderer.data.SceneVO;

public class SandboxResourceManager implements IResourceRetriever {	

    @Override
    public MySkin getSkin() {
        return TextureManager.getInstance().projectSkin;
    }

    @Override
    public TextureRegion getTextureRegion(String name) {
        TextureAtlas atl = TextureManager.getInstance().getProjectAssetsList();
        TextureRegion reg = atl.findRegion(name);
        if (reg == null) {
            reg = TextureManager.getInstance().getEditorAsset(name);
        }
        return reg;
    }

    @Override
    public ProjectInfoVO getProjectVO() {
        return DataManager.getInstance().getCurrentProjectInfoVO();
    }

    @Override
    public ParticleEffect getParticleEffect(String name) {
        return TextureManager.getInstance().getParticle(name);
    }

    @Override
    public TextureAtlas getSkeletonAtlas(String animationName) {
        SpineAnimData animData = TextureManager.getInstance().getProjectSpineAnimationsList().get(animationName);
        return animData.atlas;
    }

    @Override
    public TextureAtlas getSpriteAnimation(String animationName) {
        return TextureManager.getInstance().getProjectSpriteAnimationsList().get(animationName);
    }

    @Override
    public FileHandle getSkeletonJSON(String animationName) {
        SpineAnimData animData = TextureManager.getInstance().getProjectSpineAnimationsList().get(animationName);
        return animData.jsonFile;
    }


	@Override
	public BitmapFont getBitmapFont(String fontName, int fontSize) {		
		
		return TextureManager.getInstance().getBitmapFont(fontName, fontSize);
	}

    @Override
    public SceneVO getSceneVO(String name) {
        FileHandle file = Gdx.files.internal(DataManager.getInstance().getCurrProjectScenePathByName(name));
        Json json 	= new Json();
        SceneVO sceneVO = json.fromJson(SceneVO.class, file.readString());

        return sceneVO;
    }
}
