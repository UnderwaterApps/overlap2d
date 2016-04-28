package com.uwsoft.editor.view.stage.tools.transformStrategy;


import com.badlogic.gdx.math.MathUtils;

/**
 * Created by Sasun Poghosyan on 4/14/2016.
 */
public abstract class AbstractTransformStrategy implements ITransformStrategy {


    /**
     * on every anchor drag calculates width/height, x and y amounts.
     * @param mouseDeltaX mouse delta on x axis
     * @param mouseDeltaY mouse delta on y axis
     * @param rotation    entity rotation. If you want to find vertical anchors drag value add 90 to {@link rotation}
     * @return array of three floats (new float[]{width/height, xComponent, yComponent};)
     */
    float[] calculateSizeAndXyAmount(float mouseDeltaX, float mouseDeltaY, float rotation) {
        float mouseDragAngle = MathUtils.atan2(mouseDeltaY, mouseDeltaX) * MathUtils.radDeg;
        float deltaA = rotation - mouseDragAngle;
//        float c = TransformUtils.hypotenuse(mouseDeltaX, mouseDeltaY);
        float c = (float) Math.sqrt(mouseDeltaX * mouseDeltaX + mouseDeltaY * mouseDeltaY);
        float a = c * MathUtils.cosDeg(deltaA);
        float xComponent = a * MathUtils.cosDeg(rotation);
        float yComponent = a * MathUtils.sinDeg(rotation);
        return new float[]{a, xComponent, yComponent};
    }
}
