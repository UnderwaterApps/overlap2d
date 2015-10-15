package com.uwsoft.editor.renderer.systems.action.logic;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.renderer.components.ActionComponent;
import com.uwsoft.editor.renderer.systems.action.data.DelayData;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

/**
 * Created by Eduard on 10/15/2015.
 */
public class DelayAction  extends DelegateAction{

    @Override
    protected boolean delegate(float delta, Entity entity) {
        ActionComponent<DelayData> actionComponent = ComponentRetriever.get(entity, ActionComponent.class);
        if (actionComponent.data.passedTime < actionComponent.data.duration) {
            actionComponent.data.passedTime += delta;
            if (actionComponent.data.passedTime < actionComponent.data.duration) return false;
            delta = actionComponent.data.passedTime - actionComponent.data.duration;
        }
        /*if (action == null) return true;
        return action.act(delta);*/
        return  true;
    }
}
