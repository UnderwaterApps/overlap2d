package com.uwsoft.editor.controller.commands;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.renderer.components.MainItemComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.renderer.utils.CustomVariables;
import com.uwsoft.editor.utils.runtime.EntityUtils;

/**
 * Created by CyberJoe on 11/6/2015.
 */
public class CustomVariableModifyCommand extends EntityModifyRevertableCommand {

    private static final String CLASS_NAME = "com.uwsoft.editor.controller.commands.CustomVariableModifyCommand";
    public static final String DONE = CLASS_NAME + "DONE";

    private Integer entityId;

    private boolean isAdding;
    private String key;
    private String value;

    @Override
    public void doAction() {
        process();
        if(isAdding) {
            addVariable(key, value);
        } else {
            removeVariable(key);
        }
        sendNotification(DONE);
    }

    @Override
    public void undoAction() {
        if(isAdding) {
            removeVariable(key);
        } else {
            addVariable(key, value);
        }
        sendNotification(DONE);
    }

    private void addVariable(String key, String value) {
        Entity entity = EntityUtils.getByUniqueId(entityId);
        MainItemComponent mainItemComponent = ComponentRetriever.get(entity, MainItemComponent.class);
        CustomVariables vars = new CustomVariables();
        vars.loadFromString(mainItemComponent.customVars);
        vars.setVariable(key, value);
        mainItemComponent.customVars = vars.saveAsString();
    }

    private void removeVariable(String key) {
        Entity entity = EntityUtils.getByUniqueId(entityId);
        MainItemComponent mainItemComponent = ComponentRetriever.get(entity, MainItemComponent.class);
        CustomVariables vars = new CustomVariables();
        vars.loadFromString(mainItemComponent.customVars);
        value = vars.getStringVariable(key); //storing the backup
        vars.removeVariable(key);
        mainItemComponent.customVars = vars.saveAsString();
    }

    private void process() {
        if(entityId == null) {
            // First time call, need to prepare the data and fetch payload
            Object[] payload = getNotification().getBody();
            Entity item = (Entity) payload[0];
            entityId = EntityUtils.getEntityId(item);
            key = (String) payload[2];
            isAdding = false;
            if(((boolean) payload[1])) {
                value = (String) payload[3];
                isAdding = true;
            }
        }
    }

    public static Object addCustomVariable(Entity entity, String key, String value) {
        Object[] payload = new Object[4];
        payload[0] = entity;
        payload[1] = true; // is adding type
        payload[2] = key;
        payload[3] = value;

        return payload;
    }

    public static Object removeCustomVariable(Entity entity, String key) {
        Object[] payload = new Object[3];
        payload[0] = entity;
        payload[1] = true;
        payload[1] = false; // is adding type (removing)
        payload[2] = key;

        return payload;
    }
}
