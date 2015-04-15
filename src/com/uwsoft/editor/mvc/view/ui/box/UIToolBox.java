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
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.TextureManager;

public class UIToolBox extends VisWindow {

    public static final String SELECTING_MODE_BTN_CLICKED = "com.uwsoft.editor.mvc.view.ui.box.UIToolBox." + ".SELECTING_MODE_BTN_CLICKED";
    public static final String TRANSFORMING_MODE_BTN_CLICKED = "com.uwsoft.editor.mvc.view.ui.box.UIToolBox." + ".TRANSFORMING_MODE_BTN_CLICKED";
    private static final int BUTTON_SELECTING_MODE = 0;
    private static final int BUTTON_TRANSFORMING_MODE = 1;
    //
    private final Overlap2DFacade faced;
    private final TextureManager textureManager;

    public UIToolBox() {
        super("Tools", true);
        setMovable(false);
        faced = Overlap2DFacade.getInstance();
        textureManager = faced.retrieveProxy(TextureManager.NAME);
        VisTable mainTable = new VisTable();
        mainTable.addSeparator().colspan(2).padBottom(10);
        mainTable.add(createButton("mainIcon", BUTTON_SELECTING_MODE)).padRight(5).left();
        mainTable.add(createButton("resizeIcon", BUTTON_TRANSFORMING_MODE)).expandX().left();
        add(mainTable).expandX().fillX();
    }

    private TextureRegionDrawable getDrawable(String name) {
        return new TextureRegionDrawable(textureManager.getEditorAsset(name));
    }

    private VisImageButton createButton(String name, int buttonId) {
        VisImageButton visImageButton = new VisImageButton(getDrawable(name), getDrawable(name + "Checked"));
        visImageButton.addListener(new ToolboxButtonClickListener(buttonId));
        return visImageButton;
    }

    private class ToolboxButtonClickListener extends ClickListener {

        private final int buttonId;

        public ToolboxButtonClickListener(int buttonId) {
            this.buttonId = buttonId;
        }

        @Override
        public void clicked(InputEvent event, float x, float y) {
            super.clicked(event, x, y);
            String notification = "";
            switch (buttonId) {
                case BUTTON_SELECTING_MODE:
                    notification = SELECTING_MODE_BTN_CLICKED;
                    break;
                case BUTTON_TRANSFORMING_MODE:
                    notification = TRANSFORMING_MODE_BTN_CLICKED;
                    break;
            }
            Overlap2DFacade facade = Overlap2DFacade.getInstance();
            facade.sendNotification(notification);
        }
    }
}
