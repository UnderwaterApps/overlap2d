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
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.commons.IItemCommand;
import com.puremvc.patterns.facade.Facade;
import com.uwsoft.editor.renderer.SceneLoader;

/**
 * Plugin API is a main interface of communication between plugin and Overlap2D editor
 * Created by azakhary on 7/24/2015.
 */
public interface PluginAPI {
    /**
     * Get scene loader of the runtime to work with scene and items
     * @return SceneLoader
     */
    public SceneLoader getSceneLoader();

    /**
     * Returns MVC facade, to send notifications or commands, and register mediators
     * @return Facade
     */
    public Facade getFacade();

    /**
     * Returns Ashley engine of main scene where all entities are located
     * @return Engine
     */
    public Engine getEngine();

    /**
     * Get simple libGDX Stage for UI part of editor, to add dialogs or other UI elements and widgets
     * @return
     */
    public Stage getUIStage();

    /**
     * @return Path of plugin directory
     */
    public String getPluginDir();

    /**
     * @return Path of working project
     */
    public String getProjectPath();

    /**
     * @return TextureAtlas of loaded project
     */
    public TextureAtlas getProjectTextureAtlas();


    /**
     * Adds new sub menu item to the top bar
     * @param menu unique identifier to global menu items provided in @Overlap2DMenuBar by three constants FILE_MENU, EDIT_MENU, WINDOWS_MENU
     * @param subMenuName pretty string to name new submenu item
     * @param notificationName unique notification id that will be fired when this menu item is clicked
     */
    public void addMenuItem(String menu, String subMenuName, String notificationName);

    /**
     * Creates new menu item for Contextual drop down menu, that is created when user right clicks on something in the editor.
     * This only creates a menu item, but it should be specifically added later to action set, at the moment context menu is summoned
     * @param action unique name of notification id that will be fired when this menu item is clicked
     * @param name pretty text to be written on this menu item
     */
    public void setDropDownItemName(String action, String name);

    /**
     * re-loads current project entirely (used when changes were made that require to whole project to be reloaded)
     */
    public void reLoadProject();

    /**
     * Saves current project
     */
    public void saveProject();

    /**
     * Creates a revertable command that later can be undone or re-done by user with Ctrl+Z or similar.
     * @param command Object containing your command logic
     * @param body Additional data that can be send as parameters
     */
    public void revertableCommand(IItemCommand command, Object body);

    /**
     * Removes follower object (selection rectangle) from particular entity (usually makes sense when entity is deleted without proper action)
     * @param entity
     */
    public void removeFollower(Entity entity);

}
