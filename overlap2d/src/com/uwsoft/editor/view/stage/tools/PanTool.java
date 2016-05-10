package com.uwsoft.editor.view.stage.tools;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.uwsoft.editor.proxy.ProjectManager;
import com.uwsoft.editor.proxy.ResourceManager;
import com.uwsoft.editor.view.stage.Sandbox;
import com.uwsoft.editor.Overlap2DFacade;
import com.vo.SceneConfigVO;

/**
 * Created by CyberJoe on 5/1/2015.
 */
public class PanTool extends SimpleTool {
    private static final String EVENT_PREFIX = "com.uwsoft.editor.view.stage.tools.PanTool";
    public static final String SCENE_PANNED = EVENT_PREFIX + ".SCENE_PANNED";

    public static final String NAME = "PAN_TOOL";

    private Vector2 lastCoordinates;

    @Override
    public String getName() {
        return NAME;
    }


    @Override
    public void initTool() {

    }

    @Override
    public boolean stageMouseDown(float x, float y) {
        lastCoordinates = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        return false;
    }

    @Override
    public void stageMouseUp(float x, float y) {

    }

    @Override
    public void stageMouseDragged(float x, float y) {
        doPanning(x, y);
    }

    @Override
    public void stageMouseDoubleClick(float x, float y) {

    }

    @Override
    public boolean itemMouseDown(Entity entity, float x, float y) {
        lastCoordinates = new Vector2(Gdx.input.getX(), Gdx.input.getY());

        return true;
    }

    @Override
    public void itemMouseUp(Entity entity, float x, float y) {

    }

    @Override
    public void itemMouseDragged(Entity entity, float x, float y) {
        doPanning(x, y);
    }

    @Override
    public void itemMouseDoubleClick(Entity entity, float x, float y) {

    }

    private void doPanning(float x, float y) {
        Sandbox sandbox = Sandbox.getInstance();

        ResourceManager resourceManager = Overlap2DFacade.getInstance().retrieveProxy(ResourceManager.NAME);
        OrthographicCamera camera = sandbox.getCamera();

        float currX = camera.position.x + (lastCoordinates.x - Gdx.input.getX()) * camera.zoom / resourceManager.getProjectVO().pixelToWorld;
        float currY = camera.position.y + (Gdx.input.getY() - lastCoordinates.y) * camera.zoom / resourceManager.getProjectVO().pixelToWorld;

        sandbox.getCamera().position.set(currX, currY, 0);

        lastCoordinates = new Vector2(Gdx.input.getX(), Gdx.input.getY());

        Overlap2DFacade.getInstance().sendNotification(SCENE_PANNED);

        // Save the current position
        // TODO: (this has to move to some kind of mediator that listens to scene panned event)
        ProjectManager projectManager = Overlap2DFacade.getInstance().retrieveProxy(ProjectManager.NAME);
        SceneConfigVO sceneConfigVO = projectManager.getCurrentSceneConfigVO();
        sceneConfigVO.cameraPosition[0] = sandbox.getCamera().position.x;
        sceneConfigVO.cameraPosition[1] = sandbox.getCamera().position.y;
    }
}
