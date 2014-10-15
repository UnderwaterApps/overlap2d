package com.uwsoft.editor.gdx.ui.properties;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.uwsoft.editor.gdx.stage.UIStage;
import com.uwsoft.editor.renderer.actor.CheckBoxItem;
import com.uwsoft.editor.renderer.actor.TextBoxItem;
import com.uwsoft.editor.renderer.data.PhysicsPropertiesVO;
import com.uwsoft.editor.renderer.data.SceneVO;

public class PhysicsPropertiesBox extends PropertyBox {

    private final SceneVO currentSceneVO;
    private final PhysicsPropertiesVO physicsPropertiesVO;
    private TextBoxItem gravityValueX;
    private TextBoxItem gravityValueY;

    private TextBoxItem sleepVelocityValue;
    private CheckBoxItem enableCheckbox;

    public PhysicsPropertiesBox(UIStage stage) {
        super(stage.sceneLoader, "PhysicsItemProperties");
        currentSceneVO = stage.sandboxStage.getCurrentSceneVO();
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
        gravityValueX.addListener(new FocusListener() {
            public void keyboardFocusChanged(FocusListener.FocusEvent event,
                                             Actor actor,
                                             boolean focused) {
                if (!focused) {
                    String gravityX = gravityValueX.getText();
                    if(gravityX.isEmpty()){
                    	gravityValueX.setText("0");
                    	physicsPropertiesVO.gravityX = 0;
                    }else{
                    	physicsPropertiesVO.gravityX = Float.parseFloat(gravityX);
                    }
                }
            }
        });

        gravityValueX.addListener(new InputListener() {
            public boolean keyUp(InputEvent event, int keycode) {
                if (keycode == 66) {
                    String gravityX = gravityValueX.getText();
                    if(gravityX.isEmpty()){
                    	gravityValueX.setText("0");
                    	physicsPropertiesVO.gravityX = 0;
                    }else{
                    	physicsPropertiesVO.gravityX = Float.parseFloat(gravityX);
                    }
                }
                return true;
            }
        });

        gravityValueY.addListener(new FocusListener() {
            public void keyboardFocusChanged(FocusListener.FocusEvent event,
                                             Actor actor,
                                             boolean focused) {
                if (!focused) {
                    String gravityY = gravityValueY.getText();
                    
                    if(gravityY.isEmpty()){
                    	gravityValueY.setText("0");
                    	physicsPropertiesVO.gravityY = 0;
                    }else{
                    	physicsPropertiesVO.gravityY = Float.parseFloat(gravityY);
                    }
                }
            }
        });

        gravityValueY.addListener(new InputListener() {
            public boolean keyUp(InputEvent event, int keycode) {
                if (keycode == 66) {
                    String gravityY = gravityValueY.getText();
                    if(gravityY.isEmpty()){
                    	gravityValueY.setText("0");
                    	physicsPropertiesVO.gravityY = 0;
                    }else{
                    	physicsPropertiesVO.gravityY = Float.parseFloat(gravityY);
                    }
                }
                return true;
            }
        });

        sleepVelocityValue.addListener(new FocusListener() {
            public void keyboardFocusChanged(FocusListener.FocusEvent event,
                                             Actor actor,
                                             boolean focused) {
                if (!focused) {
                    String sleepVelocity = sleepVelocityValue.getText();
                    if(sleepVelocity.isEmpty()){
                    	sleepVelocityValue.setText("0");
                    	physicsPropertiesVO.sleepVelocity = 0;
                    }else{
                    	physicsPropertiesVO.sleepVelocity = Float.parseFloat(sleepVelocity);
                    }
                }
            }
        });

        sleepVelocityValue.addListener(new InputListener() {
            public boolean keyUp(InputEvent event, int keycode) {
                if (keycode == 66) {
                    String sleepVelocity = sleepVelocityValue.getText();
                    if(sleepVelocity.isEmpty()){
                    	sleepVelocityValue.setText("0");
                    	physicsPropertiesVO.sleepVelocity = 0;
                    }else{
                    	physicsPropertiesVO.sleepVelocity = Float.parseFloat(sleepVelocity);
                    }
                }
                return true;
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
