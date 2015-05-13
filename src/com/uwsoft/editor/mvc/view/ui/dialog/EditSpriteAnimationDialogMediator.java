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

package com.uwsoft.editor.mvc.view.ui.dialog;

import com.puremvc.patterns.mediator.SimpleMediator;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.Overlap2D;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.view.stage.UIStage;
import com.uwsoft.editor.mvc.view.ui.properties.panels.UIBasicItemProperties;
import com.uwsoft.editor.mvc.view.ui.properties.panels.UISpriteAnimationItemProperties;
import com.uwsoft.editor.renderer.actor.SpriteAnimation;

import java.util.Map;

/**
 * Created by azakhary on 5/12/2015.
 */
public class EditSpriteAnimationDialogMediator extends SimpleMediator<EditSpriteAnimationDialog> {
    private static final String TAG = EditSpriteAnimationDialogMediator.class.getCanonicalName();
    private static final String NAME = TAG;

    private SpriteAnimation observable;

    public EditSpriteAnimationDialogMediator() {
        super(NAME, new EditSpriteAnimationDialog());
    }

    @Override
    public void onRegister() {
        super.onRegister();
        facade = Overlap2DFacade.getInstance();
    }

    @Override
    public String[] listNotificationInterests() {
        return new String[]{
                Overlap2D.ITEM_SELECTED,
                UISpriteAnimationItemProperties.EDIT_ANIMATIONS_CLICKED,
                EditSpriteAnimationDialog.ADD_BUTTON_PRESSED,
                EditSpriteAnimationDialog.DELETE_BUTTON_PRESSED
        };
    }

    @Override
    public void handleNotification(Notification notification) {
        super.handleNotification(notification);

        Sandbox sandbox = Sandbox.getInstance();
        UIStage uiStage = sandbox.getUIStage();

        switch (notification.getName()) {
            case UISpriteAnimationItemProperties.EDIT_ANIMATIONS_CLICKED:
                viewComponent.show(uiStage);
                break;
            case Overlap2D.ITEM_SELECTED:
                if(notification.getBody() instanceof SpriteAnimation) {
                    setObservable(notification.getBody());
                }
            break;
            case EditSpriteAnimationDialog.ADD_BUTTON_PRESSED:
                addAnimation();
                updateView();
            break;
            case EditSpriteAnimationDialog.DELETE_BUTTON_PRESSED:
                removeAnimation(notification.getBody());
                updateView();
            break;
        }
    }

    private void setObservable(SpriteAnimation animation) {
        observable = animation;
        updateView();
        viewComponent.setName("");
        viewComponent.setFrameFrom(0);
        viewComponent.setFrameTo(0);
    }

    private void updateView() {
        Map<String, SpriteAnimation.Animation> animations = observable.getAnimations();
        viewComponent.updateView(animations);
    }

    private void addAnimation() {
        String name = viewComponent.getName();
        int frameFrom = viewComponent.getFrameFrom();
        int frameTo = viewComponent.getFrameTo();
        observable.getAnimations().put(name, new SpriteAnimation.Animation(frameFrom, frameTo, name));
        observable.updateDataVO();
        facade.sendNotification(Overlap2D.ITEM_DATA_UPDATED, observable);
    }

    private void removeAnimation(String name) {
        observable.getAnimations().remove(name);
        observable.updateDataVO();
        facade.sendNotification(Overlap2D.ITEM_DATA_UPDATED, observable);
    }
}
