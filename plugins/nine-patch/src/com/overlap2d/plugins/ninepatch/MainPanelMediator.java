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
import com.puremvc.patterns.mediator.SimpleMediator;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.renderer.components.NinePatchComponent;
import com.uwsoft.editor.renderer.components.TextureRegionComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by azakhary on 8/18/2015.
 */
public class MainPanelMediator extends SimpleMediator<MainPanel> {
    private static final String TAG = com.overlap2d.plugins.ninepatch.MainPanelMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    public static final String SCENE_LOADED = "com.uwsoft.editor.proxy.SceneDataManager.SCENE_LOADED";
    public static final String NEW_ITEM_ADDED = "com.uwsoft.editor.factory.ItemFactory.NEW_ITEM_ADDED";
    public static final String ACTION_DELETE = "com.uwsoft.editor.controller.commands.DeleteItemsCommandDONE";

    private NinePatchPlugin plugin;

    public MainPanelMediator(NinePatchPlugin plugin) {
        super(NAME, new MainPanel());
        this.plugin = plugin;
    }

    @Override
    public String[] listNotificationInterests() {
        return new String[]{
                NinePatchPlugin.PANEL_OPEN
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
        }
    }

    private void loadRegion(String name) {
        /*
        FileHandle imageFile = Gdx.files.internal(plugin.getAPI().getProjectPath() + "/assets/orig/images/"+name+".9.png");
        BufferedImage image;
        try {
            image = ImageIO.read(imageFile.file());
        } catch (IOException ex) {
            return;
        }
        if (image == null) return;
        ImageUtils imageUtils = new ImageUtils();
        imageUtils.getSplits(image, name);
        //Texture texture = new Texture(Gdx.files.internal(plugin.getAPI().getProjectPath() + "/assets/orig/images/"+name+".9.png"));
        int width = image.getWidth(), height = image.getHeight();
        // Strip split pixels.
        width -= 2;
        height -= 2;
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        newImage.getGraphics().drawImage(image, 0, 0, width, height, 1, 1, width + 1, height + 1, null);
        image = newImage;

        Texture texture = new Texture(new Pixmap(image));

        //viewComponent.setTexture(texture);
        */
        TextureAtlas atlas = plugin.getAPI().getProjectTextureAtlas();
        viewComponent.setTexture(atlas.findRegion(name));
    }
}
