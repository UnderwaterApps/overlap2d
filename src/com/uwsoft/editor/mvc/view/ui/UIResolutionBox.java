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

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.ResolutionManager;
import com.uwsoft.editor.mvc.view.stage.UIStage;
import com.uwsoft.editor.renderer.data.ProjectInfoVO;
import com.uwsoft.editor.renderer.data.ResolutionEntryVO;

public class UIResolutionBox extends VisTable {
    public static final String CREATE_NEW_RESOLUTION_BTN_CLICKED = "com.uwsoft.editor.mvc.view.ui.UIResolutionBox" + ".CREATE_NEW_RESOLUTION_BTN_CLICKED";
    public static final String CHANGE_RESOLUTION_BTN_CLICKED = "com.uwsoft.editor.mvc.view.ui.UIResolutionBox" + ".CHANGE_RESOLUTION_BTN_CLICKED";
    public static final String DELETE_RESOLUTION_BTN_CLICKED = "com.uwsoft.editor.mvc.view.ui.UIResolutionBox" + ".DELETE_RESOLUTION_BTN_CLICKED";
    public static final String REPACK_BTN_CLICKED = "com.uwsoft.editor.mvc.view.ui.UIResolutionBox" + ".REPACK_BTN_CLICKED";
    //    private final String currentResolutionName;
    private final Overlap2DFacade facade;
    private final ResolutionManager resolutionManager;
    private VisSelectBox<ResolutionEntryVO> visSelectBox;
//    private final ProjectManager projectManager;

    private UIStage stage;

    private SelectBox<String> dropdown;

    private ProjectInfoVO projectInfoVO;
    private VisTextButton deleteBtn;

    public UIResolutionBox() {
        facade = Overlap2DFacade.getInstance();
        resolutionManager = facade.retrieveProxy(ResolutionManager.NAME);
//        this.stage = s;
//
//        this.projectInfoVO = prjVo;
//
//        this.currentResolutionName = currentResolutionName;
//        projectManager = facade.retrieveProxy(ProjectManager.NAME);
//        int padding = 5;
//
//        String[] arr = new String[projectInfoVO.resolutions.size() + 1];
//
//        arr[0] = projectInfoVO.originalResolution.toString();
//        int selectedIndex = 0;
//        for (int i = 0; i < projectInfoVO.resolutions.size(); i++) {
//            ResolutionEntryVO resolution = projectInfoVO.resolutions.get(i);
//            String resolutionString = projectInfoVO.resolutions.get(i).toString();
//            arr[i + 1] = resolutionString;
//            if (resolution.name.equals(currentResolutionName)) {
//                selectedIndex = i + 1;
//            }
//        }
//
//        dropdown = new SelectBox(textureManager.editorSkin);
//        dropdown.setItems(arr);
//        dropdown.setSelectedIndex(selectedIndex);
//        dropdown.setWidth(150);
//        addActor(dropdown);
//
//        dropdown.setX(0);
//        dropdown.setY(6);
//
//        dropdown.addListener(new ChangeListener() {
//
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                loadCurrentResolution();
//
//            }
//        });
//
//        TextButton delBtn = new TextButton("Delete", textureManager.editorSkin);
//        delBtn.setX(dropdown.getX() + dropdown.getWidth() + padding);
//        delBtn.setY(8);
//        addActor(delBtn);
//
//        TextButton createBtn = new TextButton("Create New", textureManager.editorSkin);
//        createBtn.setX(delBtn.getX() + delBtn.getWidth() + padding);
//        createBtn.setY(8);
//        addActor(createBtn);
//
//        TextButton repackBtn = new TextButton("Repack", textureManager.editorSkin);
//        repackBtn.setX(createBtn.getX() + createBtn.getWidth() + padding);
//        repackBtn.setY(8);
//        addActor(repackBtn);
//
////        openBtn.addListener(new ClickListener() {
////            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
////                super.touchUp(event, x, y, pointer, button);
////
////                String res = "orig";
////
////                final int index = dropdown.getSelectedIndex();
////                if (index > 0) {
////                    res = projectInfoVO.resolutions.get(index - 1).name;
////                }
////                String name = stage.sandboxStage.getCurrentSceneVO().sceneName;
////                DataManager.getInstance().openProjectAndLoadAllData(DataManager.getInstance().getCurrentProjectVO().projectName, res);
////                stage.sandboxStage.loadCurrentProject(name);
////                stage.loadCurrentProject();
////
////            }
////        });
//
//        createBtn.addListener(new ClickListener() {
//            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
//                super.touchUp(event, x, y, pointer, button);
//
//                stage.dialogs().showCreateNewResolutionDialog();
//
//            }
//        });
//
//        delBtn.addListener(new ClickListener() {
//            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
//                super.touchUp(event, x, y, pointer, button);
//
//                final int index = dropdown.getSelectedIndex();
//                if (index == 0) {
//                    return;
//                }
//
//                ResolutionEntryVO resEntry = projectInfoVO.resolutions.get(index - 1);
//

//
//            }
//        });
//
//        repackBtn.addListener(new ClickListener() {
//            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
//                ResolutionManager resolutionManager = facade.retrieveProxy(ResolutionManager.NAME);
//                resolutionManager.rePackProjectImagesForAllResolutions();
//                loadCurrentResolution();
//            }
//        });
//
//        setWidth(340);
    }

    public void init() {
        visSelectBox = new VisSelectBox<>();
        Array<ResolutionEntryVO> resolutionEntryVOs = new Array<>();
        ResolutionEntryVO newResolutionEntryVO = new ResolutionEntryVO();
        newResolutionEntryVO.name = "Create New ...";
        resolutionEntryVOs.add(newResolutionEntryVO);
        resolutionEntryVOs.add(resolutionManager.getOriginalResolution());
        resolutionEntryVOs.addAll(resolutionManager.getResolutions());
        visSelectBox.setItems(resolutionEntryVOs);
        visSelectBox.addListener(new ResolutionChangeListener());
        add("Current Resolution : ").padRight(5);
        add(visSelectBox).padRight(5);
        deleteBtn = new VisTextButton("Delete");
        deleteBtn.addListener(new UIResolutionBoxButtonClickListener(DELETE_RESOLUTION_BTN_CLICKED));
        add(deleteBtn).padRight(5);
        VisTextButton repackBtn = new VisTextButton("Repack");
        repackBtn.addListener(new UIResolutionBoxButtonClickListener(REPACK_BTN_CLICKED));
        add(repackBtn).padRight(5);
        setCurrentResolution(resolutionManager.currentResolutionName);
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
