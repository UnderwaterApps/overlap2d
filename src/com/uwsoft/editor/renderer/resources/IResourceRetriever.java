package com.uwsoft.editor.renderer.resources;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.uwsoft.editor.renderer.data.ResolutionEntryVO;
import com.uwsoft.editor.renderer.utils.MySkin;
import com.uwsoft.editor.renderer.data.ProjectInfoVO;
import com.uwsoft.editor.renderer.data.SceneVO;

/**
 * Created by azakhary on 9/9/2014.
 */
public interface IResourceRetriever {

    public TextureRegion getTextureRegion(String name);
    public ParticleEffect getParticleEffect(String name);
    public TextureAtlas getSkeletonAtlas(String name);
    public FileHandle getSkeletonJSON(String name);
    public FileHandle getSCMLFile(String name);
    public TextureAtlas getSpriteAnimation(String name);
    public BitmapFont getBitmapFont(String name, int size);
    public MySkin getSkin();

    public SceneVO getSceneVO(String sceneName);
    public ProjectInfoVO getProjectVO();

    public ResolutionEntryVO getLoadedResolution();
    public ShaderProgram getShaderProgram(String shaderName);
}
