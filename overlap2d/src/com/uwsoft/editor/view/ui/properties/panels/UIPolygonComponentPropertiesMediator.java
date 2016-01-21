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
import com.badlogic.gdx.math.Vector2;
import com.commons.MsgAPI;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.controller.commands.RemoveComponentFromItemCommand;
import com.uwsoft.editor.controller.commands.component.UpdatePolygonComponentCommand;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.PolygonComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.view.stage.Sandbox;
import com.uwsoft.editor.view.ui.properties.UIItemPropertiesMediator;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Created by azakhary on 7/2/2015.
 */
public class UIPolygonComponentPropertiesMediator extends UIItemPropertiesMediator<Entity, UIPolygonComponentProperties> {

    private static final String TAG = UIPolygonComponentPropertiesMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    private PolygonComponent polygonComponent;

    public UIPolygonComponentPropertiesMediator() {
        super(NAME, new UIPolygonComponentProperties());
    }

    @Override
    public String[] listNotificationInterests() {
        String[] defaultNotifications = super.listNotificationInterests();
        String[] notificationInterests = new String[]{
                UIPolygonComponentProperties.ADD_DEFAULT_MESH_BUTTON_CLICKED,
                UIPolygonComponentProperties.COPY_BUTTON_CLICKED,
                UIPolygonComponentProperties.PASTE_BUTTON_CLICKED,
                UIPolygonComponentProperties.CLOSE_CLICKED
        };

        return ArrayUtils.addAll(defaultNotifications, notificationInterests);
    }

    @Override
    public void handleNotification(Notification notification) {
        super.handleNotification(notification);

        switch (notification.getName()) {
            case UIPolygonComponentProperties.ADD_DEFAULT_MESH_BUTTON_CLICKED:
                addDefaultMesh();
                break;
            case UIPolygonComponentProperties.COPY_BUTTON_CLICKED:
                copyMesh();
                break;
            case UIPolygonComponentProperties.PASTE_BUTTON_CLICKED:
                pasteMesh();
                break;
            case UIPolygonComponentProperties.CLOSE_CLICKED:
                Overlap2DFacade.getInstance().sendNotification(MsgAPI.ACTION_REMOVE_COMPONENT, RemoveComponentFromItemCommand.payload(observableReference, PolygonComponent.class));
                break;
        }
    }

    @Override
    protected void translateObservableDataToView(Entity item) {
        polygonComponent = item.getComponent(PolygonComponent.class);
        if(polygonComponent.vertices != null) {
            viewComponent.initView();
            int verticesCount = 0;
            for(int i = 0; i < polygonComponent.vertices.length; i++) {
                for(int j = 0; j < polygonComponent.vertices[i].length; j++) {
                    verticesCount++;
                }
            }
            viewComponent.setVerticesCount(verticesCount);

        } else {
            viewComponent.initEmptyView();
        }
    }

    @Override
    protected void translateViewToItemData() {

    }

    private void addDefaultMesh() {
        DimensionsComponent dimensionsComponent = ComponentRetriever.get(observableReference, DimensionsComponent.class);
        if(dimensionsComponent.boundBox != null) { // If the bound box is not null we have a Composite Item!
            polygonComponent.makeRectangle( dimensionsComponent.boundBox.x, dimensionsComponent.boundBox.y, dimensionsComponent.boundBox.width, dimensionsComponent.boundBox.height);
        }
        else // Otherwise its a normal item
        {
            polygonComponent.makeRectangle( dimensionsComponent.width, dimensionsComponent.height );
        }

        Overlap2DFacade.getInstance().sendNotification(MsgAPI.ITEM_DATA_UPDATED, observableReference);
    }

    private void copyMesh() {
        polygonComponent = observableReference.getComponent(PolygonComponent.class);
        Sandbox.getInstance().copyToLocalClipboard("meshData", polygonComponent.vertices);
    }

    private void pasteMesh() {
        Vector2[][] vertices = (Vector2[][]) Sandbox.getInstance().retrieveFromLocalClipboard("meshData");
        if(vertices == null) return;
        Object[] payload = UpdatePolygonComponentCommand.payloadInitialState(observableReference);
        payload = UpdatePolygonComponentCommand.payload(payload, vertices);
        Overlap2DFacade.getInstance().sendNotification(MsgAPI.ACTION_UPDATE_MESH_DATA, payload);
    }
}
