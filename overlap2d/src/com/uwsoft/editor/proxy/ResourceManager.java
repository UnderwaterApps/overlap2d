package com.uwsoft.editor.proxy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.uwsoft.editor.renderer.data.*;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Json;
import com.puremvc.patterns.proxy.BaseProxy;
import com.uwsoft.editor.data.SpineAnimData;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.renderer.resources.FontSizePair;
import com.uwsoft.editor.renderer.resources.IResourceRetriever;
import com.uwsoft.editor.renderer.utils.MySkin;

/**
 * Created by azakhary on 4/26/2015.
 */
public class ResourceManager extends BaseProxy implements IResourceRetriever {

    public String packResolutionName = "orig";

    private static final String TAG = ResourceManager.class.getCanonicalName();
    public static final String NAME = TAG;

    private HashMap<String, ParticleEffect> particleEffects = new HashMap<String, ParticleEffect>(1);
    private TextureAtlas currentProjectAtlas;

    private HashMap<String, SpineAnimData> spineAnimAtlases = new HashMap<String, SpineAnimData>();
    private HashMap<String, TextureAtlas> spriteAnimAtlases = new HashMap<String, TextureAtlas>();
    private HashMap<String, FileHandle> spriterAnimFiles = new HashMap<String, FileHandle>();
    private HashMap<FontSizePair, BitmapFont> bitmapFonts = new HashMap<>();
    private HashMap<String, ShaderProgram> shaderPrograms = new HashMap<String, ShaderProgram>(1);

    private TextureRegion defaultRegion;

    private ResolutionManager resolutionManager;

    public ResourceManager() {
        super(NAME);
    }

    @Override
    public void onRegister() {
        super.onRegister();
        facade = Overlap2DFacade.getInstance();
        resolutionManager = facade.retrieveProxy(ResolutionManager.NAME);

        // TODO: substitute this with "NO IMAGE" icon
        Pixmap pixmap = new Pixmap(50, 50, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(1, 1, 1, 0.4f));
        pixmap.fill();
        defaultRegion = new TextureRegion(new Texture(pixmap));
    }

    @Override
    public TextureRegion getTextureRegion(String name) {
        TextureRegion reg = currentProjectAtlas.findRegion(name);

        if(reg == null) {
            reg = defaultRegion;
        }

        return reg;
    }

    public TextureAtlas getTextureAtlas() {
        return currentProjectAtlas;
    }

    @Override
    public ParticleEffect getParticleEffect(String name) {
        return new ParticleEffect(particleEffects.get(name));
    }


    @Override
    public TextureAtlas getSkeletonAtlas(String animationName) {
        SpineAnimData animData = spineAnimAtlases.get(animationName);
        return animData.atlas;
    }

    /**
     * Sets working resolution, please set before doing any loading
     * @param resolution String resolution name, default is "orig" later use resolution names created in editor
     */
    public void setWorkingResolution(String resolution) {
        ResolutionEntryVO resolutionObject = getProjectVO().getResolution("resolutionName");
        if(resolutionObject != null) {
            packResolutionName = resolution;
        }
    }


    @Override
    public FileHandle getSkeletonJSON(String animationName) {
        SpineAnimData animData = spineAnimAtlases.get(animationName);
        return animData.jsonFile;
    }

    @Override
    public FileHandle getSCMLFile(String name) {
        return spriterAnimFiles.get(name);
    }


    @Override
    public TextureAtlas getSpriteAnimation(String animationName) {
        return spriteAnimAtlases.get(animationName);
    }


    @Override
    public BitmapFont getBitmapFont(String fontName, int fontSize) {
        FontSizePair pair = new FontSizePair(fontName, fontSize);
        return bitmapFonts.get(pair);
    }


    @Override
    public MySkin getSkin() {
        //return textureManager.projectSkin;
        // not sure if we are going to use skins for labels
        return null;
    }

    @Override
    public ProjectInfoVO getProjectVO() {
        ProjectManager projectManager = facade.retrieveProxy(ProjectManager.NAME);
        return projectManager.getCurrentProjectInfoVO();
    }


