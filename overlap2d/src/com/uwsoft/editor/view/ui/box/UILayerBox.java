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

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.event.KeyboardListener;
import com.uwsoft.editor.renderer.data.LayerItemVO;
import com.uwsoft.editor.utils.InputFilters;
import com.uwsoft.editor.utils.StandardWidgetsFactory;

/**
 * Created by azakhary on 4/17/2015.
 */
public class UILayerBox extends UICollapsibleBox {

    private static final String prefix = "com.uwsoft.editor.view.ui.box.UILayerBox";

    public static final String LAYER_ROW_CLICKED =  prefix + ".LAYER_ROW_CLICKED";
    public static final String CREATE_NEW_LAYER =   prefix + ".CREATE_NEW_LAYER";
    public static final String DELETE_LAYER =       prefix + ".DELETE_NEW_LAYER";
    public static final String CHANGE_LAYER_NAME =  prefix + ".CHANGE_LAYER_NAME";
    public static final String LOCK_LAYER =         prefix + ".LOCK_LAYER";
    public static final String UNLOCK_LAYER =       prefix + ".UNLOCK_LAYER";
    public static final String HIDE_LAYER =         prefix + ".HIDE_LAYER";
    public static final String UNHIDE_LAYER =       prefix + ".UNHIDE_LAYER";
    public static final String LAYER_DROPPED =      prefix + ".LAYER_DROPPED";

    private final DragAndDrop dragAndDrop;
    public int currentSelectedLayerIndex = 0;
    private Overlap2DFacade facade;
    private VisTable contentTable;
    private VisTable bottomPane;
    private VisScrollPane scrollPane;
    private VisTable layersTable;

    private SlotSource sourceInEdition;

    private Array<UILayerItemSlot> rows = new Array<>();

