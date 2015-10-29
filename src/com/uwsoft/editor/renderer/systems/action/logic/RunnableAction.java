package com.uwsoft.editor.renderer.systems.action.logic;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.renderer.systems.action.data.RunnableData;

/**
 * Created by ZeppLondon on 10/15/2015.
 */
public class RunnableAction<T extends RunnableData> extends ActionLogic<T> {
    @Override
    public boolean act(float delta, Entity entity, T actionData) {
        if (!actionData.ran) {
            actionData.ran = true;
            run(entity, actionData);
        }
        return true;
    }

    public void run(Entity entity, T actionData) {
        actionData.runnable.run();
    }
}
