package com.uwsoft.editor.renderer.systems.action.logic;

import com.badlogic.ashley.core.Entity;

/**
 * Created by Eduard on 10/14/2015.
 */
abstract public class Action {

    abstract public boolean act(float delta, Entity entity);
}
