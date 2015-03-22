package com.uwsoft.editor.gdx.ui.layer;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.uwsoft.editor.gdx.stage.UIStage;
import com.uwsoft.editor.gdx.ui.ExpandableUIBox;
import com.uwsoft.editor.gdx.ui.dialogs.InputDialog;
import com.uwsoft.editor.gdx.ui.dialogs.InputDialog.InputDialogListener;
import com.uwsoft.editor.gdx.ui.layer.drag.LayerItemSource;
import com.uwsoft.editor.gdx.ui.layer.drag.LayerItemTarget;
import com.uwsoft.editor.renderer.data.LayerItemVO;

import java.awt.*;
import java.util.ArrayList;

public class UILayerBox extends ExpandableUIBox {

    public int currentSelectedLayerIndex = 0;
    public Group contentGroup = new Group();
    public Group uiGroup = new Group();
    private ArrayList<LayerItemVO> layers;
    private ArrayList<LayerItem> layerActors = new ArrayList<>();
    private DragAndDrop dragAndDrop;
    private boolean isLayerDragging;

    public UILayerBox(UIStage s) {
        super(s, 250, 250);

        addActor(contentGroup);
        addActor(uiGroup);

        initBottomBar();
        dragAndDrop = new DragAndDrop();
        dragAndDrop.setDragActorPosition(0, 0);
        isLayerDragging = false;
    }

    public void selectLayerByName(String name) {
        for (int i = 0; i < layers.size(); i++) {
            if (layers.get(i).layerName.equals(name)) {
                currentSelectedLayerIndex = i;
                selectItem(i);
            }
        }
    }

    private void checkForSelectedIndexChanges() {
        int planB = -1;
        for (int i = 0; i < layers.size(); i++) {
            if (!layers.get(i).isLocked) {
                planB = i;
                if (i == currentSelectedLayerIndex) return;
            }
        }
        currentSelectedLayerIndex = planB;
    }

    public void initContent() {
        layers = stage.getSandbox().getCurrentScene().dataVO.composite.layers;
        contentGroup.clear();
        layerActors.clear();
        float heightSize = layers.size() * 20;
        float minSize = 217;
        if (heightSize < minSize) heightSize = minSize;
        contentGroup.setHeight(heightSize);
        contentGroup.setX(3);

        checkForSelectedIndexChanges();

        for (int i = 0; i < layers.size(); i++) {
            LayerItem itm = new LayerItem(layers.get(i), stage.getSandbox());
            itm.initListeners();
            itm.setY(contentGroup.getHeight() - (layers.size() - i - 1) * itm.getHeight());
            contentGroup.addActor(itm);
            layerActors.add(itm);
            dragAndDrop.addSource(new LayerItemSource(itm, this));
            dragAndDrop.addTarget(new LayerItemTarget(itm, this));

            if (i == currentSelectedLayerIndex) itm.select();

            final int iter = i;
            ClickListener listener = new ClickListener() {
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    super.touchDown(event, x, y, pointer, button);
                    return true;
                }

                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    super.touchUp(event, x, y, pointer, button);
                    if (isLayerDragging) {
                        isLayerDragging = false;
                        return;
                    }

                    if (layers.get(iter).isLocked) return;

                    currentSelectedLayerIndex = iter;
                    selectItem(iter);
                    stage.getSandbox().getSelector().selectItemsByLayerName(layers.get(iter).layerName);

                    if (getTapCount() == 2) {
                        showRenameDialog();
                    }
                    if (layers.get(iter).isLocked || layers.get(iter).isVisible) {
                        return;
                    }

                }
            };

