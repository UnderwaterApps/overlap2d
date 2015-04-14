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

package com.uwsoft.editor.mvc.view.ui.box;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.view.stage.UIStage;

public class ProjectGeneralPropertiesBox extends VisTable {

    public static final String PHYSICS_ENABLED_CHECKBOX_CLICKED = "com.uwsoft.editor.mvc.view.ui.box.ProjectGeneralPropertiesBox" + ".PHYSICS_ENABLED_CHECKBOX_CLICKED";
    public static final String GRAVITY_X_TEXT_FIELD_UPDATED = "com.uwsoft.editor.mvc.view.ui.box.ProjectGeneralPropertiesBox" + ".GRAVITY_X_TEXT_FIELD_UPDATED";
    public static final String GRAVITY_Y_TEXT_FIELD_UPDATED = "com.uwsoft.editor.mvc.view.ui.box.ProjectGeneralPropertiesBox" + ".GRAVITY_Y_TEXT_FIELD_UPDATED";
    public static final String SLEEP_VELOCITY_TEXT_FIELD_UPDATED = "com.uwsoft.editor.mvc.view.ui.box.ProjectGeneralPropertiesBox" + ".SLEEP_VELOCITY_TEXT_FIELD_UPDATED";
    public static final String ENABLE_LIGHTS_CHECKBOX_CLICKED = "com.uwsoft.editor.mvc.view.ui.box.ProjectGeneralPropertiesBox" + ".ENABLE_LIGHTS_CHECKBOX_CLICKED";
    public static final String DIFFUSE_CHECKBOX_CLICKED = "com.uwsoft.editor.mvc.view.ui.box.ProjectGeneralPropertiesBox" + ".DIFFUSE_CHECKBOX_CLICKED";
    //
    private final VisCheckBox physicsEnabledCheckBox;
    private final VisTextField gravityXTextField;
    private final VisTextField gravityYTextField;
    private final VisTextField sleepVelocityTextField;
    private final VisCheckBox enableLightsCheckBox;
    private final VisCheckBox diffuseCheckBox;
//    private final SceneVO currentSceneVO;
//    private final PhysicsPropertiesVO physicsPropertiesVO;
//    private TextBoxItem gravityValueX;
//    private TextBoxItem gravityValueY;
//
//    private TextBoxItem sleepVelocityValue;
//    private CheckBoxItem enableCheckbox;

    public ProjectGeneralPropertiesBox() {
//        //super(stage.sceneLoader, "PhysicsItemProperties");
//        currentSceneVO = stage.getSandbox().sceneControl.getCurrentSceneVO();
//        physicsPropertiesVO = currentSceneVO.physicsPropertiesVO;
//        initValues();
        debug();
        pad(15);
        add(new VisLabel("Physics enabled:", Align.right)).padRight(5).width(115).colspan(2);
        physicsEnabledCheckBox = new VisCheckBox(null);
        physicsEnabledCheckBox.addListener(new CheckBoxChangeListener(PHYSICS_ENABLED_CHECKBOX_CLICKED));
        add(physicsEnabledCheckBox).left().colspan(2);
        row().padTop(5);
        add("Gravity X:").padRight(5).width(70);
        gravityXTextField = new VisTextField();
        gravityXTextField.addListener(new KeyboardListener(GRAVITY_X_TEXT_FIELD_UPDATED));
        add(gravityXTextField).width(40).padRight(5);
        add("Gravity Y:").padRight(5).width(70);
        gravityYTextField = new VisTextField();
        gravityYTextField.addListener(new KeyboardListener(GRAVITY_Y_TEXT_FIELD_UPDATED));
        add(gravityYTextField).width(40);
        row().padTop(5);
        add(new VisLabel("Sleep velocity:", Align.right)).padRight(5).width(115).colspan(2);
        sleepVelocityTextField = new VisTextField();
        sleepVelocityTextField.addListener(new KeyboardListener(SLEEP_VELOCITY_TEXT_FIELD_UPDATED));
        add(sleepVelocityTextField).colspan(2).width(115);
        row().padTop(5);
        addSeparator().colspan(4);
        add(new VisLabel("Enable lights:", Align.right)).padRight(5).width(115).colspan(2);
        enableLightsCheckBox = new VisCheckBox(null);
        enableLightsCheckBox.addListener(new CheckBoxChangeListener(ENABLE_LIGHTS_CHECKBOX_CLICKED));
        add(enableLightsCheckBox).left().colspan(2);
        row().padTop(5);
        add(new VisLabel("Diffuse:", Align.right)).padRight(5).width(115).colspan(2);
        diffuseCheckBox = new VisCheckBox(null);
        diffuseCheckBox.addListener(new CheckBoxChangeListener(DIFFUSE_CHECKBOX_CLICKED));
        add(diffuseCheckBox).left().colspan(2);
    }

    private void initValues() {
//        gravityValueX = ui.getTextBoxById("gravityValueX");
//        gravityValueX.setText(String.valueOf(physicsPropertiesVO.gravityX));
//        gravityValueY = ui.getTextBoxById("gravityValueY");
//        gravityValueY.setText(String.valueOf(physicsPropertiesVO.gravityY));
//        sleepVelocityValue = ui.getTextBoxById("sleepVelocityValue");
//        sleepVelocityValue.setText(String.valueOf(physicsPropertiesVO.sleepVelocity));
//        enableCheckbox = ui.getCheckBoxById("enableCheckbox");
//        enableCheckbox.setChecked(physicsPropertiesVO.enabled);
//        setListeners();
    }

