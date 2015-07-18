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

package com.uwsoft.editor.view;

import java.util.Set;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.ZIndexComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

/**
 * Created by CyberJoe on 3/18/2015.
 */
public class ItemControlMediator {

    private SceneControlMediator sceneControl;
    
    private TransformComponent transformComponent;
    private ZIndexComponent zIndexComponent;

    public ItemControlMediator(SceneControlMediator sceneControl) {
        this.sceneControl = sceneControl;
    }


    public void itemZIndexChange( Set<Entity> currentSelection, boolean isUp) {
        for (Entity item : currentSelection) {
        	zIndexComponent = ComponentRetriever.get(item, ZIndexComponent.class);

            int ammount = 1;
            if (!isUp) ammount = -1;

            int setting = zIndexComponent.getZIndex() + ammount;
            if (setting < 0) setting = 0;
            zIndexComponent.setZIndex(setting);
        }
    }

    public void moveItemBy(Entity entity, float x, float y) {
    	transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
    	transformComponent.x+=x;
    	transformComponent.y+=y;
    }

    public void removeItem(Entity entity) {
    	//TODO and uncomment
//        actor.remove();
//        sceneControl.getCurrentScene().removeItem(item);
//        item.dispose();
    }

}
