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

package com.uwsoft.editor.gdx.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.uwsoft.editor.controlles.ResolutionManager;
import com.uwsoft.editor.gdx.stage.UIStage;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.DataManager;
import com.uwsoft.editor.renderer.actor.CompositeItem;
import com.uwsoft.editor.renderer.data.CompositeItemVO;

import java.util.ArrayList;

public class UICompositePanel extends UIBox {

    private final Overlap2DFacade facade;
    private final DataManager dataManager;
    private ArrayList<CompositeItemVO> scenes = new ArrayList<CompositeItemVO>();

    private UIStage uiStage;

    private UIResolutionBox resolutionBox = null;

    public UICompositePanel(UIStage s) {
        super(s, s.getWidth(), 36.0f);
        uiStage = s;
        facade = Overlap2DFacade.getInstance();
        dataManager = facade.retrieveProxy(DataManager.NAME);
    }

    @Override
    public void initPanel() {
        Image bgImg = new Image(stage.textureManager.getEditorAsset("panel"));

        bgImg.setScaleX(getWidth());

        backLayer.addActor(bgImg);
    }

    public void clearScenes() {
        scenes.clear();
    }

    public boolean isRootScene() {
        return scenes.size() == 1;
    }

    public void updateRootScene(CompositeItemVO scene) {
        scenes.set(0, scene);
    }

    public void addScene(CompositeItemVO scene) {
        scenes.add(scene);
        initView();
    }

    private void loadScene(CompositeItemVO scene) {
        uiStage.loadScene(scene);
        initView();
    }

    /*
    private void updateOriginalScene(CompositeItem copy, CompositeItem origin) {
        if(origin == null) return;

        origin.loadFromVO(copy.getDataVO());

        BasicItem originParent = origin.getParentItem();
        if(originParent != null) {
            int index = scenes.indexOf(originParent);
            CompositeItem cloneParent = scenes.get(index);
            if(cloneParent !=null) {
                updateOriginalScene(cloneParent, origins.get(index));
            }
        }
    }*/
    public void updateOriginalItem() {
        updateOriginalItem(scenes.get(scenes.size() - 1), stage.getSandbox().getCurrentScene());
    }

    private void updateOriginalItem(CompositeItemVO updatableVo, CompositeItem currItem) {
        updatableVo.update(new CompositeItemVO(currItem.getDataVO().composite));

        String libName = currItem.getDataVO().itemName;
        CompositeItemVO libItem = stage.getSandbox().sceneControl.getCurrentSceneVO().libraryItems.get(libName);

        if (libItem != null) {
            libItem.update(currItem.getDataVO());


            //TODO: update other items with same name
            revursiveUpdateLibraryVO(libName, stage.getSandbox().sceneControl.getRootSceneVO(), currItem.getDataVO());
        }
    }


    private void revursiveUpdateLibraryVO(String libName, CompositeItemVO initialVO, CompositeItemVO updatingWith) {
        for (int i = 0; i < initialVO.composite.sComposites.size(); i++) {
            if (initialVO.composite.sComposites.get(i).itemName.equals(libName)) {
                initialVO.composite.sComposites.get(i).update(updatingWith);
            } else {
                revursiveUpdateLibraryVO(libName, initialVO.composite.sComposites.get(i), updatingWith);
            }
        }
    }

    public void stepUp() {
        if (scenes.size() > 1) {
            int currIndex = scenes.size() - 1;
            stage.getSandbox().getCurrentScene().updateDataVO();
            updateOriginalItem(scenes.get(currIndex), stage.getSandbox().getCurrentScene());
            scenes.remove(currIndex);
            CompositeItemVO scn = scenes.get(currIndex - 1);
            loadScene(scn);
            if (currIndex == 1) {
                stage.getLightBox().disableAmbiance.setChecked(false);
					 stage.getSandbox().getSceneControl().disableAmbience(false);
            }
        }
    }

    public void initView() {
        mainLayer.clear();
        float pointer = 10;
        for (int i = 0; i < scenes.size(); i++) {
            String tmpName = "root scene >";
            if (i > 0) tmpName = "Scene" + i + " >";
            Label lbl = new Label(tmpName, stage.textureManager.editorSkin);
            lbl.setX(pointer);
            lbl.setY(11);
            pointer += lbl.getWidth() + 10;
            mainLayer.addActor(lbl);

            final CompositeItemVO currScn = scenes.get(i);
            final int currIter = i;

            lbl.addListener(new ClickListener() {
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    super.touchUp(event, x, y, pointer, button);

                    for (int j = scenes.size() - 1; j > currIter; j--) {
                        updateOriginalItem(scenes.get(j), stage.getSandbox().getCurrentScene());
                        scenes.remove(j);
                    }
                    loadScene(currScn);
                }
            });
        }
    }

    @SuppressWarnings("unchecked")
    public void initResolutionBox() {
        if (resolutionBox != null) {
            resolutionBox.remove();
        }
        ResolutionManager resolutionManager = facade.retrieveProxy(ResolutionManager.NAME);
        resolutionBox = new UIResolutionBox(stage, dataManager.getCurrentProjectInfoVO(), resolutionManager.curResolution);
        addActor(resolutionBox);
        resolutionBox.setX(getWidth() - resolutionBox.getWidth());
    }

    public void resize(int width, int height) {
        setWidth(width);
        if (resolutionBox != null) {
            resolutionBox.setX(getWidth() - resolutionBox.getWidth());
        }
    }
}
