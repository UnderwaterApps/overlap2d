package com.uwsoft.editor.renderer.systems.action;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.uwsoft.editor.renderer.components.ActionComponent;
import com.uwsoft.editor.renderer.systems.action.data.*;
import com.uwsoft.editor.renderer.systems.action.logic.*;

import java.util.ArrayList;

/**
 * Created by Eduard on 10/13/2015.
 */
public class ActionSystem extends IteratingSystem {
    private final ComponentMapper<ActionComponent> actionMapper;

    public ActionSystem() {
        super(Family.all(ActionComponent.class).get());
        actionMapper = ComponentMapper.getFor(ActionComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        checkAndAddScheduledActions(entity);

        ActionComponent actionComponent = actionMapper.get(entity);
        ArrayList<ActionData> dataArray = actionComponent.dataArray;
        for (int i = dataArray.size()-1; i >= 0; i--) {
            ActionData data = dataArray.get(i);
            ActionLogic actionLogic = Actions.actionLogicMap.get(data.logicClassName);
            if (actionLogic.act(deltaTime, entity, data)) {
                dataArray.remove(data);
            }
        }
        if (dataArray.size() == 0) {
            entity.remove(ActionComponent.class);
            Actions.scheduledActionsMap.remove(entity);
        }
    }

    private void checkAndAddScheduledActions(Entity entity) {
        if (Actions.scheduledActionsMap.get(entity).size() == 0) {
            return;
        }
        ActionComponent actionComponent = actionMapper.get(entity);
        ArrayList<ActionData> dataArray = actionComponent.dataArray;

        while (Actions.scheduledActionsMap.get(entity).size() > 0) {
            dataArray.add(Actions.scheduledActionsMap.get(entity).pop());
        }
    }
}
