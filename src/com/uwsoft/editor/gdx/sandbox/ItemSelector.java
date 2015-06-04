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

package com.uwsoft.editor.gdx.sandbox;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.SnapshotArray;
import com.uwsoft.editor.Overlap2D;
import com.uwsoft.editor.gdx.mediators.SceneControlMediator;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.view.MidUIMediator;
import com.uwsoft.editor.mvc.view.ui.followers.NormalSelectionFollower;
import com.uwsoft.editor.renderer.components.NodeComponent;
import com.uwsoft.editor.utils.runtime.ComponentRetriever;

/**
 * Managing item selections, selecting by criteria and so on
 *
 * @author azakhary
 */
public class ItemSelector {

    /** sandbox reference */
    private Sandbox sandbox;

    private SceneControlMediator sceneControl;

    /** list of current selected panels */
    private Set<Entity> currentSelection = new HashSet<>();

    private MidUIMediator midUIMediator;

    public ItemSelector(Sandbox sandbox) {
        this.sandbox = sandbox;
        sceneControl = sandbox.sceneControl;

        midUIMediator = Overlap2DFacade.getInstance().retrieveMediator(MidUIMediator.NAME);
    }

    /***************************** Getters *********************************/

    /**
     * @return HashMap of selection rectangles that contain panels
     */
    public Set<Entity> getCurrentSelection() {
        return currentSelection;
    }

    /**
     * @return one selected item
     */
    public Entity getSelectedItem() {
        if(currentSelection.size() > 0) {
            return currentSelection.iterator().next();
        }

        return null;
    }

    /**
    public SelectionRectangle getSelectedItemSelectionRectangle() {
        ArrayList<SelectionRectangle> items = new ArrayList<SelectionRectangle>();
        for (SelectionRectangle value : currentSelection.values()) {
            items.add(value);
            break;
        }
        if(items.size() > 0) {
            return items.get(0);
        }

        return null;
    }
     */

    /**
     * @return list of currently selected panels
     */
    public Set<Entity> getSelectedItems() {
        return currentSelection;
    }

    /*
    public BiConsumer<SelectionRectangle, AccContainer> broadestItem = (i, acc) -> {
        if (acc.carryVal == null) acc.carryVal = Float.MIN_VALUE;
        final float width = i.getVisualWidth();
        if (width > acc.carryVal) {
            acc.carryVal = width;
            acc.carry = i;
        }
    };

    public BiConsumer<SelectionRectangle, AccContainer> highestItem = (i, acc) -> {
        if (acc.carryVal == null) acc.carryVal = Float.MIN_VALUE;
        final float height = i.getVisualHeight();
        if (height > acc.carryVal) {
            acc.carryVal = height;
            acc.carry = i;
        }
    };

    public BiConsumer<SelectionRectangle, AccContainer> rightmostItem = (i, acc) -> {
        if (acc.carryVal == null) acc.carryVal = Float.MIN_VALUE;
        final float x = i.getVisualRightX();
        if (x > acc.carryVal) {
            acc.carryVal = x;
            acc.carry = i;
        }
    };

    public BiConsumer<SelectionRectangle, AccContainer> leftmostItem = (i, acc) -> {
        if (acc.carryVal == null) acc.carryVal = Float.MAX_VALUE;
        final float x = i.getVisualX();
        if (x < acc.carryVal) {
            acc.carryVal = x;
            acc.carry = i;
        }
    };

    public BiConsumer<SelectionRectangle, AccContainer> topmostItem = (i, acc) -> {
        if (acc.carryVal == null) acc.carryVal = Float.MIN_VALUE;
        final float y = i.getVisualTopY();
        if (y > acc.carryVal) {
            acc.carryVal = y;
            acc.carry = i;
        }
    };
    public BiConsumer<SelectionRectangle, AccContainer> bottommostItem = (i, acc) -> {
        if (acc.carryVal == null) acc.carryVal = Float.MAX_VALUE;
        final float y = i.getVisualY();
        if (y < acc.carryVal) {
            acc.carryVal = y;
            acc.carry = i;
        }
    };

    */

    /*
    public SelectionRectangle get(BiConsumer<SelectionRectangle, AccContainer> checkSelection) {
        final AccContainer acc = new AccContainer();

        for (SelectionRectangle value : currentSelection.values()) {
            checkSelection.accept(value, acc);
        }
        return acc.carry;
    }
    */

     /**
     * Finds all panels that are on particular layer and selects them
     * @param name of the layer
     */
    public void selectItemsByLayerName(String name) {
    	//TODO fix and uncomment
//        ArrayList<Entity> itemsArr = new ArrayList<Entity>();
//        for (int i = 0; i < sceneControl.getCurrentScene().getItems().size(); i++) {
//            if (sceneControl.getCurrentScene().getItems().get(i).getDataVO().layerName.equals(name)) {
//                itemsArr.add(sceneControl.getCurrentScene().getItems().get(i));
//            }
//        }
//
//        setSelections(itemsArr, true);
    }

