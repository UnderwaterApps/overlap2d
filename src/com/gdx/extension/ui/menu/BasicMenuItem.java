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

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Create a simple menu item.
 * 
 * @author Kyu
 *
 */
public class BasicMenuItem extends MenuItem
{
    
    /**
     * Create a {@link BasicMenuItem} with default style.
     * 
     * @param text the label of the menu
     * @param skin the skin to use
     */
    public BasicMenuItem(String text, Skin skin)
    {
	this(text, "", skin, "default");
    }
    
    /**
     * Create a {@link BasicMenuItem} with shortcut.
     * 
     * @param text the label of the menu
     * @param shortcut the shortcut for this menu function
     * @param skin the skin to use
     */
    public BasicMenuItem(String text, String shortcut, Skin skin)
    {
	this(text, shortcut, skin, "default");
    }
    
    /**
     * Create a {@link BasicMenuItem} with specified {@link MenuItemStyle style}.
     * 
     * @param text the label of the menu
     * @param skin the skin to use
     * @param styleName the style to use
     */
    public BasicMenuItem(String text, Skin skin, String styleName)
    {
	this(text, "", skin, styleName);
    }
    
    /**
     * Create a {@link BasicMenuItem} with specified style.
     * 
     * @param text the label of the menu
     * @param shortcut the shortcut for this menu function
     * @param skin the skin to use
     * @param styleName the style to use
     */
    public BasicMenuItem(String text, String shortcut, Skin skin, String styleName)
    {
	super(skin, styleName);
	
	if(style.icon != null)
	    this.iconCell.setActor(new Image(style.icon));
	
	if(!shortcut.isEmpty())
	    this.shortcutCell.setActor(new Label(shortcut, skin));
	
	this.textCell.setActor(new Label(text, skin));
    }
    
}
