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

package com.uwsoft.editor.gdx.sandbox;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.uwsoft.editor.controlles.flow.FlowActionEnum;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.ProjectManager;
import com.uwsoft.editor.renderer.data.CompositeItemVO;
import com.uwsoft.editor.renderer.data.LayerItemVO;
import com.uwsoft.editor.renderer.data.LightVO;

/**
 * Provides interface for user actions/intentions like creating panels
 * copying and pasting panels and doing redo/undo.
 */
public class UserActionController {

    private final Overlap2DFacade facade;
    private final ProjectManager projectManager;
    private Sandbox sandbox;

    public UserActionController(Sandbox sandbox) {
        this.sandbox = sandbox;
        facade = Overlap2DFacade.getInstance();
        projectManager = facade.retrieveProxy(ProjectManager.NAME);
    }

    public Boolean createImage(String regionName, Vector2 vector2) {
        LayerItemVO layer = sandbox.getSelectedLayer();
        if (layer == null) return false;
        vector2.x = MathUtils.floor(vector2.x / sandbox.getGridSize()) * sandbox.getGridSize();
        vector2.y = MathUtils.floor(vector2.y / sandbox.getGridSize()) * sandbox.getGridSize();
        sandbox.getItemFactory().createImageItem(layer, regionName, vector2.x, vector2.y);
        return true;
    }

    public Boolean create9Patch(String name, Vector2 vector2) {
        LayerItemVO layer = sandbox.getSelectedLayer();
        if (layer == null) return false;
        sandbox.getItemFactory().create9patchItem(layer, name, vector2.x, vector2.y);
        return true;
    }

    public Boolean createParticleItem(String name, Vector2 vector2) {
        LayerItemVO layer = sandbox.getSelectedLayer();
        if (layer == null) return false;

        sandbox.getItemFactory().createParticleItem(layer, name, vector2.x, vector2.y);
        return true;
    }

    public Boolean createSpriteAnimation(String name, Vector2 vector2) {
        LayerItemVO layer = sandbox.getSelectedLayer();
        if (layer == null) return false;
        sandbox.getItemFactory().createSpriteAnimation(layer, name, vector2.x, vector2.y);
        return true;
    }

    public Boolean createSpriterAnimation(String name, Vector2 vector2) {
        LayerItemVO layer = sandbox.getSelectedLayer();
        if (layer == null) return false;
        sandbox.getItemFactory().createSpriterAnimation(layer, name, vector2.x, vector2.y);
        return true;
    }

    public Boolean createSpineAnimation(String name, Vector2 vector2) {
        LayerItemVO layer = sandbox.getSelectedLayer();
        if (layer == null) return false;

        sandbox.getItemFactory().createSpineAnimation(layer, name, vector2.x, vector2.y);
        return true;
    }

    public void createLight(LightVO vo, float x, float y) {
        LayerItemVO layer = sandbox.getSelectedLayer();
        if (layer == null) return;

        sandbox.getItemFactory().createLight(layer, vo, x, y);
    }


    public Boolean createItemFromLibrary(String name, Vector2 vector2) {
        LayerItemVO layer = sandbox.getSelectedLayer();
        if (layer == null) return false;

        sandbox.getItemFactory().createItemFromLibrary(layer, name, vector2.x, vector2.y);
        return true;
    }

    /**
     * @param name
     * @param x
     * @param y
     * @depricated
     */
    public void createComponent(final String name, final float x, final float y) {
        /*
        final LayerItemVO layer = sandbox.getSelectedLayer();
        if (layer == null) return;

        // creating component requires skin
        if (!sandbox.isComponentSkinAvailable() && !name.equals("Label")) {
            DialogUtils.showConfirmDialog(sandbox.getUIStage(),
                    "Warning",
                    "There is no style imported yet. Do you want to add default style instead to make this work?",
                    new String[]{"OK", "Cancel"}, new Integer[]{0, 1},
                    result -> {
                        if (result == 0) {
                            projectManager.copyDefaultStyleIntoProject();
                            sandbox.getItemFactory().createComponent(layer, name, x, y);
                        }

                    });
        } else {
            sandbox.getItemFactory().createComponent(layer, name, x, y);
        }
        */
    }

    /**
     *
     */
    public void undo() {
        FlowActionEnum lastFlowAction = sandbox.flow.getFlowLastAction();
        CompositeItemVO compositeItemVO = sandbox.flow.undo();
        switch (lastFlowAction) {
            case GET_INTO_COMPOSITE:
                sandbox.enterIntoPrevComposite();
                break;
            case GET_OUT_COMPOSITE:
                sandbox.enterIntoComposite(compositeItemVO);
                break;
            default:
                sandbox.reconstructFromSceneVo(compositeItemVO);
                break;
        }
        sandbox.sceneControl.getCurrentScene().updateDataVO();
    }

    public void redo() {
        CompositeItemVO compositeItemVO = sandbox.flow.redo();
        FlowActionEnum lastFlowAction = sandbox.flow.getFlowLastAction();
        switch (lastFlowAction) {
            case GET_INTO_COMPOSITE:
                sandbox.enterIntoComposite(compositeItemVO);
                break;
            case GET_OUT_COMPOSITE:
                sandbox.enterIntoPrevComposite();
                break;
            default:
                sandbox.reconstructFromSceneVo(compositeItemVO);
                break;
        }
        sandbox.sceneControl.getCurrentScene().updateDataVO();
    }
}
