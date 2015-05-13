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

package com.uwsoft.editor.mvc.view.ui.properties.panels;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.Validators;
import com.kotcrab.vis.ui.widget.*;
import com.uwsoft.editor.gdx.ui.components.TintButton;
import com.uwsoft.editor.mvc.event.CheckBoxChangeListener;
import com.uwsoft.editor.mvc.event.KeyboardListener;
import com.uwsoft.editor.mvc.view.ui.properties.UIAbstractProperties;
import com.uwsoft.editor.mvc.view.ui.properties.UIItemProperties;

import java.util.HashMap;

/**
 * Created by azakhary on 4/15/2015.
 */
public class UIBasicItemProperties extends UIItemProperties {

    public static final String PREFIX = "com.uwsoft.editor.mvc.view.ui.properties.panels.UIBasicItemProperties";
    public static final String TINT_COLOR_BUTTON_CLICKED = PREFIX + ".TINT_COLOR_BUTTON_CLICKED";
    public static final String CUSTOM_VARS_BUTTON_CLICKED = PREFIX + ".CUSTOM_VARS_BUTTON_CLICKED";

    public enum ItemType {
        group,
        composite,
        texture,
        spriteAnimation,
        spineAnimation,
        spriterAnimation,
        particle,
        text
    }

    private HashMap<ItemType, String> itemTypeIconMap = new HashMap<>();
    private HashMap<ItemType, String> itemTypeNameMap = new HashMap<>();

    private Image itemTypeIcon;
    private VisLabel itemType;

    private VisTextField idBox;

    private VisTextField xValue;
    private VisTextField yValue;
    private VisTextField widthValue;
    private VisTextField heightValue;
    private VisTextField scaleXValue;
    private VisTextField scaleYValue;
    private VisCheckBox flipVertical;
    private VisCheckBox flipHorizontal;
    private TintButton tintColorComponent;
    private VisTextField rotationValue;
    private VisTextButton customVarsButton;

    public UIBasicItemProperties() {
        super();

        initMaps();

        padTop(7);

        Validators.FloatValidator floatValidator = new Validators.FloatValidator();

        itemType = createLabel("");
        itemType.setAlignment(Align.left);
        itemTypeIcon = new Image();

        VisTable iconContainer = new VisTable();
        iconContainer.add(itemTypeIcon).width(22).right();

        idBox = new VisTextField();
        xValue = new VisValidableTextField(floatValidator);
        yValue = new VisValidableTextField(floatValidator);
        widthValue = new VisValidableTextField(floatValidator);
        heightValue = new VisValidableTextField(floatValidator);
        scaleXValue = new VisValidableTextField(floatValidator);
        scaleYValue = new VisValidableTextField(floatValidator);
        flipVertical = new VisCheckBox(null);
        flipHorizontal = new VisCheckBox(null);
        tintColorComponent = new TintButton(29, 21);
        rotationValue = new VisValidableTextField(floatValidator);
        customVarsButton = new VisTextButton("Custom Vars");

        add(iconContainer).padRight(3).right().fillX();
        add(itemType).width(143).height(21).colspan(2).left();
        row().padTop(13);
        add(createLabel("Identifier:")).padRight(3).fillX();
        add(idBox).width(143).height(21).colspan(2);
        row().padTop(13);
        add(createLabel("Position:")).padRight(3).left().top();
        add(getAsTable("X:", xValue, "Y:", yValue)).left();
        add(getAsTable("Width:", widthValue, "Height:", heightValue)).right();
        row().padTop(6);
        add(createLabel("Rotation:")).padRight(3).left();
        add(rotationValue).width(45).height(21).left().padLeft(13);
        add(getTintTable()).width(45).height(21).right().padLeft(13);
        row().padTop(6);
        add(createLabel("Scale:")).padRight(3).left().top();
        add(getAsTable("X:", scaleXValue, "Y:", scaleYValue)).left();
        add(customVarsButton).height(21).left().top().padLeft(13);
        row().padTop(6);

        setListeners();
    }

    private Table getTintTable() {
        VisTable tintTable = new VisTable();
        tintTable.add(createLabel("Tint:")).padRight(3);
        tintTable.add(tintColorComponent);
        return tintTable;
    }

    private Table getAsTable(String text1, Actor actor1, String text2, Actor actor2) {
        VisTable positionTable = new VisTable();
        positionTable.add(createLabel(text1)).right().padRight(3);
        positionTable.add(actor1).width(45).height(21);
        positionTable.row().padTop(4);
        positionTable.add(createLabel(text2)).right().padRight(3);
        positionTable.add(actor2).width(45).height(21).left();
        return positionTable;
    }

