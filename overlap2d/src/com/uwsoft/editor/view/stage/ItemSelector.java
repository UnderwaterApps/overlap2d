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

package com.uwsoft.editor.view.stage;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.SnapshotArray;
import com.commons.MsgAPI;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.renderer.components.NodeComponent;
import com.uwsoft.editor.renderer.data.LayerItemVO;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.utils.Constants;
import com.uwsoft.editor.utils.EntityBounds;
import com.uwsoft.editor.utils.MoveCommandBuilder;
import com.uwsoft.editor.utils.runtime.EntityUtils;
import com.uwsoft.editor.view.SceneControlMediator;
import com.uwsoft.editor.view.ui.FollowersUIMediator;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * Managing item selections, selecting by criteria and so on
 *
 * @author azakhary
 */
public class ItemSelector {

    /** commands reference */
    private Sandbox sandbox;

    private SceneControlMediator sceneControl;

    /** list of current selected panels */
    private Set<Entity> currentSelection = new HashSet<>();

    private FollowersUIMediator followersUIMediator;

    private MoveCommandBuilder moveCommandBuilder = new MoveCommandBuilder();

    public ItemSelector(Sandbox sandbox) {
        this.sandbox = sandbox;
        sceneControl = sandbox.sceneControl;

        followersUIMediator = Overlap2DFacade.getInstance().retrieveMediator(FollowersUIMediator.NAME);
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


    public BiConsumer<Entity, AccContainer> broadestItem = (i, acc) -> {
        if (acc.carryVal == null) acc.carryVal = Constants.FLOAT_MIN;
        EntityBounds bounds = new EntityBounds(i);
        final float width = bounds.getVisualWidth();
        if (width > acc.carryVal) {
            acc.carryVal = width;
            acc.carry = i;
        }
    };

    public BiConsumer<Entity, AccContainer> highestItem = (i, acc) -> {
        if (acc.carryVal == null) acc.carryVal = Constants.FLOAT_MIN;
        EntityBounds bounds = new EntityBounds(i);
        final float height = bounds.getVisualHeight();
        if (height > acc.carryVal) {
            acc.carryVal = height;
            acc.carry = i;
        }
    };

    public BiConsumer<Entity, AccContainer> rightmostItem = (i, acc) -> {
        if (acc.carryVal == null) acc.carryVal = Constants.FLOAT_MIN;
        EntityBounds bounds = new EntityBounds(i);
        final float x = bounds.getVisualRightX();
        if (x > acc.carryVal) {
            acc.carryVal = x;
            acc.carry = i;
        }
        System.out.println("MaxFloat  = " + Float.MAX_VALUE + " MinFloat = " + Float.MIN_VALUE);
    };

    public BiConsumer<Entity, AccContainer> leftmostItem = (i, acc) -> {
        if (acc.carryVal == null) acc.carryVal = Constants.FLOAT_MAX;
        EntityBounds bounds = new EntityBounds(i);
        final float x = bounds.getVisualX();
        if (x < acc.carryVal) {
            acc.carryVal = x;
            acc.carry = i;
        }
    };

    public BiConsumer<Entity, AccContainer> topmostItem = (i, acc) -> {
        if (acc.carryVal == null) acc.carryVal = Constants.FLOAT_MIN;
        EntityBounds bounds = new EntityBounds(i);
        final float y = bounds.getVisualTopY();
        if (y > acc.carryVal) {
            acc.carryVal = y;
            acc.carry = i;
        }
    };
    public BiConsumer<Entity, AccContainer> bottommostItem = (i, acc) -> {
        if (acc.carryVal == null) acc.carryVal = Constants.FLOAT_MAX;
        EntityBounds bounds = new EntityBounds(i);
        final float y = bounds.getVisualY();
        if (y < acc.carryVal) {
            acc.carryVal = y;
            acc.carry = i;
        }
    };


    /**
     * used as accumulator container
     */
    private static class AccContainer {
        public Float carryVal = null;
        public Entity carry = null;
    }


    public Entity get(BiConsumer<Entity, AccContainer> checkSelection) {
        final AccContainer acc = new AccContainer();

        for (Entity entity : currentSelection) {
            checkSelection.accept(entity, acc);
        }
        return acc.carry;
    }


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

        Overlap2DFacade.getInstance().sendNotification(MsgAPI.ITEM_SELECTION_CHANGED, currentSelection);
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

    public boolean isSelected(Entity entity) {
        return currentSelection.contains(entity);
    }

    /**
     * set selection to a list of items
     * @param items list of panels to select
     * @param alsoShow if false, selection will remain hidden at this moment
     */
    public void setSelections(Set<Entity> items, boolean alsoShow) {
        currentSelection.clear();

        if(items == null) {
            Overlap2DFacade.getInstance().sendNotification(MsgAPI.ITEM_SELECTION_CHANGED, currentSelection);
            return;
        }

        currentSelection.addAll(items.stream().collect(Collectors.toList()));

        if (alsoShow) {
            Overlap2DFacade.getInstance().sendNotification(MsgAPI.SHOW_SELECTIONS, currentSelection);
        } else {
            Overlap2DFacade.getInstance().sendNotification(MsgAPI.HIDE_SELECTIONS, currentSelection);
        }
        Overlap2DFacade.getInstance().sendNotification(MsgAPI.ITEM_SELECTION_CHANGED, currentSelection);
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

        Overlap2DFacade.getInstance().sendNotification(MsgAPI.ITEM_SELECTION_CHANGED, currentSelection);
    }

    /**
     * clears all selections
     */
    public void clearSelections() {
        currentSelection.clear();

        Overlap2DFacade.getInstance().sendNotification(MsgAPI.ITEM_SELECTION_CHANGED, currentSelection);
    }


    /**
     * Selects all panels on currently active scene
     */
    public HashSet<Entity> getAllFreeItems() {
    	NodeComponent nodeComponent = ComponentRetriever.get(sandbox.getCurrentViewingEntity(), NodeComponent.class);
		SnapshotArray<Entity> childrenEntities = nodeComponent.children;

        Entity[] array = childrenEntities.toArray();
        HashSet<Entity> result = new HashSet<>(Arrays.asList(array));

        for (Iterator<Entity> i = result.iterator(); i.hasNext();) {
            Entity element = i.next();
            LayerItemVO layerItemVO = EntityUtils.getEntityLayer(element);
            if(layerItemVO != null && layerItemVO.isLocked) {
                i.remove();
            }
        }

        return result;
    }



    /************************ Manipulate selected panels  ******************************/

    /**
     * removes all selected panels from the scene
     */
    public void removeCurrentSelectedItems() {
        for (Entity item : currentSelection) {
            followersUIMediator.removeFollower(item);
            sandbox.getEngine().removeEntity(item);
        }

        currentSelection.clear();
    }

    public void alignSelectionsByX(Entity relativeTo, boolean toHighestX) {
    	//TODO fix and uncomment
    	if (relativeTo == null) return;

        EntityBounds bounds = new EntityBounds(relativeTo);
        final float relativeToX = (toHighestX)? (bounds.getVisualRightX()) : bounds.getVisualX();

        moveCommandBuilder.clear();
        for (Entity entity : currentSelection) {
            EntityBounds entityBounds = new EntityBounds(entity);
            final float deltaX = entityBounds.getX() - entityBounds.getVisualX();
            final float visualX = relativeToX - ((toHighestX)? 1 : 0) * entityBounds.getVisualWidth();

            moveCommandBuilder.setX(entity, visualX + deltaX);
        }
        moveCommandBuilder.execute();
    }

    public void alignSelectionsByY(Entity relativeTo, boolean toHighestY) {
    	//TODO fix and uncomment
    	if (relativeTo == null) return;

        EntityBounds bounds = new EntityBounds(relativeTo);
        final float relativeToY = (toHighestY)? bounds.getVisualTopY() : bounds.getVisualY();

        moveCommandBuilder.clear();
        for (Entity entity : currentSelection) {
            EntityBounds entityBounds = new EntityBounds(entity);
            final float deltaY = entityBounds.getY() - entityBounds.getVisualY();
            final float visualY = relativeToY - ((toHighestY)? 1 : 0) * entityBounds.getVisualHeight();

            moveCommandBuilder.setY(entity, visualY + deltaY);
        }
        moveCommandBuilder.execute();
    }

    public void alignSelectionsAtLeftEdge(Entity relativeTo) {
    	//TODO fix and uncomment
        if (relativeTo == null) return;

        EntityBounds bounds = new EntityBounds(relativeTo);
        final float relativeToX = bounds.getVisualX();

        moveCommandBuilder.clear();
        for (Entity entity : currentSelection) {
            if (entity == relativeTo) continue;
            EntityBounds entityBounds = new EntityBounds(entity);

            final float deltaX = entityBounds.getX() - entityBounds.getVisualX();
            final float visualX = relativeToX - entityBounds.getVisualWidth();

            moveCommandBuilder.setX(entity, visualX + deltaX);
        }
        moveCommandBuilder.execute();
    }

    public void alignSelectionsAtRightEdge(Entity relativeTo) {
    	//TODO fix and uncomment
        if (relativeTo == null) return;

        EntityBounds bounds = new EntityBounds(relativeTo);
        final float relativeToRightX = bounds.getVisualRightX();

        moveCommandBuilder.clear();
        for (Entity entity : currentSelection) {
            if (entity == relativeTo) continue;
            EntityBounds entityBounds = new EntityBounds(entity);

            final float deltaX = entityBounds.getX() - entityBounds.getVisualX();

            moveCommandBuilder.setX(entity, relativeToRightX + deltaX);
        }
        moveCommandBuilder.execute();
    }

    public void alignSelectionsAtTopEdge(Entity relativeTo) {
    	//TODO fix and uncomment
        if (relativeTo == null) return;

        EntityBounds bounds = new EntityBounds(relativeTo);
        final float relativeToTopY = bounds.getVisualTopY();

        moveCommandBuilder.clear();
        for (Entity entity : currentSelection) {
            if (entity == relativeTo) continue;
            EntityBounds entityBounds = new EntityBounds(entity);

            final float deltaY = entityBounds.getY() - entityBounds.getVisualY();

            moveCommandBuilder.setY(entity, relativeToTopY + deltaY);
        }
        moveCommandBuilder.execute();
    }

    public void alignSelectionsAtBottomEdge(Entity relativeTo) {
    	//TODO fix and uncomment
        if (relativeTo == null) return;

        EntityBounds bounds = new EntityBounds(relativeTo);
        final float relativeToY = bounds.getVisualY();

        moveCommandBuilder.clear();
        for (Entity entity : currentSelection) {
            if (entity == relativeTo) continue;
            EntityBounds entityBounds = new EntityBounds(entity);

            final float deltaY = entityBounds.getY() - entityBounds.getVisualY();
            final float visualY = relativeToY - entityBounds.getVisualHeight();

             moveCommandBuilder.setY(entity, visualY + deltaY);
        }
        moveCommandBuilder.execute();
    }

    public void alignSelectionsVerticallyCentered(Entity relativeTo) {
    	//TODO fix and uncomment
        if (relativeTo == null) return;

        EntityBounds bounds = new EntityBounds(relativeTo);
        final float relativeToY = bounds.getVisualY();
        final float relativeToHeight = bounds.getVisualHeight();

        moveCommandBuilder.clear();
        for (Entity entity : currentSelection) {
            if (entity == relativeTo) continue;
            EntityBounds entityBounds = new EntityBounds(entity);

            final float deltaY = entityBounds.getY() - entityBounds.getVisualY();
            final float visualY = relativeToY + (relativeToHeight - entityBounds.getVisualHeight()) / 2;

            moveCommandBuilder.setY(entity, visualY + deltaY);
        }
        moveCommandBuilder.execute();
    }

    public void alignSelectionsHorizontallyCentered(Entity relativeTo) {
    	//TODO fix and uncomment
        if (relativeTo == null) return;

        EntityBounds bounds = new EntityBounds(relativeTo);
        final float relativeToX = bounds.getVisualX();
        final float relativeToWidth = bounds.getVisualWidth();

        moveCommandBuilder.clear();
        for (Entity entity : currentSelection) {
            if (entity == relativeTo) continue;
            EntityBounds entityBounds = new EntityBounds(entity);

            final float deltaX = entityBounds.getX() - entityBounds.getVisualX();
            final float visualX = relativeToX + (relativeToWidth - entityBounds.getVisualWidth()) / 2;

            moveCommandBuilder.setX(entity, visualX + deltaX);
        }
        moveCommandBuilder.execute();
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
        for (Entity entity : currentSelection) {
            sandbox.itemControl.moveItemBy(entity, x, y);
            Overlap2DFacade.getInstance().sendNotification(MsgAPI.ITEM_DATA_UPDATED, entity);
        }

        sandbox.saveSceneCurrentSceneData();
    }

    public boolean selectionIsOneItem() {
        return getCurrentSelection().size() == 1;
    }

    public boolean selectionIsComposite() {

        if(currentSelection.isEmpty()) return false;

        Entity entity = currentSelection.stream().findFirst().get();
        NodeComponent nodeComponent = entity.getComponent(NodeComponent.class);

        return nodeComponent != null;
    }
}
