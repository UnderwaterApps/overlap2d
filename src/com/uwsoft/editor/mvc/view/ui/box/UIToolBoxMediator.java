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

import com.puremvc.patterns.mediator.SimpleMediator;

/**
 * Created by sargis on 4/9/15.
 */
public class UIToolBoxMediator extends SimpleMediator<UIToolBox> {
    private static final String TAG = UIToolBoxMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    public UIToolBoxMediator() {
        super(NAME, new UIToolBox());
    }

    //
//        topIcon.addListener(new ClickListener() {
//            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                stage.getSandbox().getSelector().alignSelections(Align.top);
//                return super.touchDown(event, x, y, pointer, button);
//            }
//        });
//
//        leftIcon.addListener(new ClickListener() {
//            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                stage.getSandbox().getSelector().alignSelections(Align.left);
//                return super.touchDown(event, x, y, pointer, button);
//            }
//        });
//
//        bottomIcon.addListener(new ClickListener() {
//            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                stage.getSandbox().getSelector().alignSelections(Align.bottom);
//                return super.touchDown(event, x, y, pointer, button);
//            }
//        });
//
//        rightIcon.addListener(new ClickListener() {
//            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                stage.getSandbox().getSelector().alignSelections(Align.right);
//                return super.touchDown(event, x, y, pointer, button);
//            }
//        });
//        hCenterIcon.addListener(new ClickListener() {
//            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                stage.getSandbox().getSelector().alignSelections(Align.center | Align.left);
//                return super.touchDown(event, x, y, pointer, button);
//            }
//        });
//        vCenterIcon.addListener(new ClickListener() {
//            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                stage.getSandbox().getSelector().alignSelections(Align.center | Align.bottom);
//                return super.touchDown(event, x, y, pointer, button);
//            }
//        });
//        leftEdgeIcon.addListener(new ClickListener() {
//            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                stage.getSandbox().getSelector().alignSelectionsAtEdge(Align.left);
//                return super.touchDown(event, x, y, pointer, button);
//            }
//        });
//        topEdgeIcon.addListener(new ClickListener() {
//            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                stage.getSandbox().getSelector().alignSelectionsAtEdge(Align.top);
//                return super.touchDown(event, x, y, pointer, button);
//            }
//        });
//        rightEdgeIcon.addListener(new ClickListener() {
//            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                stage.getSandbox().getSelector().alignSelectionsAtEdge(Align.right);
//                return super.touchDown(event, x, y, pointer, button);
//            }
//        });
//        bottomEdgeIcon.addListener(new ClickListener() {
//            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                stage.getSandbox().getSelector().alignSelectionsAtEdge(Align.bottom);
//                return super.touchDown(event, x, y, pointer, button);
//            }
//        });
//
//        mainIcon.addListener(new ClickListener() {
//            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                stage.getSandbox().setCurrentMode(EditingMode.SELECTION);
//                return super.touchDown(event, x, y, pointer, button);
//            }
//        });
//
//        resizeIcon.addListener(new ClickListener() {
//            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                stage.getSandbox().setCurrentMode(EditingMode.TRANSFORM);
//                return super.touchDown(event, x, y, pointer, button);
//            }
//        });
}
