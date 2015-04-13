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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.uwsoft.editor.gdx.stage.UIStage;
import com.uwsoft.editor.gdx.ui.UIWidgetPreparer;
import com.uwsoft.editor.renderer.actor.CheckBoxItem;
import com.uwsoft.editor.renderer.actor.TextBoxItem;
import com.uwsoft.editor.renderer.data.PhysicsPropertiesVO;
import com.uwsoft.editor.renderer.data.SceneVO;

import java.util.function.Consumer;

import static com.uwsoft.editor.gdx.ui.UIWidgetPreparer.textFieldBroker;

public class PhysicsPropertiesBox extends PropertyBox {

    private final SceneVO currentSceneVO;
    private final PhysicsPropertiesVO physicsPropertiesVO;
    private TextBoxItem gravityValueX;
    private TextBoxItem gravityValueY;

    private TextBoxItem sleepVelocityValue;
    private CheckBoxItem enableCheckbox;

    public PhysicsPropertiesBox(UIStage stage) {
        super(stage.sceneLoader, "PhysicsItemProperties");
        currentSceneVO = stage.getSandbox().sceneControl.getCurrentSceneVO();
        physicsPropertiesVO = currentSceneVO.physicsPropertiesVO;
        initValues();
    }

    private void initValues() {
        gravityValueX = ui.getTextBoxById("gravityValueX");
        gravityValueX.setText(String.valueOf(physicsPropertiesVO.gravityX));
        gravityValueY = ui.getTextBoxById("gravityValueY");
        gravityValueY.setText(String.valueOf(physicsPropertiesVO.gravityY));
        sleepVelocityValue = ui.getTextBoxById("sleepVelocityValue");
        sleepVelocityValue.setText(String.valueOf(physicsPropertiesVO.sleepVelocity));
        enableCheckbox = ui.getCheckBoxById("enableCheckbox");
        enableCheckbox.setChecked(physicsPropertiesVO.enabled);
        setListeners();
    }

    private void setListeners() {

        textFieldBroker(gravityValueX, true, gravityX -> {
            if (gravityX.isEmpty()) {
                gravityValueX.setText("0");
                physicsPropertiesVO.gravityX = 0;
            } else {
                physicsPropertiesVO.gravityX = Float.parseFloat(gravityX);
            }
        });

        textFieldBroker(gravityValueY, true, gravityY -> {
            if (gravityY.isEmpty()) {
                gravityValueY.setText("0");
                physicsPropertiesVO.gravityY = 0;
            } else {
                physicsPropertiesVO.gravityY = Float.parseFloat(gravityY);
            }
        });

        textFieldBroker(sleepVelocityValue, true, sleepVelocity -> {
            if(sleepVelocity.isEmpty()){
                sleepVelocityValue.setText("0");
                physicsPropertiesVO.sleepVelocity = 0;
            }else{
                physicsPropertiesVO.sleepVelocity = Float.parseFloat(sleepVelocity);
            }
        });

        enableCheckbox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                physicsPropertiesVO.enabled = enableCheckbox.isChecked();
            }
        });
    }
}
