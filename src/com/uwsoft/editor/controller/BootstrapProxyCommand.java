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

import com.puremvc.patterns.command.SimpleCommand;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.proxy.CommandManager;
import com.uwsoft.editor.proxy.CursorManager;
import com.uwsoft.editor.proxy.EditorTextureManager;
import com.uwsoft.editor.proxy.FontManager;
import com.uwsoft.editor.proxy.ProjectManager;
import com.uwsoft.editor.proxy.ResolutionManager;
import com.uwsoft.editor.proxy.ResourceManager;
import com.uwsoft.editor.proxy.SceneDataManager;

/**
 * Created by sargis on 4/1/15.
 */
public class BootstrapProxyCommand extends SimpleCommand {
    @Override
    public void execute(Notification notification) {
        super.execute(notification);
        facade = Overlap2DFacade.getInstance();
        facade.registerProxy(new FontManager());
        facade.registerProxy(new CommandManager());
        facade.registerProxy(new CursorManager());
        facade.registerProxy(new ProjectManager());
        facade.registerProxy(new ResolutionManager());
        facade.registerProxy(new SceneDataManager());
        facade.registerProxy(new EditorTextureManager());
        facade.registerProxy(new ResourceManager());
    }
}
