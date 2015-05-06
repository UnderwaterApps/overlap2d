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

package com.uwsoft.editor.mvc.view.ui.box.resourcespanel;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.puremvc.patterns.mediator.SimpleMediator;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.mvc.view.ui.box.resourcespanel.draggable.thumbnail.Image9PatchResource;
import com.uwsoft.editor.mvc.view.ui.box.resourcespanel.draggable.thumbnail.ImageResource;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.ProjectManager;
import com.uwsoft.editor.mvc.proxy.ResourceManager;

/**
 * Created by azakhary on 4/17/2015.
 */
public class UIImagesTabMediator extends SimpleMediator<UIImagesTab> {

    private static final String TAG = UIImagesTabMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    private ResourceManager resourceManager;

    private TextureAtlas atlas;

    public UIImagesTabMediator() {
        super(NAME, new UIImagesTab());
    }

    @Override
    public String[] listNotificationInterests() {
        return new String[]{
                ProjectManager.PROJECT_OPENED,
                ProjectManager.PROJECT_DATA_UPDATED
        };
    }

    @Override
    public void onRegister() {
        super.onRegister();

        facade = Overlap2DFacade.getInstance();
    }

    @Override
    public void handleNotification(Notification notification) {
        switch (notification.getName()) {
            case ProjectManager.PROJECT_OPENED:
                initImagesList();
                break;
            case ProjectManager.PROJECT_DATA_UPDATED:
                initImagesList();
                break;
            default:
                break;
        }
    }

    private void initImagesList() {
        resourceManager = facade.retrieveProxy(ResourceManager.NAME);

        atlas = resourceManager.getProjectAssetsList();

        Array<ImageResource> thumbnailBoxes = new Array<>();
        Array<TextureAtlas.AtlasRegion> atlasRegions = atlas.getRegions();
        for (TextureAtlas.AtlasRegion region : atlasRegions) {
            boolean is9patch = region.splits != null;
            ImageResource thumbnailBox = is9patch ? new Image9PatchResource(region) : new ImageResource(region);
            thumbnailBoxes.add(thumbnailBox);
        }

        viewComponent.setThumbnailBoxes(thumbnailBoxes);
    }
}
