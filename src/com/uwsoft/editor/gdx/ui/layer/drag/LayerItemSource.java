package com.uwsoft.editor.gdx.ui.layer.drag;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;
import com.uwsoft.editor.gdx.ui.layer.LayerItem;
import com.uwsoft.editor.gdx.ui.layer.UILayerBox;

/**
 * Created by sargis on 7/2/14.
 */
public class LayerItemSource extends Source {

    private final LayerItem layerItem;
    private final UILayerBox uiLayerBox;

    public LayerItemSource(LayerItem layerItem, UILayerBox uiLayerBox) {
        super(layerItem);
        this.layerItem = layerItem;
        this.uiLayerBox = uiLayerBox;
    }

    public Payload dragStart(InputEvent event, float x, float y, int pointer) {
        Payload payload = new Payload();
        Actor dragActor = new LayerItem(layerItem);
        dragActor.getColor().a = 0.7f;
        payload.setDragActor(dragActor);

//
//        Actor validDragActor = new Image(icon);
        //payload.setValidDragActor(layerItem);
//
//        Actor invalidDragActor = new Image(icon);
        //payload.setInvalidDragActor(actor);
        uiLayerBox.startLayerDragging(layerItem);
        return payload;
    }

    @Override
    public void dragStop(InputEvent event, float x, float y, int pointer, Payload payload, Target target) {
        uiLayerBox.stopLayerDragging(layerItem);
    }
}