    /**
     * sets selection to particular item
     * @param item to select
     * @param removeOthers if set to true this item will become the only selection, otherwise will be added to existing
     */
    public void setSelection(Entity item, boolean removeOthers) {
        if (currentSelection.contains(item)) return;

        if (removeOthers) clearSelections();

        currentSelection.add(item);

        Overlap2DFacade.getInstance().sendNotification(Overlap2D.ITEM_SELECTION_CHANGED, currentSelection);

		sandbox.getUIStage().mainDropDown.hide();

    }

    /**
     * adds to selection a list of items
     * @param items list of panels to select
     */
    public void addSelections(Set<Entity> items) {
        for (Entity item : items) {
            setSelection(item, false);
        }
    }


    /**
     * set selection to a list of items
     * @param items list of panels to select
     * @param alsoShow if false, selection will remain hidden at this moment
     */
    public void setSelections(Set<Entity> items, boolean alsoShow) {
        clearSelections();

        if(items == null) return;

        for (Entity item : items) {
            setSelection(item, false);
            if (alsoShow) {
                Overlap2DFacade.getInstance().sendNotification(Overlap2D.SHOW_SELECTIONS, currentSelection);
            } else {
                Overlap2DFacade.getInstance().sendNotification(Overlap2D.HIDE_SELECTIONS, currentSelection);
            }
        }
    }

    /**
     * remove selection to a list of items
     * @param items list of panels to remove selection
     */
    public void releaseSelections(Set<Entity> items) {
        for (Entity item : items) {
            releaseSelection(item);
        }
    }

    /**
     * Un-selects item
     * @param item to un-select
     */
    public void releaseSelection(Entity item) {
        currentSelection.remove(item);

        Overlap2DFacade.getInstance().sendNotification(Overlap2D.ITEM_SELECTION_CHANGED, currentSelection);
    }

    /**
     * clears all selections
     */
    public void clearSelections() {
        currentSelection.clear();

        Overlap2DFacade.getInstance().sendNotification(Overlap2D.ITEM_SELECTION_CHANGED, currentSelection);
    }


    /**
     * Selects all panels on currently active scene
     * TODO: This should not select locked panels, check if it's true and remove this comment
     */
    public SnapshotArray<Entity> getAllFreeItems() {
    	NodeComponent nodeComponent = ComponentRetriever.get(sandbox.getRootEntity(), NodeComponent.class);
		SnapshotArray<Entity> childrenEntities = nodeComponent.children;
        return childrenEntities;
    }



    /************************ Manipulate selected panels  ******************************/

    /**
     * removes all selected panels from the scene
     */
    public void removeCurrentSelectedItems() {
        for (Entity item : currentSelection) {
            midUIMediator.removeFollower(item);
            sandbox.itemControl.removeItem(item);
        }

        currentSelection.clear();
    }

    public void alignSelectionsByX(NormalSelectionFollower relativeTo, boolean toHighestX) {
    	//TODO fix and uncomment
//    	if (relativeTo == null) return;
//
//        final float relativeToX = (toHighestX)? relativeTo.getVisualRightX() : relativeTo.getVisualX();
//
//        for (SelectionRectangle value : currentSelection.values()) {
//            final float deltaX = value.getX() - value.getVisualX();
//            final float visualX = relativeToX - ((toHighestX)? 1 : 0) * value.getVisualWidth();
//
//            Actor actor = value.getHostAsActor();
//
//            actor.setX(visualX + deltaX);
//            value.setX(actor.getX());
//        }
    }

    public void alignSelectionsByY(NormalSelectionFollower relativeTo, boolean toHighestY) {
    	//TODO fix and uncomment
//    	if (relativeTo == null) return;
//
//        final float relativeToY = (toHighestY)? relativeTo.getVisualTopY() : relativeTo.getVisualY();
//
//        for (SelectionRectangle value : currentSelection.values()) {
//            final float deltaY = value.getY() - value.getVisualY();
//            final float visualY = relativeToY - ((toHighestY)? 1 : 0) * value.getVisualHeight();
//
//            Actor actor = value.getHostAsActor();
//
//            actor.setY(visualY + deltaY);
//            value.setY(actor.getY());
//        }
    }

    public void alignSelectionsAtLeftEdge(NormalSelectionFollower relativeTo) {
    	//TODO fix and uncomment
//        if (relativeTo == null) return;
//
//        final float relativeToX = relativeTo.getVisualX();
//
//        for (SelectionRectangle value : currentSelection.values()) {
//            if (value == relativeTo) continue;
//
//            final float deltaX = value.getX() - value.getVisualX();
//            final float visualX = relativeToX - value.getVisualWidth();
//
//            Actor actor = value.getHostAsActor();
//
//            actor.setX(visualX + deltaX);
//            value.setX(actor.getX());
//        }
    }

    public void alignSelectionsAtRightEdge(NormalSelectionFollower relativeTo) {
    	//TODO fix and uncomment
//        if (relativeTo == null) return;
//
//        final float relativeToRightX = relativeTo.getVisualRightX();
//
//        for (SelectionRectangle value : currentSelection.values()) {
//            if (value == relativeTo) continue;
//
//            final float deltaX = value.getX() - value.getVisualX();
//
//            Actor actor = value.getHostAsActor();
//
//            actor.setX(relativeToRightX + deltaX);
//            value.setX(actor.getX());
//        }
    }

