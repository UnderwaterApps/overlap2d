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

package com.uwsoft.editor.gdx.ui.components;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.Pools;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.EditorTextureManager;
import com.uwsoft.editor.mvc.proxy.ProjectManager;
import com.uwsoft.editor.mvc.view.stage.UIStage;
import com.uwsoft.editor.renderer.actor.IBaseItem;
import com.uwsoft.editor.renderer.legacy.data.MeshVO;
import com.uwsoft.editor.renderer.legacy.data.PhysicsBodyDataVO;
import com.uwsoft.editor.renderer.legacy.data.ProjectInfoVO;
import com.uwsoft.editor.renderer.physics.PhysicsBodyLoader;
import com.uwsoft.editor.utils.poly.Clipper;
import com.uwsoft.editor.utils.poly.Clipper.Polygonizer;
import com.uwsoft.editor.utils.poly.PolygonUtils;
import com.uwsoft.editor.utils.poly.tracer.Tracer;

public class ItemPhysicsEditor extends Group {
    public static final int POINT_WIDTH = 10;
    public static final int CIRCLE_RADIUS = 10;

    public static Polygonizer polygonizer = Polygonizer.BAYAZIT;
    public static float autoTraceHullTolerance = 2.5f;
    public static int autoTraceAlphaTolerance = 128;
    public static boolean autoTraceMultiPartDetection = false;
    public static boolean autoTraceHoleDetection = false;
    private final Overlap2DFacade facade;
    private final ProjectManager projectManager;
    private final Sandbox sandbox;
    public IBaseItem originalItem;
    public EditMode currentMode;
    public PhysicsBodyDataVO physicsBodyDataVO = new PhysicsBodyDataVO();
    public World physicsEditorWorld;
    public Box2DDebugRenderer box2dRenderer;
    public float zoomFactor = 1f;
    private Color goodColor = Color.GREEN;
    private Color badColor = Color.RED;
    private Color selectedLineColor = Color.MAGENTA;
    private Body currentItemBody;
    private ArrayList<Body> edgBodyList;
    private ArrayList<Body> testBodiesToDestroy;
    private ShapeRenderer shapeRenderer;
    private boolean isDragging = false;
    private Vector2[] vertices;
    private ArrayList<Vector2> verticesList;
    private Vector2 tmpvector = new Vector2();
    private Color lastLineColor = goodColor;
    private Vector2 nextPoint = new Vector2();
    private Vector2[][] minPolies;

    private selectedPoint selectedPoint = new selectedPoint(0, 0, 0);
    private selectedPoint nearestPoint = new selectedPoint(0, 0, 0);

    private int lineIndex = -1;
    private String assetName = null;
    private UIStage stage;
    private IBaseItem currentItem;
    private Actor currentActor;
    private float timeAcc = 0;
    public Vector2 resVec;

    private Color color;

    public ItemPhysicsEditor(float width, float height) {
        sandbox = Sandbox.getInstance();
        testBodiesToDestroy = new ArrayList();
        edgBodyList = new ArrayList<>();
        setWidth(width);
        setHeight(height);

        verticesList = new ArrayList<Vector2>();

        vertices = new Vector2[0];

        color = new Color(80f/255f, 146f/255f, 204f/255f, 1f);

        setListeners();

        currentMode = EditMode.Create;
        box2dRenderer = new Box2DDebugRenderer();

        facade = Overlap2DFacade.getInstance();
        projectManager = facade.retrieveProxy(ProjectManager.NAME);

        stage = Sandbox.getInstance().getUIStage();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(stage.getCamera().combined);
    }

