package com.uwsoft.editor.renderer.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.uwsoft.editor.renderer.data.ProjectInfoVO;
import com.uwsoft.editor.renderer.resources.ResourceManager;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by socheat on 8/13/15.
 */
public class ResourceManagerLoader extends AsynchronousAssetLoader<ResourceManager, ResourceManagerLoader.ResourceManagerParam> {

    private ResourceManager resourceManager;

    private ProjectInfoVO projectInfoVO;

    public ResourceManagerLoader(FileHandleResolver resolver) {
        super(resolver);
        this.resourceManager = new ResourceManager();
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, ResourceManagerParam parameter) {
    }

    @Override
    public com.uwsoft.editor.renderer.resources.ResourceManager loadSync(AssetManager manager, String fileName, FileHandle file, ResourceManagerParam parameter) {
        if (!fileName.equals("project.dt")) {
            throw new GdxRuntimeException("fileName must be project.dt");
        }

        FileHandle packFile = Gdx.files.internal(this.resourceManager.packResolutionName + File.separator + "pack.atlas");
        TextureAtlas textureAtlas = manager.get(packFile.path(), TextureAtlas.class);
        this.resourceManager.setMainPack(textureAtlas);
        this.resourceManager.loadParticleEffects();
        this.resourceManager.loadSpineAnimations(manager);
        this.resourceManager.loadSpriteAnimations(manager);
        this.resourceManager.loadSpriterAnimations();
        this.resourceManager.loadFonts();

        return this.resourceManager;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, ResourceManagerParam parameter) {
        if (!fileName.equals("project.dt")) {
            throw new GdxRuntimeException("fileName must be project.dt");
        }
        this.projectInfoVO = this.resourceManager.loadProjectVO();
        for (int i = 0; i < this.projectInfoVO.scenes.size(); i++) {
            this.resourceManager.loadSceneVO(this.projectInfoVO.scenes.get(i).sceneName);
            this.resourceManager.scheduleScene(this.projectInfoVO.scenes.get(i).sceneName);
        }
        this.resourceManager.prepareAssetsToLoad();

        Array<AssetDescriptor> deps = new Array();
        {
            FileHandle packFile = Gdx.files.internal(this.resourceManager.packResolutionName + File.separator + "pack.atlas");
            if (packFile.exists()) {
                deps.add(new AssetDescriptor(packFile, TextureAtlas.class));
            }
        }

        for (String name : this.resourceManager.getSpineAnimNamesToLoad()) {
            FileHandle packFile = Gdx.files.internal(this.resourceManager + File.separator + this.resourceManager.spineAnimationsPath + File.separator + name + File.separator + name + ".atlas");
            deps.add(new AssetDescriptor(packFile, TextureAtlas.class));
        }

        for (String name : this.resourceManager.getSpriteAnimNamesToLoad()) {
            FileHandle packFile = Gdx.files.internal(this.resourceManager.packResolutionName + File.separator + this.resourceManager.spriteAnimationsPath + File.separator + name + File.separator + name + ".atlas");
            deps.add(new AssetDescriptor(packFile, TextureAtlas.class));
        }

        return deps;
    }

    public static class ResourceManagerParam extends AssetLoaderParameters<com.uwsoft.editor.renderer.resources.ResourceManager> {
    }

    private static class ResourceManager extends com.uwsoft.editor.renderer.resources.ResourceManager {

        @Override
        public ProjectInfoVO getProjectVO() {
            return super.getProjectVO();
        }

        public void setProjectInfoVO(ProjectInfoVO vo) {
            this.projectVO = vo;
        }

        public HashSet<String> getSpineAnimNamesToLoad() {
            return this.spineAnimNamesToLoad;
        }

        public void setMainPack(TextureAtlas mainPack) {
            this.mainPack = mainPack;
        }

        @Override
        public void loadSpineAnimations() {
            throw new GdxRuntimeException("see loadSpineAnimations(AssetManager)");
        }

        @Override
        public void loadSpineAnimation(String name) {
            throw new GdxRuntimeException("see loadSpineAnimation(AssetManager, String)");
        }

        public void loadSpineAnimations(AssetManager manager) {
            Iterator it = skeletonAtlases.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pairs = (Map.Entry) it.next();
                if (spineAnimNamesToLoad.contains(pairs.getKey())) {
                    spineAnimNamesToLoad.remove(pairs.getKey());
                } else {
                    it.remove();
                    skeletonJSON.remove(pairs.getKey());
                }
            }


            for (String name : spineAnimNamesToLoad) {
                loadSpineAnimation(manager, name);
            }
        }

        public void loadSpineAnimation(AssetManager manager, String name) {
            FileHandle packFile = Gdx.files.internal(packResolutionName + File.separator + spineAnimationsPath + File.separator + name + File.separator + name + ".atlas");
            TextureAtlas animAtlas = manager.get(packFile.path(), TextureAtlas.class);
            skeletonAtlases.put(name, animAtlas);
            skeletonJSON.put(name, Gdx.files.internal("orig" + File.separator + spineAnimationsPath + File.separator + name + File.separator + name + ".json"));
        }


        @Override
        public void loadSpriteAnimations() {
            throw new GdxRuntimeException("see loadSpriteAnimations(AssetManager)");
        }

        public HashSet<String> getSpriteAnimNamesToLoad() {
            return this.spriteAnimNamesToLoad;
        }

        public void loadSpriteAnimations(AssetManager manager) {
            // empty existing ones that are not scheduled to load
            for (String key : spriteAnimations.keySet()) {
                if (!spriteAnimNamesToLoad.contains(key)) {
                    spriteAnimations.remove(key);
                }
            }

            for (String name : spriteAnimNamesToLoad) {
                FileHandle packFile = Gdx.files.internal(packResolutionName + File.separator + spriteAnimationsPath + File.separator + name + File.separator + name + ".atlas");
                spriteAnimations.put(name, manager.get(packFile.path(), TextureAtlas.class));
            }
        }
    }
}
