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

package com.overlap2d.plugins.performance;

import com.commons.plugins.MenuConnector;
import com.commons.plugins.O2DPluginAdapter;
import net.mountainblade.modular.annotations.Implementation;

/**
 * Created by azakhary on 7/24/2015.
 * Plugin to display some real times stats on performance of your scene
 */
@Implementation(authors = "azakhary", version = "0.0.1")
public class PerformancePlugin extends O2DPluginAdapter {
    public static final String CLASS_NAME = "com.uwsoft.editor.plugins.performance";

    public static final String PANEL_OPEN = CLASS_NAME + ".PANEL_OPEN";
    public static final String WINDOWS_MENU = "com.uwsoft.editor.view.Overlap2DMenuBar.WINDOW_MENU";

    private PerformancePanelMediator performancePanelMediator;

    public PerformancePlugin() {
        performancePanelMediator = new PerformancePanelMediator(this);
    }

    @Override
    public void initPlugin() {
        facade.registerMediator(performancePanelMediator);
    }

    @Override
    public void initMenuItems(MenuConnector menuConnector) {
        menuConnector.addMenuItem(WINDOWS_MENU, "Performance", PANEL_OPEN);
    }
}
