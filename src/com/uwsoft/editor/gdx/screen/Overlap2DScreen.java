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

package com.uwsoft.editor.gdx.screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.uwsoft.editor.data.manager.DataManager;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.gdx.stage.SandboxStage;
import com.uwsoft.editor.gdx.stage.UIStage;
import com.uwsoft.editor.renderer.data.SceneVO;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

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

        // first check for a local configuration
        String demoProjectDir;

        try {
            Properties localConfig = new Properties();
            InputStream fis = new FileInputStream(".." + File.separator + "local" + File.separator + "config.properties");
            localConfig.load(fis);
            fis.close();
            demoProjectDir = localConfig.getProperty("projectDir");

        } catch (Exception e) {
            System.out.println("no local config file found:");
            System.out.println(e.getMessage());
            // use overlap2d demo dir
            demoProjectDir = DataManager.getInstance().getRootPath() + File.separator + "examples" + File.separator + "OverlapDemo";
        }

        System.out.println("demo project dir: " + demoProjectDir);

        File demoDir = new File(demoProjectDir);
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
        if (Gdx.input.isKeyPressed(Input.Keys.SYM) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT)) {
            switch (keycode) {
					 case Input.Keys.N:
						  uiStage.menuMediator.showDialog("createNewProjectDialog");
						  break;
					 case Input.Keys.O:
						  uiStage.menuMediator.showOpenProject();
						  break;
					 case Input.Keys.S:
						  SceneVO vo = sandbox.sceneVoFromItems();
						  DataManager.getInstance().saveCurrentProject(vo);
						  break;
					 case Input.Keys.E:
						  DataManager.getInstance().exportProject();
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
