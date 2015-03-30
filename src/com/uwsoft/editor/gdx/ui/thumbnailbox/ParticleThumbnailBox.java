package com.uwsoft.editor.gdx.ui.thumbnailbox;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.DataManager;
import com.uwsoft.editor.gdx.stage.UIStage;
import com.uwsoft.editor.gdx.ui.payloads.AssetPayloadObject;

/**
 * Created by azakhary on 7/3/2014.
 */
public class ParticleThumbnailBox extends DraggableThumbnailBox {
    private final Overlap2DFacade facade;
    private final DataManager dataManager;

    public ParticleThumbnailBox(UIStage s, String particleName) {
        super(s);
        facade = Overlap2DFacade.getInstance();
        dataManager = facade.retrieveProxy(DataManager.NAME);
        Image img = new Image(dataManager.textureManager.getEditorAsset("resizeIconChecked"));

        this.setWidth(thumbnailSize);
        this.setHeight(thumbnailSize);

        img.setX(getWidth()/2-img.getWidth()/2);
        img.setY(getHeight()/2-img.getHeight()/2);

        addActor(img);


        Image payloadImg = new Image(dataManager.textureManager.getEditorAsset("resizeIconChecked"));
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
