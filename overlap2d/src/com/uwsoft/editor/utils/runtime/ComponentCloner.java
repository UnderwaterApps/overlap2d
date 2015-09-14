/*
 * ******************************************************************************
 *  * Copyright 2015 See AUTHORS file.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

package com.uwsoft.editor.utils.runtime;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;

import com.badlogic.ashley.core.Component;

/**
 * Created by azakhary on 6/3/2015.
 * TODO: not sure if we are going to keep this with reflection, or implement cloning for each component instead
 */
public class ComponentCloner {

    public static <E extends Component> E get(E source) {
        Class<?> eClass = source.getClass();
        E target = null;
        try {
            target = (E) eClass.newInstance();
            Field[] sourceFields = source.getClass().getDeclaredFields();
            Field[] targetFields = target.getClass().getDeclaredFields();
            for(int i = 0; i < targetFields.length; i++) {
                if(Modifier.isPublic(targetFields[i].getModifiers())) {
                    targetFields[i].set(target, sourceFields[i].get(source));
                }
            }
        } catch (InstantiationException e) {
            //e.printStackTrace();
        } catch (IllegalAccessException e) {
            //e.printStackTrace();
        }

        return target;
    }


    public static  <E extends Component> void set(E target, E source) {
        try {
            Field[] sourceFields = source.getClass().getDeclaredFields();
            Field[] targetFields = target.getClass().getDeclaredFields();
            for(int i = 0; i < targetFields.length; i++) {
                if(Modifier.isPublic(targetFields[i].getModifiers())) {
                    targetFields[i].set(target, sourceFields[i].get(source));
                }
            }
        } catch (IllegalAccessException e) {
            //e.printStackTrace();
        }
    }

    public static Collection<Component> cloneAll(Collection<Component> components) {
        Collection<Component> clones = new ArrayList<>();
        for(Component component: components) {
            clones.add(get(component));
        }

        return clones;
    }
}
