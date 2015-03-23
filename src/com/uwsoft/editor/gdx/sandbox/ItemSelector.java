package com.uwsoft.editor.gdx.sandbox;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.uwsoft.editor.gdx.actors.SelectionRectangle;
import com.uwsoft.editor.gdx.mediators.SceneControlMediator;
import com.uwsoft.editor.renderer.actor.CompositeItem;
import com.uwsoft.editor.renderer.actor.IBaseItem;
import com.uwsoft.editor.renderer.physics.PhysicsBodyLoader;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Managing item selections, selecting by criteria and so on
 * @author azakhary
 */
public class ItemSelector {

    /** sandbox reference */
    private Sandbox sandbox;

    private SceneControlMediator sceneControl;

    /** list of current selected items */
    private HashMap<IBaseItem, SelectionRectangle> currentSelection = new HashMap<IBaseItem, SelectionRectangle>();

    public ItemSelector(Sandbox sandbox) {
        this.sandbox = sandbox;
        sceneControl = sandbox.sceneControl;
    }

    /***************************** Getters *********************************/

    /**
     * @return HashMap of selection rectangles that contain items
     */
    public HashMap<IBaseItem, SelectionRectangle> getCurrentSelection() {
        return currentSelection;
    }

    /**
     * @return list of currently selected items
     */
    public ArrayList<IBaseItem> getSelectedItems() {
        ArrayList<IBaseItem> items = new ArrayList<IBaseItem>();
        for (SelectionRectangle value : currentSelection.values()) {
            items.add(value.getHost());
        }
        return items;
    }

    /**
     * @return float Y coordinate of upper bound of current selection
     */
    public float getCurrentSelectionsHighestY() {
        float highestY = -Float.MAX_VALUE;
        for (SelectionRectangle value : currentSelection.values()) {
            Actor actor = value.getHostAsActor();
            float maxY = Math.max(actor.getY(), actor.getY() + actor.getHeight() * actor.getScaleY());
            if (maxY > highestY) {
                highestY = maxY;
            }
        }
        return highestY;
    }

    /**
     * @return float X coordinate of right bound of current selection
     */
    public float getCurrentSelectionsHighestX() {
        float highestX = -Float.MAX_VALUE;
        for (SelectionRectangle value : currentSelection.values()) {
            Actor actor = value.getHostAsActor();
            float maxX = Math.max(actor.getX(), actor.getX() + actor.getWidth() * actor.getScaleX());
            if (maxX > highestX) {
                highestX = maxX;
            }
        }
        return highestX;
    }

    /**
     * @return float X coordinate of left bound of current selection
     */
    public float getCurrentSelectionsLowestX() {
        float lowestX = Float.MAX_VALUE;
        for (SelectionRectangle value : currentSelection.values()) {
            Actor actor = value.getHostAsActor();
            float minX = Math.min(actor.getX(), actor.getX() + actor.getWidth() * actor.getScaleX());
            if (minX < lowestX) {
                lowestX = minX;
            }
        }
        return lowestX;
    }

    /**
     * @return float Y coordinate of bottom bound of current selection
     */
    public float getCurrentSelectionsLowestY() {
        float lowestY = Float.MAX_VALUE;
        for (SelectionRectangle value : currentSelection.values()) {
            Actor actor = value.getHostAsActor();
            float minY = Math.min(actor.getY(), actor.getY() + actor.getHeight() * actor.getScaleY());
            if (minY < lowestY) {
                lowestY = minY;
            }
        }
        return lowestY;
    }


    /***************************** Selectors *********************************/


    /**
     * Creates @SelectionRectangle instance that contains current item, and adds it to the frontUI
     * @param item to be claimed by newly created @SelectionRectangle
     * @return newly created @SelectionRectangle
     */
    private SelectionRectangle createSelectionRect(IBaseItem item) {
        SelectionRectangle rect = new SelectionRectangle(sandbox);
        rect.claim(item);
        rect.setMode(sandbox.getCurrentMode());
        sandbox.getSandboxStage().frontUI.addActor(rect);
        rect.show();

        return rect;
    }

    /**
     * Finds all items that are on particular layer and selects them
     * @param name of the layer
     */
    public void selectItemsByLayerName(String name) {
        ArrayList<IBaseItem> itemsArr = new ArrayList<IBaseItem>();
        for (int i = 0; i < sceneControl.getCurrentScene().getItems().size(); i++) {
            if (sceneControl.getCurrentScene().getItems().get(i).getDataVO().layerName.equals(name)) {
                itemsArr.add(sceneControl.getCurrentScene().getItems().get(i));
            }
        }

        setSelections(itemsArr, true);
    }

    /**
     * sets selection to particular item
     * @param item to select
     * @param removeOthers if set to true this item will become the only selection, otherwise will be added to existing
     */
    public void setSelection(IBaseItem item, boolean removeOthers) {
        if (currentSelection.get(item) != null) return;

        if (removeOthers) clearSelections();

        SelectionRectangle rect = createSelectionRect(item);

        currentSelection.put(item, rect);

        sandbox.getSandboxStage().uiStage.itemWasSelected(item);

		  sandbox.getUIStage().mainDropDown.hide();

        sandbox.getSandboxStage().uiStage.getItemsBox().setSelected(currentSelection);

    }


