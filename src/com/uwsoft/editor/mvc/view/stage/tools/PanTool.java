package com.uwsoft.editor.mvc.view.stage.tools;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.uwsoft.editor.gdx.sandbox.Sandbox;

/**
 * Created by CyberJoe on 5/1/2015.
 */
public class PanTool implements Tool {

    public static final String NAME = "PAN_TOOL";

    private Vector2 lastCoordinates;

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
        Sandbox sandbox = Sandbox.getInstance();
        OrthographicCamera camera = (OrthographicCamera) (sandbox.getSandboxStage().getCamera());

        float currX = camera.position.x + (lastCoordinates.x - Gdx.input.getX()) * camera.zoom;
        float currY = camera.position.y + (Gdx.input.getY() - lastCoordinates.y) * camera.zoom;

        sandbox.getSandboxStage().getCamera().position.set(currX, currY, 0);

        lastCoordinates = new Vector2(Gdx.input.getX(), Gdx.input.getY());
    }

    @Override
    public void stageMouseDoubleClick(float x, float y) {

    }

    @Override
    public boolean itemMouseDown(Entity entity, float x, float y) {
        return false;
    }

    @Override
    public void itemMouseUp(Entity entity, float x, float y) {

    }

    @Override
    public void itemMouseDragged(Entity entity, float x, float y) {

    }

    @Override
    public void itemMouseDoubleClick(Entity entity, float x, float y) {

    }
}
