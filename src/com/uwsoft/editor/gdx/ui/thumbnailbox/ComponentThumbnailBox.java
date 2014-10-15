package com.uwsoft.editor.gdx.ui.thumbnailbox;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.uwsoft.editor.gdx.stage.UIStage;
import com.uwsoft.editor.gdx.ui.payloads.AssetPayloadObject;

/**
 * Created by azakhary on 7/3/2014.
 */
public class ComponentThumbnailBox extends DraggableThumbnailBox {

    public ComponentThumbnailBox(UIStage s, float width, String text) {
        super(s);
        setWidth(width);

        Label lbl = new Label(text, stage.textureManager.editorSkin);
        lbl.setWidth(getWidth());
        addActor(lbl);

        setHeight(lbl.getHeight());
        rc.setVisible(false);

        Label payloadLbl = new Label(text, stage.textureManager.editorSkin);
        AssetPayloadObject payload = new AssetPayloadObject();
        payload.assetName = text;
        payload.type = AssetPayloadObject.AssetType.Component;

        DraggableThumbnailEvent event = new DraggableThumbnailEvent() {
            @Override
            public void drop(AssetPayloadObject pld, float x, float y) {
                stage.sandboxStage.createComponent(pld.assetName, x, y);
            }
        };

        initDragDrop(stage, payloadLbl, payload, event);
    }
}
