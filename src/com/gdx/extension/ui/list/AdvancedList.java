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
package com.gdx.extension.ui.list;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.gdx.extension.exception.OptimizationException;
import com.gdx.extension.ui.SelectionMode;
import com.gdx.extension.ui.group.ActorGroup;

/**
 * Create a list with custom {@link ListRow rows}.
 * 
 * @param <T>
 */
public class AdvancedList<T extends ListRow> extends VerticalGroup
{

    private SelectionMode selectionMode;

    private boolean isSelectable = true;

    private ActorGroup<T> listRowGroup;

    /**
     * Create an {@link AdvancedList} with no default {@link ListRow items}.
     */
    public AdvancedList()
    {
	this(null);
    }

    /**
     * Create an {@link AdvancedList} and fill it with {@link ListRow items}.
     * 
     * @param items array of {@link ListRow} to fill with
     */
    public AdvancedList(T[] items)
    {
	listRowGroup = new ActorGroup<T>();
	listRowGroup.setMinCheckCount(1);

	setSelectionMode(SelectionMode.SINGLE);
	left();

	if (items == null)
	    return;

	for (T _item : items)
	    addItem(_item);
    }

    public IntArray getSelectedIndexes()
    {
	IntArray _indexes = new IntArray();
	for (T _item : listRowGroup.getAllChecked())
	    _indexes.add(listRowGroup.getActors().indexOf(_item, true));

	return _indexes;
    }

    /**
     * @return the {@link ListRow ListRows} of the currently selected items.
     */
    public Array<T> getSelection()
    {
	return listRowGroup.getAllChecked();
    }

    /**
     * @return all {@link ListRow} of this list.
     */
    public Array<T> getItems()
    {
	return listRowGroup.getActors();
    }

    /**
     * Add a {@link ListRow} to this list and set it to selected.
     * 
     * @param item the {@link ListRow} to add
     * @throws IllegalArgumentException if item is null
     */
    public void addItem(final T item)
    {
	if (item == null)
	    throw new IllegalArgumentException("item cannot be null.");

	super.addActor(item);
	item.setList(this);
	listRowGroup.add(item);
    }

    @Deprecated
    @Override
    public void addActor(Actor actor) {
	throw new OptimizationException("Use addItem(ListRow) instead.");
    }

    @Deprecated
    @Override
    public void addActorAfter(Actor actorAfter, Actor actor) {
	throw new OptimizationException("Use addItem(ListRow) instead.");
    }

    @Deprecated
    @Override
    public void addActorAt(int index, Actor actor) {
	throw new OptimizationException("Use addItem(ListRow) instead.");
    }

    @Deprecated
    @Override
    public void addActorBefore(Actor actorBefore, Actor actor) {
	throw new OptimizationException("Use addItem(ListRow) instead.");
    }

    /**
     * Remove a {@link ListRow} from this list.
     * Next {@link ListRow} will be set to selected. If there are no more {@link ListRow} next then it select the previous.
     * 
     * @param item the {@link ListRow} to remove
     * @throws IllegalArgumentException if item is null
     */
    public void removeItem(T item)
    {
	if (item == null)
	    throw new IllegalArgumentException("item cannot be null.");

	Array<T> _items = listRowGroup.getActors();
	int _index = _items.indexOf(item, true);

	listRowGroup.remove(item);
	item.remove();

	// Select an item near the one removed
	if (_items.size > 1)
	{
	    if (_index < _items.size) {
		Gdx.app.debug("Select", "Last index");
		setSelected(_index);
	    } else {
		Gdx.app.debug("Select", "Previous index");
		setSelected(_index - 1);
	    }
	}
    }

    /**
     * Remove an {@link Array array} of {@link ListRow}.
     * Next {@link ListRow} will be set to selected. If there are no more {@link ListRow} next then it select the previous.
     * 
     * @param items the {@link Array array} of {@link ListRow} to removes
     */
    public void removeItems(Array<T> items)
    {
	for (T _item : items)
	    removeItem(_item);
    }

    /**
     * Remove severals {@link ListRow} from this list.
     * Next {@link ListRow} will be set to selected. If there are no more {@link ListRow} next then it select the previous.
     * 
     * @param items the {@link ListRow ListRows} to removes.
     */
    public void removeItems(T... items)
    {
	for (T _item : items)
	    removeItem(_item);
    }

    @Deprecated
    @Override
    public boolean removeActor(Actor actor) {
	return super.removeActor(actor);
    }

    /**
     * Set the selected {@link ListRow item} by index.
     * 
     * @param index the index of the {@link ListRow item} to set selected
     */
    public void setSelected(int index)
    {
	Array<T> _items = listRowGroup.getActors();
	if (index <= 0 || index >= _items.size)
	    return;

	setSelected(_items.get(index));
    }

    /**
     * Set the selected {@link ListRow item}.
     * 
     * @param item the {@link ListRow item} to set selected
     * @throws IllegalArgumentException if item is null
     */
    public void setSelected(T item)
    {
	if (item == null)
	    throw new IllegalArgumentException("Selected item cannot be null.");

	listRowGroup.setChecked(item);
    }

    /**
     * Get the selected state of an {@link ListRow item} by index.
     * 
     * @param index the index of the item to check
     * @return false if index not found, otherwise the checked state
     */
    public boolean isSelected(int index)
    {
	return isSelected(listRowGroup.getAllChecked().get(index));
    }

    /**
     * Get the selected state of an {@link ListRow item}.
     * 
     * @param item the item to check
     * @return false if item is null, otherwise the checked state
     */
    public boolean isSelected(T item)
    {
	if (item == null)
	    return false;

	return item.isChecked();
    }

    /**
     * Set the selectable state of this {@link AdvancedList list}.
     * 
     * @param isSelectable true if {@link ListRow ListRows} should be selectable, false if not.
     */
    public void setSelectable(boolean isSelectable)
    {
	this.isSelectable = isSelectable;
    }

    /**
     * @return if {@link ListRow ListRows} are selectable.
     */
    public boolean isSelectable()
    {
	return isSelectable;
    }

    /**
     * Unselect an {@link ListRow item} by index.
     * 
     * @param index the index of the {@link ListRow item} to unselect
     */
    public void unselect(int index)
    {
	unselect(listRowGroup.getAllChecked().get(index));
    }

    /**
     * Unselect an {@link ListRow item}.
     * 
     * @param item the {@link ListRow item} to unselect
     */
    public void unselect(T item)
    {
	listRowGroup.uncheck(item);
    }

    /**
     * Set the {@link SelectionMode} of the list.
     * 
     * @param mode the new {@link SelectionMode} of the list.
     */
    public void setSelectionMode(SelectionMode mode)
    {
	selectionMode = mode;
    }

    /**
     * @return the current {@link SelectionMode} of the list.
     */
    public SelectionMode getSelectionMode()
    {
	return selectionMode;
    }

}
