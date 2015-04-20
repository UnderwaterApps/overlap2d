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

import com.badlogic.gdx.utils.Array;
import com.uwsoft.editor.mvc.view.ui.properties.UIItemPropertiesMediator;
import com.uwsoft.editor.renderer.actor.SpriteAnimation;

/**
 * Created by azakhary on 4/16/2015.
 */
public class UISpriteAnimationItemPropertiesMediator extends UIItemPropertiesMediator<SpriteAnimation, UISpineAnimationItemProperties> {
    private static final String TAG = UISpriteAnimationItemPropertiesMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    public UISpriteAnimationItemPropertiesMediator() {
        super(NAME, new UISpineAnimationItemProperties());
    }

    @Override
    protected void translateObservableDataToView(SpriteAnimation item) {
        Array<String> animations = new Array<>();
        for (String name : item.getAnimations().keySet()) {
            animations.add(name);
        }

        viewComponent.setAnimations(animations);
        viewComponent.setSelectedAnimation(item.getCurrentAnimationName());
    }

    @Override
    protected void translateViewToItemData() {
        observableReference.setAnimation(viewComponent.getSelected());
        observableReference.renew();
    }
}
