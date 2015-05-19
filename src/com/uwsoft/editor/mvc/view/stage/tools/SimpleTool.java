package com.uwsoft.editor.mvc.view.stage.tools;

import java.util.HashMap;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.gdx.actors.SelectionRectangle;
import com.uwsoft.editor.gdx.sandbox.Sandbox;

/**
 * Created by CyberJoe on 5/2/2015.
 */
public abstract class SimpleTool implements Tool {

    @Override
    public void initTool() {
        Sandbox sandbox = Sandbox.getInstance();
        HashMap<Entity, SelectionRectangle> currSelection = sandbox.getSelector().getCurrentSelection();
        for (SelectionRectangle value : currSelection.values()) {
            value.setMode(false);
        }
    }


    @Override
    public void stageMouseDragged(float x, float y) {

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
