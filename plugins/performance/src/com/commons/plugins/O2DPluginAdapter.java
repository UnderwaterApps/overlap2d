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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.puremvc.patterns.facade.Facade;
import net.mountainblade.modular.Module;

/**
 * Created by azakhary on 7/24/2015.
 */
public class O2DPluginAdapter implements O2DPlugin, Module {

    protected Stage uiStage;
    protected Engine engine;
    public Facade facade;

    @Override
    public String getName() {
        return "";
    }

    @Override
    public void injectDependencies(Facade facade, Stage uiStage, Engine engine) {
        this.uiStage = uiStage;
        this.facade = facade;
        this.engine = engine;
    }

    @Override
    public void initPlugin() {

    }

    @Override
    public void initMenuItems(MenuConnector menuConnector) {

    }

    public Stage getUiStage() {
        return uiStage;
    }

    public Engine getEngine() {
        return engine;
    }
}
