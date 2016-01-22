package com.uwsoft.editor.renderer.systems.action;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.uwsoft.editor.renderer.components.ActionComponent;
import com.uwsoft.editor.renderer.systems.action.data.*;
import com.uwsoft.editor.renderer.systems.action.logic.*;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

import java.util.HashMap;

/**
 * Created by Eduard on 10/13/2015.
 */
public class Actions {

    public static HashMap<String, ActionLogic> actionLogicMap = new HashMap<String, ActionLogic>();
    private static boolean initialized;

    private static void initialize() throws InstantiationException, IllegalAccessException {
        registerActionClass(MoveToAction.class);
        registerActionClass(MoveByAction.class);
        registerActionClass(SizeToAction.class);
        registerActionClass(SizeByAction.class);
        registerActionClass(ScaleToAction.class);
        registerActionClass(ScaleByAction.class);
        registerActionClass(RotateToAction.class);
        registerActionClass(RotateByAction.class);
        registerActionClass(ColorAction.class);
        registerActionClass(AlphaAction.class);

        registerActionClass(RunnableAction.class);
        registerActionClass(DelayAction.class);

        registerActionClass(ParallelAction.class);
        registerActionClass(SequenceAction.class);

        initialized = true;
    }

    public static <T extends ActionLogic> void registerActionClass(Class<T> type) throws IllegalAccessException, InstantiationException {
        actionLogicMap.put(type.getName(), type.newInstance());
    }

    private static void checkInit() {
        if (!initialized) try {
            initialize();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
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

    public static SizeToData sizeTo (float width, float height, float duration) {
        return sizeTo(width, height, duration, null);
    }

    public static SizeToData sizeTo (float width, float height, float duration, Interpolation interpolation) {
        SizeToData actionData = new SizeToData(
                interpolation,
                duration,
                width,
                height
        );
        actionData.logicClassName = SizeToAction.class.getName();
        return actionData;
    }

    public static SizeByData sizeBy (float width, float height, float duration) {
        return sizeBy(width, height, duration, null);
    }

    public static SizeByData sizeBy (float width, float height, float duration, Interpolation interpolation) {
        SizeByData actionData = new SizeByData(
                interpolation,
                duration,
                width,
                height
        );
        actionData.logicClassName = SizeByAction.class.getName();
        return actionData;
    }

    public static ScaleToData scaleTo (float width, float height, float duration) {
        return scaleTo(width, height, duration, null);
    }

    public static ScaleToData scaleTo (float width, float height, float duration, Interpolation interpolation) {
        ScaleToData actionData = new ScaleToData(
                interpolation,
                duration,
                width,
                height
        );
        actionData.logicClassName = ScaleToAction.class.getName();
        return actionData;
    }

    public static ScaleByData scaleBy (float width, float height, float duration) {
        return scaleBy(width, height, duration, null);
    }

    public static ScaleByData scaleBy (float width, float height, float duration, Interpolation interpolation) {
        ScaleByData actionData = new ScaleByData(
                interpolation,
                duration,
                width,
                height
        );
        actionData.logicClassName = ScaleByAction.class.getName();
        return actionData;
    }


    public static ColorData color (Color color, float duration) {
        return color(color, duration, null);
    }

    public static ColorData color (Color color, float duration, Interpolation interpolation) {
        ColorData colorData = new ColorData(
                interpolation,
                duration,
                color
        );
        colorData.logicClassName = ColorAction.class.getName();
        return colorData;
    }

    public static AlphaData alpha (float alpha, float duration) {
        return alpha(alpha, duration, null);
    }

    public static AlphaData alpha (float alpha, float duration, Interpolation interpolation) {
        AlphaData alphaData = new AlphaData(
                interpolation,
                duration,
                alpha
        );
        alphaData.logicClassName = AlphaAction.class.getName();
        return  alphaData;
    }

    public static AlphaData fadeIn (float duration) {
        return alpha(1, duration, null);
    }

    public static AlphaData fadeIn (float duration, Interpolation interpolation) {
        return alpha(1, duration, interpolation);
    }


    public static AlphaData fadeOut (float duration) {
        return alpha(0, duration, null);
    }

    public static AlphaData fadeOut (float duration, Interpolation interpolation) {
        return alpha(0, duration, interpolation);
    }

    public static DelayData delay (float duration) {
        DelayData delayData = new DelayData(
                duration
        );
        delayData.logicClassName = DelayAction.class.getName();
        return delayData;
    }

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

        if (actionComponent == null) {
            actionComponent = new ActionComponent();
            entity.add(actionComponent);
        }

        actionComponent.dataArray.add(data);
    }

    public static void removeActions(Entity entity) {
        ActionComponent actionComponent = ComponentRetriever.get(entity, ActionComponent.class);
        if (actionComponent != null){
            entity.remove(ActionComponent.class);
        }
    }

    public static void removeAction(Entity entity, ActionData data) {
        ActionComponent actionComponent = ComponentRetriever.get(entity, ActionComponent.class);
        if (actionComponent != null) {
            if (actionComponent.dataArray.contains(data, true)) {
                actionComponent.dataArray.removeValue(data, true);
            }
        }
    }
}
