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

            if (currentSelection.size() > 0) {
                dropDown.addItem(SelectionActions.GROUP_ITEMS, "Group into Composite");
            }

            if (currentSelection.size() == 1) {
                for (SelectionRectangle value : currentSelection.values()) {
                    if (value.getHost().isComposite()) {
                        dropDown.addItem(SelectionActions.ADD_TO_LIBRARY, "Add to Library");
                        dropDown.addItem(SelectionActions.EDIT_COMPOSITE, "Edit Composite");
                    }
                }
            }

            dropDown.addItem(SelectionActions.PASTE, "Paste");

            if (isItemTouched) {
                dropDown.addItem(SelectionActions.CONVERT_TO_BUTTON, "Convert to Button");
                dropDown.addItem(SelectionActions.EDIT_PHYSICS, "Edit Physics");
                dropDown.addItem(SelectionActions.CUT, "Cut");
                dropDown.addItem(SelectionActions.COPY, "Copy");
                dropDown.addItem(SelectionActions.DELETE, "Delete");
            }
        }

        dropDown.initView();
        Vector2 vector2 = uiStage.contextMenuContainer.stageToLocalCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
        dropDown.setX(vector2.x);
        dropDown.setY(uiStage.getHeight() - vector2.y);
        uiStage.contextMenuContainer.addActor(dropDown);

        //
        dropDown.setEventListener(new SelectionActions.SelectionEvent() {

            @Override
            public void doAction(int action) {
                switch (action) {
                    case SelectionActions.GROUP_ITEMS:
                        groupItemsIntoComposite();
                        saveSceneCurrentSceneData();
                        break;
                    case SelectionActions.CONVERT_TO_BUTTON:
                        CompositeItem btn = groupItemsIntoComposite();
                        btn.getDataVO().composite.layers.add(new LayerItemVO("normal"));
                        btn.getDataVO().composite.layers.add(new LayerItemVO("pressed"));
                        btn.reAssembleLayers();

                        saveSceneCurrentSceneData();
                        break;
                    case SelectionActions.EDIT_ASSET_PHYSICS:
                        uiStage.editPhysics(regionName);
                        break;
                    case SelectionActions.EDIT_PHYSICS:
                        if (currentSelection.size() == 1) {
                            for (SelectionRectangle value : currentSelection.values()) {
                                IBaseItem item = value.getHost();
                                uiStage.editPhysics(item);
                                break;
                            }
                        }

                        break;
                    case SelectionActions.ADD_TO_LIBRARY:
                        addCompositeToLibrary();
                        break;
                    case SelectionActions.EDIT_COMPOSITE:
                        getIntoComposite();
                        flow.setPendingHistory(getCurrentScene().getDataVO(), FlowActionEnum.GET_INTO_COMPOSITE);
                        flow.applyPendingAction();
                        break;
                    case SelectionActions.COPY:
                        copyAction();
                        break;
                    case SelectionActions.CUT:
                        cutAction();
                        saveSceneCurrentSceneData();
                        break;
                    case SelectionActions.PASTE:
                        pasteAction(x, y, true);
                        saveSceneCurrentSceneData();
                        break;
                    case SelectionActions.DELETE:
                        removeCurrentSelectedItems();
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
