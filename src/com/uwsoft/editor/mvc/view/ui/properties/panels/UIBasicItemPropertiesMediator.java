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

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.kotcrab.vis.ui.widget.color.ColorPicker;
import com.kotcrab.vis.ui.widget.color.ColorPickerAdapter;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.mvc.view.ui.properties.UIAbstractProperties;
import com.uwsoft.editor.mvc.view.ui.properties.UIItemPropertiesMediator;
import com.uwsoft.editor.renderer.conponents.DimensionsComponent;
import com.uwsoft.editor.renderer.conponents.MainItemComponent;
import com.uwsoft.editor.renderer.conponents.TintComponent;
import com.uwsoft.editor.renderer.conponents.TransformComponent;
import com.uwsoft.editor.renderer.conponents.ViewPortComponent;
import com.uwsoft.editor.renderer.legacy.data.MainItemVO;

/**
 * Created by azakhary on 4/15/2015.
 */
public class UIBasicItemPropertiesMediator extends UIItemPropertiesMediator<Entity, UIBasicItemProperties> {
    private static final String TAG = UIBasicItemPropertiesMediator.class.getCanonicalName();
    public static final String NAME = TAG;
    
    private ComponentMapper<TransformComponent> transformMapper = ComponentMapper.getFor(TransformComponent.class);
    private ComponentMapper<MainItemComponent> mainItemMapper =  ComponentMapper.getFor(MainItemComponent.class);
    private ComponentMapper<DimensionsComponent> dimensionMapper =  ComponentMapper.getFor(DimensionsComponent.class);
    private ComponentMapper<TintComponent> tintMapper =  ComponentMapper.getFor(TintComponent.class);
    private TransformComponent trnasformComponent;
    private MainItemComponent mainItemComponent;
    private DimensionsComponent dimensionComponent;
    private TintComponent tintComponent;

    public UIBasicItemPropertiesMediator() {
        super(NAME, new UIBasicItemProperties());
    }

    @Override
    public String[] listNotificationInterests() {
        String[] defaultNotifications = super.listNotificationInterests();
        String[] notificationInterests = new String[]{
                UIBasicItemProperties.TINT_COLOR_BUTTON_CLICKED
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
    	trnasformComponent = transformMapper.get(entity);
    	mainItemComponent = mainItemMapper.get(entity);
    	dimensionComponent = dimensionMapper.get(entity);
    	tintComponent = tintMapper.get(entity);

        viewComponent.setIdBoxValue(mainItemComponent.itemIdentifier);
        viewComponent.setXValue(trnasformComponent.x + "");
        viewComponent.setYValue(trnasformComponent.y + "");
        //TODO no flip anymore
        //viewComponent.setFlipH(vo.isFlipedH);
        //viewComponent.setFlipV(vo.isFlipedV);
        
        viewComponent.setWidthValue(dimensionComponent.width + "");
        viewComponent.setHeightValue(dimensionComponent.height + "");
        viewComponent.setRotationValue(trnasformComponent.rotation + "");
        viewComponent.setScaleXValue(trnasformComponent.scaleX + "");
        viewComponent.setScaleYValue(trnasformComponent.scaleY + "");
        viewComponent.setTintColor(new Color(tintComponent.tint[0], tintComponent.tint[1], tintComponent.tint[2], tintComponent.tint[3]));
    }

    @Override
    protected void translateViewToItemData() {
        //MainItemVO vo = observableReference.getDataVO();
    	Entity entity  = ((Entity) observableReference);
    	
    	trnasformComponent = transformMapper.get(entity);
    	mainItemComponent = mainItemMapper.get(entity);
    	dimensionComponent = dimensionMapper.get(entity);
    	tintComponent = tintMapper.get(entity);

    	mainItemComponent.itemIdentifier = viewComponent.getIdBoxValue();
    	trnasformComponent.x = NumberUtils.toFloat(viewComponent.getXValue(), trnasformComponent.x);
    	trnasformComponent.y = NumberUtils.toFloat(viewComponent.getYValue(), trnasformComponent.y);
    	
    	//TODO nor more flip
    	//vo.isFlipedH = viewComponent.getFlipH();
    	//vo.isFlipedV = viewComponent.getFlipV();
    	
        // TODO: manage width and height
    	trnasformComponent.rotation = NumberUtils.toFloat(viewComponent.getRotationValue(), trnasformComponent.rotation);
    	trnasformComponent.scaleX = (viewComponent.getFlipH() ? -1 : 1) * NumberUtils.toFloat(viewComponent.getScaleXValue(), trnasformComponent.scaleX);
    	trnasformComponent.scaleY = (viewComponent.getFlipV() ? -1 : 1) * NumberUtils.toFloat(viewComponent.getScaleYValue(), trnasformComponent.scaleY);
        Color color = viewComponent.getTintColor();
        tintComponent.tint[0] = color.r;
        tintComponent.tint[1] = color.g;
        tintComponent.tint[2] = color.b;
        tintComponent.tint[3] = color.a;
    }
}
