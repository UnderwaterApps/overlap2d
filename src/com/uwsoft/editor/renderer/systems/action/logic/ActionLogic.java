package com.uwsoft.editor.renderer.systems.action.logic;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.renderer.systems.action.data.ActionData;

/**
 * Created by ZeppLondon on 10/14/2015.
 */
abstract public class ActionLogic<T extends ActionData> {

    abstract public boolean act(float delta, Entity entity, T actionData);
}
