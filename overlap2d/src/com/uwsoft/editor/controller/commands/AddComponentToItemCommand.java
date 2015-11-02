package com.uwsoft.editor.controller.commands;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.commons.MsgAPI;

/**
 * Created by CyberJoe on 7/2/2015.
 */
public class AddComponentToItemCommand extends EntityModifyRevertableCommand {

    private static final String CLASS_NAME = "com.uwsoft.editor.controller.commands.AddComponentToItemCommand";
    public static final String DONE = CLASS_NAME + "DONE";

    private Entity entity;
    private Component component;

    private void collectData() {
        Object[] payload = getNotification().getBody();
        entity = (Entity) payload[0];
        component = (Component) payload[1];
    }

    @Override
    public void doAction() {
        collectData();

        entity.add(component);

        facade.sendNotification(DONE, entity);
        facade.sendNotification(MsgAPI.ITEM_DATA_UPDATED, entity);
    }

    @Override
    public void undoAction() {
        entity.remove(component.getClass());

        facade.sendNotification(DONE, entity);
        facade.sendNotification(MsgAPI.ITEM_DATA_UPDATED, entity);
    }

    public static Object[] payload(Entity entity, Component component) {
        Object[] payload = new Object[2];
        payload[0] = entity;
        payload[1] = component;
        return payload;
    }
}
