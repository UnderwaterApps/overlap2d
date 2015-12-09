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

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.commons.IItemCommand;
import com.commons.MsgAPI;
import com.puremvc.patterns.facade.Facade;
import com.puremvc.patterns.proxy.BaseProxy;
import com.uwsoft.editor.Overlap2DFacade;
import com.commons.plugins.O2DPlugin;
import com.commons.plugins.PluginAPI;
import com.uwsoft.editor.controller.commands.PluginItemCommand;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.data.SceneVO;
import com.uwsoft.editor.view.menu.Overlap2DMenuBarMediator;
import com.uwsoft.editor.view.stage.Sandbox;
import com.uwsoft.editor.view.ui.FollowersUIMediator;
import com.uwsoft.editor.view.ui.UIDropDownMenuMediator;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by azakhary on 7/24/2015.
 */
public class PluginManager extends BaseProxy implements PluginAPI {
    private static final String TAG = PluginManager.class.getCanonicalName();
    public static final String NAME = TAG;

    private ArrayList<O2DPlugin> plugins = new ArrayList<>();
    private String pluginDir;

    public PluginManager() {
        super(NAME);
        facade = Overlap2DFacade.getInstance();
    }

    public O2DPlugin registerPlugin(O2DPlugin plugin) {
        plugins.add(plugin);

        return plugin;
    }

    public void initPlugin(O2DPlugin plugin) {
        if(plugins.contains(plugin)) return;

        registerPlugin(plugin);
        plugin.setAPI(this);
        plugin.initPlugin();
    }

    public void dropDownActionSets(Set<Entity> selectedEntities, Array<String> actionsSet) {
        for(O2DPlugin plugin: plugins) {
            plugin.onDropDownOpen(selectedEntities, actionsSet);
        }
    }

    public void setDropDownItemName(String action, String name) {
        UIDropDownMenuMediator dropDownMenuMediator = facade.retrieveMediator(UIDropDownMenuMediator.NAME);
        dropDownMenuMediator.getViewComponent().setActionName(action, name);
    }

    @Override
    public String getProjectPath() {
        ProjectManager projectManager = facade.retrieveProxy(ProjectManager.NAME);
        return projectManager.getCurrentProjectPath();
    }

    @Override
    public TextureAtlas getProjectTextureAtlas() {
        ResourceManager resourceManager = facade.retrieveProxy(ResourceManager.NAME);
        return resourceManager.getTextureAtlas();
    }

    @Override
    public void reLoadProject() {
        Sandbox sandbox = Sandbox.getInstance();
        ProjectManager projectManager = facade.retrieveProxy(ProjectManager.NAME);
        projectManager.openProjectAndLoadAllData(projectManager.getCurrentProjectPath());
        sandbox.loadCurrentProject();
        facade.sendNotification(ProjectManager.PROJECT_DATA_UPDATED);
    }

    @Override
    public void saveProject() {
        Sandbox sandbox = Sandbox.getInstance();
        SceneDataManager sceneDataManager = facade.retrieveProxy(SceneDataManager.NAME);
        SceneVO vo = sandbox.sceneVoFromItems();
        sceneDataManager.saveScene(vo);
    }

    @Override
    public void revertableCommand(IItemCommand command, Object body) {
        Object payload = PluginItemCommand.build(command, body);
        facade.sendNotification(MsgAPI.ACTION_PLUGIN_PROXY_COMMAND, payload);
    }

    @Override
    public void removeFollower(Entity entity) {
        FollowersUIMediator followersUIMediator = Overlap2DFacade.getInstance().retrieveMediator(FollowersUIMediator.NAME);
        followersUIMediator.removeFollower(entity);
    }

    public void addMenuItem(String menu, String subMenuName, String notificationName) {
        Overlap2DMenuBarMediator overlap2DMenuBarMediator = facade.retrieveMediator(Overlap2DMenuBarMediator.NAME);
        overlap2DMenuBarMediator.addMenuItem(menu, subMenuName, notificationName);
    }

    public void setPluginDir(String pluginDir) {
        this.pluginDir = pluginDir;
    }


    @Override
    public String getPluginDir() {
        return pluginDir;
    }

    @Override
    public SceneLoader getSceneLoader() {
        return Sandbox.getInstance().getSceneControl().sceneLoader;
    }

    @Override
    public Facade getFacade() {
        return facade;
    }

    @Override
    public Engine getEngine() {
        return getSceneLoader().getEngine();
    }

    @Override
    public Stage getUIStage() {
        return Sandbox.getInstance().getUIStage();
    }
}
