/*
 * ******************************************************************************
 *  * Copyright 2015 See AUTHORS file.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

package com.uwsoft.editor.view.ui.properties.panels;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.commons.MsgAPI;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.renderer.components.CompositeTransformComponent;
import com.uwsoft.editor.renderer.systems.CompositeSystem;
import com.uwsoft.editor.view.stage.Sandbox;
import com.uwsoft.editor.view.ui.properties.UIItemPropertiesMediator;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by azakhary on 4/16/2015.
 */
public class UICompositeItemPropertiesMediator extends UIItemPropertiesMediator<Entity, UICompositeItemProperties> {

    private static final String TAG = UICompositeItemPropertiesMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    public UICompositeItemPropertiesMediator() {
        super(NAME, new UICompositeItemProperties());
    }

    @Override
    protected void translateObservableDataToView(Entity item) {
        viewComponent.setAutomaticResize(item.getComponent(CompositeTransformComponent.class).automaticResize);
    }

    @Override
    protected void translateViewToItemData() {
        CompositeTransformComponent component = observableReference.getComponent(CompositeTransformComponent.class);
        component.automaticResize = viewComponent.isAutomaticResizeIsEnabled();

        CompositeSystem compositeSystem = Sandbox.getInstance().getEngine().getSystem(CompositeSystem.class);
        if (compositeSystem != null) {
            compositeSystem.processEntity(observableReference, Gdx.graphics.getDeltaTime());
        }

        Set<Entity> entityHashSet = new HashSet<>();
        entityHashSet.add(observableReference);
        Overlap2DFacade.getInstance().sendNotification(MsgAPI.ITEM_SELECTION_CHANGED, entityHashSet);
    }
}