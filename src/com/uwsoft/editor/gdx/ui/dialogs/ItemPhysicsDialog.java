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

package com.uwsoft.editor.gdx.ui.dialogs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.uwsoft.editor.gdx.actors.basic.PixelRect;
import com.uwsoft.editor.gdx.ui.components.ItemPhysicsEditor;
import com.uwsoft.editor.gdx.ui.components.ItemPhysicsEditor.EditMode;
import com.uwsoft.editor.mvc.view.stage.UIStage;
import com.uwsoft.editor.renderer.actor.*;
import com.uwsoft.editor.renderer.data.*;
import com.uwsoft.editor.utils.poly.Clipper.Polygonizer;

public class ItemPhysicsDialog extends SimpleDialog {

    private Group controlPanel;
    private Group propertyPanel;
    private ItemPhysicsEditor itemPhysicsEditor;
    private TextBoxItem massVal;
    private TextBoxItem centerOfMassXVal;
    private TextBoxItem centerOfMassYVal;
    private TextBoxItem rotationalInertiaVal;
    private TextBoxItem dampingVal;
    private TextBoxItem gravityVal;
    private CheckBoxItem allowSleepVal;
    private CheckBoxItem awakeVal;
    private CheckBoxItem bulletVal;
    private SelectBoxItem<Polygonizer> polygonizerVal;
    private SelectBoxItem<String> bodyTypeVal;
    private TextBoxItem hullToleranceVal;
    private TextBoxItem alphaToleranceVal;
    private CheckBoxItem multiPartDetectionVal;
    private CheckBoxItem holeDetectionVal;
    private float dialogStartPositionX;
    private float dialogStartPositionY;
    private TextButtonItem btnSave;
    private TextButtonItem btnRetrace;
    private TextBoxItem densityVal;
    private TextBoxItem frictionVal;
    private TextBoxItem restitutionVal;

    private TextButtonItem dupliacteBtn;
    private TextButtonItem clearBtn;
    private TextButtonItem btnEdit;
    private TextButtonItem btnTest;

    public ItemPhysicsDialog(UIStage s) {
        super(s, Gdx.graphics.getWidth() * .8f, Gdx.graphics.getHeight() * .8f);
        setTitle("Item Physics Editor");


        setX((stage.getWidth() - getWidth()) / 2);
        setY((stage.getHeight() - getHeight()) / 2);
        dialogStartPositionX = getX();
        dialogStartPositionY = getY();
        buildUI();
        updateValues();
    }

    private void buildUI() {

        mainLayer.setWidth(getWidth() - 2);
        mainLayer.setHeight(getHeight() - topImg.getHeight());
        buildControlPanel();
        buildPropertyPanel();

        mainLayer.addActor(controlPanel);

        PixelRect pixelRect = new PixelRect(mainLayer.getWidth() - propertyPanel.getWidth() - 12, mainLayer.getHeight() - controlPanel.getHeight() - 27);
        pixelRect.setFillColor(new Color(0, 0, 0, 0.4f));
        pixelRect.setBorderColor(new Color(0, 0, 0, 0.7f));
        mainLayer.addActor(pixelRect);

        itemPhysicsEditor = new ItemPhysicsEditor(stage, pixelRect.getWidth(), pixelRect.getHeight());
        itemPhysicsEditor.setX(controlPanel.getX());
        itemPhysicsEditor.setY(10);
        mainLayer.addActor(itemPhysicsEditor);

        pixelRect.setX(itemPhysicsEditor.getX());
        pixelRect.setY(itemPhysicsEditor.getY());
    }

    @SuppressWarnings("unchecked")
    private void buildPropertyPanel() {
        propertyPanel = new Group();

        CompositeItem compositeItem = null;//stage.sceneLoader.getCompositeElementById("physicsProperties");
        compositeItem.setX(6);
        compositeItem.setY(getHeight() - compositeItem.getHeight() - 25);
        //item property
        mainLayer.addActor(propertyPanel);

        propertyPanel.addActor(compositeItem);
        propertyPanel.setWidth(compositeItem.getWidth() + 10);
        propertyPanel.setHeight(compositeItem.getHeight());
        massVal = compositeItem.getTextBoxById("massVal");


        compositeItem.getLabelById("meshNoticeLbl").setText("clear does not clone mesh");

        centerOfMassXVal = compositeItem.getTextBoxById("centerOfMassXVal");

        centerOfMassYVal = compositeItem.getTextBoxById("centerOfMassYVal");

        rotationalInertiaVal = compositeItem.getTextBoxById("rotationalInertiaVal");

        dampingVal = compositeItem.getTextBoxById("dampingVal");

        gravityVal = compositeItem.getTextBoxById("gravityScaleVal");

        clearBtn = compositeItem.getTextButtonById("clearBtn");
        dupliacteBtn = compositeItem.getTextButtonById("dupliacteBtn");


        //
        densityVal = compositeItem.getTextBoxById("densityVal");


        frictionVal = compositeItem.getTextBoxById("frictionVal");


        restitutionVal = compositeItem.getTextBoxById("restitutionVal");


        allowSleepVal = compositeItem.getCheckBoxById("allowSleepVal");

        awakeVal = compositeItem.getCheckBoxById("awakeVal");

        bulletVal = compositeItem.getCheckBoxById("bulletVal");


        btnSave = compositeItem.getTextButtonById("btnSave");
        //
        bodyTypeVal = compositeItem.getSelectBoxById("bodyTypeVal");
        bodyTypeVal.setWidth(100);
        bodyTypeVal.setItems("STATIC", "KINEMATIC", "DYNAMIC");


        polygonizerVal = compositeItem.getSelectBoxById("polygonizerVal");
        polygonizerVal.setWidth(100);
        polygonizerVal.setItems(Polygonizer.BAYAZIT, Polygonizer.EWJORDAN);


        hullToleranceVal = compositeItem.getTextBoxById("hullToleranceVal");

        alphaToleranceVal = compositeItem.getTextBoxById("alphaToleranceVal");

        multiPartDetectionVal = compositeItem.getCheckBoxById("multiPartDetectionVal");

        holeDetectionVal = compositeItem.getCheckBoxById("holeDetectionVal");

        btnRetrace = compositeItem.getTextButtonById("btnRetrace");
        setListeners();

    }


