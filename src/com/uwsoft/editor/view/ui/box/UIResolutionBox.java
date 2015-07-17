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

package com.uwsoft.editor.view.ui.box;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.proxy.ResolutionManager;
import com.uwsoft.editor.renderer.data.ResolutionEntryVO;

public class UIResolutionBox extends VisTable {
    public static final String CREATE_NEW_RESOLUTION_BTN_CLICKED = "com.uwsoft.editor.view.ui.box.UIResolutionBox" + ".CREATE_NEW_RESOLUTION_BTN_CLICKED";
    public static final String CHANGE_RESOLUTION_BTN_CLICKED = "com.uwsoft.editor.view.ui.box.UIResolutionBox" + ".CHANGE_RESOLUTION_BTN_CLICKED";
    public static final String DELETE_RESOLUTION_BTN_CLICKED = "com.uwsoft.editor.view.ui.box.UIResolutionBox" + ".DELETE_RESOLUTION_BTN_CLICKED";
    public static final String REPACK_BTN_CLICKED = "com.uwsoft.editor.view.ui.box.UIResolutionBox" + ".REPACK_BTN_CLICKED";
    //    private final String currentResolutionName;
    private final Overlap2DFacade facade;
    private final ResolutionManager resolutionManager;
    private final Skin skin;
    private VisSelectBox<ResolutionEntryVO> visSelectBox;
//    private final ProjectManager projectManager;

    private VisImageButton deleteBtn;

    public UIResolutionBox() {
        facade = Overlap2DFacade.getInstance();
        resolutionManager = facade.retrieveProxy(ResolutionManager.NAME);
        skin = VisUI.getSkin();
        init();
    }

    private void init() {

    }

    public void update() {
        clear();
        addSeparator(true).padRight(6);
        visSelectBox = new VisSelectBox<>("white");
        Array<ResolutionEntryVO> resolutionEntryVOs = new Array<>();
        ResolutionEntryVO newResolutionEntryVO = new ResolutionEntryVO();
        newResolutionEntryVO.name = "Create New ...";
        resolutionEntryVOs.add(newResolutionEntryVO);
        resolutionEntryVOs.add(resolutionManager.getOriginalResolution());
        resolutionEntryVOs.addAll(resolutionManager.getResolutions());
        visSelectBox.setItems(resolutionEntryVOs);
        add("Resolution:").padRight(4);
        add(visSelectBox).padRight(11).width(156);
        VisImageButton.VisImageButtonStyle visImageButtonStyle = new VisImageButton.VisImageButtonStyle(skin.get("dark", VisImageButton.VisImageButtonStyle.class));
        visImageButtonStyle.imageUp = skin.getDrawable("icon-trash");
        visImageButtonStyle.imageOver = skin.getDrawable("icon-trash-over");
        visImageButtonStyle.imageDisabled = skin.getDrawable("icon-trash-disabled");
        deleteBtn = new VisImageButton("dark");
        deleteBtn.setStyle(visImageButtonStyle);
        deleteBtn.addListener(new UIResolutionBoxButtonClickListener(DELETE_RESOLUTION_BTN_CLICKED));
        add(deleteBtn).padRight(11).height(25);
        VisTextButton repackBtn = new VisTextButton("Repack", "orange");
        repackBtn.addListener(new UIResolutionBoxButtonClickListener(REPACK_BTN_CLICKED));
        add(repackBtn).padRight(5).width(93).height(25);
        setCurrentResolution(resolutionManager.currentResolutionName);
        visSelectBox.addListener(new ResolutionChangeListener());
    }

    private void setCurrentResolution(String currentResolutionName) {
        Array<ResolutionEntryVO> array = visSelectBox.getItems();
        for (int i = 0; i < array.size; ++i) {
            ResolutionEntryVO resolutionEntryVO = array.get(i);
            if (resolutionEntryVO.name.equals(currentResolutionName)) {
                visSelectBox.setSelectedIndex(i);
                break;
            }
        }
    }

    private void loadCurrentResolution() {

    }

    private class UIResolutionBoxButtonClickListener extends ClickListener {
        private final String btnClicked;

        public UIResolutionBoxButtonClickListener(String btnClicked) {
            this.btnClicked = btnClicked;
        }

        @Override
        public void clicked(InputEvent event, float x, float y) {
            super.clicked(event, x, y);
            Overlap2DFacade facade = Overlap2DFacade.getInstance();
            switch (btnClicked) {
                case DELETE_RESOLUTION_BTN_CLICKED:
                    facade.sendNotification(btnClicked, visSelectBox.getSelected());
                    break;
                case REPACK_BTN_CLICKED:
                    facade.sendNotification(btnClicked);
                    break;
            }
        }
    }

    private class ResolutionChangeListener extends ChangeListener {

        @Override
        public void changed(ChangeEvent changeEvent, Actor actor) {
            deleteBtn.setDisabled(false);
            int selectedIndex = visSelectBox.getSelectedIndex();
            Overlap2DFacade facade = Overlap2DFacade.getInstance();
            if (selectedIndex == 0) {
                facade.sendNotification(CREATE_NEW_RESOLUTION_BTN_CLICKED);
                return;
            }
            if (selectedIndex == 1) {
                deleteBtn.setDisabled(true);
            }
            facade.sendNotification(CHANGE_RESOLUTION_BTN_CLICKED, visSelectBox.getSelected());
        }
    }
}
