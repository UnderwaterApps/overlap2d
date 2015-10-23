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
 * Created by azakhary on 7/24/2015.
 */
public interface PluginAPI {
    /**
     * Getters List
     */
    SceneLoader getSceneLoader();
    public Facade getFacade();
    public Engine getEngine();
    public Stage getUIStage();

    // Less common
    public String getPluginDir();
    public String getProjectPath();
    public TextureAtlas getProjectTextureAtlas();

    /**
     * Menues and Drop Downs
     */
    public void addMenuItem(String menu, String subMenuName, String notificationName);
    public void setDropDownItemName(String action, String name);

    /**
     * Editor Actions
     */
    public void reLoadProject();
    public void saveProject();
    public void revertableCommand(IItemCommand command, Object body);
    public void removeFollower(Entity entity);

}
