package com.uwsoft.editor.renderer.scene2d;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Created by CyberJoe on 8/1/2015.
 */
public class ButtonClickListener extends ClickListener {

    @Override
    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
        CompositeActor compositeActor = (CompositeActor) event.getListenerActor();
        compositeActor.setLayerVisibility("normal", false);
        compositeActor.setLayerVisibility("pressed", true);
        return true;
    }

    @Override
    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
        CompositeActor compositeActor = (CompositeActor) event.getListenerActor();
        compositeActor.setLayerVisibility("normal", true);
        compositeActor.setLayerVisibility("pressed", false);
    }

}
