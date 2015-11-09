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

package com.uwsoft.editor.view;

import java.io.File;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.commons.MsgAPI;
import com.uwsoft.editor.view.ui.widget.actors.basic.SandboxBackUI;
import com.uwsoft.editor.view.stage.Sandbox;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.proxy.ProjectManager;
import com.uwsoft.editor.view.stage.UIStage;
import com.uwsoft.editor.view.stage.input.SandboxInputAdapter;
import com.uwsoft.editor.renderer.data.SceneVO;

public class Overlap2DScreen implements Screen, InputProcessor {
    private static final String TAG = Overlap2DScreen.class.getCanonicalName();
    //public SandboxStage sandboxStage;
    
    public UIStage uiStage;
    
	private Engine engine;
    
    private InputMultiplexer multiplexer;
    private Overlap2DFacade facade;
    private ProjectManager projectManager;
    private boolean paused = false;

    private Sandbox sandbox;
    private SandboxBackUI sandboxBackUI;

    private Batch batch;
    private Color bgColor;
    private Texture bgLogo;
    private Vector2 screenSize;


    private boolean isDrawingBgLogo;

    public Overlap2DScreen() {
        facade = Overlap2DFacade.getInstance();
        bgColor = new Color(0.094f, 0.094f, 0.094f, 1.0f);
        isDrawingBgLogo = true;
        batch = new SpriteBatch();
        bgLogo = new Texture(Gdx.files.internal("style/bglogo.png"));
        screenSize = new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void render(float deltaTime) {
        if (paused) {
            return;
        }
        GL20 gl = Gdx.gl;
        gl.glClearColor(bgColor.r, bgColor.g, bgColor.b, bgColor.a);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(isDrawingBgLogo) {
            batch.begin();
            batch.setColor(1, 1, 1, 0.12f);
            batch.draw(bgLogo, screenSize.x/2 - bgLogo.getWidth()/2, screenSize.y/2 - bgLogo.getHeight()/2);
            batch.end();
        } else {
            if (sandboxBackUI != null) sandboxBackUI.render(deltaTime);
            engine.update(deltaTime);
        }

        uiStage.act(deltaTime);
        uiStage.draw();
    }

    public void disableDrawingBgLogo() {
        if(!isDrawingBgLogo) return;

        this.isDrawingBgLogo = false;
        bgLogo.dispose();
        batch.dispose();
        batch = null;
        bgLogo = null;

    }

    public void setBgColor(Color color) {
        bgColor = color;
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
        sandbox = Sandbox.getInstance();
        uiStage = sandbox.getUIStage();
        //sandboxStage = commands.getSandboxStage();
        
        //sandboxStage.commands = commands;

        projectManager = facade.retrieveProxy(ProjectManager.NAME);
        // check for demo project
        File demoDir = new File(projectManager.getRootPath() + File.separator + "examples" + File.separator + "OverlapDemo");
        //if (demoDir.isDirectory() && demoDir.exists()) {
        // TODO: temp not opening the demo
        if (false) {
            projectManager.openProjectFromPath(demoDir.getAbsolutePath() + File.separator + "project.pit");
            sandbox.loadCurrentProject();
            if(sandbox.getViewport() != null) {
                sandbox.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            }
            facade.sendNotification(ProjectManager.PROJECT_OPENED);
        }
        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(this);
        multiplexer.addProcessor(uiStage);
        multiplexer.addProcessor(new SandboxInputAdapter());
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void hide() {

    }

    @Override
    public void resize(int width, int height) {
        uiStage.resize(width, height);
        if(Sandbox.getInstance().getViewport() != null) {
            Sandbox.getInstance().getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        if (Gdx.input.isKeyPressed(Input.Keys.SYM) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT)) {
            switch (keycode) {
                case Input.Keys.N:
                    //uiStage.menuMediator.showDialog("createNewProjectDialog");
                    break;
                case Input.Keys.O:
                    //uiStage.menuMediator.showOpenProject();
                    break;
                case Input.Keys.S:
                    SceneVO vo = sandbox.sceneVoFromItems();
                    projectManager.saveCurrentProject(vo);
                    break;
                case Input.Keys.E:
                    facade.sendNotification(MsgAPI.ACTION_EXPORT_PROJECT);
                    break;
            }
        }
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

	public void setEngine(Engine engine) {
		this.engine = engine;
	}

    public void setBackUI(SandboxBackUI sandboxBackUI) {
        this.sandboxBackUI = sandboxBackUI;
    }
}
