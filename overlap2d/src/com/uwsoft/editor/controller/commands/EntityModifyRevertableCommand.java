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

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.proxy.ProjectManager;
import com.uwsoft.editor.renderer.factory.EntityFactory;
import com.uwsoft.editor.renderer.components.MainItemComponent;
import com.uwsoft.editor.renderer.components.NodeComponent;
import com.uwsoft.editor.renderer.data.CompositeItemVO;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.utils.runtime.EntityUtils;

import java.util.HashMap;

/**
 * Created by azakhary on 6/15/2015.
 */
public abstract class EntityModifyRevertableCommand extends RevertableCommand {

    @Override
    public void callDoAction() {
        super.callDoAction();
        postChange();
    }
    @Override
    public void callUndoAction() {
        super.callUndoAction();
        postChange();
    }

    protected void postChange() {
        Integer parentId = EntityUtils.getEntityId(sandbox.getCurrentViewingEntity());
        Entity entity = EntityUtils.getByUniqueId(parentId);

        // Update item library data if it was in library
        MainItemComponent mainItemComponent = ComponentRetriever.get(entity, MainItemComponent.class);
        String link = mainItemComponent.libraryLink;

        if(link != null && link.length() > 0) {
            ProjectManager projectManager = Overlap2DFacade.getInstance().retrieveProxy(ProjectManager.NAME);
            HashMap<String, CompositeItemVO> libraryItems = projectManager.currentProjectInfoVO.libraryItems;
            if (libraryItems.containsKey(mainItemComponent.libraryLink)) {
                CompositeItemVO itemVO = new CompositeItemVO();
                itemVO.loadFromEntity(entity);
                itemVO.cleanIds();
                libraryItems.put(mainItemComponent.libraryLink, itemVO);
            }

            Array<Entity> linkedEntities = EntityUtils.getByLibraryLink(link);
            for (Entity dependable : linkedEntities) {
                if(dependable == entity) continue;
                NodeComponent nodeComponent = ComponentRetriever.get(dependable, NodeComponent.class);
                for(Entity child: nodeComponent.children) {
                    sandbox.getEngine().removeEntity(child);
                }
                nodeComponent.children.clear();

                Engine engine = sandbox.getSceneControl().sceneLoader.engine;
                EntityFactory factory = sandbox.getSceneControl().sceneLoader.entityFactory;
                factory.initAllChildren(engine, dependable, libraryItems.get(link).composite);
            }
        }
    }
}
