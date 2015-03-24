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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Pools;
import com.gdx.extension.ui.SelectionMode;
import com.gdx.extension.ui.group.ActorGroup;
import com.gdx.extension.ui.group.Groupable;

/**
 * It's a selectable item from a {@link GridSelection}.
 * You can define a custom item view by extending it and add actors to it.
 * It handle multi selection
 * 
 * @author Kyu
 */
public class GridSelectionItem extends Container<Actor> implements Groupable
{

    private ActorGroup<GridSelectionItem> gridItemGroup;
    private boolean isDisabled, isChecked, isOver;
    private GridSelectionItemStyle style;
    private GridSelection<? extends GridSelectionItem> grid;

    /**
     * Create an {@link GridSelectionItem item} with default style.
     * 
     * @param skin the {@link Skin skin} to use
     */
    public GridSelectionItem(Skin skin)
    {
	this(skin, "default");
    }

    /**
     * Create an {@link GridSelectionItem item} with the specified style.
     * 
     * @param skin the {@link Skin skin} to use
     * @param styleName the {@link GridSelectionItemStyle style} to use
     */
    public GridSelectionItem(Skin skin, String styleName)
    {
	this(skin.get(styleName, GridSelectionItemStyle.class));
    }

    public GridSelectionItem(GridSelectionItemStyle style)
    {
	setTransform(false);
	setTouchable(Touchable.enabled);

	setStyle(style);
    }

    /**
     * @return the preferred width
     */
    @Override
    public float getPrefWidth()
    {
	return getWidth();
    }

    /**
     * @return the preferred height
     */
    @Override
    public float getPrefHeight()
    {
	return getHeight();
    }

    /**
     * Draw the background of the {@link GridSelectionItem item}
     */
    @Override
    public void draw(Batch batch, float parentAlpha)
    {
	super.draw(batch, parentAlpha);

	Drawable _background = null;
	if (style.background != null && isDisabled)
	    _background = style.disabled;
	else if (style.checked != null && isChecked)
	    _background = style.checked;
	else if (style.over != null && isOver)
	    _background = style.over;
	else if (style.background != null)
	    _background = style.background;

	setBackground(_background);
    }

    /**
     * Apply the style.
     * 
     * @param style the {@link GridSelectionItemStyle style} to apply
     */
    public void setStyle(GridSelectionItemStyle style)
    {
	this.style = style;
    }

    /**
     * @return the parent group
     */
    @Override
    public ActorGroup<? extends Groupable> getActorGroup()
    {
	return gridItemGroup;
    }

    /**
     * Used internally.
     */
    @SuppressWarnings("unchecked")
    @Override
    public void setActorGroup(ActorGroup<? extends Groupable> group)
    {
	gridItemGroup = (ActorGroup<GridSelectionItem>) group;
    }

    /**
     * @return if {@link GridSelectionItem item} is checked
     */
    @Override
    public boolean isChecked()
    {
	return isChecked;
    }

    /**
     * Set the checked state of the {@link GridSelectionItem item}.
     * 
     * @param isChecked the checked state
     */
    @Override
    public void setChecked(boolean isChecked)
    {
	if (this.isChecked == isChecked)
	    return;

	if (gridItemGroup != null && !gridItemGroup.canCheck(this, isChecked))
	    return;

	this.isChecked = isChecked;

	ChangeEvent changeEvent = Pools.obtain(ChangeEvent.class);
	if (fire(changeEvent))
	    this.isChecked = !isChecked;

	Pools.free(changeEvent);
    }

    /**
     * @return if the {@link GridSelectionItem item} is disable
     */
    @Override
    public boolean isDisabled()
    {
	return isDisabled;
    }

    /**
     * Set the disabled state of the {@link GridSelectionItem item}.
     * 
     * @param isDisabled the disabled state
     */
    @Override
    public void setDisabled(boolean isDisabled)
    {
	this.isDisabled = isDisabled;
    }

    /**
     * Used internally.
     * 
     * @param grid the parent grid
     */
    public <T> void setGrid(final GridSelection<? extends GridSelectionItem> grid)
    {
	this.grid = grid;

	addListener(new InputListener() {

	    @Override
	    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
	    {
		if (button != Buttons.LEFT || !grid.isSelectable())
		    return false;

		if (grid.getSelectionMode() == SelectionMode.MULTI)
		{
		    if (!gridItemGroup.isMultiSelection())
		    {
			if (gridItemGroup.isMultiOnCtrl())
			{
			    if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Keys.CONTROL_RIGHT))
				gridItemGroup.beginSelection();
			}
			else
			    gridItemGroup.beginSelection();
		    }
		    else
		    {
			if (gridItemGroup.isMultiOnCtrl())
			{
			    if (!Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) && !Gdx.input.isKeyPressed(Keys.CONTROL_RIGHT))
				gridItemGroup.endSelection();
			}
		    }
		}

		if (gridItemGroup.isMultiSelection() && isChecked())
		    gridItemGroup.uncheck(GridSelectionItem.this);
		else
		    gridItemGroup.setChecked(GridSelectionItem.this);

		return true;
	    }

	    @Override
	    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor)
	    {
		isOver = true;
	    }

	    @Override
	    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor)
	    {
		isOver = false;
	    }

	});
    }

    /**
     * Style class
     * 
     * @author Kyu
     */
    public static class GridSelectionItemStyle
    {

	/**
	 * Optional
	 */
	public Drawable background, disabled, checked, over;

	public GridSelectionItemStyle()
	{
	}

    }

}
