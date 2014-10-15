package com.uwsoft.editor.gdx.ui;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.uwsoft.editor.gdx.actors.GridView;
import com.uwsoft.editor.gdx.actors.ResolutionBounds;
import com.uwsoft.editor.gdx.stage.BaseStage;

public class SandboxUI extends Group {

    public SandboxUI(BaseStage s) {
        GridView gridView = new GridView(s);
        addActor(gridView);
        ResolutionBounds resolutionBounds = new ResolutionBounds(s);
        addActor(resolutionBounds);
    }
}
