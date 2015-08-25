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
import com.kotcrab.vis.ui.widget.VisTextField;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.Overlap2D;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.controller.commands.DeleteLayerCommand;
import com.uwsoft.editor.controller.commands.LayerSwapCommand;
import com.uwsoft.editor.controller.commands.NewLayerCommand;
import com.uwsoft.editor.renderer.components.ZIndexComponent;
import com.uwsoft.editor.utils.runtime.EntityUtils;
import com.uwsoft.editor.view.stage.Sandbox;
import com.uwsoft.editor.controller.commands.CompositeCameraChangeCommand;
import com.uwsoft.editor.factory.ItemFactory;
import com.uwsoft.editor.proxy.SceneDataManager;
import com.uwsoft.editor.renderer.components.LayerMapComponent;
import com.uwsoft.editor.renderer.components.NodeComponent;
import com.uwsoft.editor.renderer.data.LayerItemVO;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;


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
                UILayerBox.CHANGE_LAYER_NAME,
                UILayerBox.DELETE_LAYER,
                UILayerBox.LOCK_LAYER,
                UILayerBox.UNLOCK_LAYER,
                UILayerBox.HIDE_LAYER,
                UILayerBox.UNHIDE_LAYER,
                CompositeCameraChangeCommand.DONE,
                Overlap2D.ITEM_SELECTION_CHANGED,
                ItemFactory.NEW_ITEM_ADDED,
                UILayerBox.LAYER_DROPPED,
                DeleteLayerCommand.DONE,
                DeleteLayerCommand.UNDONE,
                NewLayerCommand.DONE,
                LayerSwapCommand.DONE


        }).flatMap(Stream::of).toArray(String[]::new);
    }

    @Override
    public void handleNotification(Notification notification) {
        super.handleNotification(notification);
        UILayerBox.UILayerItem layerItem;
        switch (notification.getName()) {
            case SceneDataManager.SCENE_LOADED:
                initLayerData();
                int layerid = getFirstFreeLayer();
                viewComponent.setCurrentSelectedLayer(layerid);
                viewComponent.currentSelectedLayerIndex = layerid;
                break;
            case CompositeCameraChangeCommand.DONE:
                initLayerData();
                layerid = getFirstFreeLayer();
                viewComponent.setCurrentSelectedLayer(layerid);
                viewComponent.currentSelectedLayerIndex = layerid;
                break;
            case NewLayerCommand.DONE:
                initLayerData();
                setSelectedByName(notification.getBody());
                break;
            case DeleteLayerCommand.DONE:
                initLayerData();
                int deletedIndex = (int)notification.getBody()-1;
                if(deletedIndex == -1) deletedIndex = 0;
                viewComponent.setCurrentSelectedLayer(deletedIndex);
                viewComponent.currentSelectedLayerIndex = deletedIndex;
                break;
            case DeleteLayerCommand.UNDONE:
                initLayerData();
                setSelectedByName(notification.getBody());
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
                            Object[] payload = NewLayerCommand.payload(viewComponent.getCurrentSelectedLayerIndex()+1, input);
                            facade.sendNotification(Sandbox.ACTION_NEW_LAYER, payload);
                        } else {
                            // show error dialog
                        }
                    }
                    @Override
                    public void canceled() {

                    }
                });
                break;
            case UILayerBox.LAYER_DROPPED:
                facade.sendNotification(Sandbox.ACTION_SWAP_LAYERS, notification.getBody());
                break;
            case LayerSwapCommand.DONE:
                int index = viewComponent.getCurrentSelectedLayerIndex();
                initLayerData();
                viewComponent.setCurrentSelectedLayer(index);
                break;
            case UILayerBox.DELETE_LAYER:
                if (layers == null) return;
                int deletingLayerIndex = viewComponent.getCurrentSelectedLayerIndex();
                if(deletingLayerIndex != -1) {
                    String layerName = layers.get(deletingLayerIndex).layerName;
                    facade.sendNotification(Sandbox.ACTION_DELETE_LAYER, layerName);
                }
                break;
            case UILayerBox.LOCK_LAYER:
                layerItem = notification.getBody();
                lockLayerByName(layerItem, true);
                break;
            case UILayerBox.UNLOCK_LAYER:
                layerItem = notification.getBody();
                lockLayerByName(layerItem, false);
                break;
            case UILayerBox.HIDE_LAYER:
                layerItem = notification.getBody();
                setEntityVisibilityByLayer(layerItem, false);
                break;
            case UILayerBox.UNHIDE_LAYER:
                layerItem = notification.getBody();
                setEntityVisibilityByLayer(layerItem, true);
                break;
            case Overlap2D.ITEM_SELECTION_CHANGED:
                Set<Entity> selection = notification.getBody();
                if(selection.size() == 1) {
                    ZIndexComponent zIndexComponent = ComponentRetriever.get(selection.iterator().next(), ZIndexComponent.class);
                    index = findLayerByName(zIndexComponent.layerName);
                    if(index == -1) {
                        // handle this somehow
                    } else {
                        viewComponent.setCurrentSelectedLayer(index);
                        viewComponent.currentSelectedLayerIndex = index;
                    }
                } else if (selection.size() > 1) {
                    // multi selection handling not yet clear
                }
                break;
            case ItemFactory.NEW_ITEM_ADDED:
                index = viewComponent.getCurrentSelectedLayerIndex();
                Entity item = notification.getBody();
                ZIndexComponent zIndexComponent = ComponentRetriever.get(item, ZIndexComponent.class);
                if(zIndexComponent.layerName == null) zIndexComponent.layerName = layers.get(index).layerName;
                break;
            case UILayerBox.CHANGE_LAYER_NAME:
                // TODO: this needs to be command
                String layerName = notification.getBody();
                int layerIndex = viewComponent.getCurrentSelectedLayerIndex();
                if(layerIndex == -1) break;
                LayerItemVO layer_view = layers.get(layerIndex);
                layerItem = viewComponent.getCurrentSelectedLayer();
                VisTextField textField = layerItem.getNameField();

                if(layer_view.layerName.equals(layerName))  // Name didn't change
                {
                    textField.clearSelection();
                    textField.setDisabled(true);
                    viewComponent.enableDraggingInEditedSlot();
                }
                else if(checkIfNameIsUnique(layerName)) // Name changed
                {
                    textField.clearSelection();
                    textField.setDisabled(true);
                    viewComponent.enableDraggingInEditedSlot();
                    String prevName = layer_view.layerName;
                    layer_view.layerName = layerName;
                    // update the map
                    Entity viewEntity = Sandbox.getInstance().getCurrentViewingEntity();
                    LayerMapComponent layerMapComponent = ComponentRetriever.get(viewEntity, LayerMapComponent.class);
                    layerMapComponent.rename(prevName, layerName);
                }
                else
                {
                    //Show error dialog
                }
                break;
            default:
                break;
        }
    }


    private void setSelectedByName(String name) {
        String deletedLayerName = name;
        for (int i = 0; i < layers.size(); i++) {
            if (layers.get(i).layerName.equals(deletedLayerName)) {
                viewComponent.setCurrentSelectedLayer(i);
                viewComponent.currentSelectedLayerIndex = i;
                break;
            }
        }
    }

    /*
    // Booboo madafaka funktione
    private void remakeLayersArray() {
        Array<UILayerBox.UILayerItemSlot> slots = viewComponent.getLayerSlots();
        layers = new ArrayList<>();
        for(UILayerBox.UILayerItemSlot slot: slots) {
            LayerItemVO vo = slot.getUiLayerItem().getData();
            layers.add(vo);
        }
        LayerMapComponent layerMapComponent = ComponentRetriever.get(Sandbox.getInstance().getCurrentViewingEntity(), LayerMapComponent.class);
        layerMapComponent.setLayers(layers);
    }*/

    private void addNewLayerToItemComposite(LayerItemVO layerVo) {
        LayerMapComponent layerMapComponent = ComponentRetriever.get(Sandbox.getInstance().getCurrentViewingEntity(), LayerMapComponent.class);
        layerMapComponent.addLayer(layerVo);
    }

    private void lockLayerByName(UILayerBox.UILayerItem layerItem, boolean setLocked) {
        String layerName = layerItem.getLayerName();
        if(setLocked){
            Sandbox.getInstance().getSelector().clearSelections();
        }
        Entity viewEntity = Sandbox.getInstance().getCurrentViewingEntity();
        LayerMapComponent layerMapComponent = ComponentRetriever.get(viewEntity, LayerMapComponent.class);

        layerMapComponent.getLayer(layerName).isLocked = setLocked;
    }

    private void selectEntitiesByLayerName(UILayerBox.UILayerItem layerItem) {
        if(layerItem.isLocked()){
            Sandbox.getInstance().getSelector().clearSelections();
            viewComponent.clearSelection();
            return;
        }
        String layerName = layerItem.getLayerName();
        Entity viewEntity = Sandbox.getInstance().getCurrentViewingEntity();

        NodeComponent nodeComponent = ComponentRetriever.get(viewEntity, NodeComponent.class);
        Set<Entity> items = new HashSet<>();
        for(int i=0; i<nodeComponent.children.size; i++){
            Entity entity = nodeComponent.children.get(i);
            ZIndexComponent childZComponent = ComponentRetriever.get(entity, ZIndexComponent.class);
            if(childZComponent.layerName.equals(layerName)){
                items.add(entity);
            }
        }
        Sandbox.getInstance().getSelector().clearSelections();
        facade.sendNotification(Sandbox.ACTION_ADD_SELECTION, items);
    }

    private void setEntityVisibilityByLayer(UILayerBox.UILayerItem layerItem, boolean setVisible) {
        String layerName = layerItem.getLayerName();
        Entity viewEntity = Sandbox.getInstance().getCurrentViewingEntity();

        NodeComponent nodeComponent = ComponentRetriever.get(viewEntity, NodeComponent.class);
        for(int i=0; i<nodeComponent.children.size; i++){
            Entity entity = nodeComponent.children.get(i);
            ZIndexComponent childZComponent = ComponentRetriever.get(entity, ZIndexComponent.class);
            if(childZComponent.layerName.equals(layerName)){
                EntityUtils.getEntityLayer(entity).isVisible = setVisible;
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

    private int getFirstFreeLayer() {
        for(int i = 0; i < layers.size(); i++) {
            if(!layers.get(i).isLocked) {
                return i;
            }
        }

        return -1;
    }

    private void initLayerData() {

        Entity viewEntity = Sandbox.getInstance().getCurrentViewingEntity();
        LayerMapComponent layerMapComponent = ComponentRetriever.get(viewEntity, LayerMapComponent.class);
        layers = layerMapComponent.getLayers();

        viewComponent.clearItems();

        for (int i = (layers.size()-1); i >=0; i--) {
            viewComponent.addItem(layers.get(i));
        }
    }

    public int getCurrentSelectedLayerIndex() {
        return viewComponent.getCurrentSelectedLayerIndex();
    }

    public String getCurrentSelectedLayerName() {
        if(viewComponent.getCurrentSelectedLayerIndex() == -1) return null;
        return layers.get(viewComponent.getCurrentSelectedLayerIndex()).layerName;
    }
}