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

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisTable;
import com.uwsoft.editor.mvc.Overlap2DFacade;

/**
 * Created by sargis on 4/27/15.
 */
public class UIZoomBox extends VisTable {
    private final Overlap2DFacade facade;
    private final Skin skin;
    private VisSelectBox<String> visSelectBox;
    private VisImageButton zoomInBtn;
    private VisImageButton zoomOutBtn;

    public UIZoomBox() {
        facade = Overlap2DFacade.getInstance();
        skin = VisUI.getSkin();
        init();
    }

    private void init() {
        //
        addSeparator(true).padRight(6);
        add("Zoom:").padRight(5);
        //
        VisImageButton.VisImageButtonStyle zoominButtonStyle = new VisImageButton.VisImageButtonStyle(skin.get("dark", VisImageButton.VisImageButtonStyle.class));
        zoominButtonStyle.imageUp = skin.getDrawable("icon-zoomin");
        zoominButtonStyle.imageOver = skin.getDrawable("icon-zoomin-over");
        zoominButtonStyle.imageDisabled = skin.getDrawable("icon-zoomin-disabled");
        //
        zoomInBtn = new VisImageButton("dark");
        zoomInBtn.setStyle(zoominButtonStyle);
        zoomInBtn.addListener(new UIZoomBoxButtonClickListener(1));
        add(zoomInBtn).height(25);
        //
        VisImageButton.VisImageButtonStyle zoomoutButtonStyle = new VisImageButton.VisImageButtonStyle(skin.get("dark", VisImageButton.VisImageButtonStyle.class));
        zoomoutButtonStyle.imageUp = skin.getDrawable("icon-zoomout");
        zoomoutButtonStyle.imageOver = skin.getDrawable("icon-zoomout-over");
        zoomoutButtonStyle.imageDisabled = skin.getDrawable("icon-zoomout-disabled");
        //
        zoomOutBtn = new VisImageButton("dark");
        zoomOutBtn.setStyle(zoomoutButtonStyle);
        zoomOutBtn.addListener(new UIZoomBoxButtonClickListener(1));
        add(zoomOutBtn).padRight(11).height(25);
        //
        visSelectBox = new VisSelectBox<>("white");
        visSelectBox.setItems("200%", "100%", "50%", "25%");
        add(visSelectBox).width(114);
    }

    private class UIZoomBoxButtonClickListener extends ClickListener {
        private final int direction;

        public UIZoomBoxButtonClickListener(int direction) {
            this.direction = direction;
        }


        @Override
        public void clicked(InputEvent event, float x, float y) {
            super.clicked(event, x, y);
            Overlap2DFacade facade = Overlap2DFacade.getInstance();
        }
    }


}
