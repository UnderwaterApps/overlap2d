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
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.actor.CheckBoxItem;
import com.uwsoft.editor.renderer.actor.CompositeItem;
import com.uwsoft.editor.renderer.actor.LightActor;
import com.uwsoft.editor.renderer.actor.SelectBoxItem;
import com.uwsoft.editor.renderer.actor.TextBoxItem;
import com.uwsoft.editor.renderer.data.LightVO;

/**
 * Created by azakhary on 7/23/2014.
 */
public class LightItemProperties extends PropertyBox implements IPropertyBox<LightActor>  {

    LightActor item;

    CheckBoxItem isXRay;
    CheckBoxItem isStatic;
    SelectBoxItem lightType;

    TextBoxItem radiusVal;
    TextBoxItem coneAngleVal;
    TextBoxItem distanceVal;
    TextBoxItem rays;

    public LightItemProperties(SceneLoader sceneLoader) {
        super(sceneLoader, "LightItemProperties");
    }

    @Override
    public void setObject(LightActor object) {
        item = object;

        CompositeItem coneBox = ui.getCompositeById("coneGroup");
        CompositeItem pointBox = ui.getCompositeById("pointGroup");

        coneBox.setY(pointBox.getY()+pointBox.getHeight() - coneBox.getHeight());
        coneBox.setVisible(false); coneBox.setTouchable(Touchable.disabled);
        pointBox.setVisible(false); pointBox.setTouchable(Touchable.disabled);

        lightType = ui.getSelectBoxById("lightType");
        lightType.setWidth(100);
        lightType.setItems("Point Light", "Cone Light");

        if(object.getDataVO().type == LightVO.LightType.CONE) {
            coneBox.setVisible(true);  coneBox.setTouchable(Touchable.enabled);
            lightType.setSelectedIndex(1);
        }
        if(object.getDataVO().type == LightVO.LightType.POINT) {
            pointBox.setVisible(true);  pointBox.setTouchable(Touchable.enabled);
            lightType.setSelectedIndex(0);
        }

        rays = ui.getTextBoxById("rays");
        rays.setText(roundTwoDecimals(object.getDataVO().rays)+"");
        
        radiusVal = pointBox.getTextBoxById("radiusVal");
        coneAngleVal = coneBox.getTextBoxById("angleVal");
        distanceVal = coneBox.getTextBoxById("distanceVal");

        radiusVal.setText(roundTwoDecimals(object.getDataVO().distance)+"");
        coneAngleVal.setText(roundTwoDecimals(object.getDataVO().coneDegree)+"");
        distanceVal.setText(roundTwoDecimals(object.getDataVO().distance)+"");

        isStatic = ui.getCheckBoxById("isStatic");
        isStatic.setChecked(object.getDataVO().isStatic);
        
        isXRay = ui.getCheckBoxById("isXRay");
        isXRay.setChecked(object.getDataVO().isXRay);

        setListeners();
    }


    @Override
    public void updateView() {

    }

    private void setListeners() {
    	rays.addListener(new ClickListener() {
            public boolean keyUp(InputEvent event, int keycode) {
                if (keycode == 66) {
                    // set item id to
                    int raysNum = (int) Float.parseFloat(rays.getText());
                    if(raysNum<4){
                    	raysNum = 4;
                    }
                    if (item != null) {
                        item.getDataVO().rays = raysNum;
                        item.renew();
                    }
                }
                return true;
            }
        });
    	
        radiusVal.addListener(new ClickListener() {
            public boolean keyUp(InputEvent event, int keycode) {
                if (keycode == 66) {
                    // set item id to
                    int pointRadius = (int) Float.parseFloat(radiusVal.getText());
                    if (item != null) {
                        item.getDataVO().distance = pointRadius;
                        item.renew();
                    }
                }
                return true;
            }
        });

        coneAngleVal.addListener(new ClickListener() {
            public boolean keyUp(InputEvent event, int keycode) {
                if (keycode == 66) {
                    // set item id to
                    float coneAngle = Float.parseFloat(coneAngleVal.getText());
                    if (item != null) {
                        item.getDataVO().coneDegree = coneAngle;
                        item.renew();
                    }
                }
                return true;
            }
        });

        distanceVal.addListener(new ClickListener() {
            public boolean keyUp(InputEvent event, int keycode) {
                if (keycode == 66) {
                    // set item id to
                    float coneDistance = Float.parseFloat(distanceVal.getText());
                    if (item != null) {
                        item.getDataVO().distance = coneDistance;
                        item.renew();
                    }
                }
                return true;
            }
        });

        lightType.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                if(lightType.getSelectedIndex() == 0) {
                    item.getDataVO().type = LightVO.LightType.POINT;
                }
                if(lightType.getSelectedIndex() == 1) {
                    item.getDataVO().type = LightVO.LightType.CONE;
                }
                item.renew();
                setObject(item);
            }
        });

        isStatic.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                item.getDataVO().isStatic = isStatic.isChecked();
                item.renew();
            }
        });
        
        isXRay.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                item.getDataVO().isXRay = isXRay.isChecked();
                item.renew();
            }
        });
    }
}
