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

import java.util.Set;

/**
 * Created by azakhary on 7/24/2015.
 */
public interface O2DPlugin {

    String getName();
    void initPlugin();

    public void setAPI(PluginAPI pluginAPI);

    public void onDropDownOpen(Set<Entity> selectedEntities, Array<String> actionsSet);
}
