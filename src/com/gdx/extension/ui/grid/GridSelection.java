/** Copyright 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gdx.extension.ui.grid;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.gdx.extension.exception.OptimizationException;
import com.gdx.extension.ui.SelectionMode;
import com.gdx.extension.ui.group.ActorGroup;


/**
 * It's a simple {@link Table} layouted as a grid.
 * Events can be set on each {@link GridSelectionItem}
 * 
 * @author Kyu
 */
public class GridSelection<T extends GridSelectionItem> extends Table
{
    
    private Array<HorizontalGroup> rows;
    private Array<VerticalGroup> columns;
    
    private boolean isVertical;
    
    protected float itemWidth = 100f;
    protected float itemHeight = 100f;

    protected int itemCount;
    private SelectionMode selectionMode;
    
    private boolean isSelectable = true;
    
    private ActorGroup<T> gridItemGroup;
    
    /**
     * Create a {@link GridSelection} with default parameters (vertical and 3 column).
     */
    public GridSelection()
    {
	this(true, 3);
    }
    
    /**
     * Create a {@link GridSelection} with defined parameters.
     * 
     * @param isVertical If {@link GridSelection} should be constructed and scrolled vertically or not
     * @param itemCount Number of columns if isVertical is true or number of rows if isVertical is false
     */
    public GridSelection(boolean isVertical, int itemCount)
    {
	super();
	
	this.isVertical = isVertical;
	
	align(Align.left + Align.top);
	
	gridItemGroup = new ActorGroup<T>();
	gridItemGroup.setMinCheckCount(1);
	
	setSelectionMode(SelectionMode.SINGLE);
	
	if(isVertical)
	{
	    columns = new Array<VerticalGroup>();
	    for(int i = 0; i < itemCount; i++)
		addColumn();
	}
	else
	{
	    rows = new Array<HorizontalGroup>();
	    for(int i = 0; i < itemCount; i++)
		addRow();
	}
	
    }
    
    /**
     * Add a row to the {@link GridSelection grid}.
     * The {@link GridSelection grid} must be in horizontal mode.
     * 
     * @return the added row
     * @throws OptimizationException if try to add row in vertical mode
     */
    public HorizontalGroup addRow()
    {
	if(isVertical)
	    throw new OptimizationException("GridSelection is set to vertical, so you cannot add rows.");
	
	HorizontalGroup _group = new HorizontalGroup();
	rows.add(_group);
	super.add(_group).left().row();
	itemCount++;
	
	return _group;
    }
    
    /**
     * Remove a row from the {@link GridSelection grid}.
     * The {@link GridSelection grid} must be in horizontal mode.
     * 
     * @param row the row to remove
     * @throws OptimizationException if try to remove row in vertical mode
     */
    public void removeRow(HorizontalGroup row)
    {
	if(isVertical)
	    throw new OptimizationException("GridSelection is set to vertical, so you cannot remove rows.");
	
	rows.removeValue(row, false);
	row.remove();
	itemCount--;
    }
    
    /**
     * Add a column to the {@link GridSelection grid}.
     * The {@link GridSelection grid} must be in vertical mode.
     * 
     * @return the added row
     * @throws OptimizationException if try to add column in horizontal mode
     */
    public VerticalGroup addColumn()
    {
	if(!isVertical)
	    throw new OptimizationException("GridSelection is set to horizontal, so you cannot add columns.");
	
	VerticalGroup _group = new VerticalGroup();
	columns.add(_group);
	super.add(_group).top();
	itemCount++;
	
	return _group;
    }
    
    /**
     * Remove a column from the {@link GridSelection grid}.
     * The {@link GridSelection grid} must be in vertical mode.
     * 
     * @param column the column to remove
     * @throws OptimizationException if try to remove column in horizontal mode
     */
    public void removeColumn(VerticalGroup column)
    {
	if(!isVertical)
	    throw new OptimizationException("GridSelection is set to horizontal, so you cannot remove columns.");
	
	columns.removeValue(column, false);
	column.remove();
	itemCount--;
    }
    
    @Deprecated
    @Override
    public Cell<Actor> add() { throw new OptimizationException("Use addItem(GridSelectionItem) instead."); };
    
