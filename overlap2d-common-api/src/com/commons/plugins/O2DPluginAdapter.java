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

package com.commons.plugins;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.puremvc.patterns.facade.Facade;
import net.mountainblade.modular.Module;

import java.util.Set;

/**
 * Created by azakhary on 7/24/2015.
 */
public abstract class O2DPluginAdapter implements O2DPlugin, Module {

    public Facade facade;
    protected PluginAPI pluginAPI;

    public String getName() {
        return "";
    }

    @Override
    public void setAPI(PluginAPI pluginAPI) {
        this.pluginAPI = pluginAPI;
        facade = pluginAPI.getFacade();
    }

    /**
     * Triggered whenever a context menu is displayed
     *
     * @param selectedEntities list of entities that were selected when context menu was created about, if right clicked on empty space empty array is used
     * @param actionsSet list of current actions (notification id's) planned for this particular context menu, it can be modified by adding or removing elements.
     */
    @Override
    public void onDropDownOpen(Set<Entity> selectedEntities, Array<String> actionsSet) {

    }

    public PluginAPI getAPI() {
        return pluginAPI;
    }
}
