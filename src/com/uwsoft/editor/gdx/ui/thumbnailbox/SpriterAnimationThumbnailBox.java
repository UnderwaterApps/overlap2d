package com.uwsoft.editor.gdx.ui.thumbnailbox;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.uwsoft.editor.data.manager.TextureManager;
import com.uwsoft.editor.gdx.stage.UIStage;
import com.uwsoft.editor.gdx.ui.payloads.AssetPayloadObject;
import com.uwsoft.editor.renderer.actor.SpriterActor;
import com.uwsoft.editor.renderer.data.SpriterVO;

/**
 * Created by hayk on 19/12/2014.
 */
public class SpriterAnimationThumbnailBox extends DraggableThumbnailBox {


    private AssetPayloadObject payload;

    private float scaleSize = 1;

    private boolean isMouseInside = true;

    public SpriterAnimationThumbnailBox(UIStage s, String animationName) {
        super(s);

        SpriterVO vo = new SpriterVO();
        vo.animationName = animationName;
        SpriterActor animThumb = new SpriterActor(vo, s.sceneLoader.essentials);

        if(animThumb.getWidth() > thumbnailSize || animThumb.getHeight() > thumbnailSize) {
            // resizing is needed
            float scaleFactor = 1.0f;
            if(animThumb.getWidth() > animThumb.getHeight()) {
                //scale by width
                scaleFactor = 1.0f/(animThumb.getWidth()/thumbnailSize);
            } else {
                scaleFactor = 1.0f/(animThumb.getHeight()/thumbnailSize);
            }
            scaleSize = scaleFactor;
            animThumb.setSpriterScale(scaleFactor);

//            animThumb.setX((getWidth()-animThumb.getWidth())/2);
//            animThumb.setY((getHeight()-animThumb.getHeight())/2);
            animThumb.setX(0);
            animThumb.setY(0);
        } else {
            // put it in middle
            animThumb.setX(0);
            animThumb.setY(0);
        }

        addActor(animThumb);

        Image payloadImg = new Image(TextureManager.getInstance().getEditorAsset("resizeIconChecked"));
        payload = new AssetPayloadObject();
        payload.assetName = animationName;
        payload.type = AssetPayloadObject.AssetType.Spriter;

        DraggableThumbnailEvent event = new DraggableThumbnailEvent() {
            @Override
            public void drop(AssetPayloadObject pld, float x, float y) {
                 stage.sandboxStage.createSpriterAnimation(payload.assetName, x, y);
            }
        };

        initDragDrop(stage, payloadImg, payload, event);

        setWidth(thumbnailSize);
        setHeight(thumbnailSize);

        super.act(1f);
    }

    @Override
    public void act(float delta) {
        if(isMouseInside) {
            super.act(delta);
        }
    }
}