    @Deprecated
    @Override
    public Cell<Actor> add(Actor actor) { throw new OptimizationException("Use addItem(GridSelectionItem) instead."); }
    
    @Deprecated
    @Override
    public Cell<Label> add(String text) { throw new OptimizationException("Use addItem(GridSelectionItem) instead."); }
    
    @Deprecated
    @Override
    public Cell<Label> add(String text, String fontName, Color color) { throw new OptimizationException("Use addItem(GridSelectionItem) instead."); }
    
    @Deprecated
    @Override
    public Cell<Label> add(String text, String fontName, String colorName) { throw new OptimizationException("Use addItem(GridSelectionItem) instead."); }
    
    @Deprecated
    @Override
    public Cell<Label> add(String text, String labelStyleName) { throw new OptimizationException("Use addItem(GridSelectionItem) instead."); }
    
    @Deprecated
    @Override
    public void addActor(Actor actor) { super.addActor(actor); }
    
    @Deprecated
    @Override
    public void addActorAfter(Actor actorAfter, Actor actor) { throw new OptimizationException("Use addItem(GridSelectionItem) instead."); }
    
    @Deprecated
    @Override
    public void addActorAt(int index, Actor actor) { throw new OptimizationException("Use addItem(GridSelectionItem) instead."); }
    
    @Deprecated
    @Override
    public void addActorBefore(Actor actorBefore, Actor actor) { throw new OptimizationException("Use addItem(GridSelectionItem) instead."); }
    
    /**
     * Add a {@link GridSelectionItem} to the {@link GridSelection grid} according to {@link isVertical} and {@link itemCount}.
     * 
     * @param item  {@link GridSelectionItem} to add
     * @throws IllegalArgumentException if item is null
     */
    public void addItem(final T item)
    {
	if(item == null)
	    throw new IllegalArgumentException("item cannot be null.");
	   
	item.setGrid(this);
	item.setSize(itemWidth, itemHeight);
	
	gridItemGroup.add(item);
	if (isVertical)
	{
	    columns.get((gridItemGroup.getActors().size - 1) % itemCount).addActor(item);
	}
	else
	{
	    rows.get((gridItemGroup.getActors().size - 1) % itemCount).addActor(item);
	}
    }
    
    /**
     * Remove a {@link GridSelectionItem} and relayout the {@link GridSelection}.
     * 
     * @param item {@link GridSelectionItem} to remove
     */
    public void removeItem(T item)
    {
	removeItem(item, true);
    }
    
    /**
     * Remove a {@link GridSelectionItem}.
     * 
     * @param item {@link GridSelectionItem} to remove
     * @param relayout true if you want to relayout the whole {@link GridSelection grid}
     * @throws IllegalArgumentException if item is null
     */
    public void removeItem(T item, boolean relayout)
    {
	if(item == null)
	    throw new IllegalArgumentException("item cannot be null.");
	
	gridItemGroup.remove(item);
	
	if(relayout)
	    relayout();
    }
    
    @Deprecated
    @Override
    public boolean removeActor(Actor actor)
    {
	throw new OptimizationException("Use removeItem(GridSelectionItem) instead.");
    }
    
    /**
     * Relayout the grid with {@link itemCount}.
     */
    public void relayout()
    {
	SnapshotArray<T> _actors = new SnapshotArray<T>(gridItemGroup.getActors());
	
	gridItemGroup.clear();
	if(isVertical)
	{
	    for(VerticalGroup _column : columns)
		_column.clear();
	}
	else
	{
	    for(HorizontalGroup _row : rows)
		_row.clear();
	}
	
	for(T _item : _actors)
	    addItem(_item);
    }
    
    /**
     * Set the selected {@link GridSelectionItem item} by index.
     * 
     * @param index the index of the {@link GridSelectionItem item} to be selected.
     */
    public void setSelected(int index)
    {
	Array<T> _items = gridItemGroup.getActors();
	if(index <= 0 || index >= _items.size )
	    return;
	
	setSelected(_items.get(index));
    }
    
    /**
     * Set the selected {@link GridSelectionItem item}
     * 
     * @param item the {@link GridSelectionItem item} to be selected.
     * @throws IllegalArgumentException if item is null
     */
    public void setSelected(T item)
    {
	if(item == null)
	    throw new IllegalArgumentException("Selected item cannot be null.");
	
	gridItemGroup.setChecked(item);
    }
    
