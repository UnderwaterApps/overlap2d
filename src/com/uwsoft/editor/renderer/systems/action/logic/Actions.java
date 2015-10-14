package com.uwsoft.editor.renderer.systems.action.logic;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Interpolation;
import com.uwsoft.editor.renderer.components.ActionComponent;
import com.uwsoft.editor.renderer.systems.action.ActionSystem;

/**
 * Created by Eduard on 10/13/2015.
 */
public class Actions {
    public static final String MOVE_TO = "MOVE_TO";

    public static void moveTo(Entity entity, float x, float y, float duration) {
        moveTo(entity, x, y, duration, null);
    }

    public static void moveTo(Entity entity, float x, float y, float duration, Interpolation interpolation){
        ActionComponent moveToComponent = new ActionComponent();
        moveToComponent.logicType = Actions.MOVE_TO;
        moveToComponent.endX = x;
        moveToComponent.endY = y;
        moveToComponent.duration = duration;
        moveToComponent.interpolation = interpolation;
        entity.add(moveToComponent);
    }
}
