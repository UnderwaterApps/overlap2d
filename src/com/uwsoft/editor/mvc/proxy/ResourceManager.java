package com.uwsoft.editor.mvc.proxy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Json;
import com.puremvc.patterns.proxy.BaseProxy;
import com.uwsoft.editor.data.SpineAnimData;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.renderer.data.ProjectInfoVO;
import com.uwsoft.editor.renderer.data.SceneVO;
import com.uwsoft.editor.renderer.resources.IResourceRetriever;
import com.uwsoft.editor.renderer.utils.MySkin;

/**
 * Created by azakhary on 4/26/2015.
 */
public class ResourceManager extends BaseProxy implements IResourceRetriever {

    private static final String TAG = ResourceManager.class.getCanonicalName();
    public static final String NAME = TAG;

    public ResourceManager() {
        super(NAME);
    }

    @Override
    public void onRegister() {
        super.onRegister();
        facade = Overlap2DFacade.getInstance();
    }

    @Override
    public TextureRegion getTextureRegion(String name) {
        TextureManager textureManager = facade.retrieveProxy(TextureManager.NAME);
        TextureAtlas atl = textureManager.getProjectAssetsList();
        TextureRegion reg = atl.findRegion(name);
        if (reg == null) {
            reg = textureManager.getEditorAsset(name);
        }
        return reg;
    }

    @Override
    public ParticleEffect getParticleEffect(String name) {
        TextureManager textureManager = facade.retrieveProxy(TextureManager.NAME);
        return textureManager.getParticle(name);
    }


    @Override
    public TextureAtlas getSkeletonAtlas(String animationName) {
        TextureManager textureManager = facade.retrieveProxy(TextureManager.NAME);
        SpineAnimData animData = textureManager.getProjectSpineAnimationsList().get(animationName);
        return animData.atlas;
    }


    @Override
    public FileHandle getSkeletonJSON(String animationName) {
        TextureManager textureManager = facade.retrieveProxy(TextureManager.NAME);
        SpineAnimData animData = textureManager.getProjectSpineAnimationsList().get(animationName);
        return animData.jsonFile;
    }

    @Override
    public FileHandle getSCMLFile(String name) {
        TextureManager textureManager = facade.retrieveProxy(TextureManager.NAME);
        return textureManager.getProjectSpriterAnimationsList().get(name);
    }


    @Override
    public TextureAtlas getSpriteAnimation(String animationName) {
        TextureManager textureManager = facade.retrieveProxy(TextureManager.NAME);
        return textureManager.getProjectSpriteAnimationsList().get(animationName);
    }


    @Override
    public BitmapFont getBitmapFont(String fontName, int fontSize) {
        TextureManager textureManager = facade.retrieveProxy(TextureManager.NAME);
        return textureManager.getBitmapFont(fontName, fontSize);
    }


    @Override
    public MySkin getSkin() {
        TextureManager textureManager = facade.retrieveProxy(TextureManager.NAME);
        return textureManager.projectSkin;
    }

    @Override
    public ProjectInfoVO getProjectVO() {
        ProjectManager projectManager = facade.retrieveProxy(ProjectManager.NAME);
        return projectManager.getCurrentProjectInfoVO();
    }


    @Override
    public SceneVO getSceneVO(String name) {
        SceneDataManager sceneDataManager = facade.retrieveProxy(SceneDataManager.NAME);
        FileHandle file = Gdx.files.internal(sceneDataManager.getCurrProjectScenePathByName(name));
        Json json = new Json();
        return json.fromJson(SceneVO.class, file.readString());
    }
}
