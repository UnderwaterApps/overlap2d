package com.uwsoft.editor.renderer.systems.action.logic;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.renderer.components.ActionComponent;

/**
 * Created by Eduard on 10/13/2015.
 */
public class Actions {
    public static void moveTo(Entity entity, float x, float y, float duration){
        ActionComponent moveToComponent = new ActionComponent();
        moveToComponent.type = 1;
        moveToComponent.x = x;
        moveToComponent.y = y;
        moveToComponent.duration = duration;
        entity.add(moveToComponent);
    }
}