    public void updateValues() {

        bodyTypeVal.setSelectedIndex(itemPhysicsEditor.physicsBodyDataVO.bodyType);

        massVal.setText(String.valueOf(itemPhysicsEditor.physicsBodyDataVO.mass));
        centerOfMassXVal.setText(String.valueOf(itemPhysicsEditor.physicsBodyDataVO.centerOfMass.x));
        centerOfMassYVal.setText(String.valueOf(itemPhysicsEditor.physicsBodyDataVO.centerOfMass.y));
        rotationalInertiaVal.setText(String.valueOf(itemPhysicsEditor.physicsBodyDataVO.rotationalInertia));
        dampingVal.setText(String.valueOf(itemPhysicsEditor.physicsBodyDataVO.damping));
        gravityVal.setText(String.valueOf(itemPhysicsEditor.physicsBodyDataVO.gravityScale));
        densityVal.setText(String.valueOf(itemPhysicsEditor.physicsBodyDataVO.density));
        frictionVal.setText(String.valueOf(itemPhysicsEditor.physicsBodyDataVO.friction));
        restitutionVal.setText(String.valueOf(itemPhysicsEditor.physicsBodyDataVO.restitution));
        allowSleepVal.setChecked(itemPhysicsEditor.physicsBodyDataVO.allowSleep);
        awakeVal.setChecked(itemPhysicsEditor.physicsBodyDataVO.awake);
        bulletVal.setChecked(itemPhysicsEditor.physicsBodyDataVO.bullet);
        polygonizerVal.setSelectedIndex(0);
        hullToleranceVal.setText(String.valueOf(2.5f));
        alphaToleranceVal.setText(String.valueOf(128));
        multiPartDetectionVal.setChecked(false);
        holeDetectionVal.setChecked(false);
    }

    private void dataCollect() {

        if (bodyTypeVal.getSelected().equals("STATIC")) {
            itemPhysicsEditor.physicsBodyDataVO.bodyType = BodyDef.BodyType.StaticBody.getValue();
        } else if (bodyTypeVal.getSelected().equals("KINEMATIC")) {
            itemPhysicsEditor.physicsBodyDataVO.bodyType = BodyDef.BodyType.KinematicBody.getValue();
        } else {
            itemPhysicsEditor.physicsBodyDataVO.bodyType = BodyDef.BodyType.DynamicBody.getValue();
        }

        itemPhysicsEditor.physicsBodyDataVO.mass = Float.parseFloat(massVal.getText());
        itemPhysicsEditor.physicsBodyDataVO.centerOfMass.x = Float.parseFloat(centerOfMassXVal.getText());
        itemPhysicsEditor.physicsBodyDataVO.centerOfMass.y = Float.parseFloat(centerOfMassYVal.getText());
        itemPhysicsEditor.physicsBodyDataVO.rotationalInertia = Float.parseFloat(rotationalInertiaVal.getText());
        itemPhysicsEditor.physicsBodyDataVO.damping = Float.parseFloat(dampingVal.getText());
        itemPhysicsEditor.physicsBodyDataVO.gravityScale = Float.parseFloat(gravityVal.getText());
        itemPhysicsEditor.physicsBodyDataVO.density = Float.parseFloat(densityVal.getText());
        itemPhysicsEditor.physicsBodyDataVO.friction = Float.parseFloat(frictionVal.getText());
        itemPhysicsEditor.physicsBodyDataVO.restitution = Float.parseFloat(restitutionVal.getText());
        itemPhysicsEditor.physicsBodyDataVO.allowSleep = !itemPhysicsEditor.physicsBodyDataVO.allowSleep;
        itemPhysicsEditor.physicsBodyDataVO.awake = !itemPhysicsEditor.physicsBodyDataVO.awake;
        itemPhysicsEditor.physicsBodyDataVO.bullet = !itemPhysicsEditor.physicsBodyDataVO.bullet;
    }

