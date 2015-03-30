package com.uwsoft.editor.mvc;

import com.puremvc.patterns.facade.SimpleFacade;
import com.puremvc.patterns.observer.BaseNotification;
import com.uwsoft.editor.Overlap2D;
import com.uwsoft.editor.gdx.screen.Overlap2DScreen;
import com.uwsoft.editor.mvc.controller.StartupCommand;
import com.uwsoft.editor.mvc.proxy.DataManager;
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
        registerProxy(new DataManager());
    }

    @Override
    protected void initializeView() {
        super.initializeView();
        registerMediator(new Overlap2DScreenMediator(new Overlap2DScreen()));
    }
}
