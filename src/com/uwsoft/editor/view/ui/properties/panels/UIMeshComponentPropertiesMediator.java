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
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.Overlap2D;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.controller.commands.RemoveComponentFromItemCommand;
import com.uwsoft.editor.controller.commands.component.UpdateMeshComponentCommand;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.MeshComponent;
import com.uwsoft.editor.utils.runtime.ComponentRetriever;
import com.uwsoft.editor.view.stage.Sandbox;
import com.uwsoft.editor.view.ui.properties.UIItemPropertiesMediator;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Created by azakhary on 7/2/2015.
 */
public class UIMeshComponentPropertiesMediator extends UIItemPropertiesMediator<Entity, UIMeshComponentProperties> {

    private static final String TAG = UIMeshComponentPropertiesMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    private MeshComponent meshComponent;

    public UIMeshComponentPropertiesMediator() {
        super(NAME, new UIMeshComponentProperties());
    }

    @Override
    public String[] listNotificationInterests() {
        String[] defaultNotifications = super.listNotificationInterests();
        String[] notificationInterests = new String[]{
                UIMeshComponentProperties.ADD_DEFAULT_MESH_BUTTON_CLICKED,
                UIMeshComponentProperties.COPY_BUTTON_CLICKED,
                UIMeshComponentProperties.PASTE_BUTTON_CLICKED,
                UIMeshComponentProperties.CLOSE_CLICKED
        };

        return ArrayUtils.addAll(defaultNotifications, notificationInterests);
    }

    @Override
    public void handleNotification(Notification notification) {
        super.handleNotification(notification);

        switch (notification.getName()) {
            case UIMeshComponentProperties.ADD_DEFAULT_MESH_BUTTON_CLICKED:
                addDefaultMesh();
                break;
            case UIMeshComponentProperties.COPY_BUTTON_CLICKED:
                copyMesh();
                break;
            case UIMeshComponentProperties.PASTE_BUTTON_CLICKED:
                pasteMesh();
                break;
            case UIMeshComponentProperties.CLOSE_CLICKED:
                Overlap2DFacade.getInstance().sendNotification(Sandbox.ACTION_REMOVE_COMPONENT, RemoveComponentFromItemCommand.payload(observableReference, MeshComponent.class));
                break;
        }
    }

    @Override
    protected void translateObservableDataToView(Entity item) {
        meshComponent = item.getComponent(MeshComponent.class);
        if(meshComponent.vertices != null) {
            viewComponent.initView();
            int verticesCount = 0;
            for(int i = 0; i < meshComponent.vertices.length; i++) {
                for(int j = 0; j < meshComponent.vertices[i].length; j++) {
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
        meshComponent.makeRectangle(dimensionsComponent.width, dimensionsComponent.height);

        Overlap2DFacade.getInstance().sendNotification(Overlap2D.ITEM_DATA_UPDATED, observableReference);
    }

    private void copyMesh() {
        meshComponent = observableReference.getComponent(MeshComponent.class);
        Sandbox.getInstance().copyToLocalClipboard("meshData", meshComponent.vertices);
    }

    private void pasteMesh() {
        Vector2[][] vertices = (Vector2[][]) Sandbox.getInstance().retrieveFromLocalClipboard("meshData");
        if(vertices == null) return;
        Object[] payload = UpdateMeshComponentCommand.payloadInitialState(observableReference);
        payload = UpdateMeshComponentCommand.payload(payload, vertices);
        Overlap2DFacade.getInstance().sendNotification(Sandbox.ACTION_UPDATE_MESH_DATA, payload);
    }
}
