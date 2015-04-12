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

package com.uwsoft.editor.gdx.actors;

import com.badlogic.gdx.graphics.OrthographicCamera;
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

	 private PixelRect resolutionBounds;
	 private Label label;

    public ResolutionBounds(BaseStage baseStage) {
        textureManager = baseStage.textureManager;
        detectDimensions(baseStage);
        crateBoundsRectangle();
        crateResolutionIndicator();
    }

    private void detectDimensions(BaseStage baseStage) {
        ResolutionEntryVO resolutionEntryVO = baseStage.dataManager.getCurrentProjectInfoVO().getResolution(baseStage.dataManager.resolutionManager.curResolution);
        if (resolutionEntryVO == null) {
            resolutionEntryVO = baseStage.dataManager.getCurrentProjectInfoVO().originalResolution;
        }
        width = resolutionEntryVO.width;
        height = resolutionEntryVO.height;
    }

    private void crateResolutionIndicator() {
        label = new Label((int) width + " x " + (int) height, textureManager.editorSkin);
        label.setX(width - label.getWidth());
        label.setY(height);
        addActor(label);
    }

    private void crateBoundsRectangle() {
        resolutionBounds = new PixelRect(width, height);
        addActor(resolutionBounds);
    }

	 @Override
	 public void act(float delta) {
		  super.act(delta);

		  // change size according to zoom
		  OrthographicCamera camera = (OrthographicCamera)getStage().getCamera();
		  resolutionBounds.setThickness(camera.zoom);
	 }
}
