package com.uwsoft.editor.gdx.ui.thumbnailbox;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.uwsoft.editor.data.manager.TextureManager;
import com.uwsoft.editor.gdx.stage.UIStage;
import com.uwsoft.editor.gdx.ui.payloads.AssetPayloadObject;

/**
 * Created by azakhary on 7/3/2014.
 */
public class ParticleThumbnailBox extends DraggableThumbnailBox {
    public ParticleThumbnailBox(UIStage s, String particleName) {
        super(s);

        Image img = new Image(TextureManager.getInstance().getEditorAsset("resizeIconChecked"));

        this.setWidth(thumbnailSize);
        this.setHeight(thumbnailSize);

        img.setX(getWidth()/2-img.getWidth()/2);
        img.setY(getHeight()/2-img.getHeight()/2);

        addActor(img);


        Image payloadImg = new Image(TextureManager.getInstance().getEditorAsset("resizeIconChecked"));
        AssetPayloadObject payload = new AssetPayloadObject();
        payload.assetName = particleName;
        payload.type = AssetPayloadObject.AssetType.Particle;

        DraggableThumbnailEvent event = new DraggableThumbnailEvent() {
            @Override
            public void drop(AssetPayloadObject pld, float x, float y) {
                stage.getSandbox().getUac().createParticleItem(pld.assetName, x, y);
            }
        };

        initDragDrop(stage, payloadImg, payload, event);
    }
}
