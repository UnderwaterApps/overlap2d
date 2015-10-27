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

package com.uwsoft.editor.view.ui.properties.panels;

import com.commons.color.ColorPickerAdapter;
import com.commons.color.CustomColorPicker;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.badlogic.gdx.graphics.Color;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.view.stage.Sandbox;
import com.uwsoft.editor.view.ui.properties.UIAbstractPropertiesMediator;
import com.uwsoft.editor.renderer.data.PhysicsPropertiesVO;
import com.uwsoft.editor.renderer.data.SceneVO;

/**
 * Created by azakhary on 4/16/2015.
 */
public class UIScenePropertiesMediator extends UIAbstractPropertiesMediator<SceneVO, UISceneProperties> {
    private static final String TAG = UIScenePropertiesMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    public UIScenePropertiesMediator() {
        super(NAME, new UISceneProperties());
    }

    @Override
    public String[] listNotificationInterests() {
        String[] defaultNotifications = super.listNotificationInterests();
        String[] notificationInterests = new String[]{
                UISceneProperties.AMBIENT_COLOR_BUTTON_CLICKED
        };

        return ArrayUtils.addAll(defaultNotifications, notificationInterests);
    }

    @Override
    public void handleNotification(Notification notification) {
        super.handleNotification(notification);

        switch (notification.getName()) {
            case UISceneProperties.AMBIENT_COLOR_BUTTON_CLICKED:
                CustomColorPicker picker = new CustomColorPicker(new ColorPickerAdapter() {
                    @Override
                    public void finished(Color newColor) {
                        viewComponent.setAmbientColor(newColor);
                        facade.sendNotification(viewComponent.getUpdateEventName());
                    }
                    @Override
                    public void changed(Color newColor) {
                        viewComponent.setAmbientColor(newColor);
                        facade.sendNotification(viewComponent.getUpdateEventName());
                    }
                });

                picker.setColor(viewComponent.getAmbientColor());
                Sandbox.getInstance().getUIStage().addActor(picker.fadeIn());

                break;
            default:
                break;
        }
    }

    protected void translateObservableDataToView(SceneVO item) {
        PhysicsPropertiesVO physicsVO = item.physicsPropertiesVO;

        viewComponent.setPixelsPerWorldUnit(Sandbox.getInstance().getPixelPerWU());

        viewComponent.setGravityXValue(physicsVO.gravityX + "");
        viewComponent.setGravityYValue(physicsVO.gravityY + "");
        viewComponent.setPhysicsEnable(physicsVO.enabled);
        viewComponent.setSleepVelocityValue(physicsVO.sleepVelocity + "");
        viewComponent.setAmbientColor(new Color(item.ambientColor[0], item.ambientColor[1], item.ambientColor[2], item.ambientColor[3]));

        viewComponent.setLightsEnabled(item.lightSystemEnabled);
        viewComponent.setDiffuse(Sandbox.getInstance().sceneControl.isDiffuse());
    }

    @Override
    protected void translateViewToItemData() {
        PhysicsPropertiesVO physicsVO = observableReference.physicsPropertiesVO;
        physicsVO.gravityX = NumberUtils.toFloat(viewComponent.getGravityXValue(), physicsVO.gravityX);
        physicsVO.gravityY = NumberUtils.toFloat(viewComponent.getGravityYValue(), physicsVO.gravityY);
        physicsVO.sleepVelocity = NumberUtils.toFloat(viewComponent.getSleepVelocityValue(), physicsVO.sleepVelocity);
        physicsVO.enabled = viewComponent.isPhysicsEnabled();
        Color color = viewComponent.getAmbientColor();
        observableReference.ambientColor[0] = color.r;
        observableReference.ambientColor[1] = color.g;
        observableReference.ambientColor[2] = color.b;
        observableReference.ambientColor[3] = color.a;

        observableReference.lightSystemEnabled = viewComponent.isLightsEnabled();

        Sandbox.getInstance().setSceneAmbientColor(color, viewComponent.isLightsEnabled());

        Sandbox.getInstance().sceneControl.disableLights(!observableReference.lightSystemEnabled);
        Sandbox.getInstance().sceneControl.disableAmbience(!observableReference.lightSystemEnabled);

        Sandbox.getInstance().sceneControl.setDiffuse(viewComponent.isDiffuse());
    }
}
