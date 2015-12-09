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
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.commons.MsgAPI;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.controller.commands.AddComponentToItemCommand;
import com.uwsoft.editor.controller.commands.RemoveComponentFromItemCommand;
import com.uwsoft.editor.controller.commands.component.UpdatePolygonComponentCommand;
import com.uwsoft.editor.proxy.SceneDataManager;
import com.uwsoft.editor.renderer.components.PolygonComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.utils.poly.Clipper;
import com.uwsoft.editor.utils.poly.PolygonUtils;
import com.uwsoft.editor.view.stage.Sandbox;
import com.uwsoft.editor.view.ui.FollowersUIMediator;
import com.uwsoft.editor.view.ui.followers.BasicFollower;
import com.uwsoft.editor.view.ui.followers.PolygonFollower;
import com.uwsoft.editor.view.ui.followers.PolygonTransformationListener;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * Created by azakhary on 7/2/2015.
 */
public class PolygonTool extends SelectionTool implements PolygonTransformationListener {

    public static final String NAME = "MESH_TOOL";

    private FollowersUIMediator followersUIMediator;

    private Vector2 dragLastPoint;

    private Object[] currentCommandPayload;

    private PolygonFollower lastSelectedMeshFollower = null;
    private Vector2[][] polygonBackup = null;

