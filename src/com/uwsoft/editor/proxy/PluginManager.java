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

package com.uwsoft.editor.proxy;

import com.puremvc.patterns.proxy.BaseProxy;
import com.uwsoft.editor.Overlap2DFacade;
import com.commons.plugins.O2DPlugin;
import com.commons.plugins.MenuConnector;
import com.uwsoft.editor.view.menu.Overlap2DMenuBarMediator;
import com.uwsoft.editor.view.stage.Sandbox;

import java.util.ArrayList;

/**
 * Created by azakhary on 7/24/2015.
 */
public class PluginManager extends BaseProxy {
    private static final String TAG = PluginManager.class.getCanonicalName();
    public static final String NAME = TAG;

    private ArrayList<O2DPlugin> plugins = new ArrayList<>();

    public PluginManager() {
        super(NAME);
    }

    public O2DPlugin registerPlugin(O2DPlugin plugin) {
        plugins.add(plugin);

        return plugin;
    }

    public void initPlugin(O2DPlugin plugin) {
        if(plugins.contains(plugin)) return;

        registerPlugin(plugin);
        plugin.injectDependencies(Overlap2DFacade.getInstance(), Sandbox.getInstance().getUIStage(), Sandbox.getInstance().getEngine());
        MenuConnector menuConnector = Overlap2DFacade.getInstance().retrieveMediator(Overlap2DMenuBarMediator.NAME);
        plugin.initPlugin();
        plugin.initMenuItems(menuConnector);
    }
}
