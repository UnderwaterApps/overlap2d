package com.uwsoft.editor.gdx.ui.layer;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.uwsoft.editor.data.manager.TextureManager;
import com.uwsoft.editor.gdx.stage.SandboxStage;
import com.uwsoft.editor.renderer.data.LayerItemVO;

public class LayerItem extends Group {

    private Image bgImg;

    private Image lock;
    private Image eye;

    private LayerItemVO layerItemVo;
    private SandboxStage sandboxStage;
    private Image rowSeparator;

    public LayerItem(LayerItemVO vo, SandboxStage s) {

        this.layerItemVo = vo;
        this.sandboxStage = s;
        setWidth(247);
        setHeight(20);

        bgImg = new Image(TextureManager.getInstance().getEditorAsset("pixel"));
        bgImg.setColor(0.425f, 0.425f, 0.425f, 1.0f);
        bgImg.setScaleX(getWidth());
        bgImg.setScaleY(getHeight());
        addActor(bgImg);

        Image bottom = new Image(TextureManager.getInstance().getEditorAsset("pixel"));
        bottom.setColor(0.125f, 0.125f, 0.125f, 1.0f);
        bottom.setScaleX(getWidth());
        addActor(bottom);

        Image sep1 = new Image(TextureManager.getInstance().getEditorAsset("pixel"));
        sep1.setColor(0.225f, 0.225f, 0.225f, 1.0f);
        sep1.setScaleY(getHeight());
        sep1.setX(20);
        addActor(sep1);

        Image sep2 = new Image(TextureManager.getInstance().getEditorAsset("pixel"));
        sep2.setColor(0.225f, 0.225f, 0.225f, 1.0f);
        sep2.setScaleY(getHeight());
        sep2.setX(40);
        addActor(sep2);

        lock = new Image(TextureManager.getInstance().getEditorAsset("lock"));
        lock.setX(4);
        lock.setY(4);
        addActor(lock);

        eye = new Image(TextureManager.getInstance().getEditorAsset("eye"));
        eye.setX(24);
        eye.setY(5);
        addActor(eye);

        Label lbl = new Label(layerItemVo.layerName, TextureManager.getInstance().editorSkin);
        lbl.setX(44);
        lbl.setY(4);
        addActor(lbl);

        updateUI();

    }

    public LayerItem(LayerItem layerItem) {
        this(layerItem.layerItemVo, layerItem.sandboxStage);
    }

    public String getLayerName() {
        return layerItemVo.layerName;
    }

    public void initListeners() {
        lock.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                layerItemVo.isLocked = !layerItemVo.isLocked;
                updateUI();
                sandboxStage.getCurrentScene().reAssembleLayers();
                sandboxStage.uiStage.getLayerPanel().initContent();
            }
        });
        eye.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                layerItemVo.isVisible = !layerItemVo.isVisible;
                updateUI();
                sandboxStage.getCurrentScene().reAssembleLayers();
                sandboxStage.uiStage.getLayerPanel().initContent();
            }
        });
    }

    public void showLayerRowSeparator() {
        if (rowSeparator == null) {
            rowSeparator = new Image(sandboxStage.textureManager.getEditorAsset("pixel"));
            rowSeparator.setColor(0.97f, 0.97f, 0.98f, 1.0f);
            rowSeparator.setScaleX(getWidth());
            rowSeparator.setScaleY(2);
        }
        addActor(rowSeparator);
    }

    public void hideLayerRowSeparator() {
        removeActor(rowSeparator);
    }

    public void select() {
        bgImg.setColor(90f / 255f, 102f / 255f, 121f / 255f, 1.0f);
    }

    public void unselect() {
        bgImg.setColor(0.425f, 0.425f, 0.425f, 1.0f);
    }

    public void updateUI() {
        if (!layerItemVo.isLocked) {
            lock.getColor().a = 0.5f;
        } else {
            lock.getColor().a = 1;
        }
        if (!layerItemVo.isVisible) {
            eye.getColor().a = 0.5f;
        } else {
            eye.getColor().a = 1;
        }
    }

}
