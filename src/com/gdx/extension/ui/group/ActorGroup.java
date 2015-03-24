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
package com.gdx.extension.ui.group;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

/**
 * This class is used for selection.
 * 
 * @author Kyu
 * 
 * @param <T> the type of groupable actor
 */
public class ActorGroup<T extends Groupable>
{

    protected Array<T> actors = new Array<T>();
    protected Array<T> checked = new Array<T>(1);
    protected int minCheckCount, maxCheckCount = 1;
    protected boolean uncheckLast = true;
    protected T lastChecked;

    private boolean isMultiOnCtrl = true;
    private boolean isMultiSelection;

    public ActorGroup()
    {

    }

    /**
     * Add an actor to the group.
     * 
     * @param actor the actor to add
     * @throws IllegalArgumentException if actor is null
     */
    public void add(T actor)
    {
	if (actor == null)
	    throw new IllegalArgumentException("actor cannot be null.");

	boolean _shouldCheck = actor.isChecked() || actors.size < minCheckCount;
	actor.setActorGroup(this);
	actors.add(actor);
	if (_shouldCheck)
	    actor.setChecked(true);
    }

    /**
     * Remove an actor from the group.
     * 
     * @param actor the actor to remove
     * @throws IllegalArgumentException if actor is null
     */
    public void remove(T actor)
    {
	if (actor == null)
	    throw new IllegalArgumentException("actor cannot be null.");

	actors.removeValue(actor, true);
	actor.setChecked(false);
    }

    /**
     * Change the checked state of the actor to true.
     * 
     * @param actor the actor to check
     * @throws IllegalArgumentException if actor is null
     */
    public void setChecked(T actor)
    {
	if (actor == null)
	    throw new IllegalArgumentException("actor cannot be null.");

	actor.setChecked(true);
    }

    /**
     * Used internally.
     * 
     * @param actor the actor to test
     * @param newState the new state to test
     * @return true if can be checked or false
     */
    public boolean canCheck(T actor, boolean newState)
    {
	if (actor.isChecked() == newState)
	    return false;

	if (!newState)
	{
	    // Keep actor checked to enforce minCheckCount.
	    if (checked.size <= minCheckCount)
		return false;

	    checked.removeValue(actor, true);
	}
	else
	{
	    // Keep actor unchecked to enforce maxCheckCount.
	    if (maxCheckCount != -1 && checked.size >= maxCheckCount)
	    {
		if (uncheckLast)
		{
		    final int old = minCheckCount;
		    minCheckCount = 0;
		    lastChecked.setChecked(false);
		    minCheckCount = old;
		}
		else
		    return false;
	    }
	    checked.add(actor);
	    lastChecked = actor;
	}

	return true;
    }

    /**
     * Uncheck all actor of this {@link ActorGroup group}.
     */
    public void uncheckAll()
    {
	final int _old = minCheckCount;
	minCheckCount = 0;
	for (T _actor : actors)
	    _actor.setChecked(false);
	minCheckCount = _old;
	checked.clear();
    }

    /**
     * Uncheck a specified actor.
     * 
     * @param actor the actor to uncheck
     * @throws IllegalArgumentException if actor is null
     */
    public void uncheck(T actor)
    {
	if (actor == null)
	    throw new IllegalArgumentException("actor cannot be null.");

	actor.setChecked(false);
    }

    /**
     * Start the multi selection.
     */
    public void beginSelection()
    {
	isMultiSelection = true;
	setMaxCheckCount(-1);
    }

    /**
     * End the multi selection.
     */
    public void endSelection()
    {
	isMultiSelection = false;
	setMaxCheckCount(1);
	uncheckAll();
    }

    /**
     * @return selected items
     */
    public Array<T> getAllChecked()
    {
	return checked;
    }

    /**
     * @return all {@link Actor actors} of this {@link ActorGroup group}
     */
    public Array<T> getActors()
    {
	return actors;
    }

    /**
     * @return the last {@link Actor actor} checked
     */
    public T getLastChecked()
    {
	return lastChecked;
    }

    /**
     * @return the first {@link Actor actor} checked or null if nothing is checked
     */
    public T getChecked()
    {
	if (checked.size > 0)
	    return checked.get(0);

	return null;
    }

    /*
     * @return if need to press control key to select multiple items
     */
    public boolean isMultiOnCtrl()
    {
	return isMultiOnCtrl;
    }

    /**
     * Set the way how MultiSelection is handled
     * 
     * @param isMultiOnCtrl true if must press control key to select multiple items
     */
    public void setMultiOnCtrl(boolean isMultiOnCtrl)
    {
	this.isMultiOnCtrl = isMultiOnCtrl;
    }

    /**
     * @return if the group is in multi selection mode
     */
    public boolean isMultiSelection()
    {
	return isMultiSelection;
    }

    /**
     * Set the minimum {@link Actor actor} that must be checked.
     * 
     * @param minCheckCount the minimum number of {@link Actor actor} to check
     */
    public void setMinCheckCount(int minCheckCount)
    {
	this.minCheckCount = minCheckCount;
    }

    /**
     * Set the maximum {@link Actor actor} that can be checked.
     * If you try to check more than the maxCheckCount, the first checked actor will be unchecked.
     * 
     * @param maxCheckCount the maximum number of {@link Actor actor} that can be checked in the same time
     */
    public void setMaxCheckCount(int maxCheckCount)
    {
	this.maxCheckCount = maxCheckCount;
    }

    /**
     * Set the maxCheckCount handling.
     * 
     * @param uncheckLast true if you don't want to uncheck the last actor if maxCheckCount is reached
     */
    public void setUncheckLast(boolean uncheckLast)
    {
	this.uncheckLast = uncheckLast;
    }

    /**
     * Clear this group.
     */
    public void clear()
    {
	actors.clear();
	checked.clear();

	lastChecked = null;
    }

}
