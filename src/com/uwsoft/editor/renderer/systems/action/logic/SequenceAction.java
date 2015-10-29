package com.uwsoft.editor.renderer.systems.action.logic;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.renderer.systems.action.Actions;
import com.uwsoft.editor.renderer.systems.action.data.SequenceData;

/**
 * Created by ZeppLondon on 10/23/15.
 */
public class SequenceAction<T extends SequenceData> extends ParallelAction<T> {
    @Override
    public boolean act(float delta, Entity entity, T actionData) {
        if (actionData.index >= actionData.actionDatas.length) return true;
        ActionLogic logic = Actions.actionLogicMap.get(actionData.actionDatas[actionData.index].logicClassName);
        if (logic.act(delta, entity, actionData.actionDatas[actionData.index])) {
            actionData.index++;
            if (actionData.index >= actionData.actionDatas.length) return true;
        }
        return false;
    }
}
