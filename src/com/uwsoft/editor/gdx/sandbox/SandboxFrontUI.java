package com.uwsoft.editor.gdx.sandbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.uwsoft.editor.controlles.flow.FlowActionEnum;
import com.uwsoft.editor.gdx.actors.SelectionRectangle;
import com.uwsoft.editor.gdx.ui.SelectionActions;
import com.uwsoft.editor.renderer.actor.CompositeItem;
import com.uwsoft.editor.renderer.actor.IBaseItem;
import com.uwsoft.editor.renderer.data.LayerItemVO;

/**
 * Created by CyberJoe on 3/18/2015.
 */
public class SandboxFrontUI extends Group {

    public SelectionActions dropDown;

    private Sandbox sandbox;

    public SandboxFrontUI(Sandbox sandbox) {
        this.sandbox = sandbox;
    }

    public void showDropDownForSelection(final float x, final float y) {
        showDropDownForSelection(x, y, null);
    }

    /**
     * TODO: this needs to be made right
     *
     * @param x
     * @param y
     * @param regionName
     */
    public void showDropDownForSelection(final float x, final float y, final String regionName) {
        if (dropDown != null) dropDown.remove();
        dropDown = new SelectionActions();

        if (regionName != null) {
            dropDown.addItem(SelectionActions.EDIT_ASSET_PHYSICS, "Edit Physics");
        } else {

            if (sandbox.getSelector().getCurrentSelection().size() > 0) {
                dropDown.addItem(SelectionActions.GROUP_ITEMS, "Group into Composite");
            }

            if (sandbox.getSelector().getCurrentSelection().size() == 1) {
                for (SelectionRectangle value : sandbox.getSelector().getCurrentSelection().values()) {
                    if (value.getHost().isComposite()) {
                        dropDown.addItem(SelectionActions.ADD_TO_LIBRARY, "Add to Library");
                        dropDown.addItem(SelectionActions.EDIT_COMPOSITE, "Edit Composite");
                    }
                }
            }

            dropDown.addItem(SelectionActions.PASTE, "Paste");

            if (sandbox.isItemTouched) {
                dropDown.addItem(SelectionActions.CONVERT_TO_BUTTON, "Convert to Button");
                dropDown.addItem(SelectionActions.EDIT_PHYSICS, "Edit Physics");
                dropDown.addItem(SelectionActions.CUT, "Cut");
                dropDown.addItem(SelectionActions.COPY, "Copy");
                dropDown.addItem(SelectionActions.DELETE, "Delete");
            }
        }

        dropDown.initView();
        Vector2 vector2 = sandbox.getUIStage().contextMenuContainer.stageToLocalCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
        dropDown.setX(vector2.x);
        dropDown.setY(sandbox.getUIStage().getHeight() - vector2.y);
        sandbox.getUIStage().contextMenuContainer.addActor(dropDown);

        //
        dropDown.setEventListener(new SelectionActions.SelectionEvent() {

            @Override
            public void doAction(int action) {
                switch (action) {
                    case SelectionActions.GROUP_ITEMS:
                        sandbox.getItemFactory().groupItemsIntoComposite();
                        sandbox.saveSceneCurrentSceneData();
                        break;
                    case SelectionActions.CONVERT_TO_BUTTON:
                        CompositeItem btn = sandbox.getItemFactory().groupItemsIntoComposite();
                        btn.getDataVO().composite.layers.add(new LayerItemVO("normal"));
                        btn.getDataVO().composite.layers.add(new LayerItemVO("pressed"));
                        btn.reAssembleLayers();

                        sandbox.saveSceneCurrentSceneData();
                        break;
                    case SelectionActions.EDIT_ASSET_PHYSICS:
                        sandbox.getUIStage().editPhysics(regionName);
                        break;
                    case SelectionActions.EDIT_PHYSICS:
                        if (sandbox.getSelector().getCurrentSelection().size() == 1) {
                            for (SelectionRectangle value : sandbox.getSelector().getCurrentSelection().values()) {
                                IBaseItem item = value.getHost();
                                sandbox.getUIStage().editPhysics(item);
                                break;
                            }
                        }

                        break;
                    case SelectionActions.ADD_TO_LIBRARY:
                        sandbox.getItemFactory().addCompositeToLibrary();
                        break;
                    case SelectionActions.EDIT_COMPOSITE:
                        sandbox.enterIntoComposite();
                        sandbox.flow.setPendingHistory(sandbox.getCurrentScene().getDataVO(), FlowActionEnum.GET_INTO_COMPOSITE);
                        sandbox.flow.applyPendingAction();
                        break;
                    case SelectionActions.COPY:
                        sandbox.copyAction();
                        break;
                    case SelectionActions.CUT:
                        sandbox.cutAction();
                        sandbox.saveSceneCurrentSceneData();
                        break;
                    case SelectionActions.PASTE:
                        sandbox.pasteAction(x, y, true);
                        sandbox.saveSceneCurrentSceneData();
                        break;
                    case SelectionActions.DELETE:
                        sandbox.getSelector().removeCurrentSelectedItems();
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
