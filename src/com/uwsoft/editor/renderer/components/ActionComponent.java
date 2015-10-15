package com.uwsoft.editor.renderer.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Interpolation;
import com.uwsoft.editor.renderer.systems.action.data.ActionData;

/**
 * Created by Eduard on 10/13/2015.
 * Just a dump of fields that can be used in any of Action logic implementations. I know..
 */
public class ActionComponent<T extends ActionData> implements Component {
    public T data;

    public ActionComponent(T data) {
        this.data = data;
    }
}
