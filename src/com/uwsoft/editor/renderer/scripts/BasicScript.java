package com.uwsoft.editor.renderer.scripts;

import com.badlogic.ashley.core.Entity;

/**
 * Created by CyberJoe on 6/19/2015.
 */
public abstract class BasicScript implements IScript {

    protected Entity entity;

    @Override
    public void init(Entity item) {
        entity = item;
    }

    public Entity getEntity() {
        return entity;
    }
}
