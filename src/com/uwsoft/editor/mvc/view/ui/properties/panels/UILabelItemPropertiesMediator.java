package com.uwsoft.editor.mvc.view.ui.properties.panels;

import org.apache.commons.lang3.math.NumberUtils;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.FontManager;
import com.uwsoft.editor.mvc.proxy.ResourceManager;
import com.uwsoft.editor.mvc.view.ui.properties.UIItemPropertiesMediator;
import com.uwsoft.editor.renderer.actor.LabelItem;

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
        viewComponent.setFontFamilyList(fontManager.getFontNamesFromMap());
    }

    @Override
    protected void translateObservableDataToView(Entity entity) {
    	
    	//TODO
        viewComponent.setFontFamily(item.dataVO.style);
        viewComponent.setFontSize(item.dataVO.size + "");
    }

    @Override
    protected void translateViewToItemData() {
        ResourceManager resourceManager = facade.retrieveProxy(ResourceManager.NAME);
        resourceManager.prepareEmbeddingFont(viewComponent.getFontFamily(), NumberUtils.toInt(viewComponent.getFontSize()));

        String shortFontName = fontManager.getShortName(viewComponent.getFontFamily());
        observableReference.setStyle(shortFontName, NumberUtils.toInt(viewComponent.getFontSize()));
    }
}
