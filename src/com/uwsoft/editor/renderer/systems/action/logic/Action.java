package com.uwsoft.editor.renderer.systems.action.logic;

import com.badlogic.ashley.core.Entity;

/**
 * Created by Eduard on 10/14/2015.
 */
abstract public class Action {

    abstract public boolean act(float delta, Entity entity);
    abstract public void update(float percent, Entity entity);
    abstract public void begin(Entity entity);
    abstract public void end(Entity entity);
}
