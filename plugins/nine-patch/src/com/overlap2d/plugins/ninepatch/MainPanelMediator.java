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

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.puremvc.patterns.mediator.SimpleMediator;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.renderer.components.NinePatchComponent;
import com.uwsoft.editor.renderer.components.TextureRegionComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

import javax.imageio.ImageIO;
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
                NinePatchPlugin.PANEL_OPEN,
                MainPanel.SAVE_CLICKED
        };
    }

    @Override
    public void handleNotification(Notification notification) {
        super.handleNotification(notification);
        switch (notification.getName()) {
            case NinePatchPlugin.PANEL_OPEN:
                Entity entity = plugin.currEditingEntity;
                NinePatchComponent ninePatchComponent = ComponentRetriever.get(entity, NinePatchComponent.class);
                loadRegion(ninePatchComponent.textureRegionName);
                viewComponent.show(plugin.getStage());
                break;
            case MainPanel.SAVE_CLICKED:
                entity = plugin.currEditingEntity;
                ninePatchComponent = ComponentRetriever.get(entity, NinePatchComponent.class);
                applyNewSplits(ninePatchComponent.textureRegionName, viewComponent.getSplits());
                viewComponent.hide();
                break;
        }
    }

    private void applyNewSplits(String textureRegionName, int[] splits) {
        // first need to modify original image
        FileHandle packAtlas = Gdx.files.internal(plugin.getAPI().getProjectPath() + "/assets/orig/pack/pack.atlas");
        FileHandle imagesDir = Gdx.files.internal(plugin.getAPI().getProjectPath() + "/assets/orig/pack/");
        TextureAtlas.TextureAtlasData atlas = new TextureAtlas.TextureAtlasData(packAtlas, imagesDir, false);
        BufferedImage finalImage = imageUtils.extractImage(atlas, textureRegionName, splits);
        imageUtils.saveImage(finalImage, plugin.getAPI().getProjectPath() + "/assets/orig/images/qaq_"+textureRegionName+".9.png");

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

        viewComponent.setListeners(plugin.getStage());
    }
}
