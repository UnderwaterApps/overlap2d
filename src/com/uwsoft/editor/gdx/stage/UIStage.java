package com.uwsoft.editor.gdx.stage;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.uwsoft.editor.controlles.NameConstants;
import com.uwsoft.editor.controlles.UIController;
import com.uwsoft.editor.data.manager.DataManager;
import com.uwsoft.editor.data.manager.EditorResourceManager;
import com.uwsoft.editor.gdx.ui.UICompositePanel;
import com.uwsoft.editor.gdx.ui.UIItemsBox;
import com.uwsoft.editor.gdx.ui.UILightBox;
import com.uwsoft.editor.gdx.ui.UIMainTable;
import com.uwsoft.editor.gdx.ui.dialogs.ConfirmDialog;
import com.uwsoft.editor.gdx.ui.dialogs.CreateNewResolutionDialog;
import com.uwsoft.editor.gdx.ui.dialogs.DlgExport;
import com.uwsoft.editor.gdx.ui.dialogs.DlgImport;
import com.uwsoft.editor.gdx.ui.dialogs.DlgNewProject;
import com.uwsoft.editor.gdx.ui.dialogs.InfoDialog;
import com.uwsoft.editor.gdx.ui.dialogs.InputDialog;
import com.uwsoft.editor.gdx.ui.dialogs.ItemPhysicsDialog;
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
        //
        contextMenuContainer = new Group();
        uiMainTable = new UIMainTable(this);
//        uiMainTable.setWidth(getWidth());
//        uiMainTable.setHeight(getHeight());
//        uiMainTable.setY(11);
        
        addActor(uiMainTable);
        addActor(contextMenuContainer);
        setListeners();
        
        DataManager.getInstance().setStage(this);
        
//        ItemPhysicsDialog dlg = new ItemPhysicsDialog(this);
//        addActor(dlg);
//        sandboxStage.unfocusAll();
//		unfocusAll();
//		setKeyboardFocus(dlg);
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
//        topPanel.initView();
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
        sandboxStage.initSceneView(scene);
    }

    public void createNewProjectDialg() {
        DlgNewProject dlg = new DlgNewProject(this);
        addActor(dlg);
        dlg.show();
    }

    public void showImportDialog() {
        DlgImport dlg = new DlgImport(this);
        addActor(dlg);
        dlg.show();
    }

    public void showCreateNewResolutionDialog() {
        CreateNewResolutionDialog dlg = new CreateNewResolutionDialog(this);
        addActor(dlg);
        dlg.show();
    }

    public ConfirmDialog showConfirmDialog() {
        ConfirmDialog dlg = new ConfirmDialog(this);
        addActor(dlg);
        dlg.show();

        return dlg;
    }

    public InputDialog showInputDialog() {
        InputDialog dlg = new InputDialog(this);
        addActor(dlg);
        dlg.show();

        return dlg;
    }


    public InfoDialog showInfoDialog(String desc) {
        InfoDialog dlg = new InfoDialog(this);
        dlg.setDescription(desc);
        addActor(dlg);
        dlg.show();

        return dlg;
    }
    public InfoDialog showInfoDialogNavigateBack(String desc) {
    	InfoDialog dlg = new InfoDialog(this);
    	dlg.setDescription(desc);
    	addActor(dlg);
    	dlg.show();
    	
    	return dlg;
    }

    public DlgExport showExportDialog() {
        DlgExport dlg = new DlgExport(this);
        addActor(dlg);
        dlg.show();
        return dlg;
    }


    public void setListeners() {
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (sandboxStage.dropDown != null) {
                    sandboxStage.dropDown.remove();
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

        return sandboxStage.getCurrentScene().dataVO.composite.layers.get(uiMainTable.layerPanel.currentSelectedLayerIndex);
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
