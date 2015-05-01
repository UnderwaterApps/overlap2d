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

import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.renderer.actor.IBaseItem;

/**
 * Created by azakhary on 4/30/2015.
 */
public class TransformTool implements Tool {

    public static final String NAME = "TRANSFORM_TOOL";

    @Override
    public void initTool() {
        Sandbox sandbox = Sandbox.getInstance();
        sandbox.getSelector().clearSelections();
    }

    @Override
    public boolean stageMouseDown(float x, float y) {
        return false;
    }

    @Override
    public void stageMouseUp(float x, float y) {

    }

    @Override
    public void stageMouseDragged(float x, float y) {

    }

    @Override
    public void stageMouseDoubleClick(float x, float y) {

    }

    @Override
    public boolean itemMouseDown(IBaseItem item, float x, float y) {
        Sandbox sandbox = Sandbox.getInstance();
        if (item.isLockedByLayer()) {
            // this is considered empty space click and thus should release all selections
            sandbox.getSelector().clearSelections();
            return false;
        } else {
            // select this item and remove others from selection
            sandbox.getSelector().setSelection(item, true);
            sandbox.getSelector().getSelectedItemSelectionRectangle().setMode(true);
        }

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
