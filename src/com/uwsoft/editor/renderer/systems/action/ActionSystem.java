package com.uwsoft.editor.renderer.systems.action;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.uwsoft.editor.renderer.components.ActionComponent;
import com.uwsoft.editor.renderer.systems.action.logic.Action;
import com.uwsoft.editor.renderer.systems.action.logic.Actions;
import com.uwsoft.editor.renderer.systems.action.logic.MoveToAction;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Eduard on 10/13/2015.
 */
public class ActionSystem extends IteratingSystem {
    private final ComponentMapper<ActionComponent> actionMapper;
    private HashMap<String, Action> actionsMap = new HashMap<String, Action>();

    public ActionSystem() {
        super(Family.all(ActionComponent.class).get());
        actionMapper = ComponentMapper.getFor(ActionComponent.class);
        createActions();
    }

    private void createActions() {
        actionsMap.put(Actions.MOVE_TO, new MoveToAction());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ActionComponent actionComponent = actionMapper.get(entity);
        Action actionLogic = actionsMap.get(actionComponent.logicType);
        if (actionLogic.act(deltaTime, entity)) {
            entity.remove(ActionComponent.class);
        }


    }
}
