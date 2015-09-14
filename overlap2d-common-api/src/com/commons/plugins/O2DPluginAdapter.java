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
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.puremvc.patterns.facade.Facade;
import net.mountainblade.modular.Module;

import java.util.Set;

/**
 * Created by azakhary on 7/24/2015.
 */
public abstract class O2DPluginAdapter implements O2DPlugin, Module {

    public Facade facade;
    protected Engine engine;
    protected Stage stage;
    protected PluginAPI pluginAPI;

    public String getName() {
        return "";
    }

    @Override
    public void setFacade(Facade facade) {
        this.facade = facade;
    }
    @Override
    public void setEngine(Engine engine) {
        this.engine = engine;
    }
    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    @Override
    public void setAPI(PluginAPI pluginAPI) {
        this.pluginAPI = pluginAPI;
    }

    @Override
    public void onDropDownOpen(Set<Entity> selectedEntities, Array<String> actionsSet) {

    }

    public Stage getStage() {
        return stage;
    }
    public Engine getEngine() {
        return engine;
    }
    public PluginAPI getAPI() {
        return pluginAPI;
    }
}
