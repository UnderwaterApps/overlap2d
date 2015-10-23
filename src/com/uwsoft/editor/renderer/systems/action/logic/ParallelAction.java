package com.uwsoft.editor.renderer.systems.action.logic;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.renderer.systems.action.Actions;
import com.uwsoft.editor.renderer.systems.action.data.ActionData;
import com.uwsoft.editor.renderer.systems.action.data.ParallelData;

/**
 * Created by ZeppLondon on 10/23/15.
 */
public class ParallelAction<T extends ParallelData> extends ActionLogic<T> {
    @Override
    public boolean act(float delta, Entity entity, T actionData) {
        actionData.complete = true;
        for (int i = 0; i < actionData.actionDatas.length; i++) {
            ActionData data = actionData.actionDatas[i];
            ActionLogic logic = Actions.actionLogicMap.get(actionData.actionDatas[i].logicClassName);
            if (!data.detached) {
                actionData.complete = false;
                if (logic.act(delta, entity, data)) {
                    data.detached = true;
                }
            }
        }
        return actionData.complete;
    }
}
