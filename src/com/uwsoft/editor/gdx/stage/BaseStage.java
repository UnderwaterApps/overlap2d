package com.uwsoft.editor.gdx.stage;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.DataManager;
import com.uwsoft.editor.data.manager.TextureManager;
import com.uwsoft.editor.renderer.Overlap2DStage;

public class BaseStage extends Overlap2DStage {

    private final Overlap2DFacade facade;
    public DataManager dataManager;
    public TextureManager textureManager;


    public BaseStage() {
        super(new ScreenViewport());
        facade = Overlap2DFacade.getInstance();
        dataManager = facade.retrieveProxy(DataManager.NAME);
        textureManager = dataManager.textureManager;
        initLightsConfiguration();


    }

    public void addActor(Actor actor) {
        super.addActor(actor);
    }

    public void draw() {
        super.draw();

    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public void setCursor(int cursor) {
        //UIController.instance.sendNotification(NameConstants.SET_CURSOR, cursor);
    }

    public void disableAmbience(boolean disable) {
        if (disable) {
            essentials.rayHandler.setAmbientLight(0.5f, 0.5f, 0.5f, 1f);
        } else {
            setAmbienceInfo(sceneLoader.getSceneVO());
        }
    }

    public void resize(int width, int height) {
        getViewport().update(width, height, true);
    }
}
