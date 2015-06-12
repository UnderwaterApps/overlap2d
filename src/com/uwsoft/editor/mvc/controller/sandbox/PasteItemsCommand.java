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

package com.uwsoft.editor.mvc.controller.sandbox;

import java.util.Collection;
import java.util.HashMap;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.factory.ItemFactory;
import com.uwsoft.editor.mvc.view.MidUIMediator;
import com.uwsoft.editor.renderer.components.NodeComponent;
import com.uwsoft.editor.renderer.components.ParentNodeComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.utils.runtime.ComponentRetriever;
import com.uwsoft.editor.utils.runtime.EntityUtils;

/**
 * Created by azakhary on 4/28/2015.
 */
public class PasteItemsCommand extends RevertableCommand {

    private Array<Integer> pastedEntityIds = new Array<>();

    @Override
    public void doAction() {
        Object[] payload = (Object[]) Sandbox.getInstance().retrieveFromClipboard();
        Vector2 cameraPrevPosition = (Vector2) payload[0];
        Vector2 cameraCurrPosition = new Vector2(Sandbox.getInstance().getCamera().position.x,Sandbox.getInstance().getCamera().position.y);

        Vector2 diff = cameraCurrPosition.sub(cameraPrevPosition);

        HashMap<Integer, Collection<Component>> backup = (HashMap<Integer, Collection<Component>>) payload[1];
        for (Collection<Component> components : backup.values()) {
            Entity entity = new Entity();
            for(Component component: components) {
                entity.add(component);
            }
            sandbox.getEngine().addEntity(entity);
            int uniquId = sandbox.getSceneControl().sceneLoader.entityFactory.postProcessEntity(entity);
            Entity parentEntity = entity.getComponent(ParentNodeComponent.class).parentEntity;
            NodeComponent nodeComponent = parentEntity.getComponent(NodeComponent.class);

            TransformComponent transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
            transformComponent.x += diff.x;
            transformComponent.y += diff.y;

            nodeComponent.addChild(entity);
            Overlap2DFacade.getInstance().sendNotification(ItemFactory.NEW_ITEM_ADDED, entity);

            pastedEntityIds.add(uniquId);
        }
    }

    @Override
    public void undoAction() {
        MidUIMediator midUIMediator = Overlap2DFacade.getInstance().retrieveMediator(MidUIMediator.NAME);
        for (Integer entityId : pastedEntityIds) {
            Entity entity = EntityUtils.getByUniqueId(entityId);
            midUIMediator.removeFollower(entity);
            sandbox.getEngine().removeEntity(entity);
        }
    }
}
