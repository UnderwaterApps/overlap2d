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

package com.uwsoft.editor.view.stage.tools;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.Overlap2D;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.controller.commands.AddComponentToItemCommand;
import com.uwsoft.editor.controller.commands.RemoveComponentFromItemCommand;
import com.uwsoft.editor.controller.commands.component.UpdateMeshComponentCommand;
import com.uwsoft.editor.proxy.SceneDataManager;
import com.uwsoft.editor.renderer.components.MeshComponent;
import com.uwsoft.editor.utils.poly.Clipper;
import com.uwsoft.editor.utils.poly.PolygonUtils;
import com.uwsoft.editor.utils.runtime.ComponentRetriever;
import com.uwsoft.editor.view.MidUIMediator;
import com.uwsoft.editor.view.stage.Sandbox;
import com.uwsoft.editor.view.ui.followers.BasicFollower;
import com.uwsoft.editor.view.ui.followers.MeshFollower;
import com.uwsoft.editor.view.ui.followers.MeshTransformationListener;
import com.uwsoft.editor.view.ui.followers.NormalSelectionFollower;

import java.util.Set;


/**
 * Created by azakhary on 7/2/2015.
 */
public class MeshTool extends SelectionTool implements MeshTransformationListener {

    public static final String NAME = "MESH_TOOL";

    private MidUIMediator midUIMediator;

    private Vector2 dragLastPoint;

    private Object[] currentCommandPayload;

    private MeshFollower lastSelectedMeshFollower = null;

    @Override
    public void initTool() {
        super.initTool();

        midUIMediator = Overlap2DFacade.getInstance().retrieveMediator(MidUIMediator.NAME);

        updateSubFollowerList();
    }

    @Override
    public void handleNotification(Notification notification) {
        switch (notification.getName()) {
            case AddComponentToItemCommand.DONE:
                updateSubFollowerList();
                break;
            case RemoveComponentFromItemCommand.DONE:
                updateSubFollowerList();
                break;
            case Overlap2D.ITEM_SELECTION_CHANGED:
                updateSubFollowerList();
                break;
            case SceneDataManager.SCENE_LOADED:
                updateSubFollowerList();
                break;
        }
    }

    @Override
    public boolean itemMouseDown(Entity entity, float x, float y) {
        lastSelectedMeshFollower = getMeshFollower(entity);
        return super.itemMouseDown(entity, x, y);
    }

    private void setListener(MeshFollower meshFollower) {
        meshFollower.setListener(this);
    }

    private void updateSubFollowerList() {
        Sandbox sandbox = Sandbox.getInstance();
        Set<Entity> selectedEntities = sandbox.getSelector().getSelectedItems();
        for(Entity entity: selectedEntities) {
            NormalSelectionFollower follower = (NormalSelectionFollower) midUIMediator.getFollower(entity);
            follower.removeSubFollower(MeshFollower.class);
            MeshFollower meshFollower = new MeshFollower(entity);
            follower.addSubfollower(meshFollower);
            setListener(meshFollower);
        }
    }

    @Override
    public void vertexUp(MeshFollower follower, int vertexIndex, float x, float y) {

    }

    @Override
    public void vertexDown(MeshFollower follower, int vertexIndex, float x, float y) {
        MeshComponent meshComponent = ComponentRetriever.get(follower.getEntity(), MeshComponent.class);
        currentCommandPayload = UpdateMeshComponentCommand.payloadInitialState(follower.getEntity());

        follower.getOriginalPoints().add(vertexIndex, new Vector2(x, y));
        Vector2[] points = follower.getOriginalPoints().toArray(new Vector2[0]);
        meshComponent.vertices = polygonize(points);
        follower.draggingAnchorId = vertexIndex;
        dragLastPoint = new Vector2(x, y);
        follower.setSelectedAnchor(vertexIndex);
        lastSelectedMeshFollower = follower;
    }

    @Override
    public void VertexMouseOver(MeshFollower follower, int vertexIndex, float x, float y) {

    }

    @Override
    public void anchorDown(MeshFollower follower, int anchor, float x, float y) {
        dragLastPoint = new Vector2(x, y);
        currentCommandPayload = UpdateMeshComponentCommand.payloadInitialState(follower.getEntity());
        follower.setSelectedAnchor(anchor);
        lastSelectedMeshFollower = follower;
    }

    @Override
    public void anchorDragged(MeshFollower follower, int anchor, float x, float y) {
        MeshComponent meshComponent = ComponentRetriever.get(follower.getEntity(), MeshComponent.class);

        Vector2[] points = follower.getOriginalPoints().toArray(new Vector2[0]);
        Vector2 diff = dragLastPoint.sub(x, y);
        points[anchor].sub(diff);
        dragLastPoint = new Vector2(x, y);
        meshComponent.vertices = polygonize(points);

        follower.updateDraw();
    }

    @Override
    public void anchorUp(MeshFollower follower, int anchor, float x, float y) {
        MeshComponent meshComponent = ComponentRetriever.get(follower.getEntity(), MeshComponent.class);

        currentCommandPayload = UpdateMeshComponentCommand.payload(currentCommandPayload, meshComponent.vertices);
        Overlap2DFacade.getInstance().sendNotification(Sandbox.ACTION_UPDATE_MESH_DATA, currentCommandPayload);
    }

    private Vector2[][] polygonize(Vector2[] vertices) {
        if (PolygonUtils.isPolygonCCW(vertices)) {
            // TODO
            System.out.println("IMPORTANT");
        }

        return Clipper.polygonize(Clipper.Polygonizer.BAYAZIT, vertices);
    }

    @Override
    public void keyDown(Entity entity, int keycode) {
        if(keycode == Input.Keys.DEL || keycode == Input.Keys.FORWARD_DEL) {
            deleteSelectedAnchor();
        } else {
            super.keyDown(entity, keycode);
        }
    }

    private MeshFollower getMeshFollower(Entity entity) {
        MidUIMediator midUIMediator = Overlap2DFacade.getInstance().retrieveMediator(MidUIMediator.NAME);
        BasicFollower follower = midUIMediator.getFollower(entity);
        if(follower instanceof NormalSelectionFollower) {
            MeshFollower meshFollower = (MeshFollower) ((NormalSelectionFollower) follower).getSubFollower(MeshFollower.class);
            return meshFollower;
        }

        return null;
    }

    private void deleteSelectedAnchor() {
        MeshFollower follower = lastSelectedMeshFollower;
        if(follower != null) {
            MeshComponent meshComponent = ComponentRetriever.get(follower.getEntity(), MeshComponent.class);
            currentCommandPayload = UpdateMeshComponentCommand.payloadInitialState(follower.getEntity());

            follower.getOriginalPoints().remove(follower.getSelectedAnchorId());
            Vector2[] points = follower.getOriginalPoints().toArray(new Vector2[0]);
            meshComponent.vertices = polygonize(points);
            follower.updateDraw();
        }
    }
}