    /**
     * set selection to a list of items
     * @param items list of items to select
     * @param alsoShow if false, selection will remain hidden at this moment
     */
    public void setSelections(ArrayList<IBaseItem> items, boolean alsoShow) {
        clearSelections();

        for (int i = 0; i < items.size(); i++) {
            setSelection(items.get(i), false);
            if (alsoShow) {
                currentSelection.get(items.get(i)).show();
            }
        }
    }

    /**
     * Un-selects item
     * @param item to un-select
     */
    public void releaseSelection(IBaseItem item) {
        currentSelection.get(item).remove();
        currentSelection.remove(item);

        sandbox.getSandboxStage().uiStage.getItemsBox().setSelected(currentSelection);
    }

    /**
     * clears all selections
     */
    public void clearSelections() {
        for (SelectionRectangle value : currentSelection.values()) {
            value.remove();
        }

        currentSelection.clear();
        sandbox.getSandboxStage().uiStage.getItemsBox().setSelected(currentSelection);
    }

    /**
     * updates all selection rectangles depending on their hosts.
     */
    public void updateSelections() {
        for (SelectionRectangle value : currentSelection.values()) {
            value.update();
        }
    }

    /**
     * Selects all items on currently active scene
     * TODO: This should not select locked items, check if it's true and remove this comment
     */
    public void selectAllItems() {
        ArrayList<IBaseItem> curr = new ArrayList<IBaseItem>();
        for (int i = 0; i < sandbox.getCurrentScene().getItems().size(); i++) {
            curr.add(sandbox.getCurrentScene().getItems().get(i));
        }

        setSelections(curr, true);
    }



    /************************ Manipulate selected items  ******************************/

    /**
     * Updates VO objects, and physics related data of all selected objects
     */
    public void flushAllSelectedItems() {
        for (SelectionRectangle value : getCurrentSelection().values()) {
            IBaseItem item = ((IBaseItem) value.getHostAsActor());
            item.updateDataVO();

            // update physics objetcs
            if (item.isComposite()) {
                ((CompositeItem) item).positionPhysics();
            } else if (item.getBody() != null) {
                item.getBody().setTransform(item.getDataVO().x * sandbox.getCurrentScene().mulX * PhysicsBodyLoader.SCALE, item.getDataVO().y * sandbox.getCurrentScene().mulY * PhysicsBodyLoader.SCALE, (float) Math.toRadians(item.getDataVO().rotation));
            }

        }
    }

    /**
     * removes all selected items from the scene
     */
    public void removeCurrentSelectedItems() {
        for (SelectionRectangle selectionRect : currentSelection.values()) {
            sandbox.itemControl.removeItem(selectionRect.getHostAsActor());
            selectionRect.remove();
        }
        sandbox.getSandboxStage().uiStage.getItemsBox().initContent();
        currentSelection.clear();
    }

    public void alignSelectionsByY(float y, boolean ignoreSelfHeight) {
        int ratio = ignoreSelfHeight ? 0 : 1;
        for (SelectionRectangle value : currentSelection.values()) {
            Actor actor = value.getHostAsActor();
            //actor.setY(y - ratio * actor.getHeight());
            if (actor.getScaleY() < 0) {
                actor.setY(y - (ratio + actor.getScaleY()) * actor.getHeight());
            } else {
                actor.setY(y - ratio * actor.getHeight());
            }
            value.setY(actor.getY());
        }
    }

    public void alignSelectionsByX(float x, boolean ignoreSelfWidth) {
        int ratio = ignoreSelfWidth ? 0 : 1;
        for (SelectionRectangle value : currentSelection.values()) {
            Actor actor = value.getHostAsActor();
            //actor.setX(x - ratio * actor.getWidth());
            if (actor.getScaleX() < 0) {
                actor.setX(x - (ratio + actor.getScaleX()) * actor.getWidth());
            } else {
                actor.setX(x - ratio * actor.getWidth());
            }
            value.setX(actor.getX());
        }
    }

    public void alignSelections(int align) {
        //ResolutionEntryVO resolutionEntryVO = dataManager.getCurrentProjectInfoVO().getResolution(dataManager.curResolution);
        switch (align) {
            case Align.top:
                alignSelectionsByY(getCurrentSelectionsHighestY(), false);
                break;
            case Align.left:
                alignSelectionsByX(getCurrentSelectionsLowestX(), true);
                break;
            case Align.bottom:
                alignSelectionsByY(getCurrentSelectionsLowestY(), true);
                break;
            case Align.right:
                alignSelectionsByX(getCurrentSelectionsHighestX(), false);
                break;
        }
    }

    /**
     * Moves selected items by specified values in both directions
     * @param x
     * @param y
     */
    public void moveSelectedItemsBy(float x, float y) {
        for (SelectionRectangle selectionRect : currentSelection.values()) {
            sandbox.itemControl.moveItemBy(selectionRect.getHostAsActor(), x, y);

            selectionRect.setX(selectionRect.getX() + x);
            selectionRect.setY(selectionRect.getY() + y);
        }

        sandbox.saveSceneCurrentSceneData();
    }

}
