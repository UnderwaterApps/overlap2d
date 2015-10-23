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

package com.uwsoft.editor.view.ui.properties.panels;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.Validators;
import com.kotcrab.vis.ui.widget.*;
import com.uwsoft.editor.event.ButtonToNotificationListener;
import com.uwsoft.editor.view.ui.widget.components.TintButton;
import com.uwsoft.editor.event.CheckBoxChangeListener;
import com.uwsoft.editor.event.KeyboardListener;
import com.uwsoft.editor.view.ui.properties.UIItemProperties;
import com.uwsoft.editor.utils.StandardWidgetsFactory;

/**
 * Created by azakhary on 4/15/2015.
 */
public class UIBasicItemProperties extends UIItemProperties {

    public static final String prefix = "com.uwsoft.editor.view.ui.properties.panels.UIBasicItemProperties";
    public static final String TINT_COLOR_BUTTON_CLICKED = prefix + ".TINT_COLOR_BUTTON_CLICKED";
    public static final String CUSTOM_VARS_BUTTON_CLICKED = prefix + ".CUSTOM_VARS_BUTTON_CLICKED";
    public static final String TAGS_BUTTON_CLICKED = prefix + ".TAGS_BUTTON_CLICKED";
    public static final String ADD_COMPONENT_BUTTON_CLICKED = prefix + "ADD_COMPONENT_BUTTON_CLICKED";
    public static final String LINKING_CHANGED = prefix + ".LINKING_CHANGED";

    public enum ItemType {
        multiple,
        composite,
        texture,
        spriteAnimation,
        spineAnimation,
        spriterAnimation,
        particle,
        text,
        light,
        patchImage,
        primitive
    }

    private HashMap<ItemType, String> itemTypeIconMap = new HashMap<>();
    private HashMap<ItemType, String> itemTypeNameMap = new HashMap<>();

    private Image itemTypeIcon;
    private VisLabel itemType;

    private VisImageButton linkImage;
    private VisLabel libraryLinkLabel;

    private VisTable linkageContainer;

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
    private VisTextButton tagsButton;

    private VisSelectBox<String> nonExistantComponents;
    private VisTextButton addComponentButton;

    public UIBasicItemProperties() {
        super();

        initMaps();

        Validators.FloatValidator floatValidator = new Validators.FloatValidator();

        itemType = createLabel("");
        itemType.setAlignment(Align.left);
        itemTypeIcon = new Image();

        libraryLinkLabel = createLabel("");
        libraryLinkLabel.setAlignment(Align.left);
        linkImage = new VisImageButton("library-link-button");
        linkImage.setWidth(22);

        VisTable iconContainer = new VisTable();
        iconContainer.add(itemTypeIcon).width(22).right();

        linkageContainer = new VisTable();
        linkageContainer.setVisible(false);
        linkageContainer.add(linkImage).width(22);
        linkageContainer.add(libraryLinkLabel);
        linkageContainer.row();

        idBox = StandardWidgetsFactory.createTextField();
        xValue = StandardWidgetsFactory.createValidableTextField(floatValidator);
        yValue = StandardWidgetsFactory.createValidableTextField(floatValidator);
        widthValue = StandardWidgetsFactory.createValidableTextField(floatValidator);
        heightValue = StandardWidgetsFactory.createValidableTextField(floatValidator);
        scaleXValue = StandardWidgetsFactory.createValidableTextField(floatValidator);
        scaleYValue = StandardWidgetsFactory.createValidableTextField(floatValidator);
        flipVertical = new VisCheckBox(null);
        flipHorizontal = new VisCheckBox(null);
        tintColorComponent = new TintButton(29, 21);
        rotationValue = StandardWidgetsFactory.createValidableTextField(floatValidator);
        customVarsButton = new VisTextButton("Custom Vars");
        tagsButton = new VisTextButton("Tags");

        nonExistantComponents = new VisSelectBox<>();
        addComponentButton = new VisTextButton("add");

        VisTable componentsTable = new VisTable();
        componentsTable.add(nonExistantComponents).left().width(150).padRight(10);
        componentsTable.add(addComponentButton).right().height(21);
        componentsTable.row();

        add(iconContainer).padRight(3).right().fillX();
        add(itemType).width(143).height(21).colspan(2).left();
        row();
        addSeparator().padTop(9).padBottom(6).colspan(3);
        add(createLabel("Identifier:", Align.left)).fillX();
        add(idBox).width(151).right().height(21).colspan(2);
        row();
        add(linkageContainer).colspan(3).right();
        row().padTop(2);
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
        VisTable buttonsTable = new VisTable();
        buttonsTable.add(customVarsButton);
        buttonsTable.row();
        buttonsTable.add(tagsButton).right().padTop(2);
        add(buttonsTable).height(45).left().top().padLeft(13);
        row();
        addSeparator().padTop(9).padBottom(6).colspan(3);
        add(createLabel("Add additional components:", Align.left)).fillX().colspan(3);
        row().padTop(6);
        add(componentsTable).left().colspan(3);
        row();

        setListeners();
    }

