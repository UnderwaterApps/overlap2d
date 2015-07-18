package com.uwsoft.editor.view.ui;

import com.puremvc.patterns.mediator.SimpleMediator;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.Overlap2D;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.controller.commands.CompositeCameraChangeCommand;
import com.uwsoft.editor.proxy.SceneDataManager;
import com.uwsoft.editor.view.stage.tools.PanTool;

/**
 * Created by azakhary on 7/18/2015.
 */
public class RulersUIMediator extends SimpleMediator<RulersUI> {
    private static final String TAG = RulersUIMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    /**
     * Constructor.
     */
    public RulersUIMediator() {
        super(NAME, new RulersUI());
    }

    @Override
    public void onRegister() {
        facade = Overlap2DFacade.getInstance();
    }


    @Override
    public String[] listNotificationInterests() {
        return new String[]{
                SceneDataManager.SCENE_LOADED,
                PanTool.SCENE_PANNED,
                Overlap2D.ZOOM_CHANGED,
        };
    }

    @Override
    public void handleNotification(Notification notification) {
        super.handleNotification(notification);
        switch (notification.getName()) {
            case SceneDataManager.SCENE_LOADED:

                break;
        }
    }
}
