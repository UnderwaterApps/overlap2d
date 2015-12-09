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

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.commons.MsgAPI;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.controller.commands.RemoveComponentFromItemCommand;
import com.uwsoft.editor.proxy.ResourceManager;
import com.uwsoft.editor.renderer.components.ShaderComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.view.ui.properties.UIItemPropertiesMediator;
import org.apache.commons.lang3.ArrayUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by azakhary on 8/12/2015.
 */
public class UIShaderPropertiesMediator extends UIItemPropertiesMediator<Entity, UIShaderProperties> {
    private static final String TAG = UIShaderPropertiesMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    public UIShaderPropertiesMediator() {
        super(NAME, new UIShaderProperties());

        ResourceManager resourceManager = Overlap2DFacade.getInstance().retrieveProxy(ResourceManager.NAME);
        viewComponent.initView(resourceManager.getShaders());
    }

    @Override
    public String[] listNotificationInterests() {
        String[] defaultNotifications = super.listNotificationInterests();
        String[] notificationInterests = new String[]{
                UIShaderProperties.CLOSE_CLICKED
        };

        return ArrayUtils.addAll(defaultNotifications, notificationInterests);
    }

    @Override
    public void handleNotification(Notification notification) {
        super.handleNotification(notification);

        switch (notification.getName()) {
            case UIPhysicsProperties.CLOSE_CLICKED:
                Overlap2DFacade.getInstance().sendNotification(MsgAPI.ACTION_REMOVE_COMPONENT, RemoveComponentFromItemCommand.payload(observableReference, ShaderComponent.class));
                break;
        }
    }

    @Override
    protected void translateObservableDataToView(Entity item) {
        ResourceManager resourceManager = Overlap2DFacade.getInstance().retrieveProxy(ResourceManager.NAME);

        ShaderComponent shaderComponent = ComponentRetriever.get(item, ShaderComponent.class);
        String currShaderName = shaderComponent.shaderName;
        viewComponent.setSelected(currShaderName);
    }

    @Override
    protected void translateViewToItemData() {
        ResourceManager resourceManager = Overlap2DFacade.getInstance().retrieveProxy(ResourceManager.NAME);
        ShaderComponent shaderComponent = ComponentRetriever.get(observableReference, ShaderComponent.class);
        String shaderName = viewComponent.getShader();
        if(shaderName.equals("Default")) {
            shaderComponent.clear();
        } else {
            shaderComponent.setShader(shaderName, resourceManager.getShaderProgram(shaderName));
        }
    }

    private String findShaderProgramName(HashMap<String, ShaderProgram> list, ShaderProgram object) {
        for (Map.Entry<String, ShaderProgram> entry : list.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if(value == object) {
                return key;
            }
        }
        return null;
    }
}
