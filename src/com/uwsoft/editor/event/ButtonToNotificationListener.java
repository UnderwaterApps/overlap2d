package com.uwsoft.editor.event;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.uwsoft.editor.Overlap2DFacade;

/**
 * Created by azakhary on 7/2/2015.
 */
public class ButtonToNotificationListener extends ClickListener{

    private String notificationName;

    public ButtonToNotificationListener(String notificationName) {
        this.notificationName = notificationName;
    }

    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        super.touchUp(event, x, y, pointer, button);
        Overlap2DFacade.getInstance().sendNotification(notificationName);
    }
}
