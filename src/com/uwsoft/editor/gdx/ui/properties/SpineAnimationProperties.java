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

package com.uwsoft.editor.gdx.ui.properties;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.Animation;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.actor.SelectBoxItem;
import com.uwsoft.editor.renderer.actor.SpineActor;

/**
 * Created by sargis on 8/25/14.
 */
public class SpineAnimationProperties extends PropertyBox implements IPropertyBox<SpineActor> {
    private SelectBoxItem<String> animationSelectbox;

    public SpineAnimationProperties(SceneLoader sceneLoader) {
        super(sceneLoader, "spineAnimationProperties");
    }

    @Override
    public void setObject(SpineActor object) {
        item = object;

        animationSelectbox = ui.getSelectBoxById("animationSelectbox");
        animationSelectbox.setWidth(90);
        Array<String> animations = new Array<>();
        for (Animation animation : object.getAnimations()) {
            animations.add(animation.getName());
        }
        animationSelectbox.setItems(animations);
        animationSelectbox.setSelected(object.getCurrentAnimationName());
        setListeners();
    }


    @Override
    public void updateView() {
        Array<String> animations = new Array<>();
        for (Animation animation : ((SpineActor) item).getAnimations()) {
            animations.add(animation.getName());
        }
        animationSelectbox.setItems(animations);
        animationSelectbox.setSelected(((SpineActor) item).getCurrentAnimationName());
    }

    private void setListeners() {
        animationSelectbox.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ((SpineActor) item).setAnimation(animationSelectbox.getSelected());
                item.renew();
            }
        });
    }
}

