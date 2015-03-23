package com.uwsoft.editor.gdx.stage;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.uwsoft.editor.controlles.NameConstants;
import com.uwsoft.editor.controlles.UIController;
import com.uwsoft.editor.data.manager.DataManager;
import com.uwsoft.editor.data.manager.EditorResourceManager;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.gdx.ui.UICompositePanel;
import com.uwsoft.editor.gdx.ui.UIItemsBox;
import com.uwsoft.editor.gdx.ui.UILightBox;
import com.uwsoft.editor.gdx.ui.UIMainTable;
import com.uwsoft.editor.gdx.ui.dialogs.*;
import com.uwsoft.editor.gdx.ui.layer.UILayerBox;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.actor.CompositeItem;
import com.uwsoft.editor.renderer.actor.IBaseItem;
import com.uwsoft.editor.renderer.data.CompositeItemVO;
import com.uwsoft.editor.renderer.data.LayerItemVO;

public class UIStage extends BaseStage {

    public SandboxStage sandboxStage;
    public Group dummyTarget;
    public CompositeItem sceneUI;
    public UIMainTable uiMainTable;
    public Group contextMenuContainer;

    public DialogSystem dlgSystem;

    public UIStage(SandboxStage sandboxStage) {
        super();

        EditorResourceManager edRm = new EditorResourceManager();
        essentials.rm = edRm;

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

        addActor(uiMainTable);
        addActor(contextMenuContainer);
        setListeners();
        

        initDialogSystem();
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
    
    public void editPhysics(String assetName){
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

        UIController.instance.sendNotification(NameConstants.PROJECT_OPENED, DataManager.getInstance().getCurrentProjectInfoVO());
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
                if (sandboxStage.frontUI != null && sandboxStage.frontUI.dropDown != null) {
                    sandboxStage.frontUI.dropDown.remove();
                }
                return event.getTarget() != getRoot() && event.getTarget() != dummyTarget;
            }
        });
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        getCompositePanel().resize(width, height);
    }

//    @Override
//    public void draw() {
//        super.draw();
//        Table.drawDebug(this); // draw any enabled debug lines
//    }

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
