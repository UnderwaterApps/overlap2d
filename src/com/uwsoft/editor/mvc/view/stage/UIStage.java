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

package com.uwsoft.editor.mvc.view.stage;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.gdx.ui.*;
import com.uwsoft.editor.gdx.ui.dialogs.DialogSystem;
import com.uwsoft.editor.gdx.ui.dialogs.ItemPhysicsDialog;
import com.uwsoft.editor.gdx.ui.layer.UILayerBox;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.EditorResourceManager;
import com.uwsoft.editor.mvc.view.Overlap2DMenuBarMediator;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.actor.CompositeItem;
import com.uwsoft.editor.renderer.actor.IBaseItem;
import com.uwsoft.editor.renderer.data.CompositeItemVO;
import com.uwsoft.editor.renderer.data.Essentials;
import com.uwsoft.editor.renderer.data.LayerItemVO;

public class UIStage extends Stage {

    public final SceneLoader sceneLoader;
    public final Essentials essentials;
    private final Overlap2DFacade facade;
    public SandboxStage sandboxStage;
    public Group dummyTarget;
    public CompositeItem sceneUI;
    public UIMainTable uiMainTable;
    public Group contextMenuContainer;

    public DialogSystem dlgSystem;

    public DropDown mainDropDown;

    public Overlap2DMenuBarMediator menuMediator;

    public UIStage(SandboxStage sandboxStage) {
        super();
        facade = Overlap2DFacade.getInstance();
        essentials = new Essentials();
        essentials.rm = facade.retrieveProxy(EditorResourceManager.NAME);

        SceneLoader sceneLoader = new SceneLoader(essentials);
        sceneLoader.loadScene("MainScene");

        this.sceneLoader = sceneLoader;

        this.sandboxStage = sandboxStage;

        sceneUI = sceneLoader.getSceneAsActor();

        dummyTarget = new Group();
        dummyTarget.setWidth(getWidth());
        dummyTarget.setHeight(getHeight());
        dummyTarget.setY(0);
        dummyTarget.setX(0);
        addActor(dummyTarget);

        contextMenuContainer = new Group();
        uiMainTable = new UIMainTable(this);
        menuMediator = uiMainTable.menuMediator;

        addActor(uiMainTable);
        addActor(contextMenuContainer);
        setListeners();


        initDialogSystem();

        mainDropDown = new DropDown(contextMenuContainer);
    }

    public Sandbox getSandbox() {
        return sandboxStage.sandbox;
    }

    public DialogSystem dialogs() {
        return dlgSystem;
    }

    public void initDialogSystem() {
        dlgSystem = new DialogSystem(this);
    }

    public void editPhysics(String assetName) {
        ItemPhysicsDialog dlg = new ItemPhysicsDialog(this);
        addActor(dlg);
        dlg.editAsset(assetName);
    }

    public void editPhysics(IBaseItem item) {
        ItemPhysicsDialog dlg = new ItemPhysicsDialog(this);
        addActor(dlg);
        dlg.editItem(item);
    }

    public void setKeyboardFocus() {
        setKeyboardFocus(dummyTarget);
    }

    public void updateCurrentItemState() {
        uiMainTable.propertiesPanel.updateState();
    }

    public void itemWasSelected(IBaseItem itm) {
        uiMainTable.propertiesPanel.setItem(itm);

        uiMainTable.layerPanel.selectLayerByName(itm.getDataVO().layerName);
    }

    public void loadCurrentProject() {
        uiMainTable.libraryPanel.initContent();
        uiMainTable.lightBox.initContent();
        uiMainTable.itemsBox.initContent();
        uiMainTable.compositePanel.initResolutionBox();

        uiMainTable.layerPanel.initContent();

//        UIController.instance.sendNotification(NameConstants.PROJECT_OPENED, DataManager.getInstance().getCurrentProjectInfoVO());
    }

    public void reInitLibrary() {
        uiMainTable.libraryPanel.initContent();

        uiMainTable.lightBox.initContent();
        uiMainTable.itemsBox.initContent();
    }

    public void emptyClick() {
        uiMainTable.propertiesPanel.cleanContent();
        uiMainTable.propertiesPanel.showPhysicsParams();
    }

    public void loadScene(CompositeItemVO scene) {
        getSandbox().initSceneView(scene);
    }


    public void setListeners() {
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                mainDropDown.hide();
                return event.getTarget() != getRoot() && event.getTarget() != dummyTarget;
            }
        });
    }


    public void resize(int width, int height) {
        getCompositePanel().resize(width, height);
    }

    public LayerItemVO getCurrentSelectedLayer() {
        if (uiMainTable.layerPanel.currentSelectedLayerIndex == -1) return null;
        return getSandbox().sceneControl.getCurrentScene().dataVO.composite.layers.get(uiMainTable.layerPanel.currentSelectedLayerIndex);
    }


    public UIItemsBox getItemsBox() {
        return uiMainTable.itemsBox;
    }

    public UICompositePanel getCompositePanel() {
        return uiMainTable.compositePanel;
    }

    public UILightBox getLightBox() {
        return uiMainTable.lightBox;
    }

    public UILayerBox getLayerPanel() {
        return uiMainTable.layerPanel;
    }

    @Override
    public boolean keyDown(int keyCode) {
        return super.keyDown(keyCode);
    }


}
