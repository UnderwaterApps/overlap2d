package com.uwsoft.editor.gdx.sandbox;

import com.uwsoft.editor.data.manager.DataManager;
import com.uwsoft.editor.gdx.ui.dialogs.ConfirmDialog;
import com.uwsoft.editor.renderer.data.LayerItemVO;
import com.uwsoft.editor.renderer.data.LightVO;

/**
 * Created by CyberJoe on 3/18/2015.
 */
public class UserActionController {

    private Sandbox sandbox;

    public UserActionController(Sandbox sandbox) {
        this.sandbox = sandbox;
    }

    public void createImage(String regionName, float x, float y) {
        LayerItemVO layer  = sandbox.getSelectedLayer();
        if(layer == null) return;

        sandbox.getItemFactory().createImageItem(layer, regionName, x, y);
    }

    public void create9Patch(String name, float x, float y) {
        LayerItemVO layer  = sandbox.getSelectedLayer();
        if(layer == null) return;

        sandbox.getItemFactory().create9patchItem(layer, name, x, y);
    }

    public void createParticleItem(String name, float x, float y) {
        LayerItemVO layer  = sandbox.getSelectedLayer();
        if(layer == null) return;

        sandbox.getItemFactory().createParticleItem(layer, name, x, y);

    }

    public void createSpriteAnimation(String name, float x, float y) {
        LayerItemVO layer  = sandbox.getSelectedLayer();
        if(layer == null) return;

        sandbox.getItemFactory().createSpriteAnimation(layer, name, x, y);
    }

    public void createSpriterAnimation(String name, float x, float y) {
        LayerItemVO layer  = sandbox.getSelectedLayer();
        if(layer == null) return;

        sandbox.getItemFactory().createSpriterAnimation(layer, name, x, y);
    }

    public void createSpineAnimation(String name, float x, float y) {
        LayerItemVO layer  = sandbox.getSelectedLayer();
        if(layer == null) return;

        sandbox.getItemFactory().createSpineAnimation(layer, name, x, y);
    }

    public void createLight(LightVO vo, float x, float y) {
        LayerItemVO layer  = sandbox.getSelectedLayer();
        if(layer == null) return;

        sandbox.getItemFactory().createLight(layer, vo, x, y);
    }


    public void createItemFromLibrary(String name, float x, float y) {
        LayerItemVO layer  = sandbox.getSelectedLayer();
        if(layer == null) return;

        sandbox.getItemFactory().createItemFromLibrary(layer, name, x, y);
    }

    public void createComponent(final String name, final float x, final float y) {
        final LayerItemVO layer  = sandbox.getSelectedLayer();
        if(layer == null) return;

        // creating component requires skin
        if(!sandbox.isComponentSkinAvailable()) {
            ConfirmDialog confirmDialog = sandbox.getUIStage().dialogs().showConfirmDialog();

            confirmDialog.setDescription("There is no style imported yet. Do you want to add default style instead to make this work?");
            confirmDialog.setListener(new ConfirmDialog.ConfirmDialogListener() {
                @Override
                public void onConfirm() {
                    DataManager.getInstance().copyDefaultStyleIntoProject();
                    sandbox.getItemFactory().createComponent(layer, name, x, y);
                }

                @Override
                public void onCancel() {
                    // Do nothing
                }
            });
        } else {
            sandbox.getItemFactory().createComponent(layer, name, x, y);
        }
    }
}
