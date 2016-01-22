package com.uwsoft.editor.renderer.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.uwsoft.editor.renderer.scripts.IScript;

import java.util.Iterator;

/**
 * Created by azakhary on 6/19/2015.
 */
public class ScriptComponent implements Component {

    public Array<IScript> scripts = new Array<IScript>();

    public void addScript(IScript script) {
        scripts.add(script);
    }

    public void addScript(String className) {
        try {
            IScript script = (IScript) ClassReflection.newInstance(ClassReflection.forName(className));
            addScript(script);
        } catch (ReflectionException e) {
            // well, if it's not there, then we don't care
        }
    }

    public void removeScript(Class className) {
        Iterator<IScript> i = scripts.iterator();
        while (i.hasNext()) {
            IScript s = i.next();
            if(s.getClass().getName().equals(className)) {
                i.remove();
            }
        }
    }
}
