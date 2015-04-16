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

package com.uwsoft.editor.mvc.view.ui.properties.boxes;

import com.uwsoft.editor.mvc.view.ui.properties.UIAbstractPropertiesMediator;
import com.uwsoft.editor.renderer.data.PhysicsPropertiesVO;
import com.uwsoft.editor.renderer.data.SceneVO;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * Created by azakhary on 4/16/2015.
 */
public class UIScenePropertiesMediator extends UIAbstractPropertiesMediator<SceneVO, UISceneProperties> {
    private static final String TAG = UIScenePropertiesMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    public UIScenePropertiesMediator() {
        super(NAME, new UISceneProperties());
    }

    protected void translateObservableDataToView(SceneVO item) {
        PhysicsPropertiesVO physicsVO = item.physicsPropertiesVO;

        viewComponent.setGravityXValue(physicsVO.gravityX + "");
        viewComponent.setGravityYValue(physicsVO.gravityY + "");
        viewComponent.setPhysicsEnable(physicsVO.enabled);
        viewComponent.setSleepVelocityValue(physicsVO.sleepVelocity + "");
    }

    @Override
    protected void translateViewToItemData() {
        PhysicsPropertiesVO physicsVO = observableReference.physicsPropertiesVO;
        physicsVO.gravityX = NumberUtils.toFloat(viewComponent.getGravityXValue(), physicsVO.gravityX);
        physicsVO.gravityY = NumberUtils.toFloat(viewComponent.getGravityYValue(), physicsVO.gravityY);
        physicsVO.sleepVelocity = NumberUtils.toFloat(viewComponent.getSleepVelocityValue(), physicsVO.sleepVelocity);
        physicsVO.enabled = viewComponent.isPhysicsEnabled();
    }
}
