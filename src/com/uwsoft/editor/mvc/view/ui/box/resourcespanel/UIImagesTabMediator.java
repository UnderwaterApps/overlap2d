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

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.puremvc.patterns.mediator.SimpleMediator;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.Overlap2D;
import com.uwsoft.editor.data.SpineAnimData;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.TextureManager;

import java.util.HashMap;

/**
 * Created by azakhary on 4/17/2015.
 */
public class UIImagesTabMediator extends SimpleMediator<UIImagesTab> {

    private static final String TAG = UIImagesTabMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    private TextureManager textureManager;

    private TextureAtlas atlas;

    public UIImagesTabMediator() {
        super(NAME, new UIImagesTab());
    }

    @Override
    public String[] listNotificationInterests() {
        return new String[]{
                Overlap2D.PROJECT_OPENED
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
            case Overlap2D.PROJECT_OPENED:
                initImagesList();
                break;
            default:
                break;
        }
    }

    private void initImagesList() {
        textureManager = facade.retrieveProxy(TextureManager.NAME);

        atlas = textureManager.getProjectAssetsList();

        Array<String> regionNames = new Array();

        for (int i = 0; i < atlas.getRegions().size; i++) {
            regionNames.add(atlas.getRegions().get(i).name);
        }

        viewComponent.setRegionNames(regionNames);
    }
}
