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

package com.uwsoft.editor.controller;

import com.commons.plugins.O2DPlugin;
import com.puremvc.patterns.command.SimpleCommand;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.proxy.PluginManager;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.text.WordUtils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by azakhary on 7/24/2015.
 */
public class BootstrapPlugins extends SimpleCommand {

    public void execute(Notification notification) {
        super.execute(notification);
        facade = Overlap2DFacade.getInstance();

        PluginManager pluginManager = new PluginManager();
        facade.registerProxy(pluginManager);

        //TODO: read plugin from plugins folder
        loadPlugin("D:\\work\\overlap2d\\plugins\\performance\\build\\libs\\performance.jar");
    }

    private void loadPlugin(String pluginPath) {
        String pluginName = FilenameUtils.removeExtension(FilenameUtils.getBaseName(pluginPath));
        String pluginNameFLUC = WordUtils.capitalize(pluginName);

        PluginManager pluginManager = facade.retrieveProxy(PluginManager.NAME);

        try {
            File file  = new File(pluginPath);
            URL url = file.toURI().toURL();
            URL[] urls = new URL[]{url};
            URLClassLoader child = new URLClassLoader (urls, this.getClass().getClassLoader());
            Class classToLoad = Class.forName("com.overlap2d.plugins." + pluginName + "." + pluginNameFLUC + "Plugin", true, child);
            pluginManager.initPlugin((O2DPlugin) classToLoad.newInstance());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
