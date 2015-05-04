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

import java.util.ArrayList;

import com.kotcrab.vis.ui.util.dialog.DialogUtils;
import com.kotcrab.vis.ui.util.dialog.InputDialogListener;
import com.puremvc.patterns.mediator.SimpleMediator;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.mvc.proxy.SceneDataManager;
import com.uwsoft.editor.renderer.legacy.data.CompositeItemVO;
import com.uwsoft.editor.renderer.legacy.data.LayerItemVO;


/**
 * Created by azakhary on 4/17/2015.
 */
public class UILayerBoxMediator extends SimpleMediator<UILayerBox> {

    private static final String TAG = UILayerBoxMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    private ArrayList<LayerItemVO> layers;

    public UILayerBoxMediator() {
        super(NAME, new UILayerBox());
    }

    @Override
    public String[] listNotificationInterests() {
        return new String[]{
                SceneDataManager.SCENE_LOADED,
                UILayerBox.LAYER_ROW_CLICKED,
                UILayerBox.CREATE_NEW_LAYER,
                UILayerBox.DELETE_NEW_LAYER
        };
    }

    @Override
    public void handleNotification(Notification notification) {
        switch (notification.getName()) {
            case SceneDataManager.SCENE_LOADED:
                initLayerData();
                break;
            case UILayerBox.LAYER_ROW_CLICKED:
                // select this one deselect others

                break;
            case UILayerBox.CREATE_NEW_LAYER:
                DialogUtils.showInputDialog(Sandbox.getInstance().getUIStage(), "Please set unique name for your Layer", "Please set unique name for your Layer", new InputDialogListener() {
                    @Override
                    public void finished(String input) {
                        if (checkIfNameIsUnique(input)) {
                            LayerItemVO layerVo = new LayerItemVO();
                            layerVo.layerName = input;
                            viewComponent.addItem(new UILayerItem(layerVo));
                            //currentSelectedLayerIndex = layers.indexOf(layerVo);
                        } else {
                            // show error dialog
                        }
                    }

                    @Override
                    public void canceled() {

                    }
                });
                break;
            case UILayerBox.DELETE_NEW_LAYER:
                if (layers == null) return;
                if (viewComponent.getCurrentSelectedLayerIndex() != -1 && !layers.get(viewComponent.getCurrentSelectedLayerIndex()).layerName.equals("Default")) {
                    layers.remove(viewComponent.getCurrentSelectedLayerIndex());
                    initLayerData();
                }
                break;
            default:
                break;
        }
    }

    private boolean checkIfNameIsUnique(String name) {
        for (int i = 0; i < layers.size(); i++) {
            if (layers.get(i).layerName.equals(name)) {
                return false;
            }
        }

        return true;
    }

    private void initLayerData() {
        CompositeItemVO currentComposite = Sandbox.getInstance().sceneControl.getCurrentScene().getDataVO();
        layers = currentComposite.composite.layers;

        viewComponent.clearItems();

        for (int i = 0; i < layers.size(); i++) {
            viewComponent.addItem(new UILayerItem(layers.get(i)));
        }
    }

    public int getCurrentSelectedLayerIndex() {
        return viewComponent.getCurrentSelectedLayerIndex();
    }
}
