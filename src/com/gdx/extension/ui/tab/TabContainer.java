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
package com.gdx.extension.ui.tab;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;


/**
 * Create a container for a {@link Tab tab}.
 * 
 * @author Kyu
 *
 */
public class TabContainer extends Table
{
    
    private TabContainerStyle style;

    /**
     * Create a {@link TabContainer}.
     * 
     * @param skin the skin to use in this container
     */
    public TabContainer(Skin skin)
    {
	this(skin, "default");
    }
    
    public TabContainer(Skin skin, String styleName)
    {
	super(skin);
	
	setStyle(skin.get(styleName, TabContainerStyle.class));
    }
    
    /**
     * Apply a {@link TabContainerStyle style}.
     * 
     * @param style
     */
    public void setStyle(TabContainerStyle style)
    {
	this.style = style;
	
	setBackground(style.background);
    }
    
    /**
     * @return the current {@link TabContainerStyle style}
     */
    public TabContainerStyle getStyle()
    {
	return style;
    }
    
    /**
     * Define the style of a {@link Tab tab} {@link TabContainer container}.
     * 
     * @author Kyu
     *
     */
    public static class TabContainerStyle {
	
	/** Optional */
	private Drawable background;
	
	public TabContainerStyle()
	{
	}
	
    }
    
}
