package com.uwsoft.editor.gdx.ui.thumbnailbox;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.uwsoft.editor.data.manager.TextureManager;
import com.uwsoft.editor.gdx.actors.CustomDragAndDrop;
import com.uwsoft.editor.gdx.actors.basic.PixelRect;
import com.uwsoft.editor.gdx.stage.UIStage;
import com.uwsoft.editor.gdx.ui.payloads.AssetPayloadObject;

/**
 * Created by azakhary on 7/3/2014.
 */
public class DraggableThumbnailBox extends Group {

    protected UIStage stage;

    protected float thumbnailSize = 50;

    private DraggableThumbnailEvent draggingEvent;

    protected PixelRect rc;

    public interface DraggableThumbnailEvent {
        public void drop(AssetPayloadObject pld, float x, float y);
    }

    public DraggableThumbnailBox(UIStage stage) {
        this.stage = stage;
        rc = new PixelRect(TextureManager.getInstance(), thumbnailSize, thumbnailSize);
        rc.setFillColor(new Color(1, 1, 1, 0.2f));
        rc.setBorderColor(new Color(1, 1, 1, 0.4f));
        addActor(rc);
        setWidth(thumbnailSize);
        setHeight(thumbnailSize);
    }

    public void initDragDrop(final UIStage stage, final Actor thumbnail, final AssetPayloadObject payloadData, DraggableThumbnailEvent event) {
        initDragDrop(stage, thumbnail, 1, 1, payloadData, event);
    }

    public void initDragDrop(final UIStage stage, final Actor thumbnail, final float itemScaleX, final float itemScaleY, final AssetPayloadObject payloadData, DraggableThumbnailEvent event) {
        this.draggingEvent = event;
        final CustomDragAndDrop dragAndDrop = new CustomDragAndDrop();
        
        dragAndDrop.addSource(new CustomDragAndDrop.Source(this) {
            public CustomDragAndDrop.Payload dragStart (InputEvent event, float x, float y, int pointer) {
            	CustomDragAndDrop.Payload payload = new CustomDragAndDrop.Payload();

                payloadData.xOffset = thumbnail.getWidth()/2;
                payloadData.yOffset = thumbnail.getHeight()/2;
                payload.setDragActor(thumbnail);
                payload.setObject(payloadData);
                payload.setInvalidDragActor(null);
                dragAndDrop.setDragActorPosition(-thumbnail.getWidth()/2, thumbnail.getHeight()/2);

                return payload;
            }
        });

        dragAndDrop.addTarget(new CustomDragAndDrop.Target(stage.dummyTarget) {

            @Override
            public void drop(CustomDragAndDrop.Source source, CustomDragAndDrop.Payload payload, float x, float y, int pointer) {
                AssetPayloadObject pld = ((AssetPayloadObject)(payload.getObject()));
                draggingEvent.drop(pld, x - pld.xOffset, y - pld.yOffset);
            }

            @Override
            public boolean drag(CustomDragAndDrop.Source arg0, CustomDragAndDrop.Payload arg1, float arg2, float arg3, int arg4) {

                return true;
            }
        });
    }
}