            listener.setTapCountInterval(0.5f);
            itm.addListener(listener);
        }
    }

    private void showRenameDialog() {
        InputDialog dlg = stage.dialogs().showInputDialog();

        dlg.setDescription("New name for your layer");

        dlg.setListener(new InputDialogListener() {

            @Override
            public void onConfirm(String input) {
                if (checkIfNameIsUnique(input)) {
                    LayerItemVO layerVo = layers.get(currentSelectedLayerIndex);
                    layerVo.layerName = input;
                    initContent();
                } else {
                    // show error dialog
                }
            }
        });
    }

    private boolean checkIfNameIsUnique(String name) {
        for (int i = 0; i < layers.size(); i++) {
            if (layers.get(i).layerName.equals(name)) {
                return false;
            }
        }

        return true;
    }

    public void selectItem(int index) {
        for (int i = 0; i < layerActors.size(); i++) {
            layerActors.get(i).unselect();
            if (index == i) {
                layerActors.get(i).select();
            }
        }
    }

    public void unSelectItem(int index) {
        layerActors.get(index).unselect();
        for (int i = 0; i < layerActors.size(); i++) {
            if (index != i) {
                layerActors.get(i).select();
                return;
            }
        }
    }

    private void initBottomBar() {

        Image barBg = new Image(stage.textureManager.getEditorAsset("minibar"));
        barBg.setScaleX(getWidth() - 3);
        barBg.setX(3);
        barBg.setY(2);

        uiGroup.addActor(barBg);

        Button newLayerBtn = stage.textureManager.createImageButton("nbtn", "nbtnHover", "nbtnPressed");
        newLayerBtn.setX(5);
        newLayerBtn.setY(3);
        uiGroup.addActor(newLayerBtn);

        Button delLayerBtn = stage.textureManager.createImageButton("dlt", "dltHover", "dltPressed");
        delLayerBtn.setX(newLayerBtn.getX() + newLayerBtn.getWidth());
        delLayerBtn.setY(3);
        uiGroup.addActor(delLayerBtn);

        delLayerBtn.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if (layers == null) return;
                if (currentSelectedLayerIndex != -1 && !layers.get(currentSelectedLayerIndex).layerName.equals("Default")) {
                    layers.remove(currentSelectedLayerIndex);
                    initContent();
                }
            }
        });

        newLayerBtn.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if (layers == null) return;
                InputDialog dlg = stage.dialogs().showInputDialog();

                dlg.setDescription("Please set unique name for your Layer");

                dlg.setListener(new InputDialogListener() {

                    @Override
                    public void onConfirm(String input) {
                        if (checkIfNameIsUnique(input)) {
                            LayerItemVO layerVo = new LayerItemVO();
                            layerVo.layerName = input;
                            layers.add(layerVo);
                            currentSelectedLayerIndex = layers.indexOf(layerVo);
                            initContent();
                        } else {
                            // show error dialog
                        }
                    }
                });
            }
        });

    }

    public void arrangeLayers(LayerItem sourceLayerItem, LayerItem targetLayerItem) {
        int sourceIndex = layerActors.indexOf(sourceLayerItem);
        int targetIndex = layerActors.indexOf(targetLayerItem);
        LayerItemVO sourceLayerItemVo = layers.get(sourceIndex);
        if (targetIndex < sourceIndex) {
            for (int i = sourceIndex; i > targetIndex; --i) {
                layers.set(i, layers.get(i - 1));
            }
            layers.set(targetIndex, sourceLayerItemVo);

        } else {
            for (int i = sourceIndex; i < targetIndex - 1; ++i) {
                layers.set(i, layers.get(i + 1));
            }
            layers.set(targetIndex - 1, sourceLayerItemVo);
        }
        initContent();
        stage.getSandbox().getCurrentScene().sortZindexes();
        selectLayerByName(sourceLayerItem.getLayerName());
    }

    public void startLayerDragging(LayerItem layerItem) {
        isLayerDragging = true;
        stage.sandboxStage.setCursor(Cursor.HAND_CURSOR);
    }

//    @Override
//    public void draw(SpriteBatch batch, float parentAlpha) {
//        batch.draw(sprite, getX(), getY(), 0f, 0f, getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
//    }

    public void stopLayerDragging(LayerItem layerItem) {
        stage.sandboxStage.setCursor(Cursor.DEFAULT_CURSOR);
    }

    @Override
    protected void expand() {
        setHeight(expandedHeight);
        if (contentGroup != null) {
            contentGroup.setVisible(true);
        }
        uiGroup.setVisible(true);
    }

    @Override
    protected void collapse() {
        setHeight(topImg.getHeight());
        if (contentGroup != null) {
            contentGroup.setVisible(false);
        }
        uiGroup.setVisible(false);
    }
}