    /**
     * Check if an {@link GridSelectionItem item} is selected by index.
     * 
     * @param index the index of the {@link GridSelectionItem item} to check
     * @return true if selected, false if not
     */
    public boolean isSelected(int index)
    {
	return isSelected(gridItemGroup.getAllChecked().get(index));
    }
    
    /**
     * Check if {@link GridSelectionItem item} is selected.
     * 
     * @param item the {@link GridSelectionItem item} to check
     * @return true if selected, false if not
     */
    public boolean isSelected(T item)
    {
	if(item == null)
	    return false;
	
	return item.isChecked();
    }
    
    /**
     * Unselect an {@link GridSelectionItem item} by index.
     * 
     * @param index the index of the {@link GridSelectionItem item} to unselect
     */
    public void unselect(int index)
    {
	unselect(gridItemGroup.getAllChecked().get(index));
    }
    
    /**
     * Unselect an {@link GridSelectionItem item}.
     * 
     * @param item the {@link GridSelectionItem item} to unselect
     */
    public void unselect(T item)
    {
	gridItemGroup.uncheck(item);
    }
    
    /**
     * @return If {@link GridSelection} should be created vertically or horizontally
     */
    public boolean isVertical()
    {
	return isVertical;
    }
    
    /**
     * Set if {@link GridSelectionItem items} of this {@link GridSelection grid} are selectable.
     * 
     * @param isSelectable true if {@link GridSelectionItem items} are selectable, false if not.
     */
    public void setSelectable(boolean isSelectable)
    {
	this.isSelectable = isSelectable;
    }
    
    /**
     * @return If {@link GridSelectionItem items} are selectable
     */
    public boolean isSelectable()
    {
	return isSelectable;
    }
    
    /**
     * @return Number of columns if isVertical is true else, number of rows
     */
    public int getItemCount()
    {
	return itemCount;
    }
    
    /**
     * @return Return the current selection
     */
    public Array<T> getSelection()
    {	    
	return gridItemGroup.getAllChecked();
    }
    
    /**
     * Set the {@link SelectionMode} of the {@link GridSelection grid}.
     * 
     * @param mode the {@link SelectionMode}
     */
    public void setSelectionMode(SelectionMode mode)
    {
	selectionMode = mode;
    }
    
    /**
     * Get the {@link SelectionMode} of this {@link GridSelection grid}.
     * 
     * @return the {@link SelectionMode} of this {@link GridSelection grid}
     */
    public SelectionMode getSelectionMode()
    {
	return selectionMode;
    }

    /**
     * @return the width of {@link GridSelectionItem items}
     */
    public float getItemWidth()
    {
	return itemWidth;
    }

    /**
     * Set the width of {@link GridSelectionItem items}.
     * 
     * @param itemWidth the width of {@link GridSelectionItem items}
     */
    public void setItemWidth(float itemWidth)
    {
	this.itemWidth = itemWidth;
	
	invalidateHierarchy();
    }

    /**
     * @return the height of {@link GridSelectionItem items}
     */
    public float getItemHeight()
    {
	return itemHeight;
    }

    /**
     * Set the height of {@link GridSelectionItem items}.
     * 
     * @param itemHeight the height of {@link GridSelectionItem items}
     */
    public void setItemHeight(float itemHeight)
    {
	this.itemHeight = itemHeight;
	
	invalidateHierarchy();
    }
    
    /**
     * Set the itemWidth and itemHeight with only one relayout.
     * 
     * @param itemWidth the width of {@link GridSelectionItem items}
     * @param itemHeight the height of {@link GridSelectionItem items}
     */
    public void setItemSize(float itemWidth, float itemHeight)
    {
	this.itemWidth = itemWidth;
	this.itemHeight = itemHeight;
	
	invalidateHierarchy();
    }
    
    /**
     * Invalidate children sizes.
     */
    @Override
    public void invalidateHierarchy()
    {
        super.invalidateHierarchy();
        
        for(GridSelectionItem _actor : gridItemGroup.getActors())
            _actor.setSize(itemWidth, itemHeight);
    }

}
