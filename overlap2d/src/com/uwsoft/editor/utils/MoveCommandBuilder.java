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

package com.uwsoft.editor.utils;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.commons.MsgAPI;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

/**
 * Created by azakhary on 6/5/2015.
 */
public class MoveCommandBuilder {

    Array<Object[]> payload;

    public MoveCommandBuilder() {
        payload =  new Array<>();
    }

    public void setX(Entity entity, float x) {
        TransformComponent transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
        setXY(entity, x, transformComponent.y);
    }

    public void setY(Entity entity, float y) {
        TransformComponent transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
        setXY(entity, transformComponent.x, y);
    }

    public void setXY(Entity entity, float x, float y) {
        Object[] data = new Object[2];
        data[0] = entity;
        data[1] = new Vector2(x, y);
        payload.add(data);
    }

    public void clear() {
        payload = new Array<>();
    }

    public void execute() {
        Overlap2DFacade.getInstance().sendNotification(MsgAPI.ACTION_ITEMS_MOVE_TO, payload);
    }

}
