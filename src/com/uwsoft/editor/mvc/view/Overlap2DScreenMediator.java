package com.uwsoft.editor.mvc.view;

import com.puremvc.patterns.mediator.SimpleMediator;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.Overlap2D;
import com.uwsoft.editor.gdx.screen.Overlap2DScreen;

/**
 * Created by sargis on 3/30/15.
 */
public class Overlap2DScreenMediator extends SimpleMediator<Overlap2DScreen> {
    private static final String TAG = Overlap2DScreenMediator.class.getCanonicalName();
    private static final String NAME = TAG;

    public Overlap2DScreenMediator() {
        super(NAME, null);
    }

    @Override
    public String[] listNotificationInterests() {
        return new String[]{
                Overlap2D.CREATE,
                Overlap2D.PAUSE,
                Overlap2D.RESUME,
                Overlap2D.RENDER,
                Overlap2D.RESIZE,
                Overlap2D.DISPOSE,
        };
    }

    @Override
    public void handleNotification(Notification notification) {
        super.handleNotification(notification);
        switch (notification.getName()) {
            case Overlap2D.CREATE:
                setViewComponent(new Overlap2DScreen());
                viewComponent.show();
                break;
            case Overlap2D.PAUSE:
                viewComponent.pause();
                break;
            case Overlap2D.RESUME:
                viewComponent.resume();
                break;
            case Overlap2D.RENDER:
                viewComponent.render(notification.getBody());
                break;
            case Overlap2D.RESIZE:
                int[] data = notification.getBody();
                viewComponent.resize(data[0], data[1]);
                break;
            case Overlap2D.DISPOSE:
                break;
        }
    }
}