    public UILayerBox() {
        super("Layers", 222);

        facade = Overlap2DFacade.getInstance();

        setMovable(false);
        contentTable = new VisTable();

        layersTable = new VisTable();
        scrollPane = new VisScrollPane(layersTable);
        scrollPane.setFadeScrollBars(false);
        contentTable.add(scrollPane).padTop(2).width(221).height(150);
        layersTable.top();

        scrollPane.layout();

        bottomPane = new VisTable();
        contentTable.row();
        contentTable.add(bottomPane).expandX().fillX();


        VisImageButton newBtn = new VisImageButton("new-layer-button");
        VisImageButton deleteBtn = new VisImageButton("trash-button");

        bottomPane.add().expandX();
        bottomPane.add(newBtn).right().pad(3);
        bottomPane.add(deleteBtn).right().pad(3);

        newBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                facade.sendNotification(CREATE_NEW_LAYER);
            }
        });

        deleteBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                facade.sendNotification(DELETE_LAYER);
            }
        });
        dragAndDrop = new DragAndDrop();



        createCollapsibleWidget(contentTable);
    }

    public void enableDraggingInEditedSlot() {
        if(sourceInEdition != null)
        {
            dragAndDrop.addSource(sourceInEdition);
            sourceInEdition = null;
        }
    }
    public void disableDraggingInEditedSlot() {
        if(sourceInEdition != null)
        {
            dragAndDrop.removeSource(sourceInEdition);
        }
    }

    public int getCurrentSelectedLayerIndex() {
        return currentSelectedLayerIndex;
    }

    public int getRowCount() {
        return rows.size;
    }

    public UILayerItem getCurrentSelectedLayer() {
        return rows.get(rows.size-1-currentSelectedLayerIndex).uiLayerItem;
    }

    public void clearItems() {
        layersTable.clear();
        rows.clear();
    }

    public void clearSelection() {
        for (int i = 0; i < rows.size; i++) {
            rows.get(i).getUiLayerItem().setSelected(false);
        }
        currentSelectedLayerIndex = -1;
    }

    public void setCurrentSelectedLayer(int index) {
        if(index == -1) return;
        UILayerItemSlot slot = rows.get(rows.size-1-index);

        clearSelection();
        slot.getUiLayerItem().setSelected(true);
    }

    public void addItem(LayerItemVO itemVO) {
        UILayerItemSlot itemSlot = new UILayerItemSlot();
        UILayerItem item = new UILayerItem(itemVO, itemSlot);
        layersTable.add(itemSlot).left().expandX().fillX();
        layersTable.row().padTop(1);
        SlotSource sourceItem = new SlotSource(item);
        dragAndDrop.addSource(sourceItem);
        dragAndDrop.addTarget(new SlotTarget(itemSlot));
        dragAndDrop.setDragActorPosition(0, 0);
        rows.add(itemSlot);

        itemSlot.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                VisTextField textField = itemSlot.getUiLayerItem().getNameField();

                if(sourceInEdition != null)
                {
                    VisTextField prevField = ((UILayerItem) sourceInEdition.getActor()).getNameField();
                    if(textField != prevField)
                    {
                        prevField.clearSelection();
                        prevField.setDisabled(true);
                        enableDraggingInEditedSlot();
                    }
                }

                clearSelection();
                itemSlot.getUiLayerItem().setSelected(true);
                currentSelectedLayerIndex = rows.size - rows.indexOf(itemSlot, true) - 1;

                facade.sendNotification(LAYER_ROW_CLICKED, itemSlot.getUiLayerItem());

                // Change name mode if double click
                if(getTapCount() == 2 && !itemSlot.getUiLayerItem().getData().isLocked)
                {
                    sourceInEdition = sourceItem;
                    textField.setDisabled(false);
                    textField.focusField();
                    textField.selectAll();
                    disableDraggingInEditedSlot();
                }
            }
        });
    }

    private static class SlotSource extends DragAndDrop.Source {


        public SlotSource(UILayerItem item) {
            super(item);
        }

        @Override
        public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
            DragAndDrop.Payload payload = new DragAndDrop.Payload();
            payload.setDragActor(new UILayerItemDragActor((UILayerItem) getActor()));
            return payload;
        }

        @Override
        public void dragStop(InputEvent event, float x, float y, int pointer, DragAndDrop.Payload payload, DragAndDrop.Target target) {
            UILayerItem uiLayerItemActor = (UILayerItem) getActor();
            UILayerItemSlot uiLayerItemSlot = uiLayerItemActor.getItemSlot();
            if (target != null) {
                UILayerItemSlot uiLayerItemSlotTarget = (UILayerItemSlot) target.getActor();
                UILayerItem uiLayerItemTarget = uiLayerItemSlotTarget.getUiLayerItem();
                //
                uiLayerItemActor.setItemSlot(uiLayerItemSlotTarget);
                uiLayerItemTarget.setItemSlot(uiLayerItemSlot);

                String sourceLayer = uiLayerItemActor.getLayerName();
                String targetLayer = uiLayerItemTarget.getLayerName();
                String[] notificationPayload = {sourceLayer, targetLayer};

                Overlap2DFacade.getInstance().sendNotification(LAYER_DROPPED, notificationPayload);
            } else {
                uiLayerItemActor.setItemSlot(uiLayerItemSlot);
            }
        }
    }

    private static class SlotTarget extends DragAndDrop.Target {

        //private Slot targetSlot;

        public SlotTarget(UILayerItemSlot item) {
            super(item);
        }

        @Override
        public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
//            Slot payloadSlot = (Slot) payload.getObject();
//            // if (targetSlot.getItem() == payloadSlot.getItem() ||
//            // targetSlot.getItem() == null) {
//            getActor().setColor(Color.BLUE);
            return true;
            // } else {
            // getActor().setColor(Color.DARK_GRAY);
            // return false;
            // }
        }

        @Override
        public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
            //Overlap2DFacade.getInstance().sendNotification(LAYER_DROPPED);
        }

        @Override
        public void reset(DragAndDrop.Source source, DragAndDrop.Payload payload) {
//            getActor().setColor(Color.LIGHT_GRAY);
        }

    }

    public static class UILayerItemDragActor extends VisTable {

        public String layerName;

        public UILayerItemDragActor(UILayerItem actor) {
            layerName = actor.getLayerName();
            setWidth(actor.getWidth());
            setHeight(actor.getPrefHeight());
            VisImageButton lockBtn = new VisImageButton("layer-lock");
            VisImageButton visibleBtn = new VisImageButton("layer-visible");
            add(lockBtn).left();
            add(visibleBtn).left().padRight(6);
            add(actor.getLayerName()).expandX().fillX();
            setBackground(VisUI.getSkin().getDrawable(actor.isSelected() ? "layer-bg-over" : "layer-bg"));
            getColor().a = .9f;
        }
    }

    public class UILayerItemSlot extends VisTable {
        private final Cell cell;
        private final Drawable normalBg;
        private final Drawable selectedBg;
        private UILayerItem uiLayerItem;

        public UILayerItemSlot() {
            normalBg = VisUI.getSkin().getDrawable("layer-bg");
            selectedBg = VisUI.getSkin().getDrawable("layer-bg-over");
            setBackground(normalBg);
            cell = add().expandX().fillX();
        }

        private UILayerItemSlot(UILayerItemSlot uiLayerItemSlot) {
            this();
            cell.width(uiLayerItemSlot.cell.getPrefWidth());
            cell.height(uiLayerItemSlot.cell.getPrefHeight());
        }

        public void setLayerItem(UILayerItem uiLayerItem) {
            this.uiLayerItem = uiLayerItem;
            cell.setActor(uiLayerItem);
            cell.height(uiLayerItem.getHeight());
        }

        public UILayerItem getUiLayerItem() {
            return uiLayerItem;
        }

        public void setSelected(boolean selected) {
            setBackground(selected ? selectedBg : normalBg);
        }

        @Override
        protected UILayerItemSlot clone() throws CloneNotSupportedException {
            return new UILayerItemSlot(this);
        }
    }

    public class UILayerItem extends VisTable {


        private final LayerItemVO layerData;
        private UILayerItemSlot itemSlot;
        private boolean selected;

        private VisTextField layerNameField;

        public UILayerItem(LayerItemVO layerData, UILayerItemSlot itemSlot) {
            super();
            this.layerData = layerData;
            this.itemSlot = itemSlot;

            VisImageButton lockBtn = new VisImageButton("layer-lock");
            lockBtn.addListener(new CheckClickListener(lockBtn, LOCK_LAYER, UNLOCK_LAYER));

            VisImageButton visibleBtn = new VisImageButton("layer-visible");
            visibleBtn.addListener(new CheckClickListener(visibleBtn, HIDE_LAYER, UNHIDE_LAYER));

            add(lockBtn).left();
            add(visibleBtn).left().padRight(6);

            layerNameField = StandardWidgetsFactory.createTextField("transparent", false);
            layerNameField.setTextFieldFilter(InputFilters.ALPHANUMERIC);
            layerNameField.setText(layerData.layerName);
            layerNameField.setDisabled(true);
            // This listener will manage Enter and lost focus events
            layerNameField.addListener(new KeyboardListener(CHANGE_LAYER_NAME));

            add(layerNameField).expandX().fillX();
            lockBtn.setChecked(layerData.isLocked);
            visibleBtn.setChecked(!layerData.isVisible);

            itemSlot.setLayerItem(this);
        }

        public VisTextField getNameField() {
            return layerNameField;
        }

        public boolean isLocked() {
            return layerData.isLocked;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
            itemSlot.setSelected(selected);
        }

        public LayerItemVO getData() {
            return layerData;
        }

        public boolean isLayerVisible() {
            return layerData.isVisible;
        }

        public String getLayerName() {
            return layerData.layerName;
        }

        public UILayerItemSlot getItemSlot() {
            return itemSlot;
        }

        public void setItemSlot(UILayerItemSlot itemSlot) {
            this.itemSlot = itemSlot;
            itemSlot.setLayerItem(this);
            itemSlot.setSelected(selected);
        }

        private class CheckClickListener extends ClickListener {

            final VisImageButton owner;
            final String eventOnUnchecking;
            final String eventOnChecking;

            CheckClickListener(VisImageButton owner, String eventOnUnchecking, String eventOnChecking) {
                this.owner = owner;
                this.eventOnChecking = eventOnChecking;
                this.eventOnUnchecking = eventOnUnchecking;
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                final String sendEvent = (owner.isChecked())? eventOnUnchecking : eventOnChecking;
                facade.sendNotification(sendEvent, itemSlot.getUiLayerItem());
            }
        }


    }


    public Array<UILayerItemSlot> getLayerSlots() {
        return rows;
    }

}