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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.BiConsumer;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.uwsoft.editor.gdx.actors.SelectionRectangle;
import com.uwsoft.editor.gdx.mediators.SceneControlMediator;
import com.uwsoft.editor.renderer.actor.CompositeItem;
import com.uwsoft.editor.renderer.actor.IBaseItem;
import com.uwsoft.editor.renderer.physics.PhysicsBodyLoader;

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
    private HashMap<Entity, SelectionRectangle> currentSelection = new HashMap<Entity, SelectionRectangle>();

    public ItemSelector(Sandbox sandbox) {
        this.sandbox = sandbox;
        sceneControl = sandbox.sceneControl;
    }

    /***************************** Getters *********************************/

    /**
     * @return HashMap of selection rectangles that contain panels
     */
    public HashMap<Entity, SelectionRectangle> getCurrentSelection() {
        return currentSelection;
    }

    /**
     * @return list of currently selected panels
     */
    public ArrayList<Entity> getSelectedItems() {
        ArrayList<Entity> items = new ArrayList<Entity>();
        for (SelectionRectangle value : currentSelection.values()) {
            items.add(value.getHost());
        }
        return items;
    }

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

    public SelectionRectangle get(BiConsumer<SelectionRectangle, AccContainer> checkSelection) {
        final AccContainer acc = new AccContainer();

        for (SelectionRectangle value : currentSelection.values()) {
            checkSelection.accept(value, acc);
        }
        return acc.carry;
    }

    /***************************** Selectors *********************************/


    /**
     * Creates @SelectionRectangle instance that contains current item, and adds it to the frontUI
     * @param entity to be claimed by newly created @SelectionRectangle
     * @return newly created @SelectionRectangle
     */
    private SelectionRectangle createSelectionRect(Entity entity) {
        SelectionRectangle rect = new SelectionRectangle(sandbox);
        rect.claim(entity);
        rect.setMode(sandbox.getCurrentMode());
        sandbox.getSandboxStage().frontUI.addActor(rect);
        rect.show();

        return rect;
    }

    /**
     * Finds all panels that are on particular layer and selects them
     * @param name of the layer
     */
    public void selectItemsByLayerName(String name) {
        ArrayList<Entity> itemsArr = new ArrayList<Entity>();
        for (int i = 0; i < sceneControl.getCurrentScene().getItems().size(); i++) {
            if (sceneControl.getCurrentScene().getItems().get(i).getDataVO().layerName.equals(name)) {
                itemsArr.add(sceneControl.getCurrentScene().getItems().get(i));
            }
        }

        setSelections(itemsArr, true);
    }

    /**
     * sets selection to particular item
     * @param entity to select
     * @param removeOthers if set to true this item will become the only selection, otherwise will be added to existing
     */
    public void setSelection(Entity entity, boolean removeOthers) {
        if (currentSelection.get(entity) != null) return;

        if (removeOthers) clearSelections();

        SelectionRectangle rect = createSelectionRect(entity);

        currentSelection.put(entity, rect);

        sandbox.getSandboxStage().uiStage.itemWasSelected(entity);

		  sandbox.getUIStage().mainDropDown.hide();

        sandbox.getSandboxStage().uiStage.getItemsBox().setSelected(currentSelection);

    }


    /**
     * set selection to a list of panels
     * @param items list of panels to select
     * @param alsoShow if false, selection will remain hidden at this moment
     */
    public void setSelections(ArrayList<Entity> items, boolean alsoShow) {
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
     * @param entity to un-select
     */
    public void releaseSelection(Entity entity) {
        currentSelection.get(entity).remove();
        currentSelection.remove(entity);

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
     * Selects all panels on currently active scene
     * TODO: This should not select locked panels, check if it's true and remove this comment
     */
    public void selectAllItems() {
        ArrayList<Entity> curr = new ArrayList<Entity>();
        for (int i = 0; i < sandbox.getCurrentScene().getItems().size(); i++) {
            curr.add(sandbox.getCurrentScene().getItems().get(i));
        }

        setSelections(curr, true);
    }



    /************************ Manipulate selected panels  ******************************/

    /**
     * Updates VO objects, and physics related tools of all selected objects
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
     * removes all selected panels from the scene
     */
    public void removeCurrentSelectedItems() {
        for (SelectionRectangle selectionRect : currentSelection.values()) {
            sandbox.itemControl.removeItem(selectionRect.getHostAsActor());
            selectionRect.remove();
        }
//        sandbox.getSandboxStage().uiStage.getItemsBox().init();
        currentSelection.clear();
    }

    public void alignSelectionsByX(SelectionRectangle relativeTo, boolean toHighestX) {
    	if (relativeTo == null) return;

        final float relativeToX = (toHighestX)? relativeTo.getVisualRightX() : relativeTo.getVisualX();

        for (SelectionRectangle value : currentSelection.values()) {
            final float deltaX = value.getX() - value.getVisualX();
            final float visualX = relativeToX - ((toHighestX)? 1 : 0) * value.getVisualWidth();

            Actor actor = value.getHostAsActor();

            actor.setX(visualX + deltaX);
            value.setX(actor.getX());
        }
    }

    public void alignSelectionsByY(SelectionRectangle relativeTo, boolean toHighestY) {
    	if (relativeTo == null) return;

        final float relativeToY = (toHighestY)? relativeTo.getVisualTopY() : relativeTo.getVisualY();

        for (SelectionRectangle value : currentSelection.values()) {
            final float deltaY = value.getY() - value.getVisualY();
            final float visualY = relativeToY - ((toHighestY)? 1 : 0) * value.getVisualHeight();

            Actor actor = value.getHostAsActor();

            actor.setY(visualY + deltaY);
            value.setY(actor.getY());
        }
    }

    public void alignSelectionsAtLeftEdge(SelectionRectangle relativeTo) {
        if (relativeTo == null) return;

        final float relativeToX = relativeTo.getVisualX();

        for (SelectionRectangle value : currentSelection.values()) {
            if (value == relativeTo) continue;

            final float deltaX = value.getX() - value.getVisualX();
            final float visualX = relativeToX - value.getVisualWidth();

            Actor actor = value.getHostAsActor();

            actor.setX(visualX + deltaX);
            value.setX(actor.getX());
        }
    }

    public void alignSelectionsAtRightEdge(SelectionRectangle relativeTo) {
        if (relativeTo == null) return;

        final float relativeToRightX = relativeTo.getVisualRightX();

        for (SelectionRectangle value : currentSelection.values()) {
            if (value == relativeTo) continue;

            final float deltaX = value.getX() - value.getVisualX();

            Actor actor = value.getHostAsActor();

            actor.setX(relativeToRightX + deltaX);
            value.setX(actor.getX());
        }
    }

    public void alignSelectionsAtTopEdge(SelectionRectangle relativeTo) {
        if (relativeTo == null) return;

        final float relativeToTopY = relativeTo.getVisualTopY();

        for (SelectionRectangle value : currentSelection.values()) {
            if (value == relativeTo) continue;

            final float deltaY = value.getY() - value.getVisualY();

            Actor actor = value.getHostAsActor();

            actor.setY(relativeToTopY + deltaY);
            value.setY(actor.getY());
        }
    }

    public void alignSelectionsAtBottomEdge(SelectionRectangle relativeTo) {
        if (relativeTo == null) return;

        final float relativeToY = relativeTo.getVisualY();

        for (SelectionRectangle value : currentSelection.values()) {
            if (value == relativeTo) continue;

            final float deltaY = value.getY() - value.getVisualY();
            final float visualY = relativeToY - value.getVisualHeight();

            Actor actor = value.getHostAsActor();

            actor.setY(visualY + deltaY);
            value.setY(actor.getY());
        }
    }

    public void alignSelectionsVerticallyCentered(SelectionRectangle relativeTo) {
        if (relativeTo == null) return;

        final float relativeToY = relativeTo.getVisualY();
        final float relativeToHeight = relativeTo.getVisualHeight();

        for (SelectionRectangle value : currentSelection.values()) {
            if (value == relativeTo) continue;

            final float deltaY = value.getY() - value.getVisualY();
            final float visualY = relativeToY + (relativeToHeight - value.getVisualHeight()) / 2;

            Actor actor = value.getHostAsActor();

            actor.setY(visualY + deltaY);
            value.setY(actor.getY());
        }
    }

    public void alignSelectionsHorizontallyCentered(SelectionRectangle relativeTo) {

        if (relativeTo == null) return;

        final float relativeToX = relativeTo.getVisualX();
        final float relativeToWidth = relativeTo.getVisualWidth();

        for (SelectionRectangle value : currentSelection.values()) {
            if (value == relativeTo) continue;

            final float deltaX = value.getX() - value.getVisualX();
            final float visualX = relativeToX + (relativeToWidth - value.getVisualWidth()) / 2;

            Actor actor = value.getHostAsActor();

            actor.setX(visualX + deltaX);
            value.setX(actor.getX());
        }
    }

    public void alignSelections(int align) {
        //ResolutionEntryVO resolutionEntryVO = dataManager.getCurrentProjectInfoVO().getResolution(dataManager.currentResolutionName);
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
        }
    }

    public void alignSelectionsAtEdge(int align) {
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
    }

    /**
     * Moves selected panels by specified values in both directions
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

    public boolean selectionIsComposite() {
        for (SelectionRectangle value : getCurrentSelection().values()) {
            if (value.getHost().isComposite()) {
                return true;
            }
        }

        return false;
    }

    /**
     * used as accumulator container
     */
    private static class AccContainer {
        public Float carryVal = null;
        public SelectionRectangle carry = null;
    }

}
