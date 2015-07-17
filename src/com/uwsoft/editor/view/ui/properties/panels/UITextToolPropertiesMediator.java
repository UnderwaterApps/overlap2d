package com.uwsoft.editor.view.ui.properties.panels;

import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.proxy.FontManager;
import com.uwsoft.editor.view.stage.tools.TextTool;
import com.uwsoft.editor.view.ui.properties.UIAbstractPropertiesMediator;

/**
 * Created by avetiszakharyan on 4/24/15.
 */
public class UITextToolPropertiesMediator extends UIAbstractPropertiesMediator<TextTool, UITextToolProperties> {

    private static final String TAG = UITextToolPropertiesMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    private FontManager fontManager;

    public UITextToolPropertiesMediator() {
        super(NAME, new UITextToolProperties());
    }

    @Override
    public void onRegister() {
        facade = Overlap2DFacade.getInstance();
        fontManager = facade.retrieveProxy(FontManager.NAME);
        viewComponent.setFontFamilyList(fontManager.getFontNamesFromMap());
    }

    @Override
    protected void translateObservableDataToView(TextTool item) {
        viewComponent.setFontFamily(item.getFontFamily());
        viewComponent.setFontSize(item.getFontSize());
    }

    @Override
    public void setItem(TextTool settings) {
        super.setItem(settings);
        observableReference.setFontFamily(viewComponent.getFontFamily());

    }

    @Override
    protected void translateViewToItemData() {
        observableReference.setFontFamily(viewComponent.getFontFamily());
        observableReference.setFontSize(viewComponent.getFontSize());
    }
}
