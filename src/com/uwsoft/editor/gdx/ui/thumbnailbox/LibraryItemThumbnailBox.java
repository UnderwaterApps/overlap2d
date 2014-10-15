package com.uwsoft.editor.gdx.ui.thumbnailbox;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.uwsoft.editor.data.manager.TextureManager;
import com.uwsoft.editor.gdx.stage.UIStage;
import com.uwsoft.editor.gdx.ui.payloads.AssetPayloadObject;
import com.uwsoft.editor.renderer.data.CompositeItemVO;

/**
 * Created by azakhary on 7/3/2014.
 */
public class LibraryItemThumbnailBox extends DraggableThumbnailBox {

    private final Image bgImg;
    private final Label label;
    private final Label payloadLbl;
    private final AssetPayloadObject payload;
    private final CompositeItemVO compositeItemVO;
    private String key;

    public LibraryItemThumbnailBox(UIStage s, float width, String key, CompositeItemVO compositeItemVO) {
        super(s);
        this.key = key;
        this.compositeItemVO = compositeItemVO;
        setWidth(width);
        bgImg = new Image(TextureManager.getInstance().getEditorAsset("pixel"));
        bgImg.setColor(0.425f, 0.425f, 0.425f, 1.0f);


        label = new Label(key, stage.textureManager.editorSkin);
        label.setWidth(getWidth());
        setHeight(label.getHeight());

        bgImg.setScaleX(getWidth());
        bgImg.setScaleY(getHeight());

        addActor(bgImg);
        addActor(label);
        rc.setVisible(false);

        payloadLbl = new Label(key, stage.textureManager.editorSkin);
        payload = new AssetPayloadObject();
        payload.assetName = key;
        payload.type = AssetPayloadObject.AssetType.Component;

        DraggableThumbnailEvent event = new DraggableThumbnailEvent() {
            @Override
            public void drop(AssetPayloadObject pld, float x, float y) {
                stage.sandboxStage.createItemFromLibrary(pld.assetName, x, y);
            }
        };

        initDragDrop(stage, payloadLbl, payload, event);
    }

    public void select() {
        bgImg.setColor(90f / 255f, 102f / 255f, 121f / 255f, 1.0f);
    }

    public void unselect() {
        bgImg.setColor(0.425f, 0.425f, 0.425f, 1.0f);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
        label.setText(key);
        payloadLbl.setText(key);
        payload.assetName = key;
    }

    public CompositeItemVO getCompositeItemVO() {
        return compositeItemVO;
    }
}
