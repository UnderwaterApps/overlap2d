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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.commons.IItemCommand;
import com.commons.view.tools.Tool;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.puremvc.patterns.facade.Facade;
import com.uwsoft.editor.renderer.SceneLoader;
import com.vo.CursorData;

import java.util.HashMap;
import java.util.HashSet;

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
     * Adds new tool to the tool bar
     * @param toolName pretty string to name new tool item
     * @param toolBtnStyle tool button style
     * @param addSeparator true, if should add menu separator
     * @param tool the tool object that is going to be added
     */
    public void addTool(String toolName, VisImageButton.VisImageButtonStyle toolBtnStyle, boolean addSeparator, Tool tool);


    /**
     * hot-swaps a tool
     */
    public void toolHotSwap(Tool tool);

    /**
     * hot-swaps a tool back
     */
    public void toolHotSwapBack();

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

    /**
     * Draws an image at selected position
     * @param regionName name of texture region to create image from
     * @param position position to draw image at
     * @return image entity
     */
    Entity drawImage(String regionName, Vector2 position);

    /**
     * @return entities that are on scene
     */
    HashSet<Entity> getProjectEntities();

    /**
     * @param entity
     * @return if entity is on visible layer
     */
    boolean isEntityVisible(Entity entity);

    /**
     * shows drop down menu with specified actions set
     * @param actionsSet
     * @param observable item with right click on it
     */
    void showPopup(HashMap<String, String> actionsSet, Object observable);

    /**
     * sets cursor to new one with cursorData
     * @param cursorData
     * @param region for plugin unic textureRegion
     */
    void setCursor(CursorData cursorData, TextureRegion region);

    /**
     * returns current selected layer name
     */
    String getCurrentSelectedLayerName();
}
