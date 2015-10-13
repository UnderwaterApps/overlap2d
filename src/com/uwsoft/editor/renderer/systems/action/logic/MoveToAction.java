package com.uwsoft.editor.renderer.systems.action.logic;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.renderer.components.ActionComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

/**
 * Created by Eduard on 10/13/2015.
 */
public class MoveToAction {
    public static void act(float delta, Entity entity) {
        ActionComponent actionComponent = ComponentRetriever.get(entity, ActionComponent.class);
        TransformComponent transformComponent = ComponentRetriever.get(entity, TransformComponent.class);


    }
}
