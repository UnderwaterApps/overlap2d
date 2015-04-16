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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.uwsoft.editor.mvc.view.ui.properties.UIItemPropertiesMediator;
import com.uwsoft.editor.renderer.actor.IBaseItem;
import com.uwsoft.editor.renderer.data.MainItemVO;

/**
 * Created by azakhary on 4/15/2015.
 */
public class UIBasicItemPropertiesMediator extends UIItemPropertiesMediator<IBaseItem, UIBasicItemProperties> {
    private static final String TAG = UIBasicItemPropertiesMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    public UIBasicItemPropertiesMediator() {
        super(NAME, new UIBasicItemProperties());
    }


    protected void translateItemDataToView(IBaseItem item) {
        MainItemVO vo = item.getDataVO();

        Actor itemAsActor = (Actor) item;

        viewComponent.setIdBoxValue(vo.itemIdentifier);
        viewComponent.setXValue(vo.x + "");
        viewComponent.setYValue(vo.y + "");
        viewComponent.setFlipH(vo.isFlipedH);
        viewComponent.setFlipV(vo.isFlipedV);
        viewComponent.setWidthValue(itemAsActor.getWidth() + "");
        viewComponent.setHeightValue(itemAsActor.getHeight() + "");
        viewComponent.setRotationValue(vo.rotation + "");
        viewComponent.setScaleXValue(vo.scaleX + "");
        viewComponent.setScaleYValue(vo.scaleY + "");
        viewComponent.setTintColor(new Color(vo.tint[0], vo.tint[1], vo.tint[1], vo.tint[2]));
    }
}
