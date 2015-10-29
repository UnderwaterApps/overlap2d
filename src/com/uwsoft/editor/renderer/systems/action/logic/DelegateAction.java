package com.uwsoft.editor.renderer.systems.action.logic;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.renderer.systems.action.data.DelegateData;

/**
 * Created by ZeppLondon on 10/15/2015.
 */
public abstract class DelegateAction<T extends DelegateData> extends ActionLogic<T> {
    @Override
    public boolean act(float delta, Entity entity, T actionData) {
        return delegate(delta, entity, actionData);
    }

    abstract protected boolean delegate (float delta, Entity entity, T actionData);
}
