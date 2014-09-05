package com.uwsoft.editor.renderer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.Json;
import com.uwsoft.editor.renderer.data.ProjectInfoVO;

public class DefaultAssetManager implements IResource {
	
	private ProjectInfoVO projectVo;
	private TextureAtlas mainPack;
	private HashMap<String, ParticleEffect> particleEffects = new HashMap<String, ParticleEffect>();
	private HashMap<String, TextureAtlas> spriteAnimations = new HashMap<String, TextureAtlas>();	
	private HashMap<String, BitmapFont> bitmapFonts = new HashMap<String, BitmapFont>();
	
	public String[] particleNames = new String[0];

	public String[] spriteAnimationNames = new String[0];
	
	public DefaultAssetManager() {
		
	}
	
	public void loadData() {
		mainPack = new TextureAtlas(Gdx.files.internal("orig" + File.separator + "pack.atlas"));
		
		String projectJson = Gdx.files.internal("project.dt").readString();
		Json json = new Json();
		projectVo = json.fromJson(ProjectInfoVO.class, projectJson);
		
		loadParticleEffects();
		loadSpriteAnimations();

	}
	
	
	
	
	public void loadParticleEffects() {
		for(int i  = 0; i < particleNames.length; i++) {
			ParticleEffect effect = new ParticleEffect();
			effect.load(Gdx.files.internal("particles" + File.separator + particleNames[i]), mainPack, "");
			particleEffects.put(particleNames[i], effect);
		}
	}
	
	public void loadSpriteAnimations() {
		for(int i  = 0; i < spriteAnimationNames.length; i++) {
			TextureAtlas animAtlas = new TextureAtlas(Gdx.files.internal("sprite-animations" + File.separator + spriteAnimationNames[i] + File.separator + spriteAnimationNames[i] + ".atlas"));
			spriteAnimations.put(spriteAnimationNames[i], animAtlas);
		}
	}
	
	@Override
	public TextureAtlas getAtlas() {
		return mainPack;
	}

	@Override
	public TextureRegion getAsset(String name) {
		return mainPack.findRegion(name);
	}

	@Override
	public MySkin getSkin() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProjectInfoVO getProjectVO() {
		return projectVo;
	}

	@Override
	public ParticleEffect getParticle(String name) {
		return particleEffects.get(name);
	}

	@Override
	public ArrayList<ParticleEffect> getParticles() {
		return (ArrayList<ParticleEffect>) particleEffects.values();
	}

	@Override
	public TextureAtlas getSkeletonAtlas(String animationName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TextureAtlas getSpriteAnimationAtlas(String animationName) {
		return spriteAnimations.get(animationName);
	}

	@Override
	public FileHandle getSkeletonJSON(String animationName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override

	public BitmapFont getBitmapFont(String fontName, int fontSize) {
		String fontpair	=	 fontName+"_"+fontSize;
		if(bitmapFonts.containsKey(fontpair)){
			return bitmapFonts.get(fontpair);
		}else{
			FileHandle fontFile;
			fontFile	=	Gdx.files.internal("freetypefonts" + File.separator + fontName+".ttf");
			if(!fontFile.exists())	fontFile	=	Gdx.files.internal("freetypefonts" + File.separator + "arial.ttf");
			FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
			FreeTypeFontParameter parameter = new FreeTypeFontParameter();		
	        parameter.size = fontSize;        
	        BitmapFont font 	= 	generator.generateFont(parameter); 
	        bitmapFonts.put(fontpair, font);
			return font;
		
		}
	}


}
