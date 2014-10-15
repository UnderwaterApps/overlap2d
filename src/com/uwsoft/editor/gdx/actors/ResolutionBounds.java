package com.uwsoft.editor.gdx.actors;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.uwsoft.editor.data.manager.TextureManager;
import com.uwsoft.editor.gdx.actors.basic.PixelRect;
import com.uwsoft.editor.gdx.stage.BaseStage;
import com.uwsoft.editor.renderer.data.ResolutionEntryVO;

/**
 * Created by sargis on 7/10/14.
 */
public class ResolutionBounds extends Group {
    private final TextureManager textureManager;
    private float width;
    private float height;

    public ResolutionBounds(BaseStage baseStage) {
        textureManager = baseStage.textureManager;
        detectDimensions(baseStage);
        crateBoundsRectangle();
        crateResolutionIndicator();
    }

    private void detectDimensions(BaseStage baseStage) {
        ResolutionEntryVO resolutionEntryVO = baseStage.dataManager.getCurrentProjectInfoVO().getResolution(baseStage.dataManager.curResolution);
        if (resolutionEntryVO == null) {
            resolutionEntryVO = baseStage.dataManager.getCurrentProjectInfoVO().originalResolution;
        }
        width = resolutionEntryVO.width;
        height = resolutionEntryVO.height;
    }

    private void crateResolutionIndicator() {
        Label label = new Label((int) width + " x " + (int) height, textureManager.editorSkin);
        label.setX(width - label.getWidth());
        label.setY(height);
        addActor(label);
    }

    private void crateBoundsRectangle() {
        PixelRect resolutionBounds = new PixelRect(textureManager, width, height);
        addActor(resolutionBounds);
    }
}
