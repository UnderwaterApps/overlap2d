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
import com.uwsoft.editor.data.SpineAnimData;
import com.uwsoft.editor.gdx.ui.thumbnailbox.AnimationThumbnailBox;
import com.uwsoft.editor.gdx.ui.thumbnailbox.SpineAnimationThumbnailBox;
import com.uwsoft.editor.gdx.ui.thumbnailbox.SpriteAnimationThumbnailBox;
import com.uwsoft.editor.gdx.ui.thumbnailbox.SpriterAnimationThumbnailBox;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.ProjectManager;
import com.uwsoft.editor.mvc.proxy.EditorTextureManager;
import com.uwsoft.editor.mvc.proxy.ResourceManager;

import java.util.HashMap;

/**
 * Created by azakhary on 4/17/2015.
 */
public class UIAnimationsTabMediator extends SimpleMediator<UIAnimationsTab> {

    private static final String TAG = UIAnimationsTabMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    private ResourceManager resourceManager;

    private HashMap<String, SpineAnimData> spineAnimations;
    private HashMap<String, TextureAtlas> spriteAnimations;
    private HashMap<String, FileHandle> spriterAnimations;

    public UIAnimationsTabMediator() {
        super(NAME, new UIAnimationsTab());
    }


    @Override
    public void onRegister() {
        super.onRegister();

        facade = Overlap2DFacade.getInstance();
    }

    @Override
    public String[] listNotificationInterests() {
        return new String[]{
                ProjectManager.PROJECT_OPENED,
                ProjectManager.PROJECT_DATA_UPDATED
        };
    }

    @Override
    public void handleNotification(Notification notification) {
        switch (notification.getName()) {
            case ProjectManager.PROJECT_OPENED:
//                initAnimationsList();
                break;
            case ProjectManager.PROJECT_DATA_UPDATED:
                initAnimationsList();
                break;
            default:
                break;
        }
    }

    private void initAnimationsList() {
        resourceManager = facade.retrieveProxy(ResourceManager.NAME);

        spineAnimations = resourceManager.getProjectSpineAnimationsList();
        spriteAnimations = resourceManager.getProjectSpriteAnimationsList();
        spriterAnimations = resourceManager.getProjectSpriterAnimationsList();

        Array<AnimationThumbnailBox> animationBoxes = new Array<>();

        for (String animationName : spineAnimations.keySet()) {
            SpineAnimationThumbnailBox thumb = new SpineAnimationThumbnailBox(spineAnimations.get(animationName));
            animationBoxes.add(thumb);
        }
        for (String animationName : spriteAnimations.keySet()) {
            SpriteAnimationThumbnailBox thumb = new SpriteAnimationThumbnailBox(animationName);
            animationBoxes.add(thumb);
        }
        for (String animationName : spriterAnimations.keySet()) {
            SpriterAnimationThumbnailBox thumb = new SpriterAnimationThumbnailBox(animationName);
            animationBoxes.add(thumb);
        }

        viewComponent.setThumbnailBoxes(animationBoxes);
    }
}
