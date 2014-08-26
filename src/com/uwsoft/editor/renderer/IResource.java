package com.uwsoft.editor.renderer;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.uwsoft.editor.renderer.data.ProjectInfoVO;

import java.util.ArrayList;


public interface IResource {

    public TextureAtlas getAtlas();

    public TextureRegion getAsset(String name);

    public MySkin getSkin();

    public ProjectInfoVO getProjectVO();

    public ParticleEffect getParticle(String name);

    public ArrayList<ParticleEffect> getParticles();

    public TextureAtlas getSkeletonAtlas(String animationName);

    public TextureAtlas getSpriteAnimationAtlas(String animationName);

    public FileHandle getSkeletonJSON(String animationName);
}
