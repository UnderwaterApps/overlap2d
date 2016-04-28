package com.uwsoft.editor.view.stage.tools.transformStrategy;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.uwsoft.editor.utils.TransformCommandBuilder;

/**
 * Created by Sasun Poghosyan on 4/13/2016.
 */
public interface ITransformStrategy {
    void calculate(float mouseDx, float mouseDy, int anchor, Entity entity, TransformCommandBuilder transformCommandBuilder, Vector2 mousePoint, float lastTransformAngle, float lastEntityAngle);
}
