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
import com.uwsoft.editor.mvc.proxy.EditorTextureManager;

public class UIToolBox extends VisWindow {

    private static final String PREFIX =  "com.uwsoft.editor.mvc.view.ui.box.UIToolBox.";

    public static final String SELECTING_MODE_BTN_CLICKED = PREFIX + ".SELECTING_MODE_BTN_CLICKED";
    public static final String TRANSFORMING_MODE_BTN_CLICKED = PREFIX + ".TRANSFORMING_MODE_BTN_CLICKED";
    public static final String TEXT_MODE_BTN_CLICKED = PREFIX + ".TEXT_MODE_BTN_CLICKED";

    public static final int SELECTION_TOOL = 0;
    public static final int TRANSFORM_TOOL = 1;
    public static final int TEXT_TOOL = 2;

    private final Overlap2DFacade faced;
    private final EditorTextureManager textureManager;

    public UIToolBox() {
        super("Tools", true);
        setMovable(false);
        faced = Overlap2DFacade.getInstance();
        textureManager = faced.retrieveProxy(EditorTextureManager.NAME);
        VisTable mainTable = new VisTable();
        mainTable.addSeparator().colspan(2).padBottom(10);
        mainTable.add(createButton("mainIcon", SELECTION_TOOL)).padRight(5).left();
        mainTable.add(createButton("resizeIcon", TRANSFORM_TOOL)).padRight(5).left();
        mainTable.add(createButton("resizeIcon", TEXT_TOOL)).expandX().left();
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
                case SELECTION_TOOL:
                    notification = SELECTING_MODE_BTN_CLICKED;
                    break;
                case TRANSFORM_TOOL:
                    notification = TRANSFORMING_MODE_BTN_CLICKED;
                    break;
                case TEXT_TOOL:
                    notification = TEXT_MODE_BTN_CLICKED;
                    break;
            }
            Overlap2DFacade facade = Overlap2DFacade.getInstance();
            facade.sendNotification(notification, buttonId);
        }
    }
}
