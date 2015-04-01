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

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.uwsoft.editor.data.SpineAnimData;
import com.uwsoft.editor.mvc.proxy.DataManager;
import com.uwsoft.editor.renderer.resources.FontSizePair;
import com.uwsoft.editor.renderer.utils.MySkin;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;


public class TextureManager {


    private final DataManager dataManager;
    public AssetManager assetsManager;

    public String labelStylePath;
    public Skin newEditorSkin;
    public MySkin editorSkin;
    public MySkin projectSkin;
    public HashMap<String, ParticleEffect> particleEffects = new HashMap<String, ParticleEffect>(1);
    private TextureAtlas currentProjectAtlas;
    private TextureAtlas editorAtlas;
    private HashMap<String, SpineAnimData> spineAnimAtlases = new HashMap<String, SpineAnimData>();
    private HashMap<String, TextureAtlas> spriteAnimAtlases = new HashMap<String, TextureAtlas>();
    private HashMap<String, FileHandle> spriterAnimFiles = new HashMap<String, FileHandle>();

    private HashMap<FontSizePair, BitmapFont> bitmapFonts = new HashMap<FontSizePair, BitmapFont>();

    public TextureManager(DataManager dataManager) {
        this.dataManager = dataManager;
        assetsManager = new AssetManager();

        loadEditorAssets();
    }

    public void loadEditorAssets() {
        editorAtlas = new TextureAtlas(Gdx.files.getFileHandle("imgs/pack.atlas", FileType.Internal));
        editorSkin = new MySkin(Gdx.files.internal("ui/styles.dt"));

//		  newEditorSkin = new Skin(Gdx.files.internal("ui/newskin.dt"));
        /*
        Texture fntTexture = new Texture("ui/arial.png");
		fntTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureRegion tmpRegion = new TextureRegion(fntTexture, 0, 0, 512, 512);
    	BitmapFont fontBitmap = new BitmapFont(Gdx.files.getFileHandle("ui/arial.fnt", FileType.Internal), tmpRegion, false);
    	
    	editorSkin.*/
    }

    public void loadCurrentProjectParticles(String path) {
        particleEffects.clear();
        FileHandle sourceDir = new FileHandle(path);
        for (FileHandle entry : sourceDir.list()) {
            File file = entry.file();
            String filename = file.getName();
            if (file.isDirectory() || filename.endsWith(".DS_Store")) continue;

            ParticleEffect particleEffect = new ParticleEffect();
            particleEffect.load(Gdx.files.internal(file.getAbsolutePath()), currentProjectAtlas, "");
            particleEffects.put(filename, particleEffect);
        }

    }


