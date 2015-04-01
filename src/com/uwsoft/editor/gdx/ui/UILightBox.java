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

package com.uwsoft.editor.gdx.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.kotcrab.vis.ui.widget.color.ColorPicker;
import com.kotcrab.vis.ui.widget.color.ColorPickerAdapter;
import com.uwsoft.editor.gdx.stage.UIStage;
import com.uwsoft.editor.gdx.ui.components.ColorPickerButton;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.DataManager;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.actor.CheckBoxItem;
import com.uwsoft.editor.renderer.actor.CompositeItem;
import com.uwsoft.editor.renderer.actor.ImageItem;
import com.uwsoft.editor.renderer.data.LightVO;
import com.uwsoft.editor.renderer.data.LightVO.LightType;

public class UILightBox extends ExpandableUIBox {

    private final Overlap2DFacade facade;
    private final DataManager dataManager;
    public CheckBoxItem disableAmbiance;
    private ColorPickerButton cPicker;
    private ColorPickerButton cPickerElems;
    private CheckBoxItem disableLights;

    public UILightBox(UIStage s) {
        super(s, 160, 300);
        facade = Overlap2DFacade.getInstance();
        dataManager = facade.retrieveProxy(DataManager.NAME);
    }

    public void initContent() {
        SceneLoader sceneLoader = stage.sceneLoader;

        CompositeItem ui = sceneLoader.getCompositeElementById("lightBox");
        ui.setX(7);
        ui.setY(getHeight() - ui.getHeight() - 14);
        mainLayer.addActor(ui);

        cPicker = new ColorPickerButton();
        cPicker.setX(100);
        cPicker.setY(getHeight() - 92);
        mainLayer.addActor(cPicker);

        float[] ambientColor = stage.getSandbox().sceneControl.getCurrentSceneVO().ambientColor;
        cPicker.setColorValue(new Color(ambientColor[0], ambientColor[1], ambientColor[2], ambientColor[3]));

        //System.out.println("ambient " +cPicker.getColorValue().toString());

        ImageItem bulb = ui.getImageById("bulbBtn");
        ImageItem cone = ui.getImageById("coneBtn");


        cPickerElems = new ColorPickerButton();
        cPickerElems.setX(85);
        cPickerElems.setY(getHeight() - 196);
        mainLayer.addActor(cPickerElems);

        //
        // Image coneThumb = new Image(TextureManager.getInstance().getEditorAsset("cone"));

        cPicker.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                ColorPicker picker = new ColorPicker(new ColorPickerAdapter() {
                    @Override
                    public void finished (Color newColor) {
                        cPicker.setColorValue(newColor);

                        // change scene ambient light
                        stage.getSandbox().setSceneAmbientColor(newColor);
                    }
                });
                stage.addActor(picker.fadeIn());
            }
        });

        cPickerElems.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                ColorPicker picker = new ColorPicker(new ColorPickerAdapter() {
                    @Override
                    public void finished (Color newColor) {
                        cPickerElems.setColorValue(newColor);
                    }
                });
                stage.addActor(picker.fadeIn());
            }
        });


        final DragAndDrop dragAndDropBulb = new DragAndDrop();
        dragAndDropBulb.addSource(new DragAndDrop.Source(bulb) {
            public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {

                DragAndDrop.Payload payload = new DragAndDrop.Payload();
                Image bulbThumb = new Image(dataManager.textureManager.getEditorAsset("bulb"));
                payload.setDragActor(bulbThumb);
                dragAndDropBulb.setDragActorPosition(-bulbThumb.getWidth() / 2, bulbThumb.getHeight() / 2);

                return payload;
            }
        });

        dragAndDropBulb.addTarget(new DragAndDrop.Target(stage.dummyTarget) {

            @Override
            public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                if (disableLights.isChecked()) return;
                LightVO vo = new LightVO();
                Color tint = cPickerElems.getColorValue();

                float[] clr = new float[4];
                clr[0] = tint.r;
                clr[1] = tint.g;
                clr[2] = tint.b;
                clr[3] = tint.a;
                vo.tint = clr;

                vo.type = LightType.POINT;
                stage.getSandbox().getUac().createLight(vo, x, y);
            }

            @Override
            public boolean drag(DragAndDrop.Source arg0, DragAndDrop.Payload arg1, float arg2, float arg3, int arg4) {

                return true;
            }
        });


        final DragAndDrop dragAndDropCone = new DragAndDrop();
        dragAndDropCone.addSource(new DragAndDrop.Source(cone) {
            public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {

                DragAndDrop.Payload payload = new DragAndDrop.Payload();
                Image coneThumb = new Image(dataManager.textureManager.getEditorAsset("cone"));
                payload.setDragActor(coneThumb);
                dragAndDropCone.setDragActorPosition(-coneThumb.getWidth() / 2, coneThumb.getHeight() / 2);

                return payload;
            }
        });

        dragAndDropCone.addTarget(new DragAndDrop.Target(stage.dummyTarget) {

            @Override
            public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                if (disableLights.isChecked()) return;
                LightVO vo = new LightVO();
                vo.type = LightType.CONE;
                Color tint = cPickerElems.getColorValue();

                float[] clr = new float[4];
                clr[0] = tint.r;
                clr[1] = tint.g;
                clr[2] = tint.b;
                clr[3] = tint.a;
                vo.tint = clr;
                stage.getSandbox().getUac().createLight(vo, x, y);
            }

            @Override
            public boolean drag(DragAndDrop.Source arg0, DragAndDrop.Payload arg1, float arg2, float arg3, int arg4) {

                return true;
            }
        });

        disableLights = ui.getCheckBoxById("disableLights");
        disableLights.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                stage.getSandbox().sceneControl.disableLights(disableLights.isChecked());
            }

        });

        disableAmbiance = ui.getCheckBoxById("disableAmbiance");
        disableAmbiance.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                stage.getSandbox().getSceneControl().disableAmbience(disableAmbiance.isChecked());
            }

        });
    }

    @Override
    protected void expand() {
        setHeight(expandedHeight);
        if (mainLayer != null) {
            mainLayer.setVisible(true);
        }
    }

    @Override
    protected void collapse() {
        setHeight(topImg.getHeight());
        if (mainLayer != null) {
            mainLayer.setVisible(false);
        }
    }

}
