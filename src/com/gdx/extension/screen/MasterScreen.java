/** Copyright 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gdx.extension.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.viewport.StretchViewport;

/**
 * The master screen is singleton and
 * 
 * @author Kyu
 * 
 */
public abstract class MasterScreen implements Screen
{

    /**
     * Style
     */
    protected static Skin skin;

    /**
     * Propagate inputs
     */
    protected static InputMultiplexer inputProcessor;

    /**
     * {@link Batch} used for all screens (improve performances)
     */
    protected static Batch mainBatch;

    /**
     * Catch events and draw actors
     */
    protected static Stage stage;

    /**
     * Overlap all screen layouts
     */
    protected static Stack stageStack;

    /**
     * Initialize MasterScreen
     */
    static {
	inputProcessor = new InputMultiplexer();
	Gdx.input.setInputProcessor(inputProcessor);

	stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
	inputProcessor.addProcessor(stage);
	stageStack = new Stack();
	stage.addActor(stageStack);
	stageStack.setFillParent(true);
    }

    /**
     * Called when the screen is added to the screen manager.
     */
    protected abstract void create();

    /**
     * Render and act the stage.
     * 
     * @param delta time from last frame
     */
    protected final static void masterRender(float delta) {
	synchronized (stageStack) {
	    stage.act(delta);
	    stage.draw();
	}
    }

    @Override
    public abstract void render(float delta);

    /**
     * Resize the stage viewport.
     * 
     * @param width the new width
     * @param height the new height
     */
    protected final static void masterResize(int width, int height) {
	stage.getViewport().update(width, height, true);
    }

    /**
     * Called when the screen is resized.
     */
    @Override
    public abstract void resize(int width, int height);

    /**
     * Show the screen.
     */
    @Override
    public abstract void show();

    /**
     * Hide the screen.
     */
    @Override
    public abstract void hide();

    /**
     * Called when the screen is paused (on android).
     */
    @Override
    public abstract void pause();

    /**
     * Called when the screen is resumed (on android).
     */
    @Override
    public abstract void resume();

    /**
     * Called when the screen need to be disposed.
     */
    @Override
    public abstract void dispose();

    /**
     * Free resources of MasterScreen.
     */
    protected final static void masterDispose() {
	skin.dispose();
	stage.dispose();
	mainBatch.dispose();
    }

    /**
     * @return the skin that is used by all screens
     */
    public static Skin getSkin() {
	return skin;
    }

    /**
     * @return the {@link InputMultiplexer}
     */
    public static InputMultiplexer getInputProcessor() {
	return inputProcessor;
    }

    /**
     * @return the {@link Stage stage} that is used by all screens
     */
    public static Stage getStage() {
	return stage;
    }

}