    private void setListeners() {

        btnSave.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                dataCollect();
                itemPhysicsEditor.save();
                super.clicked(event, x, y);
            }
        });

        btnRetrace.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                itemPhysicsEditor.reTrace();
                super.clicked(event, x, y);
            }
        });

        clearBtn.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                itemPhysicsEditor.clearMesh();
                super.clicked(event, x, y);
            }
        });

        dupliacteBtn.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                itemPhysicsEditor.duplicateMesh();
                super.clicked(event, x, y);
            }
        });

        btnTest.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (itemPhysicsEditor.currentMode == EditMode.Test || !btnEdit.isChecked()) {
                    btnTest.setChecked(true);
                    return;
                }
                btnEdit.setChecked(false);
                itemPhysicsEditor.startTest();
            }
        });

        btnEdit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!btnTest.isChecked()) {
                    btnEdit.setChecked(true);
                    return;
                }
                btnTest.setChecked(false);
                itemPhysicsEditor.endTest();
            }
        });

    }

    private void buildControlPanel() {
        controlPanel = new Group();
        CompositeItem compositeItem = null;//stage.sceneLoader.getCompositeElementById("physicsToolbar");
        btnEdit = compositeItem.getTextButtonById("btnEdit");
        btnTest = compositeItem.getTextButtonById("btnTest");
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle(btnTest.getStyle());
        textButtonStyle.checked = textButtonStyle.down;
        btnEdit.setStyle(textButtonStyle);
        btnTest.setStyle(textButtonStyle);
        btnEdit.setChecked(true);
        compositeItem.setX(0);
        compositeItem.setY(0);
        controlPanel.addActor(compositeItem);
        controlPanel.setWidth(compositeItem.getWidth());
        controlPanel.setHeight(compositeItem.getHeight());

        controlPanel.setX(220);
        controlPanel.setY(getHeight() - controlPanel.getHeight() - 30);
    }

//    private Button crateButton(String img) {
//        TextButton.TextButtonStyle btnStl = new TextButton.TextButtonStyle();
////        btnStl.up = new TextureRegionDrawable(stage.textureManager.getEditorAsset(img));
////        btnStl.down = new TextureRegionDrawable(stage.textureManager.getEditorAsset(img + "Checked"));
////        btnStl.checked = new TextureRegionDrawable(stage.textureManager.getEditorAsset(img + "Checked"));
//        return new TextButton("qaq", btnStl);
//    }

    @Override
    public void act(float delta) {
        float diffX = dialogStartPositionX - getX();
        float diffY = dialogStartPositionY - getY();
        if (diffX != 0 || diffY != 0) {
            itemPhysicsEditor.moveRenderer(diffX, diffY);
            dialogStartPositionX = getX();
            dialogStartPositionY = getY();
        }
        super.act(delta);
    }

    public void editAsset(String assetName) {
        itemPhysicsEditor.editAsset(assetName);
        updateValues();

        dupliacteBtn.setVisible(false);
        dupliacteBtn.setTouchable(Touchable.disabled);
    }

    public void editItem(IBaseItem item) {
        MainItemVO data = item.getDataVO();
        String className = data.getClass().getSimpleName();

        itemPhysicsEditor.originalItem = item;

        dupliacteBtn.setVisible(true);
        dupliacteBtn.setTouchable(Touchable.enabled);

        IBaseItem itemCopy = null;
        Essentials essentials = stage.sandboxStage.essentials;

        if (className.equals("SimpleImageVO")) {
            itemCopy = new ImageItem((SimpleImageVO) data, essentials);
        }
        if (className.equals("Image9patchVO")) {
            return;
        }
        if (className.equals("TextBoxVO")) {
            return;
        }
        if (className.equals("ButtonVO")) {
            return;
        }
        if (className.equals("LabelVO")) {
            return;
        }
        if (className.equals("CompositeItemVO")) {
            itemCopy = new CompositeItem((CompositeItemVO) data, stage.sandboxStage.essentials);
        }
        if (className.equals("CheckBoxVO")) {
            return;
        }
        if (className.equals("SelectBoxVO")) {
            return;
        }
        if (className.equals("ParticleEffectVO")) {
            itemCopy = new ParticleItem((ParticleEffectVO) data, essentials);
        }
        if (className.equals("LightVO")) {
            itemCopy = new ParticleItem((ParticleEffectVO) data, essentials);
        }
        if (className.equals("SpineVO")) {
            itemCopy = new SpineActor((SpineVO) data, stage.sandboxStage.essentials);
        }
        if (className.equals("SpriteAnimationVO")) {
            itemCopy = new SpriteAnimation((SpriteAnimationVO) data, stage.sandboxStage.essentials);
        }

        itemPhysicsEditor.editItem(itemCopy);
        updateValues();
    }

}
