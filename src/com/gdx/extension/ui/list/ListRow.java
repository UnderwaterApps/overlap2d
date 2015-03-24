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
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Pools;
import com.gdx.extension.ui.SelectionMode;
import com.gdx.extension.ui.group.ActorGroup;
import com.gdx.extension.ui.group.Groupable;
import com.gdx.extension.ui.panel.Panel;

/**
 * Extend this class to define the custom view of a {@link AdvancedList list} {@link ListRow item}.
 * 
 * @author Kyu
 * 
 */
public class ListRow extends Panel implements Groupable
{

    protected ActorGroup<ListRow> listRowGroup;
    protected boolean isDisabled, isChecked, isOver;
    protected AdvancedList<? extends ListRow> list;

    protected Skin skin;
    protected ListRowStyle style;

    /**
     * Create a {@link AdvancedList list} {@link ListRow item} with default {@link ListRowStyle style}.
     * 
     * @param skin the skin to use
     */
    public ListRow(Skin skin)
    {
	this(skin, "default");
    }

    /**
     * Create a {@link AdvancedList list} {@link ListRow item} with the specified {@link ListRowStyle style}.
     * 
     * @param skin the skin to use
     * @param styleName the style name to use
     */
    public ListRow(Skin skin, String styleName)
    {
	super(skin);

	this.skin = skin;
	setTouchable(Touchable.enabled);

	setStyle(skin.get(styleName, ListRowStyle.class));
    }

    public void setStyle(ListRowStyle style)
    {
	this.style = style;
    }

    /**
     * Draw the background of the {@link ListRow}.
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

    @Override
    public void layout() {
	super.layout();

	if (list != null)
	    setWidth(list.getWidth());
    }

    /**
     * Define the style of a {@link ListRow}.
     * 
     * @author Kyu
     * 
     */
    public static class ListRowStyle
    {

	/** Optional */
	public Drawable background, disabled, checked, over;

	public ListRowStyle()
	{

	}

    }

    @Override
    public ActorGroup<? extends Groupable> getActorGroup()
    {
	return listRowGroup;
    }

    @Override
    public void setActorGroup(ActorGroup<? extends Groupable> group)
    {
	listRowGroup = (ActorGroup<ListRow>) group;
    }

    @Override
    public boolean isChecked()
    {
	return isChecked;
    }

    @Override
    public void setChecked(boolean isChecked)
    {
	if (this.isChecked == isChecked)
	    return;

	if (listRowGroup != null && !listRowGroup.canCheck(this, isChecked))
	    return;

	this.isChecked = isChecked;

	ChangeEvent changeEvent = Pools.obtain(ChangeEvent.class);
	if (fire(changeEvent))
	    this.isChecked = !isChecked;

	Pools.free(changeEvent);
    }

    @Override
    public boolean isDisabled()
    {
	return isDisabled;
    }

    @Override
    public void setDisabled(boolean isDisabled)
    {
	this.isDisabled = isDisabled;
    }

    /**
     * Used internally.
     * 
     * @param list the list where the {@link ListRow} is added
     */
    public void setList(final AdvancedList<? extends ListRow> list)
    {
	this.list = list;

	addListener(new InputListener() {

	    @Override
	    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
	    {
		if (button != Buttons.LEFT || !list.isSelectable())
		    return false;

		if (list.getSelectionMode() == SelectionMode.MULTI)
		{
		    if (!listRowGroup.isMultiSelection())
		    {
			if (listRowGroup.isMultiOnCtrl())
			{
			    if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Keys.CONTROL_RIGHT))
				listRowGroup.beginSelection();
			}
			else
			    listRowGroup.beginSelection();
		    }
		    else
		    {
			if (listRowGroup.isMultiOnCtrl())
			{
			    if (!Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) && !Gdx.input.isKeyPressed(Keys.CONTROL_RIGHT))
				listRowGroup.endSelection();
			}
		    }
		}

		if (listRowGroup.isMultiSelection() && ListRow.this.isChecked())
		    listRowGroup.uncheck(ListRow.this);
		else
		    listRowGroup.setChecked(ListRow.this);

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

}
