package com.uwsoft.editor.mvc.view.stage.tools;

import com.uwsoft.editor.gdx.actors.SelectionRectangle;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.renderer.actor.IBaseItem;

import java.util.HashMap;

/**
 * Created by CyberJoe on 5/2/2015.
 */
public abstract class SimpleTool implements Tool {

    @Override
    public void initTool() {
        Sandbox sandbox = Sandbox.getInstance();
        HashMap<IBaseItem, SelectionRectangle> currSelection = sandbox.getSelector().getCurrentSelection();
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
    public boolean itemMouseDown(IBaseItem item, float x, float y) {
        return false;
    }

    @Override
    public void itemMouseUp(IBaseItem item, float x, float y) {

    }

    @Override
    public void itemMouseDragged(IBaseItem item, float x, float y) {

    }

    @Override
    public void itemMouseDoubleClick(IBaseItem item, float x, float y) {

    }
}