    public void startTest() {
        if (minPolies == null) {
            return;
        }
        testBodiesToDestroy.clear();
        MeshVO vo = new MeshVO();
        vo.minPolygonData = minPolies;
        physicsEditorWorld = new World(new Vector2(0, -10), true);
        physicsEditorWorld.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
                if (edgBodyList.indexOf(contact.getFixtureA().getBody()) != -1) {
                    testBodiesToDestroy.add(contact.getFixtureB().getBody());
                }
                if (edgBodyList.indexOf(contact.getFixtureB().getBody()) != -1) {
                    testBodiesToDestroy.add(contact.getFixtureA().getBody());
                }
            }
        });
        currentItemBody = PhysicsBodyLoader.createBody(physicsEditorWorld, physicsBodyDataVO, vo, new Vector2(1, 1));
        //
        crateEdgPlatform(0, 0, getWidth(), 0);
        crateEdgPlatform(0, 0, 0, getHeight());
        crateEdgPlatform(getWidth(), 0, 0, getHeight());
        crateEdgPlatform(0, getHeight(), getWidth(), 0);

        currentMode = EditMode.Test;
        nearestPoint.unSet();
        selectedPoint.unSet();
        lineIndex = -1;
    }

    private void crateEdgPlatform(float x, float y, float w, float h) {
        BodyDef bodyDef = new BodyDef();
        Vector2 vec = new Vector2(x, y);
        this.localToStageCoordinates(vec);
        vec.scl(PhysicsBodyLoader.SCALE);
        bodyDef.position.set(vec);
        bodyDef.type = BodyDef.BodyType.StaticBody;
        Body platformBody = physicsEditorWorld.createBody(bodyDef);
        FixtureDef fixtureDef = new FixtureDef();
        EdgeShape polygonShape = new EdgeShape();
        polygonShape.set(0, 0, w * PhysicsBodyLoader.SCALE, h * PhysicsBodyLoader.SCALE);
        fixtureDef.shape = polygonShape;
        platformBody.createFixture(fixtureDef);
        edgBodyList.add(platformBody);
    }

    public void endTest() {
        if (minPolies == null) {
            return;
        }
        if (minPolies.length > 0) {
            currentMode = EditMode.Edit;
        }
        edgBodyList.clear();
        if (currentItemBody != null) {
            physicsEditorWorld.destroyBody(currentItemBody);
        }
        physicsEditorWorld.dispose();
        physicsEditorWorld = null;
        testBodiesToDestroy.clear();
    }

    public void editAsset(String assetName) {
        this.assetName = assetName;
        EditorTextureManager textureManager = facade.retrieveProxy(EditorTextureManager.NAME);
        Texture texture = textureManager.getRegionOriginalImage(assetName);
        Image img = new Image(texture);
        currentActor = img;
        if (currentActor.getWidth() > this.getWidth() || currentActor.getHeight() > this.getHeight()) {
            if (currentActor.getWidth() > currentActor.getHeight()) {
                zoomFactor = this.getWidth() / currentActor.getWidth();
            } else {
                zoomFactor = this.getHeight() / currentActor.getHeight();
            }
            System.out.println("zoom Amount" + zoomFactor);
        }


        currentActor.setScale(zoomFactor);

        currentActor.setX((getWidth() - currentActor.getWidth() * zoomFactor) / 2);
        currentActor.setY((getHeight() - currentActor.getHeight() * zoomFactor) / 2);
        addActor(currentActor);


        ProjectInfoVO projectInfo = projectManager.getCurrentProjectInfoVO();
        MeshVO mesh = null;
        if (projectInfo.assetMeshMap.containsKey(assetName)) {
            mesh = projectInfo.meshes.get(projectInfo.assetMeshMap.get(assetName));
            if (mesh.initialProperties != null) {
                physicsBodyDataVO = new PhysicsBodyDataVO(mesh.initialProperties);
            }
            minPolies = new Vector2[mesh.minPolygonData.length][];
            //System.arraycopy(mesh.minPolygonData, 0, minPolies, 0, mesh.minPolygonData.length);
            arrayCopy(mesh.minPolygonData, minPolies, false);

            Vector2 localToGlobal = new Vector2();
            currentActor.localToStageCoordinates(localToGlobal);
            for (Vector2[] poly : minPolies) {
                for (int i = 0; i < poly.length; i++) {
                    if (!verticesList.contains(poly[i]))
                        verticesList.add(poly[i]);
                }
            }

            for (Vector2 poly : verticesList) {
                poly.add(localToGlobal);
            }

            currentMode = EditMode.Edit;
        }
        Collections.reverse(verticesList);
        vertices = verticesList.toArray(new Vector2[0]);
        stage.setScrollFocus(this);

        printArray(vertices);
        if (mesh != null) {
            printArray(minPolies);
        }
    }

    public void editItem(IBaseItem item) {
        currentItem = item;
        currentActor = (Actor) currentItem;

        if (currentActor.getWidth() > this.getWidth() || currentActor.getHeight() > this.getHeight()) {
            if (currentActor.getWidth() > currentActor.getHeight()) {
                zoomFactor = this.getWidth() / currentActor.getWidth();
            } else {
                zoomFactor = this.getHeight() / currentActor.getHeight();
            }
            System.out.println("zoom Amount" + zoomFactor);
        }

        currentActor.setScale(zoomFactor);


        currentActor.setX((getWidth() - currentActor.getWidth() * zoomFactor) / 2);
        currentActor.setY((getHeight() - currentActor.getHeight() * zoomFactor) / 2);
        addActor(currentActor);


        if (currentItem.getDataVO().physicsBodyData != null) {
            physicsBodyDataVO = new PhysicsBodyDataVO(currentItem.getDataVO().physicsBodyData);
        }

        ProjectInfoVO projectInfo = projectManager.getCurrentProjectInfoVO();
        if (Integer.parseInt(currentItem.getDataVO().meshId) >= 0) {
            Vector2 localToGlobal = new Vector2();
            currentActor.localToStageCoordinates(localToGlobal);
            MeshVO mesh = projectInfo.meshes.get(currentItem.getDataVO().meshId);
            minPolies = new Vector2[mesh.minPolygonData.length][];
            //System.arraycopy(mesh.minPolygonData, 0, minPolies, 0, mesh.minPolygonData.length);
            arrayCopy(mesh.minPolygonData, minPolies, false);
            for (Vector2[] poly : minPolies) {
                for (int i = 0; i < poly.length; i++) {
                    if (!verticesList.contains(poly[i]))
                        verticesList.add(poly[i]);
                }
            }

            for (Vector2 poly : verticesList) {
                poly.scl(resVec).add(localToGlobal);
            }

            currentMode = EditMode.Edit;
        }
        Collections.reverse(verticesList);
        vertices = verticesList.toArray(new Vector2[0]);
        stage.setScrollFocus(this);
    }

    public void reTrace() {

        if (assetName == null) {
            return;
        }

        System.out.println("Retrace");
        EditorTextureManager textureManager = facade.retrieveProxy(EditorTextureManager.NAME);
        Texture texture = textureManager.getRegionOriginalImage(assetName);
        Image img = new Image(texture);
        img.setX((getWidth() - img.getWidth()) / 2);
        img.setY((getHeight() - img.getHeight()) / 2);
        addActor(img);

        Vector2 localToGlobal = new Vector2();
        img.localToStageCoordinates(localToGlobal);
        minPolies = Tracer.trace(texture, autoTraceHullTolerance, autoTraceAlphaTolerance, autoTraceMultiPartDetection, autoTraceHoleDetection);
        for (Vector2[] poly : minPolies) {
            for (int i = 0; i < poly.length; i++) {
                poly[i].x *= img.getWidth();
                poly[i].x += localToGlobal.x;
                poly[i].y *= img.getHeight();
                poly[i].y += localToGlobal.y;
                if (!verticesList.contains(poly[i]))
                    verticesList.add(poly[i]);
            }
        }

        currentMode = EditMode.Edit;
        vertices = verticesList.toArray(new Vector2[0]);
        finish();
    }

    public void save() {
        World world = Sandbox.getInstance().getSandboxStage().getWorld();

        ProjectInfoVO projectInfo = projectManager.getCurrentProjectInfoVO();
        MeshVO mesh = null;
        if (assetName != null && !assetName.isEmpty()) {
            if (projectInfo.assetMeshMap.containsKey(assetName)) {
                mesh = projectInfo.meshes.get(projectInfo.assetMeshMap.get(assetName));
                mesh.minPolygonData = new Vector2[minPolies.length][];
                mesh.initialProperties = new PhysicsBodyDataVO(physicsBodyDataVO);
                arrayCopy(minPolies, mesh.minPolygonData, true);
                printArray(vertices);
                if (mesh != null) {
                    printArray(mesh.minPolygonData);
                }
            } else {
                if (minPolies == null) {
                    projectInfo.assetMeshMap.remove(assetName);
                } else {
                    mesh = new MeshVO();
                    mesh.minPolygonData = new Vector2[minPolies.length][];
                    mesh.initialProperties = new PhysicsBodyDataVO(physicsBodyDataVO);
                    arrayCopy(minPolies, mesh.minPolygonData, true);
                    String meshKey = projectInfo.addNewMesh(mesh);
                    projectInfo.assetMeshMap.put(assetName, meshKey + "");
                }
                printArray(vertices);
                if (mesh != null) {
                    printArray(mesh.minPolygonData);
                }


            }
        } else if (currentItem != null) {
            if (Integer.parseInt(currentItem.getDataVO().meshId) >= 0) {
                mesh = projectInfo.meshes.get(currentItem.getDataVO().meshId);
                mesh.minPolygonData = new Vector2[minPolies.length][];
                arrayCopy(minPolies, mesh.minPolygonData, true);
            } else {
                if (minPolies == null) {
                    currentItem.getDataVO().meshId = "-1";
                } else {
                    mesh = new MeshVO();
                    mesh.minPolygonData = new Vector2[minPolies.length][];
                    arrayCopy(minPolies, mesh.minPolygonData, true);
                    String meshKey = projectInfo.addNewMesh(mesh);
                    currentItem.getDataVO().meshId = meshKey;
                }
            }
            Vector2 resVec = new Vector2(sandbox.getCurrentScene().mulX, sandbox.getCurrentScene().mulY);

            if (mesh != null) {
                if (originalItem.getBody() != null) {
                    world.destroyBody(originalItem.getBody());
                }
                currentItem.getDataVO().physicsBodyData = new PhysicsBodyDataVO(physicsBodyDataVO);
                originalItem.setBody(PhysicsBodyLoader.createBody(world, physicsBodyDataVO, mesh, resVec));
                originalItem.getBody().setTransform(currentItem.getDataVO().x * PhysicsBodyLoader.SCALE, currentItem.getDataVO().y * PhysicsBodyLoader.SCALE, (float) Math.toRadians(currentItem.getDataVO().rotation));
            }

            System.out.println("asd");
        }
        projectManager.saveCurrentProject();

    }

    public void printArray(Vector2[] array) {
        String strToPrint = "";
        for (int i = 0; i < array.length; i++) {
            strToPrint += " " + array[i].toString();
        }
        System.out.println("array" + strToPrint);
    }

    public void printArray(Vector2[][] array) {
        String strToPrint = "";
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                strToPrint += " " + array[i][j].toString();
            }
            System.out.println("array " + i + " " + strToPrint);
        }
    }

    public void arrayCopy(Vector2[][] aSource, Vector2[][] aDestination, boolean toLocal) {
        //Vector2 globalToLocal = new Vector2();

        for (int i = 0; i < aSource.length; i++) {
            aDestination[i] = new Vector2[aSource[i].length];
            for (int j = 0; j < aSource[i].length; j++) {
                aDestination[i][j] = aSource[i][j].cpy();
                if (toLocal && currentActor != null) {

                    currentActor.stageToLocalCoordinates(aDestination[i][j]);
                    if (assetName == null || assetName.isEmpty()) {
                        aDestination[i][j].x /= resVec.x;
                        aDestination[i][j].y /= resVec.y;
                    }
                    //aDestination[i][j].sub(globalToLocal);
                }
            }
        }
    }

    private void setListeners() {
        addListener(new InputListener() {

            public float touchDownX;
            public float touchDownY;

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (currentMode == EditMode.Test) {
                    touchDownX = x;
                    touchDownY = y;
                    return true;
                }
                stage.setKeyboardFocus(ItemPhysicsEditor.this);

                localToStageCoordinates(tmpvector.set(x, y));

                if (vertices.length == 0) {
                    verticesList.add(tmpvector.cpy());
                    System.out.println("ADD POINT " + tmpvector.toString());
                }
                selectedPoint.unSet();
                if (currentMode == EditMode.Edit && vertices.length > 1) {
                    for (int i = 0; i < vertices.length; i++) {
                        //shapeRenderer.rect(vertices[i-1]-POINT_WIDTH/2, vertices[i]-POINT_WIDTH/2, POINT_WIDTH, POINT_WIDTH);
                        if (tmpvector.x >= (vertices[i].x - POINT_WIDTH / 2) && tmpvector.x <= (vertices[i].x + POINT_WIDTH / 2)
                                && tmpvector.y >= (vertices[i].y - POINT_WIDTH / 2) && tmpvector.y <= (vertices[i].y + POINT_WIDTH / 2)) {
                            selectedPoint.set(vertices[i], i);
                            //System.out.println("waaaaaaaaaaaaaaaaaaaaaaaaa");
                            break;
                        }
                    }

                    if (currentMode == EditMode.Edit && lineIndex >= 0 && !selectedPoint.isSet) {
//						if(lineIndex == vertices.length-1){
//							verticesList.add(tmpvector.cpy());
//						}else{
                        verticesList.add(lineIndex, tmpvector.cpy());
                        selectedPoint.set(vertices[lineIndex], lineIndex);
                        vertices = verticesList.toArray(new Vector2[0]);
                        //}
                        finish();
                    }
                }

//                else if (currentMode==EditMode.Test){


//                }
                isDragging = false;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (currentMode == EditMode.Test) {
                    BodyDef bodyDef = new BodyDef();
                    Vector2 vec = new Vector2(touchDownX, touchDownY);
                    ItemPhysicsEditor.this.localToStageCoordinates(vec);
                    vec.scl(PhysicsBodyLoader.SCALE);
                    bodyDef.position.set(vec);
                    bodyDef.type = BodyDef.BodyType.DynamicBody;
                    Body body = physicsEditorWorld.createBody(bodyDef);
                    FixtureDef fixtureDef = new FixtureDef();
                    fixtureDef.shape = new CircleShape();
                    fixtureDef.shape.setRadius(10 * PhysicsBodyLoader.SCALE);
                    body.createFixture(fixtureDef);
                    body.applyLinearImpulse((x - touchDownX) * PhysicsBodyLoader.SCALE * 5, (y - touchDownY) * PhysicsBodyLoader.SCALE * 5, 0, 0, false);
                    return;
                }

                System.out.println("waaat up");
                isDragging = true;
                localToStageCoordinates(tmpvector.set(x, y));

                if (currentMode == EditMode.Edit) {
                    finish();
                    //selectedPoint.unSet();
                    nearestPoint.unSet();
                    return;
                }
                if (nearestPoint.isSet && nearestPoint.index == 0 && currentMode == EditMode.Create) {
                    finish();
                    selectedPoint.unSet();
                    nearestPoint.unSet();
                    return;
                }

                if (vertices.length > 0 && !checkForIntersection(vertices[vertices.length - 1], tmpvector) && currentMode == EditMode.Create) {
                    if (!verticesList.contains(tmpvector)) {
                        verticesList.add(tmpvector.cpy());
                        System.out.println("ADD POINT " + tmpvector.toString());
                    }

                }
                nextPoint.set(tmpvector);
                vertices = verticesList.toArray(new Vector2[0]);
            }


            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (selectedPoint.isSet) {
                    localToStageCoordinates(tmpvector.set(x, y));
                    vertices[selectedPoint.index].set(tmpvector);
                    verticesList.get(selectedPoint.index).set(tmpvector);
                    finish();
                }
                super.touchDragged(event, x, y, pointer);
            }

            @Override
            public boolean mouseMoved(InputEvent event, float x, float y) {
                localToStageCoordinates(tmpvector.set(x, y));
                if (vertices.length > 0 && isDragging && currentMode == EditMode.Create) {
                    nextPoint.set(tmpvector);
                    lastLineColor = goodColor;
                    if (checkForIntersection(vertices[vertices.length - 1], nextPoint)) {
                        lastLineColor = badColor;
                    }
                }
                nearestPoint.unSet();
                for (int i = 0; i < vertices.length; i++) {
                    if (vertices.length > 1 && tmpvector.x >= (vertices[i].x - POINT_WIDTH / 2) && tmpvector.x <= (vertices[i].x + POINT_WIDTH / 2)
                            && tmpvector.y >= (vertices[i].y - POINT_WIDTH / 2) && tmpvector.y <= (vertices[i].y + POINT_WIDTH / 2)) {
                        nearestPoint.set(vertices[i], i);
                        //System.out.println("PTUK SET " +i);
                        //System.out.println(tmpvector.x+">="+(vertices[i].x-POINT_WIDTH/2)+" && " +tmpvector.x+"<="+(vertices[i].x+POINT_WIDTH/2)+" && "+tmpvector.y+">="+(vertices[i].y-POINT_WIDTH/2) +" && "+ tmpvector.y+"<="+(vertices[i].y+POINT_WIDTH/2));
                        break;
                    }
                }

                if (currentMode == EditMode.Edit && !nearestPoint.isSet) {
                    lineIndex = -1;
                    for (int i = 1; i < vertices.length; i++) {
                        if (Intersector.intersectSegmentCircle(vertices[i - 1], vertices[i], tmpvector, CIRCLE_RADIUS)) {
                            lineIndex = i;
                            break;
                        }

                    }
                    if (vertices.length > 0 && Intersector.intersectSegmentCircle(vertices[vertices.length - 1], vertices[0], tmpvector, CIRCLE_RADIUS)) {
                        lineIndex = 0;
                    }
                }


                return super.mouseMoved(event, x, y);
            }

            @Override
            public boolean scrolled(InputEvent event, float x, float y, int amount) {
                if (currentMode == EditMode.Test) {
                    return false;
                }
                float oldzoomfactor = zoomFactor;
                zoomFactor += amount / 10f;
                if (zoomFactor <= 0.2f) {
                    zoomFactor = 0.2f;
                }

                currentActor.setScale(zoomFactor);
                currentActor.setX((getWidth() - currentActor.getWidth() * zoomFactor) / 2);
                currentActor.setY((getHeight() - currentActor.getHeight() * zoomFactor) / 2);

                float mul = zoomFactor / oldzoomfactor;
                Vector2 diff = new Vector2(getWidth() / 2, getHeight() / 2);
                localToStageCoordinates(diff);

                for (int i = 0; i < verticesList.size(); i++) {
                    verticesList.get(i).sub(diff);
                    verticesList.get(i).scl(mul);
                    verticesList.get(i).add(diff);
                }
                if (currentMode == EditMode.Edit) {
                    finish();
                }
                return super.scrolled(event, x, y, amount);
            }

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                System.out.println("DELETE DELE TE DELETE");
                if (keycode == 67 && currentMode == EditMode.Edit && selectedPoint.isSet) {

                    verticesList.remove(selectedPoint.index);
                    selectedPoint.unSet();
                    finish();
                }
                return super.keyDown(event, keycode);
            }

            private boolean checkForIntersection(Vector2 v1, Vector2 v2) {
                if (vertices.length < 3) {
                    return false;
                }

                Vector2 intersect = new Vector2();
                for (int i = 1; i < vertices.length - 1; i++) {
                    if (Intersector.intersectSegments(v1, v2, vertices[i - 1], vertices[i], intersect)) {
                        return true;
                    }
                }
                return false;
            }

        });
    }

    private void finish() {

        vertices = verticesList.toArray(new Vector2[0]);

        if (PolygonUtils.isPolygonCCW(vertices)) {
            Collections.reverse(verticesList);
            vertices = verticesList.toArray(new Vector2[0]);
        }

        minPolies = Clipper.polygonize(polygonizer, vertices);
        currentMode = EditMode.Edit;
    }

    public void moveRenderer(float diffX, float diffY) {
        for (int i = 0; i < verticesList.size(); i++) {
            verticesList.get(i).sub(diffX, diffY);
        }
        if (currentMode == EditMode.Edit) {
            finish();
        } else {
            vertices = verticesList.toArray(new Vector2[0]);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        //super.draw(batch, parentAlpha);

        if (isTransform()) {
            applyTransform(batch, computeTransform());
        }
        Rectangle calculatedScissorBounds = Pools.obtain(Rectangle.class);
        getStage().calculateScissors(new Rectangle(0, 0, getWidth(), getHeight()), calculatedScissorBounds);
        // Enable scissors.
        if (ScissorStack.pushScissors(calculatedScissorBounds)) {
            drawChildren(batch, parentAlpha);
            ScissorStack.popScissors();
            if (isTransform()) resetTransform(batch);
        }

        batch.end();

        Gdx.gl.glLineWidth(2);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        //

        shapeRenderer.setProjectionMatrix(stage.getCamera().combined);
        Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
        Gdx.gl.glScissor((int) calculatedScissorBounds.x, (int) calculatedScissorBounds.y, (int) getWidth(), (int) getHeight());
        Pools.free(calculatedScissorBounds);
        if (currentMode == EditMode.Create) {
            drawOutlines();
            drawPoints();
            drawNextLine();
        }
        if (currentMode == EditMode.Edit) {
            drawPolygon();
            drawOutlines();
            drawPoints();

        }
        Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
        Gdx.gl.glDisable(GL20.GL_BLEND);
        if (currentMode == EditMode.Test) {
            box2dRenderer.render(physicsEditorWorld, stage.getCamera().combined.scl(1 / PhysicsBodyLoader.SCALE));
        }
        batch.begin();
    }

    private void drawNextLine() {
        if (vertices.length > 0) {
            shapeRenderer.begin(ShapeType.Line);
            shapeRenderer.setColor(lastLineColor);
            shapeRenderer.line(vertices[vertices.length - 1], nextPoint);
            shapeRenderer.end();
        }
    }

    public void drawOutlines() {
        if (vertices.length > 0) {
            shapeRenderer.begin(ShapeType.Line);
            shapeRenderer.setColor(color);
            for (int i = 1; i < vertices.length; i++) {
                if (lineIndex == i) {
                    shapeRenderer.setColor(selectedLineColor);
                }
                shapeRenderer.line(vertices[i], vertices[i - 1]);
                if (lineIndex == i) {
                    shapeRenderer.setColor(color);
                }
            }
            if (currentMode == EditMode.Edit) {
                if (lineIndex == 0) {
                    shapeRenderer.setColor(selectedLineColor);
                }
                shapeRenderer.line(vertices[vertices.length - 1], vertices[0]);
            }
            shapeRenderer.end();
        }

    }

    public void drawPolygon() {
        if (minPolies == null) {
            return;
        }
        shapeRenderer.begin(ShapeType.Line);
        shapeRenderer.setColor(goodColor);
        for (Vector2[] poly : minPolies) {
            for (int i = 1; i < poly.length; i++) {
                shapeRenderer.line(poly[i - 1], poly[i]);
            }
            if (poly.length > 0)
                shapeRenderer.line(poly[poly.length - 1].x, poly[poly.length - 1].y, poly[0].x, poly[0].y);
        }
        shapeRenderer.end();
    }

    public void drawPoints() {

        shapeRenderer.begin(ShapeType.Line);
        shapeRenderer.setColor(0, 0, 0.7f, 1);
        for (int i = 0; i < vertices.length; i++) {
            if (nearestPoint.isSet && nearestPoint.index == i || selectedPoint.isSet && selectedPoint.index == i) {
                shapeRenderer.end();
                shapeRenderer.begin(ShapeType.Filled);
                shapeRenderer.rect(vertices[i].x - POINT_WIDTH / 2, vertices[i].y - POINT_WIDTH / 2, POINT_WIDTH, POINT_WIDTH);
                shapeRenderer.end();
                shapeRenderer.begin(ShapeType.Line);
            } else {
                shapeRenderer.rect(vertices[i].x - POINT_WIDTH / 2, vertices[i].y - POINT_WIDTH / 2, POINT_WIDTH, POINT_WIDTH);
            }
        }
        shapeRenderer.end();
    }

    public void clearMesh() {
        if (assetName != null && !assetName.isEmpty()) {
            ProjectInfoVO projectInfo = projectManager.getCurrentProjectInfoVO();
            if (projectInfo.assetMeshMap.containsKey(assetName)) {
                projectInfo.assetMeshMap.remove(assetName);
            }
        } else if (currentItem != null) {
            if (currentItem != null) currentItem.getDataVO().meshId = "-1";
            currentItem.getDataVO().physicsBodyData = null;
        }
        minPolies = null;
        vertices = new Vector2[0];
        verticesList.clear();
        currentMode = EditMode.Create;
    }

    public void duplicateMesh() {
        // start a fresh copy;
        if (currentItem != null) {
            ProjectInfoVO projectInfo = projectManager.getCurrentProjectInfoVO();
            currentItem.getDataVO().meshId = projectInfo.cloneMesh(currentItem.getDataVO().meshId);
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (currentMode != EditMode.Test) {
            return;
        }
        // physics is enabled
        while (timeAcc < delta) {
            timeAcc += 1f / 60;
            physicsEditorWorld.step(1f / 60, 10, 10);
            for (Body body : testBodiesToDestroy) {
                physicsEditorWorld.destroyBody(body);
            }
            testBodiesToDestroy.clear();
        }
        timeAcc -= delta;
    }

    public enum EditMode {
        Edit, Create, Test
    }

    public class selectedPoint {

        public float x;
        public float y;
        public int index;
        public boolean isSet = false;

        public selectedPoint(float x, float y, int index) {
            this.x = x;
            this.y = y;
            this.index = index;
        }

        public selectedPoint(Vector2 vec, int index) {
            this(vec.x, vec.y, index);
        }

        public void set(float x, float y, int index) {
            this.x = x;
            this.y = y;
            this.index = index;
            isSet = true;
        }

        public void set(Vector2 vec, int index) {
            set(vec.x, vec.y, index);
        }

        public void unSet() {
            isSet = false;
        }
    }

}
