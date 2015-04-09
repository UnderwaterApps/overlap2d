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

package com.uwsoft.editor.mvc.view.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTable;
import com.uwsoft.editor.mvc.Overlap2DFacade;

/**
 * Created by sargis on 4/8/15.
 */
public class UISubmenuBar extends VisTable {
    private final Overlap2DFacade facade;

    public UISubmenuBar() {
        Skin skin = VisUI.getSkin();
        facade = Overlap2DFacade.getInstance();
//        debug();
        setBackground(skin.getDrawable("window-bg"));
        add("root scene > ").left().expandX();
        //
        UIResolutionBoxMediator uiResolutionBoxMediator = facade.retrieveMediator(UIResolutionBoxMediator.NAME);
        UIResolutionBox uiResolutionBox = uiResolutionBoxMediator.getViewComponent();
        add(uiResolutionBox).right().expandX();
    }
}