    private class CheckBoxChangeListener extends ChangeListener {

        private final String eventName;

        public CheckBoxChangeListener(String eventName) {
            this.eventName = eventName;
        }

        @Override
        public void changed(ChangeEvent changeEvent, Actor actor) {
            Overlap2DFacade facade = Overlap2DFacade.getInstance();
            facade.sendNotification(eventName, ((VisCheckBox) actor).isChecked());
        }
    }

    private class KeyboardListener implements EventListener {

        private final String eventName;

        public KeyboardListener(String eventName) {
            this.eventName = eventName;
        }

        @Override
        public boolean handle(Event event) {
            if (event instanceof FocusListener.FocusEvent) {
                handleFocusListener((FocusListener.FocusEvent) event);
                return true;
            }
            if (event instanceof InputEvent) {
                handleInputListener((InputEvent) event);
                return true;
            }
            return false;
        }

        private void handleInputListener(InputEvent event) {
            switch (event.getType()) {
                case keyUp:
                    if (event.getKeyCode() == Input.Keys.ENTER) {
                        keyboardHandler((VisTextField) event.getTarget());
                    }
                    break;
            }
        }

        private void handleFocusListener(FocusListener.FocusEvent event) {
            switch (event.getType()) {
                case keyboard:
                    keyboardHandler((VisTextField) event.getTarget());
                    break;
                case scroll:
                    break;
            }

        }

        private void keyboardHandler(VisTextField target) {
            Overlap2DFacade facade = Overlap2DFacade.getInstance();
            facade.sendNotification(eventName, target.getText());
        }
    }

//    private void setListeners() {
//        gravityValueX.addListener(new FocusListener() {
//            public void keyboardFocusChanged(FocusListener.FocusEvent event,
//                                             Actor actor,
//                                             boolean focused) {
//                if (!focused) {
//                    String gravityX = gravityValueX.getText();
//                    if(gravityX.isEmpty()){
//                    	gravityValueX.setText("0");
//                    	physicsPropertiesVO.gravityX = 0;
//                    }else{
//                    	physicsPropertiesVO.gravityX = Float.parseFloat(gravityX);
//                    }
//                }
//            }
//        });
//
//        gravityValueX.addListener(new InputListener() {
//            public boolean keyUp(InputEvent event, int keycode) {
//                if (keycode == 66) {
//                    String gravityX = gravityValueX.getText();
//                    if(gravityX.isEmpty()){
//                    	gravityValueX.setText("0");
//                    	physicsPropertiesVO.gravityX = 0;
//                    }else{
//                    	physicsPropertiesVO.gravityX = Float.parseFloat(gravityX);
//                    }
//                }
//                return true;
//            }
//        });
//
//        gravityValueY.addListener(new FocusListener() {
//            public void keyboardFocusChanged(FocusListener.FocusEvent event,
//                                             Actor actor,
//                                             boolean focused) {
//                if (!focused) {
//                    String gravityY = gravityValueY.getText();
//
//                    if(gravityY.isEmpty()){
//                    	gravityValueY.setText("0");
//                    	physicsPropertiesVO.gravityY = 0;
//                    }else{
//                    	physicsPropertiesVO.gravityY = Float.parseFloat(gravityY);
//                    }
//                }
//            }
//        });
//
//        gravityValueY.addListener(new InputListener() {
//            public boolean keyUp(InputEvent event, int keycode) {
//                if (keycode == 66) {
//                    String gravityY = gravityValueY.getText();
//                    if(gravityY.isEmpty()){
//                    	gravityValueY.setText("0");
//                    	physicsPropertiesVO.gravityY = 0;
//                    }else{
//                    	physicsPropertiesVO.gravityY = Float.parseFloat(gravityY);
//                    }
//                }
//                return true;
//            }
//        });
//
//        sleepVelocityValue.addListener(new FocusListener() {
//            public void keyboardFocusChanged(FocusListener.FocusEvent event,
//                                             Actor actor,
//                                             boolean focused) {
//                if (!focused) {
//                    String sleepVelocity = sleepVelocityValue.getText();
//                    if(sleepVelocity.isEmpty()){
//                    	sleepVelocityValue.setText("0");
//                    	physicsPropertiesVO.sleepVelocity = 0;
//                    }else{
//                    	physicsPropertiesVO.sleepVelocity = Float.parseFloat(sleepVelocity);
//                    }
//                }
//            }
//        });
//
//        sleepVelocityValue.addListener(new InputListener() {
//            public boolean keyUp(InputEvent event, int keycode) {
//                if (keycode == 66) {
//                    String sleepVelocity = sleepVelocityValue.getText();
//                    if(sleepVelocity.isEmpty()){
//                    	sleepVelocityValue.setText("0");
//                    	physicsPropertiesVO.sleepVelocity = 0;
//                    }else{
//                    	physicsPropertiesVO.sleepVelocity = Float.parseFloat(sleepVelocity);
//                    }
//                }
//                return true;
//            }
//        });
//
//        enableCheckbox.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                physicsPropertiesVO.enabled = enableCheckbox.isChecked();
//            }
//        });
//    }
}