    public void alignSelectionsAtTopEdge(NormalSelectionFollower relativeTo) {
    	//TODO fix and uncomment
//        if (relativeTo == null) return;
//
//        final float relativeToTopY = relativeTo.getVisualTopY();
//
//        for (SelectionRectangle value : currentSelection.values()) {
//            if (value == relativeTo) continue;
//
//            final float deltaY = value.getY() - value.getVisualY();
//
//            Actor actor = value.getHostAsActor();
//
//            actor.setY(relativeToTopY + deltaY);
//            value.setY(actor.getY());
//        }
    }

    public void alignSelectionsAtBottomEdge(NormalSelectionFollower relativeTo) {
    	//TODO fix and uncomment
//        if (relativeTo == null) return;
//
//        final float relativeToY = relativeTo.getVisualY();
//
//        for (SelectionRectangle value : currentSelection.values()) {
//            if (value == relativeTo) continue;
//
//            final float deltaY = value.getY() - value.getVisualY();
//            final float visualY = relativeToY - value.getVisualHeight();
//
//            Actor actor = value.getHostAsActor();
//
//            actor.setY(visualY + deltaY);
//            value.setY(actor.getY());
//        }
    }

    public void alignSelectionsVerticallyCentered(NormalSelectionFollower relativeTo) {
    	//TODO fix and uncomment
//        if (relativeTo == null) return;
//
//        final float relativeToY = relativeTo.getVisualY();
//        final float relativeToHeight = relativeTo.getVisualHeight();
//
//        for (SelectionRectangle value : currentSelection.values()) {
//            if (value == relativeTo) continue;
//
//            final float deltaY = value.getY() - value.getVisualY();
//            final float visualY = relativeToY + (relativeToHeight - value.getVisualHeight()) / 2;
//
//            Actor actor = value.getHostAsActor();
//
//            actor.setY(visualY + deltaY);
//            value.setY(actor.getY());
//        }
    }

    public void alignSelectionsHorizontallyCentered(NormalSelectionFollower relativeTo) {
    	//TODO fix and uncomment
//        if (relativeTo == null) return;
//
//        final float relativeToX = relativeTo.getVisualX();
//        final float relativeToWidth = relativeTo.getVisualWidth();
//
//        for (SelectionRectangle value : currentSelection.values()) {
//            if (value == relativeTo) continue;
//
//            final float deltaX = value.getX() - value.getVisualX();
//            final float visualX = relativeToX + (relativeToWidth - value.getVisualWidth()) / 2;
//
//            Actor actor = value.getHostAsActor();
//
//            actor.setX(visualX + deltaX);
//            value.setX(actor.getX());
//        }
    }

    public void alignSelections(int align) {
        //ResolutionEntryVO resolutionEntryVO = dataManager.getCurrentProjectInfoVO().getResolution(dataManager.currentResolutionName);
        /*
        switch (align) {
            case Align.top:
                alignSelectionsByY(get(topmostItem), true);
                break;
            case Align.left:
                alignSelectionsByX(get(leftmostItem), false);
                break;
            case Align.bottom:
                alignSelectionsByY(get(bottommostItem), false);
                break;
            case Align.right:
                alignSelectionsByX(get(rightmostItem), true);
                break;
            case Align.center | Align.left: //horizontal
                alignSelectionsHorizontallyCentered(get(broadestItem));
                break;
            case Align.center | Align.bottom: //vertical
                alignSelectionsVerticallyCentered(get(highestItem));
                break;
        }*/
    }

    public void alignSelectionsAtEdge(int align) {
        /*
        switch (align) {
            case Align.top:
                alignSelectionsAtTopEdge(get(bottommostItem));
                break;
            case Align.left:
                alignSelectionsAtLeftEdge(get(rightmostItem));
                break;
            case Align.bottom:
                alignSelectionsAtBottomEdge(get(topmostItem));
                break;
            case Align.right:
                alignSelectionsAtRightEdge(get(leftmostItem));
                break;
        }
        */
    }

    /**
     * Moves selected panels by specified values in both directions
     * @param x
     * @param y
     */
    public void moveSelectedItemsBy(float x, float y) {
        for (Entity entity : currentSelection) {
            sandbox.itemControl.moveItemBy(entity, x, y);
            Overlap2DFacade.getInstance().sendNotification(Overlap2D.ITEM_DATA_UPDATED, entity);
        }

        sandbox.saveSceneCurrentSceneData();
    }

    public boolean selectionIsOneItem() {
        if(getCurrentSelection().size() == 1) {
            return true;
        }

        return false;
    }

    public boolean selectionIsComposite() {
    	//TODO fix and uncomment
//        for (SelectionRectangle value : getCurrentSelection().values()) {
//            if (value.getHost().isComposite()) {
//                return true;
//            }
//        }

        return false;
    }

    /**
     * used as accumulator container
     */
    private static class AccContainer {
        public Float carryVal = null;
        public NormalSelectionFollower carry = null;
    }

}
