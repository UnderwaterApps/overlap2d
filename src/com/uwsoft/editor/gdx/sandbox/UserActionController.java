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
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.CommandManager;
import com.uwsoft.editor.mvc.proxy.ProjectManager;
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
     *
     */
    public void undo() {
        CommandManager commandManager = facade.retrieveProxy(CommandManager.NAME);
        commandManager.undoCommand();
    }

    public void redo() {
        CommandManager commandManager = facade.retrieveProxy(CommandManager.NAME);
        commandManager.redoCommand();
    }
}
