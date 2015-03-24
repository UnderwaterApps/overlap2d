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
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.gdx.extension.ui.panel.Panel;


/**
 * Create a simple toolbar where you can add actor horizontally.
 * 
 * @author Kyu
 *
 */
public class ToolBar extends Panel
{

    private HorizontalGroup bar;
    
    @SuppressWarnings("unused")
    private ToolBarStyle style;
    
    /**
     * Create a {@link ToolBar toolbar} with default style.
     * 
     * @param skin the skin to use
     */
    public ToolBar(Skin skin)
    {
	this(skin, "default");
    }
    
    /*
     * Create a {@link ToolBar toolbar} with the specified style.
     */
    public ToolBar(Skin skin, String styleName)
    {
	super(skin);
	
	bar = new HorizontalGroup();
	add(bar);
	
	setStyle(skin.get(styleName, ToolBarStyle.class));
    }
    
    /**
     * Apply the style of this {@link ToolBar toolbar}.
     * 
     * @param style the style to apply
     */
    public void setStyle(ToolBarStyle style)
    {
	this.style = style;
	
	if(style.background != null)
	    this.setBackground(style.background);
	
	this.left();
    }
    
    /**
     * Add an actor to the {@link ToolBar toolbar}.
     */
    @Override
    public Cell<?> add(Actor actor)
    {
        if(actor instanceof HorizontalGroup)
            return super.add(actor);
        else
        {
            bar.addActor(actor);
            
            return null;
        }
    }
    
    /**
     * Define the style of a {@link ToolBar toolbar}.
     * 
     * @author Kyu
     *
     */
    public static class ToolBarStyle
    {
	
	/** Optional */
	public Drawable background;
	
	public ToolBarStyle()
	{

	}
	
    }
    
}
