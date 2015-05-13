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

import org.apache.commons.lang3.math.NumberUtils;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.mvc.view.ui.properties.UIItemPropertiesMediator;
import com.uwsoft.editor.renderer.conponents.light.LightObjectComponent;
import com.uwsoft.editor.renderer.legacy.data.LightVO;

/**
 * Created by azakhary on 4/28/2015.
 */
public class UILightItemPropertiesMediator extends UIItemPropertiesMediator<Entity, UILightItemProperties> {

    private static final String TAG = UILightItemPropertiesMediator.class.getCanonicalName();
    public static final String NAME = TAG;
    
    private ComponentMapper<LightObjectComponent> lightObjectMapper;
    private LightObjectComponent lightObjectComponent; 

    public UILightItemPropertiesMediator() {
        super(NAME, new UILightItemProperties());
        lightObjectMapper = ComponentMapper.getFor(LightObjectComponent.class);
    }

    @Override
    protected void translateObservableDataToView(Entity entity) {
    	lightObjectComponent = lightObjectMapper.get(entity);

        viewComponent.setType(lightObjectComponent.type);
        viewComponent.setRayCount(lightObjectComponent.rays);
        viewComponent.setStatic(lightObjectComponent.isStatic);
        viewComponent.setXRay(lightObjectComponent.isXRay);
        viewComponent.setRadius(lightObjectComponent.distance + "");
        viewComponent.setAngle(lightObjectComponent.coneDegree + "");
        viewComponent.setDistance(lightObjectComponent.distance + "");
    }

    @Override
    protected void translateViewToItemData() {
    	lightObjectComponent = lightObjectMapper.get(observableReference);

    	lightObjectComponent.type = viewComponent.getType();
    	lightObjectComponent.rays = viewComponent.getRayCount();
    	lightObjectComponent.isStatic = viewComponent.isStatic();
    	lightObjectComponent.isXRay = viewComponent.isXRay();

    	lightObjectComponent.coneDegree = NumberUtils.toFloat(viewComponent.getAngle());

        if(viewComponent.getType() == LightVO.LightType.POINT) {
        	lightObjectComponent.distance = NumberUtils.toFloat(viewComponent.getRadius());
        } else {
        	lightObjectComponent.distance = NumberUtils.toFloat(viewComponent.getDistance());
        }
    }
}
