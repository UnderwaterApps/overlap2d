package com.uwsoft.editor.renderer.systems.action.logic;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Interpolation;
import com.uwsoft.editor.renderer.components.ActionComponent;
import com.uwsoft.editor.renderer.systems.action.data.MoveByData;
import com.uwsoft.editor.renderer.systems.action.data.MoveToData;
import com.uwsoft.editor.renderer.systems.action.data.RunnableData;

/**
 * Created by Eduard on 10/13/2015.
 */
public class Actions {

    public static void moveTo(Entity entity, float x, float y, float duration) {
        moveTo(entity, x, y, duration, null);
    }

    public static void moveTo(Entity entity, float x, float y, float duration, Interpolation interpolation){
        ActionComponent<MoveToData> actionComponent = new ActionComponent(new MoveToData());
        actionComponent.data.endX = x;
        actionComponent.data.endY = y;
        actionComponent.data.duration = duration;
        actionComponent.data.interpolation = interpolation;
        entity.add(actionComponent);
    }

    public static void moveBy(Entity entity, float x, float y, float duration) {
        moveBy(entity, x, y, duration, null);
    }

    public static void moveBy(Entity entity, float x, float y, float duration, Interpolation interpolation){
        ActionComponent<MoveByData> actionComponent = new ActionComponent(new MoveByData());
        actionComponent.data.amountX = x;
        actionComponent.data.amountY = y;
        actionComponent.data.duration = duration;
        actionComponent.data.interpolation = interpolation;
        entity.add(actionComponent);
    }

    static public void run (Entity entity, Runnable runnable) {
        ActionComponent<RunnableData> actionComponent = new ActionComponent(new RunnableData());
        actionComponent.data.runnable = runnable;
        entity.add(actionComponent);
    }
}
