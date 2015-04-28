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

package com.uwsoft.editor.mvc.view.ui.box;

import com.badlogic.gdx.utils.Align;
import com.puremvc.patterns.mediator.SimpleMediator;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.gdx.sandbox.ItemSelector;
import com.uwsoft.editor.gdx.sandbox.Sandbox;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by sargis on 4/10/15.
 */
public class UIAlignBoxMediator extends SimpleMediator<UIAlignBox> {
    private static final String TAG = UIAlignBoxMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    public UIAlignBoxMediator() {
        super(NAME, new UIAlignBox());
    }

    @Override
    public String[] listNotificationInterests() {
        return new String[]{
                UIAlignBox.ALIGN_TOP_BTN_CLICKED,
                UIAlignBox.ALIGN_LEFT_BTN_CLICKED,
                UIAlignBox.ALIGN_BOTTOM_BTN_CLICKED,
                UIAlignBox.ALIGN_RIGHT_BTN_CLICKED,
                UIAlignBox.ALIGN_CENTER_LEFT_BTN_CLICKED,
                UIAlignBox.ALIGN_CENTER_BOTTOM_BTN_CLICKED,
                UIAlignBox.ALIGN_AT_EDGE_TOP_BTN_CLICKED,
                UIAlignBox.ALIGN_AT_EDGE_LEFT_BTN_CLICKED,
                UIAlignBox.ALIGN_AT_EDGE_BOTTOM_BTN_CLICKED,
                UIAlignBox.ALIGN_AT_EDGE_RIGHT_BTN_CLICKED
        };
    }

    @Override
    public void handleNotification(Notification notification) {
        super.handleNotification(notification);
        String alignFunctionName = "";
        int align = -1;
        switch (notification.getName()) {
            case UIAlignBox.ALIGN_TOP_BTN_CLICKED:
                align = Align.top;
                alignFunctionName = "alignSelections";
                break;
            case UIAlignBox.ALIGN_LEFT_BTN_CLICKED:
                align = Align.left;
                alignFunctionName = "alignSelections";
                break;
            case UIAlignBox.ALIGN_BOTTOM_BTN_CLICKED:
                align = Align.bottom;
                alignFunctionName = "alignSelections";
                break;
            case UIAlignBox.ALIGN_RIGHT_BTN_CLICKED:
                align = Align.right;
                alignFunctionName = "alignSelections";
                break;
            case UIAlignBox.ALIGN_CENTER_LEFT_BTN_CLICKED:
                align = Align.center | Align.left;
                alignFunctionName = "alignSelections";
                break;
            case UIAlignBox.ALIGN_CENTER_BOTTOM_BTN_CLICKED:
                align = Align.center | Align.bottom;
                alignFunctionName = "alignSelections";
                break;
            case UIAlignBox.ALIGN_AT_EDGE_TOP_BTN_CLICKED:
                align = Align.top;
                alignFunctionName = "alignSelectionsAtEdge";
                break;
            case UIAlignBox.ALIGN_AT_EDGE_LEFT_BTN_CLICKED:
                align = Align.left;
                alignFunctionName = "alignSelectionsAtEdge";
                break;
            case UIAlignBox.ALIGN_AT_EDGE_BOTTOM_BTN_CLICKED:
                align = Align.bottom;
                alignFunctionName = "alignSelectionsAtEdge";
                break;
            case UIAlignBox.ALIGN_AT_EDGE_RIGHT_BTN_CLICKED:
                align = Align.right;
                alignFunctionName = "alignSelectionsAtEdge";
                break;
        }
        delegateAlignFunction(alignFunctionName, align);
    }

    private void delegateAlignFunction(String alignFunctionName, int align) {
        try {
            Sandbox sandbox = Sandbox.getInstance();
            ItemSelector selector = sandbox.getSelector();
            Method method = selector.getClass().getMethod(alignFunctionName, int.class);
            method.invoke(selector, align);
        } catch (SecurityException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
