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

package com.uwsoft.editor.view.ui.box;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import com.badlogic.ashley.core.Entity;
import com.kotcrab.vis.ui.util.dialog.DialogUtils;
import com.kotcrab.vis.ui.util.dialog.InputDialogListener;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.Overlap2D;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.view.stage.Sandbox;
import com.uwsoft.editor.view.ui.box.UILayerBox.UILayerItem;
import com.uwsoft.editor.controller.commands.CompositeCameraChangeCommand;
import com.uwsoft.editor.factory.ItemFactory;
import com.uwsoft.editor.proxy.SceneDataManager;
import com.uwsoft.editor.renderer.components.LayerMapComponent;
import com.uwsoft.editor.renderer.components.MainItemComponent;
import com.uwsoft.editor.renderer.components.NodeComponent;
import com.uwsoft.editor.renderer.data.LayerItemVO;
import com.uwsoft.editor.utils.runtime.ComponentRetriever;


/**
 * Created by azakhary on 4/17/2015.
 */
public class UILayerBoxMediator extends PanelMediator<UILayerBox> {

    private static final String TAG = UILayerBoxMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    private ArrayList<LayerItemVO> layers;

    public UILayerBoxMediator() {
        super(NAME, new UILayerBox());
        facade = Overlap2DFacade.getInstance();
    }

    @Override
    public String[] listNotificationInterests() {
        String[] parentNotifications = super.listNotificationInterests();
        return Stream.of(parentNotifications, new String[]{
                SceneDataManager.SCENE_LOADED,
                UILayerBox.LAYER_ROW_CLICKED,
                UILayerBox.CREATE_NEW_LAYER,
                UILayerBox.DELETE_NEW_LAYER,
                UILayerBox.LOCK_LAYER,
                UILayerBox.HIDE_LAYER,
                CompositeCameraChangeCommand.DONE,
                Overlap2D.ITEM_SELECTION_CHANGED,
                ItemFactory.NEW_ITEM_ADDED


        }).flatMap(Stream::of).toArray(String[]::new);
    }

    @Override
    public void handleNotification(Notification notification) {
        super.handleNotification(notification);
        UILayerItem layerItem;
        switch (notification.getName()) {
            case SceneDataManager.SCENE_LOADED:
                initLayerData();
                break;
            case CompositeCameraChangeCommand.DONE:
                initLayerData();
                break;
            case UILayerBox.LAYER_ROW_CLICKED:
            	layerItem = notification.getBody();
                selectEntitiesByLayerName(layerItem);
                break;
            case UILayerBox.CREATE_NEW_LAYER:
                DialogUtils.showInputDialog(Sandbox.getInstance().getUIStage(), "Please set unique name for your Layer", "Please set unique name for your Layer", new InputDialogListener() {
                    @Override
                    public void finished(String input) {
                        if (checkIfNameIsUnique(input)) {
                            LayerItemVO layerVo = new LayerItemVO();
                            layerVo.layerName = input;
                            viewComponent.addItem(layerVo);
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
            case UILayerBox.LOCK_LAYER:
            	layerItem = notification.getBody();
            	lockLayerByName(layerItem);
                break;
            case UILayerBox.HIDE_LAYER:
            	layerItem = notification.getBody();
            	hideEntitiesByLayerName(layerItem);
                break;
            case Overlap2D.ITEM_SELECTION_CHANGED:
                Set<Entity> selection = notification.getBody();
                if(selection.size() == 1) {
                    MainItemComponent mainItemComponent = ComponentRetriever.get(selection.iterator().next(), MainItemComponent.class);
                    int index = findLayerByName(mainItemComponent.layer);
                    viewComponent.setCurrentSelectedLayer(index);
                } else if (selection.size() > 1) {
                    // multi selection handling not yet clear
                }
                break;
            case ItemFactory.NEW_ITEM_ADDED:
                int index = viewComponent.getCurrentSelectedLayerIndex();
                Entity item = notification.getBody();
                MainItemComponent mainItemComponent = ComponentRetriever.get(item, MainItemComponent.class);
                mainItemComponent.layer = layers.get(index).layerName;
                break;
            default:
                break;
        }
    }

    private void lockLayerByName(UILayerItem layerItem) {
    	String layerName = layerItem.getLayerName();
    	boolean toLock = layerItem.isLocked();
    	if(toLock){
    		Sandbox.getInstance().getSelector().clearSelections();
    	}
    	Entity viewEntity = Sandbox.getInstance().getCurrentViewingEntity();
        LayerMapComponent layerMapComponent = ComponentRetriever.get(viewEntity, LayerMapComponent.class);
        for(int i=0; i<layerMapComponent.layers.size(); i++){
        	LayerItemVO layerVO = layerMapComponent.layers.get(i);
        	if(layerVO.layerName.equals(layerName)){
        		layerVO.isLocked = toLock;
        		break;
        	}
        }
        
	}

	private void selectEntitiesByLayerName(UILayerItem layerItem) {
		if(layerItem.isLocked()){
			Sandbox.getInstance().getSelector().clearSelections();
			return;
		}
		String layerName = layerItem.getLayerName();
    	Entity viewEntity = Sandbox.getInstance().getCurrentViewingEntity();

    	NodeComponent nodeComponent = ComponentRetriever.get(viewEntity, NodeComponent.class);
    	Set<Entity> items = new HashSet<>();
    	for(int i=0; i<nodeComponent.children.size; i++){
    		Entity entity = nodeComponent.children.get(i);
    		MainItemComponent childeMainItemComponent = ComponentRetriever.get(entity, MainItemComponent.class);
    		if(childeMainItemComponent.layer.equals(layerName)){
    			items.add(entity);
    		}
    	}
    	Sandbox.getInstance().getSelector().clearSelections();
    	facade.sendNotification(Sandbox.ACTION_ADD_SELECTION, items);
	}
    
    private void hideEntitiesByLayerName(UILayerItem layerItem) {
    	String layerName = layerItem.getLayerName();
    	boolean toHide = layerItem.isLayerVisible();
    	Entity viewEntity = Sandbox.getInstance().getCurrentViewingEntity();

    	NodeComponent nodeComponent = ComponentRetriever.get(viewEntity, NodeComponent.class);
    	for(int i=0; i<nodeComponent.children.size; i++){
    		Entity entity = nodeComponent.children.get(i);
    		MainItemComponent childeMainItemComponent = ComponentRetriever.get(entity, MainItemComponent.class);
    		if(childeMainItemComponent.layer.equals(layerName)){
    			childeMainItemComponent.visible = toHide;
    		}
    	}
	}

	private int findLayerByName(String name) {
        for (int i = 0; i < layers.size(); i++) {
            if (layers.get(i).layerName.equals(name)) {
                return i;
            }
        }

        return -1;
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

        Entity viewEntity = Sandbox.getInstance().getCurrentViewingEntity();
        LayerMapComponent layerMapComponent = ComponentRetriever.get(viewEntity, LayerMapComponent.class);
        layers = layerMapComponent.layers;

        viewComponent.clearItems();

        for (int i = (layers.size()-1); i >=0; i--) {
            viewComponent.addItem(layers.get(i));
        }
    }

    public int getCurrentSelectedLayerIndex() {
        return viewComponent.getCurrentSelectedLayerIndex();
    }
}
