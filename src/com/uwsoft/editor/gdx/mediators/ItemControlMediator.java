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

package com.uwsoft.editor.gdx.mediators;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.uwsoft.editor.data.manager.DataManager;
import com.uwsoft.editor.gdx.actors.SelectionRectangle;
import com.uwsoft.editor.renderer.actor.*;
import com.uwsoft.editor.renderer.data.*;

import java.io.File;
import java.util.HashMap;

/**
 * Created by CyberJoe on 3/18/2015.
 */
public class ItemControlMediator {

    private SceneControlMediator sceneControl;

    public ItemControlMediator(SceneControlMediator sceneControl) {
        this.sceneControl = sceneControl;
    }


    public void itemZIndexChange( HashMap<IBaseItem, SelectionRectangle> currentSelection, boolean isUp) {
        for (SelectionRectangle value : currentSelection.values()) {
            sceneControl.getCurrentScene().updateDataVO();

            int ammount = 1;
            if (!isUp) ammount = -1;

            int setting = value.getHostAsActor().getZIndex() + ammount;
            if (setting < 0) setting = 0;
            Group parent = value.getHostAsActor().getParent();
            parent.swapActor(value.getHostAsActor().getZIndex(), setting);

            sceneControl.getCurrentScene().updateDataVO();
        }
    }

    public void moveItemBy(Actor actor, float x, float y) {
        actor.setX(actor.getX() + x);
        actor.setY(actor.getY() + y);
    }

    public void removeItem(Actor actor) {
        IBaseItem item = (IBaseItem) actor;
        actor.remove();
        sceneControl.getCurrentScene().removeItem(item);
        item.dispose();
    }

}
