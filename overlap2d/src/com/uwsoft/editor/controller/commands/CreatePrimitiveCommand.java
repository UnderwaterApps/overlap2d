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

package com.uwsoft.editor.controller.commands;

import com.badlogic.gdx.math.Vector2;
import com.uwsoft.editor.factory.ItemFactory;
import com.uwsoft.editor.renderer.data.ColorPrimitiveVO;
import com.uwsoft.editor.renderer.data.ShapeVO;

/**
 * Created by azakhary on 10/21/2015.
 */
public class CreatePrimitiveCommand extends EntityModifyRevertableCommand {

    @Override
    public void doAction() {
        Vector2 position = new Vector2(0, 0);
        ShapeVO shape = ShapeVO.createRect(100f / sandbox.getPixelPerWU(), 100f / sandbox.getPixelPerWU());

        ItemFactory.get().createPrimitive(position, shape);
    }

    @Override
    public void undoAction() {

    }
}
