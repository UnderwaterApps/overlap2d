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

