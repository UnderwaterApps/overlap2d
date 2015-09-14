package com.uwsoft.editor.view.ui.properties.panels;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.PolygonComponent;
import com.uwsoft.editor.renderer.components.TextureRegionComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.view.ui.properties.UIItemPropertiesMediator;

/**
 * Created by azakhary on 8/2/2015.
 */
public class UIImageItemPropertiesMediator extends UIItemPropertiesMediator<Entity, UIImageItemProperties> {
    private static final String TAG = UIImageItemPropertiesMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    private TextureRegionComponent textureRegionComponent;

    public UIImageItemPropertiesMediator() {
        super(NAME, new UIImageItemProperties());
    }

    @Override
    protected void translateObservableDataToView(Entity item) {
        textureRegionComponent = ComponentRetriever.get(item, TextureRegionComponent.class);

        if(textureRegionComponent.isRepeat) {
            viewComponent.setRenderMode("REPEAT");
        } else {
            viewComponent.setRenderMode("STRETCH");
        }

        if(textureRegionComponent.isPolygon) {
            viewComponent.setSpriteType("POLYGON");
        } else {
            viewComponent.setSpriteType("SQUARE");
        }
    }

    @Override
    protected void translateViewToItemData() {
        if(viewComponent.getRenderMode().equals("REPEAT")) {
            textureRegionComponent.isRepeat = true;
        } else {
            textureRegionComponent.isRepeat = false;
        }
        DimensionsComponent dimensionsComponent = ComponentRetriever.get(observableReference, DimensionsComponent.class);

        if(viewComponent.getSpriteType().equals("POLYGON")) {
            textureRegionComponent.isPolygon = true;
            PolygonComponent polygonComponent = ComponentRetriever.get(observableReference, PolygonComponent.class);

            if (polygonComponent != null && polygonComponent.vertices != null) {
            	float ppwu = dimensionsComponent.width/textureRegionComponent.region.getRegionWidth();
                textureRegionComponent.setPolygonSprite(polygonComponent,1f/ppwu);
                dimensionsComponent.setPolygon(polygonComponent);
            }
        } else {
            textureRegionComponent.polygonSprite = null;
            textureRegionComponent.isPolygon = false;
            dimensionsComponent.polygon = null;
        }
    }
}
