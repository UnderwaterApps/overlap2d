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

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by socheat on 8/13/15.
 */
public class ResourceManagerLoader extends AsynchronousAssetLoader<ResourceManager, ResourceManagerLoader.AsyncResourceManagerParam> {

    private AsyncResourceManager asyncResourceManager;

    private ProjectInfoVO projectInfoVO;

    public ResourceManagerLoader(FileHandleResolver resolver) {
        super(resolver);
        this.asyncResourceManager = new AsyncResourceManager();
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, AsyncResourceManagerParam parameter) {
    }

    @Override
    public com.uwsoft.editor.renderer.resources.ResourceManager loadSync(AssetManager manager, String fileName, FileHandle file, AsyncResourceManagerParam parameter) {
        if (!fileName.equals("project.dt")) {
            throw new GdxRuntimeException("fileName must be project.dt");
        }

        FileHandle packFile = Gdx.files.internal(this.asyncResourceManager.packResolutionName + File.separator + "pack.atlas");
        TextureAtlas textureAtlas = manager.get(packFile.path(), TextureAtlas.class);
        this.asyncResourceManager.setMainPack(textureAtlas);
        this.asyncResourceManager.loadParticleEffects();
        this.asyncResourceManager.loadSpineAnimations(manager);
        this.asyncResourceManager.loadSpriteAnimations(manager);
        this.asyncResourceManager.loadSpriterAnimations();
        this.asyncResourceManager.loadFonts();

        return this.asyncResourceManager;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, AsyncResourceManagerParam parameter) {
        if (!fileName.equals("project.dt")) {
            throw new GdxRuntimeException("fileName must be project.dt");
        }
        this.projectInfoVO = this.asyncResourceManager.loadProjectVO();
        for (int i = 0; i < this.projectInfoVO.scenes.size(); i++) {
            this.asyncResourceManager.loadSceneVO(this.projectInfoVO.scenes.get(i).sceneName);
            this.asyncResourceManager.scheduleScene(this.projectInfoVO.scenes.get(i).sceneName);
        }
        this.asyncResourceManager.prepareAssetsToLoad();

        Array<AssetDescriptor> deps = new Array();
        {
            FileHandle packFile = Gdx.files.internal(this.asyncResourceManager.packResolutionName + File.separator + "pack.atlas");
            if (packFile.exists()) {
                deps.add(new AssetDescriptor(packFile, TextureAtlas.class));
            }
        }

        for (String name : this.asyncResourceManager.getSpineAnimNamesToLoad()) {
            FileHandle packFile = Gdx.files.internal(this.asyncResourceManager + File.separator + this.asyncResourceManager.spineAnimationsPath + File.separator + name + File.separator + name + ".atlas");
            deps.add(new AssetDescriptor(packFile, TextureAtlas.class));
        }

        for (String name : this.asyncResourceManager.getSpriteAnimNamesToLoad()) {
            FileHandle packFile = Gdx.files.internal(this.asyncResourceManager.packResolutionName + File.separator + this.asyncResourceManager.spriteAnimationsPath + File.separator + name + File.separator + name + ".atlas");
            deps.add(new AssetDescriptor(packFile, TextureAtlas.class));
        }

        return deps;
    }

    public static class AsyncResourceManagerParam extends AssetLoaderParameters<com.uwsoft.editor.renderer.resources.ResourceManager> {
    }

    private static class AsyncResourceManager extends com.uwsoft.editor.renderer.resources.ResourceManager {

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