    public void setItemType(ItemType type) {
        itemType.setText(itemTypeNameMap.get(type));
        itemTypeIcon.setDrawable(VisUI.getSkin().getDrawable(itemTypeIconMap.get(type)));
        itemTypeIcon.setWidth(22);
    }

    public String getIdBoxValue() {
        return idBox.getText();
    }

    public void setIdBoxValue(String idBox) {
        this.idBox.setText(idBox);
    }

    public String getRotationValue() {
        return rotationValue.getText();
    }

    public void setRotationValue(String rotationValue) {
        this.rotationValue.setText(rotationValue);
    }

    public String getXValue() {
        return xValue.getText();
    }

    public void setXValue(String xValue) {
        this.xValue.setText(xValue);
    }

    public String getYValue() {
        return yValue.getText();
    }

    public void setYValue(String yValue) {
        this.yValue.setText(yValue);
    }

    //private TextButton customVarsBtn;

    public String getWidthValue() {
        return widthValue.getText();
    }

    public void setWidthValue(String widthValue) {
        this.widthValue.setText(widthValue);
    }

    public String getHeightValue() {
        return heightValue.getText();
    }

    public void setHeightValue(String heightValue) {
        this.heightValue.setText(heightValue);
    }

    public String getScaleXValue() {
        return scaleXValue.getText();
    }

    public void setScaleXValue(String scaleXValue) {
        this.scaleXValue.setText(scaleXValue);
    }

    public String getScaleYValue() {
        return scaleYValue.getText();
    }

    public void setScaleYValue(String scaleYValue) {
        this.scaleYValue.setText(scaleYValue);
    }

    public boolean getFlipV() {
        return flipVertical.isChecked();
    }

    public void setFlipV(boolean flipVertical) {
        this.flipVertical.setChecked(flipVertical);
    }

    public boolean getFlipH() {
        return flipHorizontal.isChecked();
    }

    public void setFlipH(boolean flipHorizontal) {
        this.flipHorizontal.setChecked(flipHorizontal);
    }

    public Color getTintColor() {
        return tintColorComponent.getColorValue();
    }

    public void setTintColor(Color tintColor) {
        tintColorComponent.setColorValue(tintColor);
    }

    private void setListeners() {
        idBox.addListener(new KeyboardListener(UIAbstractProperties.PROPERTIES_UPDATED));
        xValue.addListener(new KeyboardListener(UIAbstractProperties.PROPERTIES_UPDATED));
        yValue.addListener(new KeyboardListener(UIAbstractProperties.PROPERTIES_UPDATED));
        widthValue.addListener(new KeyboardListener(UIAbstractProperties.PROPERTIES_UPDATED));
        heightValue.addListener(new KeyboardListener(UIAbstractProperties.PROPERTIES_UPDATED));
        scaleXValue.addListener(new KeyboardListener(UIAbstractProperties.PROPERTIES_UPDATED));
        scaleYValue.addListener(new KeyboardListener(UIAbstractProperties.PROPERTIES_UPDATED));
        flipVertical.addListener(new CheckBoxChangeListener(UIAbstractProperties.PROPERTIES_UPDATED));
        flipHorizontal.addListener(new CheckBoxChangeListener(UIAbstractProperties.PROPERTIES_UPDATED));
        rotationValue.addListener(new KeyboardListener(UIAbstractProperties.PROPERTIES_UPDATED));

        tintColorComponent.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                facade.sendNotification(TINT_COLOR_BUTTON_CLICKED);
            }
        });

        customVarsButton.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                facade.sendNotification(CUSTOM_VARS_BUTTON_CLICKED);
            }
        });
    }

    private void initMaps() {
        itemTypeNameMap.put(ItemType.group, "Multiple Selection");
        itemTypeNameMap.put(ItemType.composite, "Composite item");
        itemTypeNameMap.put(ItemType.particle, "Particle Effect");
        itemTypeNameMap.put(ItemType.text, "Text");
        itemTypeNameMap.put(ItemType.texture, "Texture");
        itemTypeNameMap.put(ItemType.spineAnimation, "Spine animation");
        itemTypeNameMap.put(ItemType.spriteAnimation, "Sprite Animation");
        itemTypeNameMap.put(ItemType.spriterAnimation, "Spriter Animation");

        itemTypeIconMap.put(ItemType.group, "icon-library");
        itemTypeIconMap.put(ItemType.composite, "icon-library");
        itemTypeIconMap.put(ItemType.particle, "icon-library");
        itemTypeIconMap.put(ItemType.text, "icon-library");
        itemTypeIconMap.put(ItemType.texture, "icon-library");
        itemTypeIconMap.put(ItemType.spineAnimation, "icon-library");
        itemTypeIconMap.put(ItemType.spriteAnimation, "icon-library");
        itemTypeIconMap.put(ItemType.spriterAnimation, "icon-library");
    }
}
