package com.uwsoft.editor.view.ui.properties.panels;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.PolygonComponent;
import com.uwsoft.editor.renderer.components.TextureRegionComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.renderer.utils.PolygonUtils;
import com.uwsoft.editor.view.stage.Sandbox;
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
                textureRegionComponent.setPolygonSprite(polygonComponent);
                dimensionsComponent.setPolygon(polygonComponent);
            }
        } else {
            textureRegionComponent.polygonSprite = null;
            textureRegionComponent.isPolygon = false;
            dimensionsComponent.polygon = null;
        }
    }
}
