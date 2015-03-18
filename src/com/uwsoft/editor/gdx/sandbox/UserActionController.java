package com.uwsoft.editor.gdx.sandbox;

import com.uwsoft.editor.data.manager.DataManager;
import com.uwsoft.editor.gdx.ui.dialogs.ConfirmDialog;
import com.uwsoft.editor.renderer.data.LayerItemVO;

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

    public void create9Patch() {

    }

    public void createParticleItem() {

    }

    public void createSpriteAnimation() {

    }

    public void createSpineAnimation() {

    }

    public void createSpriterAnimation() {

    }

    public void createLight() {

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

    public void createItemFromLibrary() {

    }
}
