package com.uwsoft.editor.gdx.screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.uwsoft.editor.data.manager.DataManager;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.gdx.stage.SandboxStage;
import com.uwsoft.editor.gdx.stage.UIStage;

import java.io.File;

public class Overlap2DScreen implements Screen, InputProcessor {
    private static final String TAG = Overlap2DScreen.class.getCanonicalName();
    private final InputMultiplexer multiplexer;
    public SandboxStage sandboxStage;
    public UIStage uiStage;
    private boolean paused = false;

    private Sandbox sandbox;

    public Overlap2DScreen() {

        sandbox = Sandbox.getInstance();
        sandboxStage = sandbox.getSandboxStage();
        uiStage = sandbox.getUIStage();
        sandboxStage.sandbox = sandbox;

        // check for demo project
        File demoDir = new File(DataManager.getInstance().getRootPath() + File.separator + "examples" + File.separator + "OverlapDemo");
        if (demoDir.isDirectory() && demoDir.exists()) {
            DataManager.getInstance().openProjectFromPath(demoDir.getAbsolutePath() + File.separator + "project.pit");
            sandbox.loadCurrentProject();
            uiStage.loadCurrentProject();
            sandboxStage.getCamera().position.set(400, 200, 0);
        }
        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(this);
        multiplexer.addProcessor(uiStage);
        multiplexer.addProcessor(sandboxStage);
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float deltaTime) {
        if (paused) {
            return;
        }
        GL20 gl = Gdx.gl;
        gl.glClearColor(0.129f, 0.129f, 0.129f, 1.0f);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        sandboxStage.act(deltaTime);
        sandboxStage.draw();

        uiStage.act(deltaTime);
        uiStage.draw();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void resize(int width, int height) {
        uiStage.resize(width, height);//getViewport().update(width, height, true);
        sandboxStage.resize(width, height);//getViewport().update(width, height, true);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (Gdx.input.isKeyPressed(Input.Keys.SYM)) {
            switch (keycode) {
                case Input.Keys.X:
                    sandbox.getUac().cutAction();
                    break;
                case Input.Keys.C:
                    sandbox.getUac().copyAction();
                    break;
                case Input.Keys.V:
                    sandbox.getUac().pasteAction(0, 0, false);
                    break;
                case Input.Keys.Z:
                    sandbox.getUac().undo();
                    break;
                case Input.Keys.Y:
                    sandbox.getUac().redo();
                    break;
            }
        }
        Gdx.app.log(TAG, "keyDown : " + keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
