package com.uwsoft.editor.renderer.systems.action.logic;

import com.badlogic.ashley.core.Entity;

/**
 * Created by Eduard on 10/15/2015.
 */
public abstract class DelegateAction extends Action {
    @Override
    public boolean act(float delta, Entity entity) {
        return delegate(delta, entity);
    }

    @Override
    public void begin(Entity entity) {

    }

    @Override
    public void end(Entity entity) {

    }

    abstract protected boolean delegate (float delta, Entity entity);
}
