package com.overlap2d.plugins.tiled.manager;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.google.common.io.ByteStreams;
import com.overlap2d.plugins.tiled.TiledPlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by mariam on 4/21/16.
 */
public class ResourcesManager {

    private final String RESOURCES_FILE_NAME = "tiled";

    private TiledPlugin tiledPlugin;
    private TextureAtlas textureAtlas;

    public ResourcesManager(TiledPlugin tiledPlugin) {
        this.tiledPlugin = tiledPlugin;

        init();
    }

    private void init() {
        File atlasTempFile = getResourceFileFromJar(".atlas");
        File pngTempFile = getResourceFileFromJar(".png");
        textureAtlas = new TextureAtlas(new FileHandle(atlasTempFile));
        atlasTempFile.delete();
        pngTempFile.delete();
    }

    private File getResourceFileFromJar(String extension) {
        String fileName = "/"+RESOURCES_FILE_NAME+extension;
        File tempFile = new File (System.getProperty("user.dir")+fileName);
        try {
            InputStream in = getClass().getResourceAsStream(fileName);
            FileOutputStream out = new FileOutputStream(tempFile);
            ByteStreams.copy(in, out);
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempFile;
    }

    public TextureRegion getTextureRegion(String name) {
        TextureRegion region = textureAtlas.findRegion(name); // try to get region from plugin assets
        if (region == null) { // take the region from overlap assets
            region = tiledPlugin.getPluginAPI().getSceneLoader().getRm().getTextureRegion(name);
        }
        return region;
    }
}
