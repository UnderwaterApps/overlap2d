package com.uwsoft.editor.renderer.systems.action.logic;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.renderer.systems.action.data.RunnableData;

/**
 * Created by Eduard on 10/15/2015.
 */
public class RunnableAction extends ActionLogic<RunnableData> {
    @Override
    public boolean act(float delta, Entity entity, RunnableData actionData) {
        if (!actionData.ran) {
            actionData.ran = true;
            run(entity, actionData);
        }
        return true;
    }

    public void run(Entity entity, RunnableData actionData) {
        actionData.runnable.run();
    }
}
