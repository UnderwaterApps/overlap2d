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

package com.uwsoft.editor.view.ui.followers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kotcrab.vis.ui.VisUI;

/**
 * Created by azakhary on 5/20/2015.
 */
public class LightFollower extends BasicFollower {

    private Image icon;

    public LightFollower(Entity entity) {
        super(entity);
    }

    @Override
    public void create() {
        icon = new Image(VisUI.getSkin().getDrawable("tool-sphericlight"));
        icon.setTouchable(Touchable.disabled);
        icon.setX(-icon.getWidth() / 2);
        icon.setY(-icon.getHeight() / 2);
        addActor(icon);
    }

    @Override
    public void act(float delta) {
        if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
            setVisible(false);
        } else {
            setVisible(true);
        }
        super.act(delta);
    }

    @Override
    public void hide() {
        // you cannot hide light folower
        icon.setColor(Color.WHITE);
    }

    @Override
    public void show() {
        super.show();
        icon.setColor(Color.ORANGE);
    }
}
