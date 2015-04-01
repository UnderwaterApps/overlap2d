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

package com.uwsoft.editor.mvc;

import com.puremvc.patterns.facade.SimpleFacade;
import com.puremvc.patterns.observer.BaseNotification;
import com.uwsoft.editor.Overlap2D;
import com.uwsoft.editor.mvc.proxy.ResolutionManager;
import com.uwsoft.editor.mvc.controller.StartupCommand;
import com.uwsoft.editor.mvc.proxy.ProjectManager;
import com.uwsoft.editor.mvc.proxy.SceneDataManager;
import com.uwsoft.editor.mvc.proxy.TextureManager;
import com.uwsoft.editor.mvc.view.Overlap2DScreenMediator;

/**
 * Created by sargis on 3/30/15.
 */
public class Overlap2DFacade extends SimpleFacade {
    public static final String STARTUP = "startup";
    private static Overlap2DFacade instance = null;
    private Overlap2D overlap2D;

    protected Overlap2DFacade() {
        super();
    }

    /**
     * Facade Singleton Factory method
     *
     * @return The Singleton instance of the Facade
     */
    public synchronized static Overlap2DFacade getInstance() {
        if (instance == null) {
            instance = new Overlap2DFacade();
        }
        return instance;
    }

    public void startup(Overlap2D overlap2D) {
        this.overlap2D = overlap2D;
        notifyObservers(new BaseNotification(STARTUP, null, null));
    }

    @Override
    protected void initializeFacade() {
        super.initializeFacade();
    }

    @Override
    protected void initializeController() {
        super.initializeController();
        registerCommand(STARTUP, StartupCommand.class);
    }

    @Override
    protected void initializeModel() {
        super.initializeModel();
        registerProxy(new ProjectManager());
        registerProxy(new ResolutionManager());
        registerProxy(new SceneDataManager());
        registerProxy(new TextureManager());
    }

    @Override
    protected void initializeView() {
        super.initializeView();
        registerMediator(new Overlap2DScreenMediator());
    }
}
