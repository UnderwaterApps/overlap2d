package com.uwsoft.editor.view.ui.followers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.puremvc.patterns.observer.Notification;

/**
 * Created by CyberJoe on 7/2/2015.
 */
public abstract class SubFollower extends Actor {

    protected Entity entity;

    public SubFollower(Entity entity) {
        setItem(entity);
        create();
        update();
    }

    private void setItem(Entity entity) {
        this.entity = entity;
    }


    public void handleNotification(Notification notification) {

    }

    public abstract void create();
    public abstract void update();
}