    public void loadCurrentProjectSpineAnimations(String path, String curResolution) {
        spineAnimAtlases.clear();
        FileHandle sourceDir = new FileHandle(path + "orig/spine-animations");
        for (FileHandle entry : sourceDir.list()) {
            if (entry.file().isDirectory()) {
                String animName = FilenameUtils.removeExtension(entry.file().getName());
                TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(path + curResolution + "/spine-animations/" + File.separator + animName + File.separator + animName + ".atlas"));
                FileHandle animJsonFile = Gdx.files.internal(entry.file().getAbsolutePath() + File.separator + animName + ".json");
                SpineAnimData data = new SpineAnimData();
                data.atlas = atlas;
                data.jsonFile = animJsonFile;
                data.animName = animName;
                spineAnimAtlases.put(animName, data);
            }
        }

    }

    public void loadCurrentProjectSpriteAnimations(String path, String curResolution) {
        spriteAnimAtlases.clear();
        FileHandle sourceDir = new FileHandle(path + curResolution + "/sprite-animations");
        for (FileHandle entry : sourceDir.list()) {
            if (entry.file().isDirectory()) {
                String animName = FilenameUtils.removeExtension(entry.file().getName());
                TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(entry.file().getAbsolutePath() + File.separator + animName + ".atlas"));
                spriteAnimAtlases.put(animName, atlas);
            }
        }
    }

    public void loadCurrentProjectSpriterAnimations(String path, String curResolution) {
        spriterAnimFiles.clear();
        FileHandle sourceDir = new FileHandle(path + "orig" + "/spriter-animations");
        for (FileHandle entry : sourceDir.list()) {
            if (entry.file().isDirectory()) {
                String animName = entry.file().getName();
                FileHandle scmlFile = new FileHandle(path + "orig" + "/spriter-animations/" + animName + "/" + animName + ".scml");
                spriterAnimFiles.put(animName, scmlFile);
            }
        }
    }

    public void loadCurrentProjectAssets(String packPath) {
        try {
            currentProjectAtlas = new TextureAtlas(Gdx.files.getFileHandle(packPath, FileType.Internal));
        } catch (Exception e) {
            currentProjectAtlas = new TextureAtlas();
        }
    }

    public void loadCurrentProjectSkin(String fontPath) {
//		projectSkin	=	DataManager.getInstance().getSkinFromStyle();
        File styleFile = new File(fontPath, "styles.dt");
        FileHandle f = new FileHandle(styleFile);

        if (styleFile.isFile() && styleFile.exists()) {
            //System.out.println(f.readString());
            projectSkin = new MySkin(f);
            ObjectMap<String, BitmapFont> map = projectSkin.getAll(BitmapFont.class);
            for (Entry<String, BitmapFont> entry : map.entries()) {
                projectSkin.getFont(entry.key).getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
            }
        }

    }

    public AtlasRegion getProjectAsset(String name) {
        return currentProjectAtlas.findRegion(name);
    }

    public AtlasRegion getEditorAsset(String name) {
        return editorAtlas.findRegion(name);
    }

    public TextureAtlas getProjectAssetsList() {
        return currentProjectAtlas;
    }

    public HashMap<String, SpineAnimData> getProjectSpineAnimationsList() {
        return spineAnimAtlases;
    }

    public HashMap<String, ParticleEffect> getProjectParticleList() {
        return particleEffects;
    }

    public HashMap<String, TextureAtlas> getProjectSpriteAnimationsList() {
        return spriteAnimAtlases;
    }

    public HashMap<String, FileHandle> getProjectSpriterAnimationsList() {
        return spriterAnimFiles;
    }

    public TextureAtlas getEditorAssetsList() {
        return editorAtlas;
    }

    public void dispose() {

    }

    public TextureRegion getLoadedAtlasRegion(String key) {

        return null;
    }

    public Button createImageButton(String button, String hover, String pressed) {
        ButtonStyle btnStl = new ButtonStyle();
        btnStl.up = new TextureRegionDrawable(getEditorAsset(button));
        btnStl.down = new TextureRegionDrawable(getEditorAsset(pressed));
        btnStl.over = new TextureRegionDrawable(getEditorAsset(hover));

        Button btn = new Button(btnStl);

        return btn;
    }

    public ParticleEffect getParticle(String name) {
        return new ParticleEffect(particleEffects.get(name));
    }

    public ArrayList<ParticleEffect> getParticles() {
        return new ArrayList<ParticleEffect>(particleEffects.values());
    }

    public FileHandle getFontFile(String fontName) {
        FileHandle file = Gdx.files.internal(labelStylePath + File.separator + fontName + ".ttf");
        if (!file.exists()) {
            file = Gdx.files.internal("freetypefonts/arial.ttf");
        }
        return file;
    }

    public void loadCurrentProjectLabelStylePath(String string) {
        labelStylePath = string;
    }

    public boolean checkFontExistence() {
        File folder = new File(dataManager.getFreeTypeFontPath());
        if (!folder.exists()) return false;
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.getName().toLowerCase().endsWith(".ttf")) {
                return true;
            }
        }
        return false;
    }

    public void loadBitmapFonts(FontSizePair[] fonts, float mulX) {
        bitmapFonts.clear();
        for (FontSizePair pair : fonts) {
            FileHandle fontFile;
            fontFile = Gdx.files.internal(dataManager.getFreeTypeFontPath() + File.separator + pair.fontName + ".ttf");
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.size = Math.round(pair.fontSize * mulX);
            BitmapFont font = generator.generateFont(parameter);
            bitmapFonts.put(pair, font);
        }
    }

    public BitmapFont getBitmapFont(String name, int size) {
        FontSizePair pair = new FontSizePair(name.isEmpty() ? "arial" : name, size);
        FontSizePair[] tmp = new FontSizePair[1];
        tmp[0] = pair;

        if (!bitmapFonts.containsKey(pair))
            loadBitmapFonts(tmp, dataManager.resolutionManager.getCurrentMul());

        return bitmapFonts.get(pair);
    }

    public Texture getRegionOriginalImage(String regionName) {
        String sourcePath = dataManager.getCurrentWorkingPath() + "/" + dataManager.getCurrentProjectVO().projectName + "/assets/orig/images/" + regionName + ".png";
        return new Texture(Gdx.files.absolute(sourcePath));
    }

    public void loadCurrentProjectData(String currentWorkingPath, String projectName, String curResolution) {
        loadCurrentProjectAssets(currentWorkingPath + "/" + projectName + "/assets/" + curResolution + "/pack/pack.atlas");
        loadCurrentProjectSkin(currentWorkingPath + "/" + projectName + "/assets/orig/styles");
        loadCurrentProjectLabelStylePath(currentWorkingPath + "/" + projectName + "/assets/orig/freetypefonts");
        loadCurrentProjectParticles(currentWorkingPath + "/" + projectName + "/assets/orig/particles");
        loadCurrentProjectSpineAnimations(currentWorkingPath + "/" + projectName + "/assets/", curResolution);
        loadCurrentProjectSpriteAnimations(currentWorkingPath + "/" + projectName + "/assets/", curResolution);
        loadCurrentProjectSpriterAnimations(currentWorkingPath + "/" + projectName + "/assets/", curResolution);
    }
}
