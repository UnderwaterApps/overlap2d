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

package com.overlap2d.plugins.ninepatch;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.puremvc.patterns.mediator.SimpleMediator;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.renderer.components.MainItemComponent;
import com.uwsoft.editor.renderer.components.NinePatchComponent;
import com.uwsoft.editor.renderer.components.TextureRegionComponent;
import com.uwsoft.editor.renderer.factory.EntityFactory;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by azakhary on 8/18/2015.
 */
public class MainPanelMediator extends SimpleMediator<MainPanel> {
    private static final String TAG = com.overlap2d.plugins.ninepatch.MainPanelMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    private NinePatchPlugin plugin;

    private ImageUtils imageUtils = new ImageUtils();

    public MainPanelMediator(NinePatchPlugin plugin) {
        super(NAME, new MainPanel());
        this.plugin = plugin;
    }

    @Override
    public String[] listNotificationInterests() {
        return new String[]{
                NinePatchPlugin.EDIT_NINE_PATCH,
                NinePatchPlugin.CONVERT_TO_NINE_PATCH,
                MainPanel.SAVE_CLICKED
        };
    }

    @Override
    public void handleNotification(Notification notification) {
        super.handleNotification(notification);
        switch (notification.getName()) {
            case NinePatchPlugin.EDIT_NINE_PATCH:
                loadNinePatch();
                break;
            case NinePatchPlugin.CONVERT_TO_NINE_PATCH:
                convertImageToNinePatch();
                loadNinePatch();
                break;
            case MainPanel.SAVE_CLICKED:
                Entity entity = plugin.currEditingEntity;
                NinePatchComponent ninePatchComponent = ComponentRetriever.get(entity, NinePatchComponent.class);
                applyNewSplits(ninePatchComponent.textureRegionName, viewComponent.getSplits());
                viewComponent.hide();
                break;
        }
    }

    private void convertImageToNinePatch() {
        Entity entity = plugin.currEditingEntity;
        MainItemComponent mainItemComponent = ComponentRetriever.get(entity, MainItemComponent.class);
        mainItemComponent.entityType = EntityFactory.NINE_PATCH;
        TextureRegionComponent textureRegionComponent = ComponentRetriever.get(entity, TextureRegionComponent.class);
        String regionName = textureRegionComponent.regionName;
        NinePatchComponent ninePatchComponent = new NinePatchComponent();
        ninePatchComponent.textureRegionName = regionName;
        TextureAtlas.AtlasRegion newRegion = (TextureAtlas.AtlasRegion) textureRegionComponent.region;
        int[] splits = {0, 0, 0, 0};
        newRegion.splits = splits;
        ninePatchComponent.ninePatch = new NinePatch(textureRegionComponent.region, 0, 0, 0, 0);
        entity.add(ninePatchComponent);

        //remove original image
        File originalImg = new File(plugin.getAPI().getProjectPath() + "/assets/orig/images/"+regionName+".png");
        originalImg.delete();

        //save project
        plugin.getAPI().saveProject();

        //save split data
        addSplitsToImageInAtlas(regionName, splits);
        applyNewSplits(regionName, splits);
    }

    private void loadNinePatch() {
        Entity entity = plugin.currEditingEntity;
        NinePatchComponent ninePatchComponent = ComponentRetriever.get(entity, NinePatchComponent.class);
        loadRegion(ninePatchComponent.textureRegionName);
        viewComponent.show(plugin.getAPI().getUIStage());
    }

    private void addSplitsToImageInAtlas(String textureRegionName, int[] splits) {
        FileHandle packAtlas = Gdx.files.internal(plugin.getAPI().getProjectPath() + "/assets/orig/pack/pack.atlas");
        String content = packAtlas.readString();
        int regionIndex = content.indexOf(textureRegionName);
        int splitEnd = content.indexOf("orig: ", regionIndex);
        String splitStr = "split: "+splits[0]+", "+splits[1]+", "+splits[2]+", "+splits[3]+"\n  ";
        String newContent = content.substring(0, splitEnd) + splitStr + content.substring(splitEnd, content.length());
        File test = new File(plugin.getAPI().getProjectPath() + "/assets/orig/pack/pack.atlas");
        writeFile(newContent, test);
    }

    private void applyNewSplits(String textureRegionName, int[] splits) {
        // first need to modify original image
        FileHandle packAtlas = Gdx.files.internal(plugin.getAPI().getProjectPath() + "/assets/orig/pack/pack.atlas");
        FileHandle imagesDir = Gdx.files.internal(plugin.getAPI().getProjectPath() + "/assets/orig/pack/");
        TextureAtlas.TextureAtlasData atlas = new TextureAtlas.TextureAtlasData(packAtlas, imagesDir, false);
        BufferedImage finalImage = imageUtils.extractImage(atlas, textureRegionName, splits);
        imageUtils.saveImage(finalImage, plugin.getAPI().getProjectPath() + "/assets/orig/images/"+textureRegionName+".9.png");

        // now need to modify the pack
        String content = packAtlas.readString();
        int regionIndex = content.indexOf(textureRegionName);
        int splitStart = content.indexOf("split: ", regionIndex) + "split: ".length();
        int splitEnd = content.indexOf("orig: ", splitStart);
        String splitStr = splits[0]+", "+splits[1]+", "+splits[2]+", "+splits[3]+"\n  ";
        String newContent = content.substring(0, splitStart) + splitStr + content.substring(splitEnd, content.length());
        File test = new File(plugin.getAPI().getProjectPath() + "/assets/orig/pack/pack.atlas");
        writeFile(newContent, test);

        // reload
        plugin.getAPI().reLoadProject();
    }

    private void writeFile(String content, File file) {
        BufferedWriter output = null;
        try {
            output = new BufferedWriter(new FileWriter(file));
            output.write(content);
        } catch ( IOException e ) {
            e.printStackTrace();
        } finally {
            if ( output != null ) try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadRegion(String name) {
        TextureAtlas atlas = plugin.getAPI().getProjectTextureAtlas();
        viewComponent.setTexture(atlas.findRegion(name));

        viewComponent.setListeners(plugin.getAPI().getUIStage());
    }
}