    public void setNonExistantComponents(Array<String> componentNames) {
        nonExistantComponents.setItems(componentNames);
    }

    public String getSelectedComponent() {
        return nonExistantComponents.getSelected();
    }

    public void setLinkage(boolean isLinked, String text) {
        linkageContainer.setVisible(true);
        linkImage.setChecked(isLinked);
        libraryLinkLabel.setText(text);
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

    public void setItemType(ItemType type, int itemUniqueId) {
        itemType.setText(itemTypeNameMap.get(type) + " ("+itemUniqueId+")");
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

    @Override
    public String getPrefix() {
        return prefix;
    }

    private void setListeners() {
        idBox.addListener(new KeyboardListener(getUpdateEventName()));
        xValue.addListener(new KeyboardListener(getUpdateEventName()));
        yValue.addListener(new KeyboardListener(getUpdateEventName()));
        widthValue.addListener(new KeyboardListener(getUpdateEventName()));
        heightValue.addListener(new KeyboardListener(getUpdateEventName()));
        scaleXValue.addListener(new KeyboardListener(getUpdateEventName()));
        scaleYValue.addListener(new KeyboardListener(getUpdateEventName()));
        flipVertical.addListener(new CheckBoxChangeListener(getUpdateEventName()));
        flipHorizontal.addListener(new CheckBoxChangeListener(getUpdateEventName()));
        rotationValue.addListener(new KeyboardListener(getUpdateEventName()));

        tintColorComponent.addListener(new ButtonToNotificationListener(TINT_COLOR_BUTTON_CLICKED));
        customVarsButton.addListener(new ButtonToNotificationListener(CUSTOM_VARS_BUTTON_CLICKED));
        tagsButton.addListener(new ButtonToNotificationListener(TAGS_BUTTON_CLICKED));
        addComponentButton.addListener(new ButtonToNotificationListener(ADD_COMPONENT_BUTTON_CLICKED));

        linkImage.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                boolean isLinked = linkImage.isChecked();
                facade.sendNotification(LINKING_CHANGED, isLinked);
            }
        });
    }

    private void initMaps() {
        itemTypeNameMap.put(ItemType.multiple, "Multiple Selection");
        itemTypeNameMap.put(ItemType.composite, "Composite item");
        itemTypeNameMap.put(ItemType.particle, "Particle Effect");
        itemTypeNameMap.put(ItemType.text, "Text");
        itemTypeNameMap.put(ItemType.texture, "Texture");
        itemTypeNameMap.put(ItemType.patchImage, "9Patch");
        itemTypeNameMap.put(ItemType.light, "Light");
        itemTypeNameMap.put(ItemType.spineAnimation, "Spine animation");
        itemTypeNameMap.put(ItemType.spriteAnimation, "Sprite Animation");
        itemTypeNameMap.put(ItemType.spriterAnimation, "Spriter Animation");
        itemTypeNameMap.put(ItemType.primitive, "Primitive");

        itemTypeIconMap.put(ItemType.multiple, "icon-multiple");
        itemTypeIconMap.put(ItemType.composite, "icon-composite");
        itemTypeIconMap.put(ItemType.particle, "icon-particle-white");
        itemTypeIconMap.put(ItemType.text, "icon-label");
        itemTypeIconMap.put(ItemType.texture, "icon-image");
        itemTypeIconMap.put(ItemType.patchImage, "icon-image");
        itemTypeIconMap.put(ItemType.light, "icon-particle-white");
        itemTypeIconMap.put(ItemType.spineAnimation, "icon-spine");
        itemTypeIconMap.put(ItemType.spriteAnimation, "icon-animation");
        itemTypeIconMap.put(ItemType.spriterAnimation, "icon-animation");
        itemTypeIconMap.put(ItemType.primitive, "icon-image");
    }
}
