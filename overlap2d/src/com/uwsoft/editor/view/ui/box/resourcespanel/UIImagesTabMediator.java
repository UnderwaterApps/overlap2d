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

package com.uwsoft.editor.view.ui.box.resourcespanel;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.commons.MsgAPI;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.controller.commands.resource.DeleteImageResource;
import com.uwsoft.editor.controller.commands.resource.DeleteLibraryItem;
import com.uwsoft.editor.view.stage.Sandbox;
import com.uwsoft.editor.factory.ItemFactory;
import com.uwsoft.editor.proxy.ResourceManager;
import com.uwsoft.editor.view.ui.box.resourcespanel.draggable.DraggableResource;
import com.uwsoft.editor.view.ui.box.resourcespanel.draggable.box.ImageResource;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Created by azakhary on 4/17/2015.
 */
public class UIImagesTabMediator extends UIResourcesTabMediator<UIImagesTab> {

    private static final String TAG = UIImagesTabMediator.class.getCanonicalName();
    public static final String NAME = TAG;


    public UIImagesTabMediator() {
        super(NAME, new UIImagesTab());
    }

    @Override
    public String[] listNotificationInterests() {
        String[] listNotification = super.listNotificationInterests();

        listNotification = ArrayUtils.add(listNotification, DeleteImageResource.DONE);

        return listNotification;
    }

    @Override
    public void handleNotification(Notification notification) {
        super.handleNotification(notification);
        switch (notification.getName()) {
            case DeleteImageResource.DONE:
                initList(viewComponent.searchString);
                break;
            default:
                break;
        }
    }

    @Override
    protected void initList(String searchText) {
        Sandbox sandbox = Sandbox.getInstance();
        ResourceManager resourceManager = facade.retrieveProxy(ResourceManager.NAME);

        TextureAtlas atlas = resourceManager.getProjectAssetsList();

        Array<DraggableResource> thumbnailBoxes = new Array<>();
        Array<TextureAtlas.AtlasRegion> atlasRegions = atlas.getRegions();
        for (TextureAtlas.AtlasRegion region : atlasRegions) {
            if(!region.name.contains(searchText))continue;
            boolean is9patch = region.splits != null;
            DraggableResource draggableResource = new DraggableResource(new ImageResource(region));
            if (is9patch) {
                draggableResource.setFactoryFunction(ItemFactory.get()::create9Patch);
            } else {
                draggableResource.setFactoryFunction(ItemFactory.get()::createSimpleImage);
            }
            draggableResource.initDragDrop();
            thumbnailBoxes.add(draggableResource);
        }

        viewComponent.setThumbnailBoxes(thumbnailBoxes);
    }
}
