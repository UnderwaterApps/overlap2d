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
import com.kotcrab.vis.ui.widget.color.ColorPicker;
import com.kotcrab.vis.ui.widget.color.ColorPickerAdapter;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.mvc.view.ui.properties.UIItemPropertiesMediator;
import com.uwsoft.editor.renderer.actor.IBaseItem;
import com.uwsoft.editor.renderer.data.MainItemVO;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * Created by azakhary on 4/15/2015.
 */
public class UIBasicItemPropertiesMediator extends UIItemPropertiesMediator<IBaseItem, UIBasicItemProperties> {
    private static final String TAG = UIBasicItemPropertiesMediator.class.getCanonicalName();
    public static final String NAME = TAG;

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
                    }
                });

                picker.setColor(viewComponent.getTintColor());
                Sandbox.getInstance().getUIStage().addActor(picker.fadeIn());

                break;
            default:
                break;
        }
    }

    protected void translateObservableDataToView(IBaseItem item) {
        MainItemVO vo = item.getDataVO();

        Actor itemAsActor = (Actor) item;

        viewComponent.setIdBoxValue(vo.itemIdentifier);
        viewComponent.setXValue(itemAsActor.getX() + "");
        viewComponent.setYValue(itemAsActor.getY() + "");
        viewComponent.setFlipH(vo.isFlipedH);
        viewComponent.setFlipV(vo.isFlipedV);
        viewComponent.setWidthValue(itemAsActor.getWidth() + "");
        viewComponent.setHeightValue(itemAsActor.getHeight() + "");
        viewComponent.setRotationValue(vo.rotation + "");
        viewComponent.setScaleXValue(vo.scaleX + "");
        viewComponent.setScaleYValue(vo.scaleY + "");
        viewComponent.setTintColor(new Color(vo.tint[0], vo.tint[1], vo.tint[2], vo.tint[3]));
    }

    @Override
    protected void translateViewToItemData() {
        MainItemVO vo = observableReference.getDataVO();

        vo.itemIdentifier = viewComponent.getIdBoxValue();
        vo.x = NumberUtils.toFloat(viewComponent.getXValue(), vo.x);
        vo.y = NumberUtils.toFloat(viewComponent.getYValue(), vo.y);
        vo.isFlipedH = viewComponent.getFlipH();
        vo.isFlipedV = viewComponent.getFlipV();
        // TODO: manage width and height
        vo.rotation = NumberUtils.toFloat(viewComponent.getRotationValue(), vo.rotation);
        vo.scaleX = (viewComponent.getFlipH() ? -1 : 1) * NumberUtils.toFloat(viewComponent.getScaleXValue(), vo.scaleX);
        vo.scaleY = (viewComponent.getFlipV() ? -1 : 1) * NumberUtils.toFloat(viewComponent.getScaleYValue(), vo.scaleY);
        Color color = viewComponent.getTintColor();
        vo.tint[0] = color.r;
        vo.tint[1] = color.g;
        vo.tint[2] = color.b;
        vo.tint[3] = color.a;

        observableReference.renew();

        Sandbox.getInstance().getSelector().updateSelections();
        Sandbox.getInstance().saveSceneCurrentSceneData();
    }
}
