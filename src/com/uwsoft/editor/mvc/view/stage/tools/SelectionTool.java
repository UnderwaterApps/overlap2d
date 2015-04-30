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

package com.uwsoft.editor.mvc.view.stage.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.uwsoft.editor.gdx.sandbox.Sandbox;

/**
 * Created by azakhary on 4/30/2015.
 */
public class SelectionTool implements Tool {

    public static final String NAME = "SELECTION_TOOL";

    private Sandbox sandbox;

    public SelectionTool() {

    }

    @Override
    public void stageMouseDown(float x, float y) {
        sandbox = Sandbox.getInstance();

        boolean setOpacity = false;

        //TODO: Anyone can explain what was the purpose of this?
        if (!Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            setOpacity = true;
        }

        // preparing selection tool rectangle to follow mouse
        sandbox.prepareSelectionRectangle(x, y, setOpacity);
    }

    @Override
    public void stageMouseUp(float x, float y) {
        sandbox = Sandbox.getInstance();
        // selection is complete, this will check for what get caught in selection rect, and select 'em
        sandbox.selectionComplete();
    }

    @Override
    public void stageMouseDragged(float x, float y) {
        sandbox = Sandbox.getInstance();

        sandbox.isUsingSelectionTool = true;
        sandbox.getSandboxStage().selectionRec.setWidth(x - sandbox.getSandboxStage().selectionRec.getX());
        sandbox.getSandboxStage().selectionRec.setHeight(y - sandbox.getSandboxStage().selectionRec.getY());
    }

    @Override
    public void itemMouseDown(float x, float y) {
        sandbox = Sandbox.getInstance();

    }

    @Override
    public void itemMouseUp(float x, float y) {
        sandbox = Sandbox.getInstance();
    }

    @Override
    public void itemMouseDragged(float x, float y) {
        sandbox = Sandbox.getInstance();
    }
}
