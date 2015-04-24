package com.uwsoft.editor.mvc.view.ui.properties.panels;

import com.uwsoft.editor.mvc.view.ui.properties.UIItemPropertiesMediator;
import com.uwsoft.editor.renderer.actor.CompositeItem;
import com.uwsoft.editor.renderer.actor.LabelItem;

/**
 * Created by avetiszakharyan on 4/24/15.
 */
public class UILabelItemPropertiesMediator extends UIItemPropertiesMediator<LabelItem, UILabelItemProperties> {

    private static final String TAG = UILabelItemPropertiesMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    public UILabelItemPropertiesMediator() {
        super(NAME, new UILabelItemProperties());
    }

    @Override
    protected void translateObservableDataToView(LabelItem item) {

    }

    @Override
    protected void translateViewToItemData() {

    }
}
