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
package com.gdx.extension.ui.menu;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;


/**
 * Base for all menu type.
 * 
 * @author Kyu
 *
 */
public class MenuItem extends Button
{
    
    protected MenuItemStyle style;
    
    protected Cell<Actor> iconCell;
    protected Cell<Actor> textCell;
    protected Cell<Actor> shortcutCell;
    protected Cell<Actor> subCell;
    
    /**
     * Create a raw menu with default {@link MenuItemStyle style}.
     * 
     * @param skin the skin to use
     */
    public MenuItem(Skin skin)
    {
	this(skin, "default");
    }
    
    /**
     * Create a raw menu with specified {@link MenuItemStyle style}.
     * 
     * @param skin the skin to use
     * @param styleName the style to use
     */
    @SuppressWarnings("unchecked")
    public MenuItem(Skin skin, String styleName)
    {
	super();
	
	setStyle(skin.get(styleName, MenuItemStyle.class));
	
	setSize(getPrefWidth(), getPrefHeight());
	
	defaults().minHeight(10f);
	iconCell = this.add().minWidth(16f);
	textCell = this.add().minWidth(150f).padLeft(5f);
	shortcutCell = this.add().minWidth(50f);
	subCell = this.add().minWidth(8f);
    }
    
    /**
     * Apply the {@link MenuItemStyle style}.
     * 
     * @param style the {@link MenuItemStyle style} to apply
     */
    public void setStyle(MenuItemStyle style)
    {
	this.style = style;
	
	super.setStyle(new ButtonStyle(style.buttonStyle));
    }

    /**
     * {@link MenuItemStyle Style} for all menu item.
     * 
     * @author Kyu
     *
     */
    public static class MenuItemStyle
    {

	public TextButtonStyle buttonStyle;
	
	/** Optional. */
	public Drawable icon, sub;

	public MenuItemStyle()
	{

	}

    }
    
}
