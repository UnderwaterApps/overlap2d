package com.uwsoft.editor.mvc.view.ui.properties.panels;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.uwsoft.editor.renderer.components.label.LabelComponent;
import com.uwsoft.editor.renderer.factory.component.LabelComponentFactory;
import com.uwsoft.editor.utils.runtime.ComponentCloner;
import com.uwsoft.editor.utils.runtime.ComponentRetriever;
import org.apache.commons.lang3.math.NumberUtils;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.FontManager;
import com.uwsoft.editor.mvc.proxy.ResourceManager;
import com.uwsoft.editor.mvc.view.ui.properties.UIItemPropertiesMediator;

/**
 * Created by avetiszakharyan on 4/24/15.
 */
public class UILabelItemPropertiesMediator extends UIItemPropertiesMediator<Entity, UILabelItemProperties> {

    private static final String TAG = UILabelItemPropertiesMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    private FontManager fontManager;

    public UILabelItemPropertiesMediator() {
        super(NAME, new UILabelItemProperties());
    }

    @Override
    public void onRegister() {
        facade = Overlap2DFacade.getInstance();
        fontManager = facade.retrieveProxy(FontManager.NAME);
        lockUpdates = true;
        viewComponent.setFontFamilyList(fontManager.getFontNamesFromMap());
        lockUpdates = false;
    }

    @Override
    protected void translateObservableDataToView(Entity item) {
        LabelComponent labelComponent = ComponentRetriever.get(item, LabelComponent.class);
        viewComponent.setFontFamily(labelComponent.fontName);
        viewComponent.setFontSize(labelComponent.fontSize + "");
        viewComponent.setAlignValue(labelComponent.labelAlign);
    }

    @Override
    protected void translateViewToItemData() {
        ResourceManager resourceManager = facade.retrieveProxy(ResourceManager.NAME);
        resourceManager.prepareEmbeddingFont(viewComponent.getFontFamily(), NumberUtils.toInt(viewComponent.getFontSize()));

        Object[] payload = new Object[4];
        payload[0] = observableReference;
        payload[1] = viewComponent.getFontFamily();
        payload[2] = Integer.parseInt(viewComponent.getFontSize());
        payload[3] = viewComponent.getAlignValue();
        Overlap2DFacade.getInstance().sendNotification(Sandbox.ACTION_UPDATE_LABEL_DATA, payload);
    }

    @Override
    protected void afterItemDataModified() {

    }
}
