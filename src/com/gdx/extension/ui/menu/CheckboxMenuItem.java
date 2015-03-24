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

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;


/**
 * Create a checkable menu item.
 * This menu use rules to know if it should be checked or not and allow external changes.
 * 
 * @author Kyu
 *
 */
public abstract class CheckboxMenuItem extends MenuItem
{
    
    private Image checkImage;
    
    /**
     * Create a {@link CheckboxMenuItem} with default style.
     * 
     * @param text the label of the menu
     * @param skin the skin to use
     */
    public CheckboxMenuItem(String text , Skin skin)
    {
	this(text, "", skin, "default-checkbox");
    }
    
    /**
     * Create a {@link CheckboxMenuItem} with default style.
     * 
     * @param text the label of the menu
     * @param shortcut the shortcut for this menu function
     * @param skin the skin to use
     */
    public CheckboxMenuItem(String text, String shortcut ,Skin skin)
    {
	this(text, shortcut, skin, "default-checkbox");
    }
    
    /**
     * Create a {@link CheckboxMenuItem} with the specified style.
     * 
     * @param text the label of the menu
     * @param shortcut the shortcut for this menu function
     * @param skin the skin to use
     * @param styleName the style to use
     */
    public CheckboxMenuItem(String text, String shortcut, Skin skin, String styleName)
    {
	super(skin, styleName);
	
	if(style.icon != null)
	{
	    checkImage = new Image(style.icon);
	    this.iconCell.setActor(checkImage);
	}
	if(!shortcut.isEmpty())
	    this.shortcutCell.setActor(new Label(shortcut, skin));
	this.textCell.setActor(new Label(text, skin));
    }
    
    /**
     * Draw the checkbox.
     */
    @Override
    public void draw(Batch batch, float parentAlpha)
    {
	if(style.icon != null)
	    checkImage.setVisible(rules());
	    
	super.draw(batch, parentAlpha);
    }
    
    /**
     * Define when menu should be checked/unchecked
     */
    public abstract boolean rules();
    
}
