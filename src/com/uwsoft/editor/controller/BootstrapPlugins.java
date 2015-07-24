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
import com.uwsoft.editor.proxy.ProjectManager;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.text.WordUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by azakhary on 7/24/2015.
 */
public class BootstrapPlugins extends SimpleCommand {

    public void execute(Notification notification) {
        super.execute(notification);
        facade = Overlap2DFacade.getInstance();

        PluginManager pluginManager = new PluginManager();
        facade.registerProxy(pluginManager);

        ProjectManager projectManager = facade.retrieveProxy(ProjectManager.NAME);
        File pluginDir = new File(projectManager.getRootPath() + File.separator + "plugins");
        if(pluginDir.exists() && pluginDir.isDirectory()) {
            File[] listOfFiles = pluginDir.listFiles();
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile() && FilenameUtils.getExtension(listOfFiles[i].getName()).equals("jar")) {
                    loadPlugin(listOfFiles[i].getAbsolutePath());
                }
            }
        }

    }

    private void loadPlugin(String pluginPath) {
        String pluginName = FilenameUtils.removeExtension(FilenameUtils.getBaseName(pluginPath));
        String pluginNameFLUC = WordUtils.capitalize(pluginName);

        PluginManager pluginManager = facade.retrieveProxy(PluginManager.NAME);

        try {
            JarFile jarFile = new JarFile(pluginPath);
            Enumeration ee = jarFile.entries();
            URL[] urls = { new URL("jar:file:" + pluginPath+"!/") };
            URLClassLoader cl = URLClassLoader.newInstance(urls);

            while (ee.hasMoreElements()) {
                JarEntry je = (JarEntry) ee.nextElement();
                if(je.isDirectory() || !je.getName().endsWith(".class")){
                    continue;
                }
                // -6 because of .class
                String className = je.getName().substring(0,je.getName().length()-6);
                className = className.replace('/', '.');
                if(className.contains(pluginNameFLUC + "Plugin")) {
                    Class c = cl.loadClass(className);
                    pluginManager.initPlugin((O2DPlugin) c.newInstance());
                }
            }
        } catch (MalformedURLException e) {
            //e.printStackTrace();
        } catch (ClassNotFoundException e) {
            //e.printStackTrace();
        } catch (IOException e) {
            //e.printStackTrace();
        } catch (InstantiationException e) {
            //e.printStackTrace();
        } catch (IllegalAccessException e) {
            //e.printStackTrace();
        }
    }
}