    @Override
    public SceneVO getSceneVO(String name) {
        SceneDataManager sceneDataManager = facade.retrieveProxy(SceneDataManager.NAME);
        // TODO: this should be cached
        FileHandle file = Gdx.files.internal(sceneDataManager.getCurrProjectScenePathByName(name));
        Json json = new Json();
        json.setIgnoreUnknownFields(true);
        return json.fromJson(SceneVO.class, file.readString());
    }

    public void loadCurrentProjectData(String projectPath, String curResolution) {
        packResolutionName = curResolution;
        loadCurrentProjectAssets(projectPath + "/assets/" + curResolution + "/pack/pack.atlas");
        loadCurrentProjectSkin(projectPath + "/assets/orig/styles");
        loadCurrentProjectParticles(projectPath + "/assets/orig/particles");
        loadCurrentProjectSpineAnimations(projectPath + "/assets/", curResolution);
        loadCurrentProjectSpriteAnimations(projectPath + "/assets/", curResolution);
        loadCurrentProjectSpriterAnimations(projectPath + "/assets/", curResolution);
        loadCurrentProjectBitmapFonts(projectPath, curResolution);
        loadCurrentProjectShaders(projectPath + "/assets/shaders/");
    }

    private void loadCurrentProjectParticles(String path) {
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


    private void loadCurrentProjectSpineAnimations(String path, String curResolution) {
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

    private void loadCurrentProjectSpriteAnimations(String path, String curResolution) {
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

    private void loadCurrentProjectSpriterAnimations(String path, String curResolution) {
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
            currentProjectAtlas = new TextureAtlas(Gdx.files.getFileHandle(packPath, Files.FileType.Internal));
        } catch (Exception e) {
            currentProjectAtlas = new TextureAtlas();
        }
    }

    public ArrayList<FontSizePair> getProjectRequiredFontsList() {
        HashSet<FontSizePair> fontsToLoad = new HashSet<>();

        for (int i = 0; i < getProjectVO().scenes.size(); i++) {
            SceneVO scene = getSceneVO(getProjectVO().scenes.get(i).sceneName);
            CompositeVO composite = scene.composite;
            if (composite == null) {
                continue;
            }
            FontSizePair[] fonts = composite.getRecursiveFontList();
            for (CompositeItemVO library : getProjectVO().libraryItems.values()) {
                FontSizePair[] libFonts = library.composite.getRecursiveFontList();
                Collections.addAll(fontsToLoad, libFonts);
            }
            Collections.addAll(fontsToLoad, fonts);
        }

        return new ArrayList<>(fontsToLoad);
    }

    public void loadCurrentProjectBitmapFonts(String path, String curResolution) {
        bitmapFonts.clear();

        ArrayList<FontSizePair> requiredFonts = getProjectRequiredFontsList();
        for (int i = 0; i < requiredFonts.size(); i++) {
            FontSizePair pair = requiredFonts.get(i);
            FileHandle fontFile;
            try {
                fontFile = getTTFSafely(pair.fontName);
                FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
                FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
                parameter.size = Math.round(pair.fontSize * resolutionManager.getCurrentMul());
                BitmapFont font = generator.generateFont(parameter);
                bitmapFonts.put(pair, font);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void loadCurrentProjectShaders(String path) {
    	Iterator<Entry<String, ShaderProgram>> it = shaderPrograms.entrySet().iterator();
    	while (it.hasNext()) {
    		Entry<String, ShaderProgram> pair = it.next();
    		pair.getValue().dispose();
    		it.remove(); 
    	}
        shaderPrograms.clear();
        FileHandle sourceDir = new FileHandle(path);
        for (FileHandle entry : sourceDir.list()) {
            File file = entry.file();
            String filename = file.getName().replace(".vert", "").replace(".frag", "");
            if (file.isDirectory() || filename.endsWith(".DS_Store") || shaderPrograms.containsKey(filename)) continue;
            // check if pair exists.
            if(Gdx.files.internal(path + filename + ".vert").exists() && Gdx.files.internal(path + filename + ".frag").exists()) {
                ShaderProgram shaderProgram = new ShaderProgram(Gdx.files.internal(path + filename + ".vert"), Gdx.files.internal(path + filename + ".frag"));
                System.out.println(shaderProgram.getLog());
                shaderPrograms.put(filename, shaderProgram);
            }
        }

    }

    /**
     * @param fontPath
     * @deprecated
     */
    private void loadCurrentProjectSkin(String fontPath) {
        /*
        File styleFile = new File(fontPath, "styles.dt");
        FileHandle f = new FileHandle(styleFile);

        if (styleFile.isFile() && styleFile.exists()) {
            projectSkin = new MySkin(f);
            ObjectMap<String, BitmapFont> map = projectSkin.getAll(BitmapFont.class);
            for (ObjectMap.Entry<String, BitmapFont> entry : map.entries()) {
                projectSkin.getFont(entry.key).getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            }
        }
        */
    }

    public FileHandle getTTFSafely(String fontName) throws IOException {
        FontManager fontManager = facade.retrieveProxy(FontManager.NAME);

        ProjectManager projectManager = facade.retrieveProxy(ProjectManager.NAME);
        String expectedPath = projectManager.getFreeTypeFontPath() + File.separator + fontName + ".ttf";
        FileHandle expectedFile = Gdx.files.internal(expectedPath);
        if (!expectedFile.exists()) {
            // let's check if system fonts fot it
            HashMap<String, String> fonts = fontManager.getFontsMap();
            if (fonts.containsKey(fontName)) {
                File source = new File(fonts.get(fontName));
                FileUtils.copyFile(source, expectedFile.file());
                expectedFile = Gdx.files.internal(expectedPath);
            } else {
                throw new FileNotFoundException();
            }
        }

        return expectedFile;
    }

    public void addBitmapFont(String name, int size, BitmapFont font) {
        bitmapFonts.put(new FontSizePair(name, size), font);
    }

    public void flushAllUnusedFonts() {
        //List of fonts that are required to be in memory
        ArrayList<FontSizePair> requiredFonts = getProjectRequiredFontsList();
        ArrayList<FontSizePair> fontsInMemory = new ArrayList<>(bitmapFonts.keySet());

        for (FontSizePair font : fontsInMemory) {
            if (!requiredFonts.contains(font)) {
                bitmapFonts.remove(font);
            }
        }
    }

    public boolean isFontLoaded(String shortName, int fontSize) {
        return bitmapFonts.containsKey(new FontSizePair(shortName, fontSize));
    }

    public void prepareEmbeddingFont(String fontfamily, int fontSize) {
        flushAllUnusedFonts();

        if (isFontLoaded(fontfamily, fontSize)) {
            return;
        }

        FontManager fontManager = facade.retrieveProxy(FontManager.NAME);

        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = fontSize;

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontManager.getTTFByName(fontfamily));
        BitmapFont font = generator.generateFont(parameter);

        addBitmapFont(fontfamily, parameter.size, font);
    }

    public HashMap<String, SpineAnimData> getProjectSpineAnimationsList() {
        return spineAnimAtlases;
    }

    public HashMap<String, TextureAtlas> getProjectSpriteAnimationsList() {
        return spriteAnimAtlases;
    }

    public HashMap<String, FileHandle> getProjectSpriterAnimationsList() {
        return spriterAnimFiles;
    }

    public TextureAtlas getProjectAssetsList() {
        return currentProjectAtlas;
    }

    public HashMap<String, ParticleEffect> getProjectParticleList() {
        return particleEffects;
    }

    @Override
    public ResolutionEntryVO getLoadedResolution() {
        if(packResolutionName.equals("orig")) {
            return getProjectVO().originalResolution;
        }
        return getProjectVO().getResolution(packResolutionName);
    }

	@Override
	public ShaderProgram getShaderProgram(String shaderName) {
		return shaderPrograms.get(shaderName);
	}

    public HashMap<String, ShaderProgram> getShaders() {
        return shaderPrograms;
    }
}