    @Override
    public void initTool() {
        super.initTool();

        followersUIMediator = Overlap2DFacade.getInstance().retrieveMediator(FollowersUIMediator.NAME);

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
            case MsgAPI.ITEM_SELECTION_CHANGED:
                updateSubFollowerList();
                break;
            case MsgAPI.SCENE_LOADED:
                updateSubFollowerList();
                break;
        }
    }

    @Override
    public boolean itemMouseDown(Entity entity, float x, float y) {
        lastSelectedMeshFollower = getMeshFollower(entity);
        return super.itemMouseDown(entity, x, y);
    }

    private void setListener(PolygonFollower meshFollower) {
        meshFollower.setListener(this);
    }

    private void updateSubFollowerList() {
        Sandbox sandbox = Sandbox.getInstance();
        Set<Entity> selectedEntities = sandbox.getSelector().getSelectedItems();
        for(Entity entity: selectedEntities) {
            BasicFollower follower = followersUIMediator.getFollower(entity);
            follower.removeSubFollower(PolygonFollower.class);
            PolygonFollower meshFollower = new PolygonFollower(entity);
            follower.addSubfollower(meshFollower);
            setListener(meshFollower);
            lastSelectedMeshFollower = meshFollower;
        }
    }

    @Override
    public void vertexUp(PolygonFollower follower, int vertexIndex, float x, float y) {

    }

    @Override
    public void vertexDown(PolygonFollower follower, int vertexIndex, float x, float y) {
        PolygonComponent polygonComponent = ComponentRetriever.get(follower.getEntity(), PolygonComponent.class);
        currentCommandPayload = UpdatePolygonComponentCommand.payloadInitialState(follower.getEntity());

        follower.getOriginalPoints().add(vertexIndex, new Vector2(x, y));
        Vector2[] points = follower.getOriginalPoints().toArray(new Vector2[0]);

        polygonComponent.vertices = polygonize(points);
        follower.updateDraw();

        follower.draggingAnchorId = vertexIndex;
        dragLastPoint = new Vector2(x, y);
        follower.setSelectedAnchor(vertexIndex);
        lastSelectedMeshFollower = follower;

        polygonBackup = polygonComponent.vertices.clone();
    }

    @Override
    public void VertexMouseOver(PolygonFollower follower, int vertexIndex, float x, float y) {

    }

    @Override
    public void anchorDown(PolygonFollower follower, int anchor, float x, float y) {
        dragLastPoint = new Vector2(x, y);
        currentCommandPayload = UpdatePolygonComponentCommand.payloadInitialState(follower.getEntity());
        follower.setSelectedAnchor(anchor);
        lastSelectedMeshFollower = follower;

        PolygonComponent polygonComponent = ComponentRetriever.get(follower.getEntity(), PolygonComponent.class);
        polygonBackup = polygonComponent.vertices.clone();
    }

    @Override
    public void anchorDragged(PolygonFollower follower, int anchor, float x, float y) {
        PolygonComponent polygonComponent = ComponentRetriever.get(follower.getEntity(), PolygonComponent.class);

        Vector2[] points = follower.getOriginalPoints().toArray(new Vector2[0]);
        Vector2 diff = dragLastPoint.sub(x, y);
        points[anchor].sub(diff);
        dragLastPoint = new Vector2(x, y);

        // check if any of near lines intersect
        int[] intersections = checkForIntersection(anchor, points);
        if(intersections == null) {
            polygonComponent.vertices = polygonize(points);
            follower.setProblems(null);
        } else {
            follower.setProblems(intersections);
        }

        follower.updateDraw();
    }

    @Override
    public void anchorUp(PolygonFollower follower, int anchor, float x, float y) {
        PolygonComponent polygonComponent = ComponentRetriever.get(follower.getEntity(), PolygonComponent.class);

        Vector2[] points = follower.getOriginalPoints().toArray(new Vector2[0]);

        int[] intersections = checkForIntersection(anchor, points);
        if(intersections == null) {
            if(PolygonUtils.isPolygonCCW(points)){
                Collections.reverse(follower.getOriginalPoints());
                points = follower.getOriginalPoints().toArray(new Vector2[0]);
            }
            polygonComponent.vertices = polygonize(points);
        }

        if(polygonComponent.vertices == null) {
            // restore from backup
            polygonComponent.vertices = polygonBackup.clone();
        } else if(intersections != null) {
            polygonComponent.vertices = polygonBackup.clone();
        }

        follower.setProblems(null);

        currentCommandPayload = UpdatePolygonComponentCommand.payload(currentCommandPayload, polygonComponent.vertices);
        Overlap2DFacade.getInstance().sendNotification(MsgAPI.ACTION_UPDATE_MESH_DATA, currentCommandPayload);
    }

    private Vector2[][] polygonize(Vector2[] vertices) {
        return Clipper.polygonize(Clipper.Polygonizer.EWJORDAN, vertices);
    }

    @Override
    public void keyDown(Entity entity, int keycode) {
        if(keycode == Input.Keys.DEL || keycode == Input.Keys.FORWARD_DEL) {
            if(!deleteSelectedAnchor()) {
                super.keyDown(entity, keycode);
            }
        } else {
            super.keyDown(entity, keycode);
        }
    }

    private PolygonFollower getMeshFollower(Entity entity) {
        FollowersUIMediator followersUIMediator = Overlap2DFacade.getInstance().retrieveMediator(FollowersUIMediator.NAME);
        BasicFollower follower = followersUIMediator.getFollower(entity);

        PolygonFollower meshFollower = (PolygonFollower) (follower).getSubFollower(PolygonFollower.class);
        return meshFollower;
    }

    private boolean deleteSelectedAnchor() {
        PolygonFollower follower = lastSelectedMeshFollower;
        PolygonComponent polygonComponent = ComponentRetriever.get(follower.getEntity(), PolygonComponent.class);
        if(follower != null) {
            if(polygonComponent == null || polygonComponent.vertices == null || polygonComponent.vertices.length == 0) return false;
            if(follower.getOriginalPoints().size() <= 3) return false;

            polygonBackup = polygonComponent.vertices.clone();
            currentCommandPayload = UpdatePolygonComponentCommand.payloadInitialState(follower.getEntity());

            follower.getOriginalPoints().remove(follower.getSelectedAnchorId());
            follower.getSelectedAnchorId(follower.getSelectedAnchorId()-1);
            Vector2[] points = follower.getOriginalPoints().toArray(new Vector2[0]);
            polygonComponent.vertices = polygonize(points);

            if(polygonComponent.vertices == null) {
                // restore from backup
                polygonComponent.vertices = polygonBackup.clone();
                follower.update();
            }

            currentCommandPayload = UpdatePolygonComponentCommand.payload(currentCommandPayload, polygonComponent.vertices);
            Overlap2DFacade.getInstance().sendNotification(MsgAPI.ACTION_UPDATE_MESH_DATA, currentCommandPayload);

            follower.updateDraw();

            return true;
        }

        return false;
    }

    private boolean intersectSegments(Vector2[] points, int index1, int index2, int index3, int index4) {
        Vector2 intersectionPoint = new Vector2(points[index1]);
        boolean isIntersecting = Intersector.intersectSegments(points[index1], points[index2], points[index3], points[index4], intersectionPoint);
        if(isIntersecting && !isSamePoint(intersectionPoint, points[index1]) && !isSamePoint(intersectionPoint, points[index2]) && !isSamePoint(intersectionPoint, points[index3]) && !isSamePoint(intersectionPoint, points[index4])) {
            return true;
        }

        return false;
    }

    private boolean isSamePoint(Vector2 point1, Vector2 point2) {
        int pixelsPerWU = Sandbox.getInstance().getPixelPerWU();
        int precision = 10000 * pixelsPerWU;
        Vector2 pointA = new Vector2(point1);
        Vector2 pointB = new Vector2(point2);
        pointA.x = Math.round(point1.x * precision) / (float)precision;
        pointA.y = Math.round(point1.y * precision) / (float)precision;
        pointB.x = Math.round(point2.x * precision) / (float)precision;
        pointB.y = Math.round(point2.y * precision) / (float)precision;

        return pointA.equals(pointB);
    }


    private int[] checkForIntersection(int anchor, Vector2[] points) {
        int leftPointIndex = points.length-1;
        int rightPointIndex = 0;
        if(anchor > 0) {
            leftPointIndex = anchor-1;
        }
        if(anchor < points.length-1) {
            rightPointIndex =  anchor+1;
        }

        HashSet<Integer> problems = new HashSet<>();

        for(int i = 0; i < points.length-1; i++) {

           if(i != leftPointIndex && i != anchor) {
               if(intersectSegments(points, i, i+1, leftPointIndex, anchor)) {
                   problems.add(leftPointIndex);
               }
               if(intersectSegments(points, i, i+1, anchor, rightPointIndex)) {
                   problems.add(anchor);
               }
           }
        }
        if(anchor != points.length-1 && leftPointIndex != points.length-1 && intersectSegments(points, points.length-1, 0, leftPointIndex, anchor)) {
            problems.add(leftPointIndex);
        }
        if(anchor != points.length-1 && leftPointIndex != points.length-1 && intersectSegments(points, points.length-1, 0, anchor, rightPointIndex)) {
            problems.add(anchor);
        }

        if(problems.size() == 0) {
            return null;
        }

        int[] result = problems.stream().mapToInt(i->i).toArray();

        return result;
    }
}
