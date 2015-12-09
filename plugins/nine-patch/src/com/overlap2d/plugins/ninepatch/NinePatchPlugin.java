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

package com.overlap2d.plugins.ninepatch;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.commons.plugins.O2DPluginAdapter;
import com.uwsoft.editor.renderer.components.MainItemComponent;
import com.uwsoft.editor.renderer.factory.EntityFactory;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import net.mountainblade.modular.annotations.Implementation;

import java.util.Set;

/**
 * Created by azakhary on 8/18/2015.
 * Plugin to edit Nine Patch data on imported texture region
 */
@Implementation(authors = "azakhary", version = "0.0.1")
public class NinePatchPlugin extends O2DPluginAdapter {
    public static final String CLASS_NAME = "com.uwsoft.editor.plugins.ninepatch.NinePatchPlugin";

    public static final String EDIT_NINE_PATCH = CLASS_NAME + ".EDIT_NINE_PATCH";
    public static final String CONVERT_TO_NINE_PATCH = CLASS_NAME + ".CONVERT_TO_NINE_PATCH";
    public static final String SCENE_LOADED = "com.uwsoft.editor.proxy.SceneDataManager.SCENE_LOADED";

    private MainPanelMediator performancePanelMediator;

    public Entity currEditingEntity;

    public NinePatchPlugin() {
        performancePanelMediator = new MainPanelMediator(this);
    }

    @Override
    public void initPlugin() {
        facade.registerMediator(performancePanelMediator);
        pluginAPI.setDropDownItemName(EDIT_NINE_PATCH, "Edit NinePatch");
        pluginAPI.setDropDownItemName(CONVERT_TO_NINE_PATCH, "Convert to NinePatch");
    }

    @Override
    public void onDropDownOpen(Set<Entity> selectedEntities, Array<String> actionsSet) {
        if(selectedEntities.size() == 1) {
            Entity entity = selectedEntities.stream().findFirst().get();
            MainItemComponent mainItemComponent = ComponentRetriever.get(entity, MainItemComponent.class);

            if(mainItemComponent.entityType == EntityFactory.NINE_PATCH) {
                // it's our guy
                currEditingEntity = entity;
                actionsSet.add(EDIT_NINE_PATCH);
            }
            if(mainItemComponent.entityType == EntityFactory.IMAGE_TYPE) {
                // it's our guy
                currEditingEntity = entity;
                actionsSet.add(CONVERT_TO_NINE_PATCH);
            }
        }
    }
}
