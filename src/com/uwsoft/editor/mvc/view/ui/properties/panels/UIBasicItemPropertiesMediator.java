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

package com.uwsoft.editor.mvc.view.ui.properties.panels;

import java.util.HashMap;

import com.uwsoft.editor.mvc.view.stage.input.InputListenerComponent;
import com.uwsoft.editor.utils.runtime.ComponentRetriever;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.kotcrab.vis.ui.widget.color.ColorPicker;
import com.kotcrab.vis.ui.widget.color.ColorPickerAdapter;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.mvc.view.ui.properties.UIAbstractProperties;
import com.uwsoft.editor.mvc.view.ui.properties.UIItemPropertiesMediator;
import com.uwsoft.editor.renderer.EntityFactory;
import com.uwsoft.editor.renderer.conponents.DimensionsComponent;
import com.uwsoft.editor.renderer.conponents.MainItemComponent;
import com.uwsoft.editor.renderer.conponents.TintComponent;
import com.uwsoft.editor.renderer.conponents.TransformComponent;

/**
 * Created by azakhary on 4/15/2015.
 */
public class UIBasicItemPropertiesMediator extends UIItemPropertiesMediator<Entity, UIBasicItemProperties> {
    private static final String TAG = UIBasicItemPropertiesMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    private TransformComponent transformComponent;
    private MainItemComponent mainItemComponent;
    private DimensionsComponent dimensionComponent;
    private TintComponent tintComponent;

    private HashMap<String, UIBasicItemProperties.ItemType> itemTypeMap = new HashMap<>();

    public UIBasicItemPropertiesMediator() {
        super(NAME, new UIBasicItemProperties());
    }

    @Override
    public void onRegister() {
        itemTypeMap.put("ENTITY_"+EntityFactory.COMPOSITE_TYPE, UIBasicItemProperties.ItemType.composite);
        itemTypeMap.put("ENTITY_"+EntityFactory.IMAGE_TYPE, UIBasicItemProperties.ItemType.texture);
        itemTypeMap.put("ENTITY_"+EntityFactory.PARTICLE_TYPE, UIBasicItemProperties.ItemType.particle);
        itemTypeMap.put("ENTITY_"+EntityFactory.LABEL_TYPE, UIBasicItemProperties.ItemType.text);
        itemTypeMap.put("ENTITY_"+EntityFactory.SPRITE_TYPE, UIBasicItemProperties.ItemType.spriteAnimation);
        itemTypeMap.put("ENTITY_"+EntityFactory.SPRITER_TYPE, UIBasicItemProperties.ItemType.spriterAnimation);
        itemTypeMap.put("ENTITY_"+EntityFactory.SPINE_TYPE, UIBasicItemProperties.ItemType.spineAnimation);
        itemTypeMap.put("ENTITY_"+EntityFactory.LIGHT_TYPE, UIBasicItemProperties.ItemType.light);
    }

    @Override
    public String[] listNotificationInterests() {
        String[] defaultNotifications = super.listNotificationInterests();
        String[] notificationInterests = new String[]{
                UIBasicItemProperties.TINT_COLOR_BUTTON_CLICKED,
        };

        return ArrayUtils.addAll(defaultNotifications, notificationInterests);
    }

    @Override
    public void handleNotification(Notification notification) {
        super.handleNotification(notification);

        switch (notification.getName()) {
            case UIBasicItemProperties.TINT_COLOR_BUTTON_CLICKED:
                ColorPicker picker = new ColorPicker(new ColorPickerAdapter() {
                    @Override
                    public void finished(Color newColor) {
                        viewComponent.setTintColor(newColor);
                        facade.sendNotification(UIAbstractProperties.PROPERTIES_UPDATED);
                    }
                });

                picker.setColor(viewComponent.getTintColor());
                Sandbox.getInstance().getUIStage().addActor(picker.fadeIn());

                break;
            default:
                break;
        }
    }

    protected void translateObservableDataToView(Entity entity) {
    	transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
    	mainItemComponent = ComponentRetriever.get(entity, MainItemComponent.class);
    	dimensionComponent = ComponentRetriever.get(entity, DimensionsComponent.class);
    	tintComponent = ComponentRetriever.get(entity, TintComponent.class);

        viewComponent.setItemType(itemTypeMap.get("ENTITY_"+entity.flags));
        viewComponent.setIdBoxValue(mainItemComponent.itemIdentifier);
        viewComponent.setXValue(transformComponent.x + "");
        viewComponent.setYValue(transformComponent.y + "");
        //TODO no flip anymore
        //viewComponent.setFlipH(vo.isFlipedH);
        //viewComponent.setFlipV(vo.isFlipedV);
        
        viewComponent.setWidthValue(dimensionComponent.width + "");
        viewComponent.setHeightValue(dimensionComponent.height + "");
        viewComponent.setRotationValue(transformComponent.rotation + "");
        viewComponent.setScaleXValue(transformComponent.scaleX + "");
        viewComponent.setScaleYValue(transformComponent.scaleY + "");
        viewComponent.setTintColor(tintComponent.color);
    }

    @Override
    protected void translateViewToItemData() {
        //MainItemVO vo = observableReference.getDataVO();
    	Entity entity  = ((Entity) observableReference);

        transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
        mainItemComponent = ComponentRetriever.get(entity, MainItemComponent.class);
        dimensionComponent = ComponentRetriever.get(entity, DimensionsComponent.class);
        tintComponent = ComponentRetriever.get(entity, TintComponent.class);

    	mainItemComponent.itemIdentifier = viewComponent.getIdBoxValue();
    	transformComponent.x = NumberUtils.toFloat(viewComponent.getXValue(), transformComponent.x);
    	transformComponent.y = NumberUtils.toFloat(viewComponent.getYValue(), transformComponent.y);
    	
    	//TODO nor more flip
    	//vo.isFlipedH = viewComponent.getFlipH();
    	//vo.isFlipedV = viewComponent.getFlipV();
    	
        // TODO: manage width and height
    	transformComponent.rotation = NumberUtils.toFloat(viewComponent.getRotationValue(), transformComponent.rotation);
    	transformComponent.scaleX = (viewComponent.getFlipH() ? -1 : 1) * NumberUtils.toFloat(viewComponent.getScaleXValue(), transformComponent.scaleX);
    	transformComponent.scaleY = (viewComponent.getFlipV() ? -1 : 1) * NumberUtils.toFloat(viewComponent.getScaleYValue(), transformComponent.scaleY);
        Color color = viewComponent.getTintColor();
        tintComponent.tint[0] = color.r;
        tintComponent.tint[1] = color.g;
        tintComponent.tint[2] = color.b;
        tintComponent.tint[3] = color.a;
    }
}
