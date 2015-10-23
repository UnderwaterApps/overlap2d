package com.uwsoft.editor.renderer.systems.action;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.math.Interpolation;
import com.uwsoft.editor.renderer.components.ActionComponent;
import com.uwsoft.editor.renderer.systems.action.data.*;
import com.uwsoft.editor.renderer.systems.action.logic.*;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Eduard on 10/13/2015.
 */
public class Actions {

    public static HashMap<String, ActionLogic> actionLogicMap = new HashMap<String, ActionLogic>();
    public static HashMap<Entity, LinkedList<ActionData>> scheduledActionsMap = new HashMap<Entity, LinkedList<ActionData>>();
    private static boolean initialized;

    private static void initialize() {
        actionLogicMap.put(MoveToAction.class.getName(), new MoveToAction());
        actionLogicMap.put(MoveByAction.class.getName(), new MoveByAction());
        actionLogicMap.put(RotateToAction.class.getName(), new RotateToAction());
        actionLogicMap.put(RotateByAction.class.getName(), new RotateByAction());
        actionLogicMap.put(RunnableAction.class.getName(), new RunnableAction());
        actionLogicMap.put(DelayAction.class.getName(), new DelayAction());
        actionLogicMap.put(ParallelAction.class.getName(), new ParallelAction());
        actionLogicMap.put(SequenceAction.class.getName(), new SequenceAction());
        initialized = true;
    }

//    public static void registerActionClass(Class<T extends ActionLogic> type) {
//        actionLogicMap.put(type.getName(), type.newInstance());
//    }

    private static void checkInit() {
        if (!initialized) initialize();
    }

    public static ActionData moveTo(float x, float y, float duration) {
        return moveTo(x, y, duration, null);
    }

    public static ActionData moveTo(float x, float y, float duration, Interpolation interpolation){
        MoveToData actionData = new MoveToData(
                interpolation,
                duration,
                x,
                y
        );
        actionData.logicClassName = MoveToAction.class.getName();
        return (actionData);
    }

    public static ActionData moveBy(float x, float y, float duration) {
        return moveBy(x, y, duration, null);
    }

    public static ActionData moveBy(float x, float y, float duration, Interpolation interpolation){
        MoveByData actionData = new MoveByData(
                interpolation,
                duration,
                x,
                y
        );
        actionData.logicClassName = MoveByAction.class.getName();
        return actionData;
    }

    static public ActionData run (Runnable runnable) {
        RunnableData actionData = new RunnableData(
                runnable
        );
        actionData.logicClassName = RunnableAction.class.getName();
        return actionData;
    }

    static public RotateToData rotateTo(float end, float duration) {
        return rotateTo(end, duration, null);
    }

    static public RotateToData rotateTo(float end, float duration, Interpolation interpolation) {
        RotateToData actionData = new RotateToData(
                interpolation,
                duration,
                end
        );
        actionData.logicClassName = RotateToAction.class.getName();
        return actionData;
    }


    static public RotateByData rotateBy(float amount, float duration) {
        return rotateBy(amount, duration, null);
    }

    static public RotateByData rotateBy(float amount, float duration, Interpolation interpolation) {
        RotateByData actionData =  new RotateByData(
                interpolation,
                duration,
                amount
        );
        actionData.logicClassName = RotateByAction.class.getName();
        return actionData;
    }
/*
    static public void customAction(Entity entity, Data, Logic) {
        RotateByData actionData =  new RotateByData(
                interpolation,
                duration,
                amount
        );
        addActionObject(entity, actionData, actionLogicMap.get("RotaBy"));
    }*/

    static public ParallelData parallel(ActionData... actionDatas) {
        ParallelData actionData = new ParallelData(actionDatas);
        actionData.logicClassName = ParallelAction.class.getName();
        return actionData;
    }


    static public SequenceData sequence(ActionData... actionDatas) {
        SequenceData actionData = new SequenceData(actionDatas);
        actionData.logicClassName = SequenceAction.class.getName();
        return actionData;
    }


    public static void addAction(final Entity entity, ActionData data){
        checkInit();
        ActionComponent actionComponent;
        actionComponent = ComponentRetriever.get(entity, ActionComponent.class);

        if (actionComponent != null) {
            if (actionComponent.dataArray.size() == 0) {
                // action is scheduled for remove, so we "schedule" add new ActionComponent after its removal
                entity.componentAdded.add(new Listener<Entity>() {
                    @Override
                    public void receive(Signal<Entity> signal, Entity object) {
                        entity.add(new ActionComponent());
                    }
                });
            }
        } else {
            if ((scheduledActionsMap.get(entity) == null || scheduledActionsMap.get(entity).size() == 0)) {
                // There was no add ActionComponent scheduled, so we add the component
                entity.add(new ActionComponent());
            }
        }

        if (scheduledActionsMap.get(entity) == null) {
            scheduledActionsMap.put(entity, new LinkedList<ActionData>());
        }
        scheduledActionsMap.get(entity).add(data);
    }
}
