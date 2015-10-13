package com.uwsoft.editor.renderer.systems.action;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.uwsoft.editor.renderer.components.ActionComponent;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.systems.action.logic.MoveToAction;

/**
 * Created by Eduard on 10/13/2015.
 */
public class ActionSystem extends IteratingSystem {
    private final ComponentMapper<ActionComponent> actionMapper;
    private ActionComponent actionComponent;

    public ActionSystem() {
        super(Family.all(ActionComponent.class).get());
        actionMapper = ComponentMapper.getFor(ActionComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        actionComponent = actionMapper.get(entity);
        switch (actionComponent.type) {
            case 1 :
                MoveToAction.act(deltaTime, entity);
                break;
        }

    }
}
